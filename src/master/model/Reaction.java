package master.model;

import beast.core.BEASTObject;
import beast.core.Input;
import beast.core.Input.Validate;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.*;
import java.util.*;
import master.model.parsers.ExpressionLexer;
import master.model.parsers.ExpressionParser;
import master.model.parsers.ReactionStringBaseListener;
import master.model.parsers.ReactionStringLexer;
import master.model.parsers.ReactionStringParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Class of objects describing the reactions which occur between the various
 * populations in the model. Reactions may involve both scalar and structured
 * populations.
 *
 * @author Tim Vaughan
 *
 */
public class Reaction extends BEASTObject {
    
    public Input<String> nameInput = new Input<>("reactionName",
            "Name of reaction. (Not used for grouped reactions.)");
    
    public Input<String> rateInput = new Input<>("rate",
            "Individual reaction rate. (Only used if group rate unset.)");

    public Input<String> rateMultiplierInput = new Input("rateMultiplier",
        "The result of evaluating this expression involving "
            + "population location variables is multiplied with"
            + "the base reaction rate to produce a location-specific rate.");

    public Input<String> reactionStringInput = new Input<>(
            "value",
            "String description of reaction.", Validate.REQUIRED);

    public Input<List<Predicate>> predicatesInput = new Input<>(
        "predicate",
        "Predicate used to determine which reactions to include",
        new ArrayList<>());

    public String reactionName;
    public Map<Population, Integer> reactCount, prodCount, deltaCount;
    public Map<Population, List<Node>> reactNodes, prodNodes;
    public List<Double> rates, rateTimes;
    public double propensity;

    ParseTree reactionStringParseTree, rateMultiplierParseTree;
    ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
    RateMultiplierExpressionVisitor rateMultiplierVisitor;
    
    /**
     * Constructor without name.
     */
    public Reaction() { }
    
    /**
     * Constructor with name.
     * 
     * @param reactionGroupName
     */
    public Reaction(String reactionGroupName) {
        this.reactionName = reactionGroupName;
}
    
    @Override
    public void initAndValidate() {
        reactionName = nameInput.get();

        // Parse rate string
        if (rateInput.get() != null) {
            setRateFromString(rateInput.get());
        }

        // Parse reaction string
        ANTLRInputStream rsInput = new ANTLRInputStream(reactionStringInput.get());
        ReactionStringLexer rsLexer = new ReactionStringLexer(rsInput);
        CommonTokenStream rsTokens = new CommonTokenStream(rsLexer);
        ReactionStringParser rsParser= new ReactionStringParser(rsTokens);
        reactionStringParseTree = rsParser.reaction();

        // Parse rate multiplier string
        if (rateMultiplierInput.get() != null) {
            ANTLRInputStream rmInput = new ANTLRInputStream(rateMultiplierInput.get());
            ExpressionLexer rmLexer = new ExpressionLexer(rmInput);
            CommonTokenStream rmTokens = new CommonTokenStream(rmLexer);
            ExpressionParser rmParser = new ExpressionParser(rmTokens);
            rateMultiplierParseTree = rmParser.expression();
        }
    }

    /**
     * Recursive function used to assemble list of variable indices.  The reason
     * we have to use a function like this (rather than a bunch of nested for
     * loops) is that the number of variables whose values need to be iterated
     * over is model-dependent.
     * 
     * @param depth recursion depth
     * @param maxIndices list of bounds on variable values
     * @param indices variable value array
     * @param indicesList list to be filled with variable value arrays
     */
    private void variableLoop(int depth, int[] maxIndices, int[] indices, List<int[]> indicesList) {
        if (depth==indices.length) {
            indicesList.add(Arrays.copyOf(indices, indices.length));
        }  else {
            for (int i=0; i<maxIndices[depth]; i++) {
                indices[depth] = i;
                variableLoop(depth+1, maxIndices, indices, indicesList);
            }
        }
    }
    
