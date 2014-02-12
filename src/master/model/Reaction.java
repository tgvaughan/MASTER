package master.model;

import beast.core.BEASTObject;
import beast.core.Input;
import com.google.common.collect.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Class of objects describing the reactions which occur between the various
 * populations in the model. Reactions may involve both scalar and structured
 * populations.
 *
 * @author Tim Vaughan
 *
 */
public class Reaction extends BEASTObject {
    
    public Input<String> nameInput = new Input<String>("reactionName",
            "Name of reaction. (Not used for grouped reactions.)");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Individual reaction rate. (Only used if group rate unset.)");
    
    public Input<List<Range>> rangesInput = new Input<List<Range>>("range",
            "Define multiple reactions for different values of a variable.",
            new ArrayList<Range>());
    
    public Input<String> reactionStringInput = new Input<String>(
            "value",
            "String description of reaction.", Input.Validate.REQUIRED);

    public String reactionName;
    public Map<Population,Integer> reactCount, prodCount, deltaCount;
    public Map<Population, List<Node>> reactNodes, prodNodes;
    public double rate = -1.0;
    public double propensity;
    
    private final List<Range> ranges;
    private final List<String> rangeVariableNames;
    private final List<Integer> rangeFromValues, rangeToValues;
    
    /**
     * Constructor without name.
     */
    public Reaction() {
        ranges = Lists.newArrayList();
        rangeVariableNames = Lists.newArrayList();
        rangeFromValues = Lists.newArrayList();
        rangeToValues = Lists.newArrayList();
    }   
    