    /**
     * Obtain list containing this reaction, or the reactions implied
     * by the ranges given.
     * 
     * @param populationTypes
     * @return list of reactions
     */
    public List<Reaction> getAllReactions(List<PopulationType> populationTypes) {
        List<Reaction> reactions = Lists.newArrayList();

        // Grab lists of population types and variable names, keeping track
        // of maximum values location index variables can take.
        Map<String, PopulationType> popTypes = new HashMap<>();
        Map<String, Integer> varNameBoundsMap = new HashMap<>();
        parseTreeWalker.walk(new ReactionStringBaseListener() {

            @Override
            public void exitPopel(ReactionStringParser.PopelContext ctx) {

                PopulationType popType = null;
                for (PopulationType thisPopType : populationTypes) {
                    if (ctx.popname().NAME().toString().equals(thisPopType.getName()))
                        popType = thisPopType;
                }

                if (popType == null) {
                    System.err.println("Uknown population type '"
                        + ctx.popname().NAME() + "' in reaction string.");
                    System.exit(1);
                }

                popTypes.put(ctx.popname().NAME().toString(), popType);

                if (ctx.loc() != null) {

                    if (ctx.loc().locel().size() != popType.dims.length) {
                        System.err.println("Population location vector"
                            + " length does not match length of dims vector.");
                        System.exit(0);
                    }
                    
                    for (int i=0; i<ctx.loc().locel().size(); i++) {
                        if (ctx.loc().locel(i).NAME() != null) {
                            String varName = ctx.loc().locel(i).NAME().toString();
                            if (!varNameBoundsMap.containsKey(varName)) {
                                varNameBoundsMap.put(varName, popType.dims[i]);
                            } else {
                                if (varNameBoundsMap.get(varName)>popType.dims[i]) {
                                    varNameBoundsMap.put(varName, popType.dims[i]);
                                }
                            }
                        }
                    }
                }
            }
            
        }, reactionStringParseTree);


        List<String> varNames = new ArrayList<>(varNameBoundsMap.keySet());
        int[] varBounds = new int[varNames.size()];
        for (int i=0; i<varNames.size(); i++)
            varBounds[i] = varNameBoundsMap.get(varNames.get(i));

        List<int[]> variableValuesList = new ArrayList<>();
        variableLoop(0, varBounds, new int[varNames.size()], variableValuesList);
        
        for (int[] varVals : variableValuesList) {

            // Test predicates
            boolean include = true;
            for (Predicate pred : predicatesInput.get()) {
                if (!pred.isTrue(varNames, varVals)) {
                    include = false;
                    break;
                }
            }

            // Skip this reaction
            if (!include)
                continue;

            // Create reaction object

            Reaction reaction;
            if (reactionName != null) {
                if (reactions.size()>0)
                    reaction = new Reaction(reactionName + reactions.size());
                else
                    reaction = new Reaction(reactionName);
            } else {
                reaction = new Reaction();
            }

            if (rateMultiplierInput.get() != null) {
                // Need copies when reactions can have different rates:
                reaction.rates = new ArrayList<>(rates);
            } else
                reaction.rates = rates;
            reaction.rateTimes = rateTimes;

            // Apply rate multiplier if present:

            if (rateMultiplierInput.get() != null)
                applyRateMultiplier(reaction.rates, varNames, varVals);

            // Walk reaction string parse tree to set up reaction topology

            reaction.reactNodes = new HashMap<>();
            reaction.prodNodes = new HashMap<>();
            parseTreeWalker.walk(new ReactionStringBaseListener() {

                List<Node> nodeList, reactNodeList, prodNodeList;
                List<Integer> popIDs, reactPopIDs, prodPopIDs;
                Map<Population, List<Node>> popNodeMap, reactPopNodeMap, prodPopNodeMap;
                Map<PopulationType, Integer> seenTypeIDs = new HashMap<>();
                int nextPopID = 0;


                @Override
                public void exitReactants(ReactionStringParser.ReactantsContext ctx) {
                    reactNodeList = nodeList;
                    reactPopIDs = popIDs;
                    reaction.reactNodes = popNodeMap;
                }

                @Override
                public void exitProducts(ReactionStringParser.ProductsContext ctx) {
                    prodNodeList = nodeList;
                    prodPopIDs = popIDs;
                    reaction.prodNodes = popNodeMap;

                    for (int pi=0; pi<prodNodeList.size(); pi++) {
                        for (int ri=0; ri<reactNodeList.size(); ri++) {
                            if (Objects.equals(reactPopIDs.get(ri), prodPopIDs.get(pi))) {
                                reactNodeList.get(ri).addChild(prodNodeList.get(pi));
                            }
                        }
                    }
                }

                @Override
                public void enterPopsum(ReactionStringParser.PopsumContext ctx) {
                    nodeList = new ArrayList<>();
                    popIDs = new ArrayList<>();
                    popNodeMap = new HashMap<>();
                }

                @Override
                public void exitPopel(ReactionStringParser.PopelContext ctx) {

                    PopulationType popType = popTypes.get(ctx.popname().getText());

                    // Assemble loc

                    List<Integer> locList = new ArrayList<>();
                    if (ctx.loc() != null) {
                        for (ReactionStringParser.LocelContext locelCtx : ctx.loc().locel()) {
                            if (locelCtx.NAME() == null) {
                                locList.add(Integer.parseInt(locelCtx.getText()));
                            } else {
                                String varName = locelCtx.NAME().getText();
                                int varIdx = varNames.indexOf(varName);
                                locList.add(varVals[varIdx]);
                            }
                        }
                    }

                    int[] loc = new int[locList.size()];
                    for (int i=0; i<loc.length; i++)
                        loc[i] = locList.get(i);

                    Population pop = new Population(popType, loc);

                    // Determine the number of replicates of this reagent
                    int factor;
                    if (ctx.factor() != null)
                        factor = Integer.parseInt(ctx.factor().getText());
                    else
                        factor = 1;

                    for (int i=0; i<factor; i++) {
                        Node popNode = new Node(pop);

                        // Add corresponding node to relevant maps, lists...
                        nodeList.add(popNode);
                        if (!popNodeMap.containsKey(popNode.getPopulation()))
                            popNodeMap.put(popNode.getPopulation(), new ArrayList<>());
                        popNodeMap.get(popNode.getPopulation()).add(popNode);

                        // Assign ID to node if not explicitly given.
                        // The default greedily assigns all products of a
                        // particular type to be children of the first
                        // reactant of the same type.  This always generates
                        // trees in forward time.
                        int id;
                        if (ctx.id() != null) {
                            id = Integer.parseInt(ctx.id().getText());
                        } else {
                            if (ctx.getParent().getParent()
                                instanceof ReactionStringParser.ReactantsContext) {
                                id = nextPopID++;
                                if (!seenTypeIDs.containsKey(popType))
                                    seenTypeIDs.put(popType, id);
                            } else {
                                if (seenTypeIDs.containsKey(popType))
                                    id = seenTypeIDs.get(popType);
                                else
                                    id = nextPopID++;
                            }
                        }
                        popIDs.add(id);
                    }
                    
                }
                
            }, reactionStringParseTree);
            
            // Calculate individual population counts for non-inheritance
            // trajectory code
            
            reaction.reactCount = Maps.newHashMap();
            for (Population pop : reaction.reactNodes.keySet())
                reaction.reactCount.put(pop, reaction.reactNodes.get(pop).size());
            
            reaction.prodCount = Maps.newHashMap();
            for (Population pop : reaction.prodNodes.keySet())
                reaction.prodCount.put(pop, reaction.prodNodes.get(pop).size());
            
            // Loosely, calculate deltas=prodLocSchema-reactLocSchema.
            
            reaction.deltaCount = Maps.newHashMap();
            
            for (Population pop : reaction.reactCount.keySet())
                reaction.deltaCount.put(pop, -reaction.reactCount.get(pop));
            
            for (Population pop : reaction.prodCount.keySet()) {
                if (!reaction.deltaCount.containsKey(pop))
                    reaction.deltaCount.put(pop, reaction.prodCount.get(pop));
                else {
                    int val = reaction.deltaCount.get(pop);
                    val += reaction.prodCount.get(pop);
                    reaction.deltaCount.put(pop, val);
                }
            }
            
            reactions.add(reaction);
        }

        return reactions;
    }

    /**
     * Evaluates rate multiplier for particular location variable combination
     * and applies this multiplier to the given rate list.
     * 
     * @param theseRates   Rate list
     * @param varNames     Variable name list
     * @param varVals      Variable value array
     */
    public void applyRateMultiplier(List<Double> theseRates, List<String> varNames, int[] varVals) {

        // Compute rate multiplier
        
        if (rateMultiplierVisitor == null)
            rateMultiplierVisitor = new RateMultiplierExpressionVisitor(varNames);
        rateMultiplierVisitor.setVarVals(varVals);
        
        Double[] rmRes = rateMultiplierVisitor.visit(rateMultiplierParseTree);
        if (rmRes.length != 1) {
            throw new IllegalArgumentException(
                    "Reaction rate multiplier must be scalar!");
        }
        
        // Multiply original rates by multiplier

        for (int i=0; i<theseRates.size(); i++)
            theseRates.set(i, theseRates.get(i)*rmRes[0]);
        
    }
  
    /**
     * Use string of the form "rate1:time1,rate2:time2,..."
     * to set the list of reaction rates associated with
     * this reaction.
     * 
     * @param rateString 
     */
    public void setRateFromString(String rateString) {
        rates = Lists.newArrayList();
        rateTimes = Lists.newArrayList();
        
        boolean isFirst = true;
        for (String ratePairStr : rateInput.get().split(",")) {
            String [] ratePairSplitStr = ratePairStr.trim().split(":");
            if (ratePairSplitStr.length==1) {
                if (!isFirst) {
                    throw new IllegalArgumentException("Only first rate "
                            + "pair in a reaction rate string can omit "
                            + "the time.");
                }
                
                rates.add(Double.valueOf(ratePairSplitStr[0]));
                rateTimes.add(0.0);
            }
            
            if (ratePairSplitStr.length==2) {
                
                double thisRate = Double.valueOf(ratePairSplitStr[0]);
                double thisTime = Double.valueOf(ratePairSplitStr[1]);
                
                // First time >0 implies reaction is off at start of sim
                if (isFirst && thisTime>0.0) {
                    rates.add(0.0);
                    rateTimes.add(0.0);
                }
                
                rates.add(thisRate);
                rateTimes.add(thisTime);
            }
            
            if (rateTimes.size()>1 &&
                    rateTimes.get(rateTimes.size()-1)<rateTimes.get(rateTimes.size()-2))
                throw new IllegalArgumentException("Rate change times must "
                        + "be monotonically increasing.");
            
            if (isFirst)
                isFirst = false;
        }
    }
    