    @Override
    public void initAndValidate() {
        reactionName = nameInput.get();
        
        if (rateInput.get() != null)
            rate = rateInput.get();
        
        ranges.addAll(rangesInput.get());
        for (Range range : ranges)
            rangeVariableNames.add(range.getVariableName());

        for (Range range : ranges) {
            String fromStr = range.fromInput.get();
            if(rangeVariableNames.contains(fromStr))
                rangeFromValues.add(-(rangeVariableNames.indexOf(fromStr)+1));
            else
                rangeFromValues.add(Integer.parseInt(fromStr));

            String toStr = range.toInput.get();
            if (rangeVariableNames.contains(toStr))
                rangeToValues.add(-(rangeVariableNames.indexOf(toStr)+1));
            else
                rangeToValues.add(Integer.parseInt(toStr));
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
        
        if (ranges.isEmpty()) {
            try {
                setSchemaFromString(reactionStringInput.get(), populationTypes);
            } catch (ParseException ex) {
                Logger.getLogger(Reaction.class.getName()).log(Level.SEVERE, null, ex);
            }
            reactions.add(this);
        } else {
            int [] indices = new int[ranges.size()];
            rangeLoop(0, indices, populationTypes, reactions);
        }
        
        return reactions;
    }
    
    
    /**
     * Recursion used to loop over range variables and assemble reaction
     * for each combination.
     * 
     * @param depth current recursion depth
     * @param indices list of range variable values
     * @param parser result of parsing reaction string
     * @param model model to which reactions are to be added
     * @param group reaction group to which reactions are to be added (may be null)
     * @throws ParseException 
     */
    private void rangeLoop(int depth, int [] indices,
            List<PopulationType> populationTypes,
            List<Reaction> reactions) {
        

        if (depth==indices.length) {
            
            // Make required replacements in reaction schema string
            String schemaString = reactionStringInput.get().replaceAll("  *", "");
            for (int rangeIdx = 0; rangeIdx < ranges.size(); rangeIdx++) {
                String var = rangeVariableNames.get(rangeIdx);
                int val = indices[rangeIdx];
                schemaString = schemaString.replace("["+var+"]", "["+val+"]")
                        .replace("["+var+",", "["+val+",")
                        .replace(","+var+"]", ","+val+"]")
                        .replace(","+var+",", ","+val+",");
            }
            
            // Assemble reaction
            Reaction reaction;
            if (reactionName != null) {
                if (reactions.size()>0)
                    reaction = new Reaction(reactionName + reactions.size());
                else
                    reaction = new Reaction(reactionName);
            } else
                reaction = new Reaction();
            
            reaction.setRate(rate);
            
            try {
                reaction.setSchemaFromString(schemaString, populationTypes);
            } catch (ParseException ex) {
                Logger.getLogger(Reaction.class.getName()).log(Level.SEVERE, null, ex);
            }

            reactions.add(reaction);
            
        } else {
            int from;
            if (rangeFromValues.get(depth)<0)
                from = indices[-rangeFromValues.get(depth)-1];
            else
                from = rangeFromValues.get(depth);

            int to;
            if (rangeToValues.get(depth)<0)
                to = indices[-rangeToValues.get(depth)-1];
            else
                to = rangeToValues.get(depth);
            
            for (int i=from; i<=to; i++) {
                indices[depth] = i;
                rangeLoop(depth+1, indices, populationTypes, reactions);
            }

        }
    }
    
    /**
     * Constructor with name.
     * 
     * @param reactionGroupName
     */
    public Reaction(String reactionGroupName) {
        this.reactionName = reactionGroupName;
        
        ranges = Lists.newArrayList();
        rangeVariableNames = Lists.newArrayList();
        rangeFromValues = Lists.newArrayList();
        rangeToValues = Lists.newArrayList();
    }
    
    /**
     * Attempt to determine reaction schema from string representation.
     * 
     * @param schemaString string to parse.
     * @param popTypes list of population types present in model.
     * @throws java.text.ParseException 
     */
    public void setSchemaFromString(String schemaString, List<PopulationType> popTypes) throws ParseException {
        
        ReactionStringParser parser = new ReactionStringParser(schemaString, popTypes);
        
        // Assemble node representation of reaction
        
        reactNodes = Maps.newHashMap();
        List<Node> reactNodeList = Lists.newArrayList();
        for (int i=0; i<parser.getReactantPops().size(); i++) {
            Population pop = parser.getReactantPops().get(i);
            Node node = new Node(pop);
            
            if (!reactNodes.containsKey(pop))
                reactNodes.put(pop, new ArrayList<Node>());

            reactNodes.get(pop).add(node);
            reactNodeList.add(node);
        }
        
        prodNodes = Maps.newHashMap();
        for (int i=0; i<parser.getProductPops().size(); i++) {
            Population pop = parser.getProductPops().get(i);
            Node node = new Node(pop);
            
            if (!prodNodes.containsKey(pop))
                prodNodes.put(pop, new ArrayList<Node>());
            
            prodNodes.get(pop).add(node);
            
            int nodeId = parser.getProductIDs().get(i);
            for (int ip=0; ip<parser.getReactantIDs().size(); ip++) {
                if (parser.getReactantIDs().get(ip)==nodeId) {
                    Node parent = reactNodeList.get(ip);
                    parent.addChild(node);
                }
            }
        }
        
        // Calculate individual population counts for non-inheritance
        // trajectory code
        
        reactCount = Maps.newHashMap();
        for (Population pop : reactNodes.keySet())
            reactCount.put(pop, reactNodes.get(pop).size());
        
        prodCount = Maps.newHashMap();
        for (Population pop : prodNodes.keySet())
            prodCount.put(pop, prodNodes.get(pop).size());
        
        // Loosely, calculate deltas=prodLocSchema-reactLocSchema.

        deltaCount = Maps.newHashMap();
            
        for (Population pop : reactCount.keySet()) 
            deltaCount.put(pop, -reactCount.get(pop));
            
        for (Population pop : prodCount.keySet()) {
            if (!deltaCount.containsKey(pop))
                deltaCount.put(pop, prodCount.get(pop));
            else {
                int val = deltaCount.get(pop);
                val += prodCount.get(pop);
                deltaCount.put(pop, val);
            }
        }
    }

    /**
     * Adds rate of specific reaction.
     *
     * @param rate
     */
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    /**
     * Retrieve rate of this reaction.
     * 
     * @return rate
     */
    public double getRate() {
        return rate;
    }
    
    /**
     * Calculate instantaneous reaction rates (propensities) for a given system
     * state.
     *
     * @param state	PopulationState used to calculate propensities.
     */
    public void calcPropensity(PopulationState state) {
        
        propensity = rate;

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

        return sb.toString();

    }

}