    /**
     * Retrieve rate list for this reaction.
     * 
     * @return rates
     */
    public List<Double> getRates() {
        return rates;
    }
    
    /**
     * Retrieve rate change time list for this reaction.
     * 
     * @return list of rate change times
     */
    public List<Double> getRateTimes() {
        return rateTimes;
    }
    
    /**
     * Obtain rate at particular time.
     * 
     * @param t
     * @return 
     */
    public double getRate(double t) {
        int interval;
        for (interval = 0; interval<rateTimes.size()-1; interval++) {
            if (rateTimes.get(interval+1)>t)
                break;
        } 
        
        return rates.get(interval);
    }
    
    /**
     * Calculate instantaneous reaction rates (propensities) for a given system
     * state.
     *
     * @param state	PopulationState used to calculate propensities.
     * @param t         Time at which propensity is calculated.
     */
    public void calcPropensity(PopulationState state, double t) {
        
        int interval;
        for (interval=0; interval<rates.size()-1; interval++) {
            if (rateTimes.get(interval+1)>t)
                break;
        }
        
        propensity = rates.get(interval);

        for (Population pop : reactCount.keySet()) {
            for (int m = 0; m<reactCount.get(pop); m++)
                propensity *= state.get(pop)-m;
        }
    }
    
    /**
     * Retrieve recently calculated propensity.
     * 
     * @return propensity
     */
    public double getPropensity() {
        return propensity;
    }
    
    /**
     * Set name of reaction.
     * 
     * @param reactionName 
     */
    public void setName(String reactionName) {
        this.reactionName = reactionName;
    }
    
    /**
     * Retrieve name of reaction optionally provided during
     * specification.
     * 
     * @return reaction name
     */
    public String getName() {
        return reactionName;
    }
   
    /*
     * Methods for JSON object mapper
     */
    
    @Override
    @JsonValue
    public String toString() {

        // Construct reaction string
        StringBuilder sb = new StringBuilder();
        if (reactionName != null)
            sb.append(reactionName).append(": ");
        
        if (!reactCount.isEmpty()) {
            boolean first = true;
            for (Population pop : reactCount.keySet()) {
                if (!first)
                    sb.append(" + ");
                else
                    first = false;
                
                if (reactCount.get(pop)>1)
                    sb.append(reactCount.get(pop));
                sb.append(pop.type.name);
                if (!pop.isScalar()) {
                    sb.append("[");
                    int [] loc = pop.getLocation();
                    for (int i=0; i<loc.length; i++) {
                        if (i>0)
                            sb.append(',');
                        sb.append(loc[i]);
                    }
                    sb.append("]");
                }
            }
        } else
            sb.append("0");
        
        sb.append(" -> ");
        
        if (!prodCount.isEmpty()) {
            boolean first = true;
            for (Population pop : prodCount.keySet()) {
                if (!first)
                    sb.append(" + ");
                else
                    first = false;
                
                if (prodCount.get(pop)>1)
                    sb.append(prodCount.get(pop));
                sb.append(pop.type.name);
                if (!pop.isScalar()) {
                    sb.append("[");
                    int [] loc = pop.getLocation();
                    for (int i=0; i<loc.length; i++) {
                        if (i>0)
                            sb.append(',');
                        sb.append(loc[i]);
                    }
                    sb.append("]");
                }
            }
        } else
            sb.append("0");

        // Include rates:
        sb.append(" (");
        for (int i=0; i<rates.size(); i++) {
            if (i>0)
                sb.append(", ");

            sb.append(rates.get(i));

            if (rates.size()>1)
                sb.append(":").append(rateTimes.get(i));
        }
        sb.append(")");

        return sb.toString();

    }

}