package master;

import beast.core.BEASTObject;
import beast.core.Input;
import com.google.common.collect.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import master.beast.InheritanceReaction;

/**
 * Class of objects describing the reactions which occur between the various
 * populations in the model. Reactions may involve both scalar and structured
 * populations.
 *
 * @author Tim Vaughan
 *
 */
public class NewReaction extends BEASTObject {
    
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
    public double rate = -1.0;
    public double propensity;
    
    private final List<Range> ranges;
    private final List<String> rangeVariableNames;
    private final List<Integer> rangeFromValues, rangeToValues;
    
    /**
     * Constructor without name.
     */
    public NewReaction() {
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
    }
    
    /**
     * Constructor with name.
     * 
     * @param reactionGroupName
     */
    public NewReaction(String reactionGroupName) {
        this.reactionName = reactionGroupName;
        
        ranges = Lists.newArrayList();
        rangeVariableNames = Lists.newArrayList();
        rangeFromValues = Lists.newArrayList();
        rangeToValues = Lists.newArrayList();
    }
    


    /**
     * Define a particular schema by listing the individual
     * reactants involved in a reaction.
     *
     * @param pops varargs list of reactant populations.
     */
    public void setReactantSchema(Population ... pops) {

        // Record unique sub-population counts:
        reactCount = getPopCount(pops);

    }
    
    /**
     * Define a particular schema by listing the individual
     * products involved in a reaction.
     *
     * @param pops varargs list of product populations.
     */
    public void setProductSchema(Population ... pops) {

        // Record unique population counts:
        prodCount = getPopCount(pops);
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
        setReactantSchema((Population[])parser.getReactantPops().toArray());
        setProductSchema((Population[])parser.getReactantPops().toArray());
    }
    
    /**
     * Add range.  Causes multiple reactions to be added to model.
     * 
     * @param range Range object.
     */
    public void addRange(Range range) {
        ranges.add(range);
    }
    
    /**
     * Internal method which takes a list of populations and constructs
     * a map from the populations to their multiplicity in the list.
     * 
     * @param pops List of populations.
     * @return Map from populations to their list multiplicity.
     */
    private Map<Population, Integer> getPopCount(Population ... pops) {

        // Condense provided schema into a map of the form
        // SubPop->count, where count is the number of times
        // that specific offset appears as a reactant/product in this schema.
        Map<Population, Integer> popCount = Maps.newHashMap();
        
        for (Population pop : pops) {   
            
            if (!popCount.containsKey(pop))
                popCount.put(pop, 1);
            else {
                int val = popCount.get(pop);
                popCount.put(pop, val+1);
            }
        }

        return popCount;
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
     * Perform that part of the initialization process which can only be
     * completed once the reaction schema is in place.
     *
     * Also performs validation of the specified schema.
     */
    public void postSpecInit() {

        // Perform sanity check on schema:
        if (reactCount == null || prodCount == null || rate < 0.0)
            throw new IllegalArgumentException("Inconsistent number of schemas and/or rates.");

        // Pre-calculate reaction-induced changes to sub-population sizes:
        calcDelta();

    }
    
    /**
     * Pre-calculate reaction-induced changes to population sizes.
     *
     * Determines the difference between each reactant and product
     * schema defined in reactCounts and prodCounts.
     */
    private void calcDelta() {

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
     * Retrieve name of reaction group optionally provided during
     * specification.
     * 
     * @return reaction group name
     */
    public String getName() {
        return reactionName;
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
    private void rangeLoop(int depth, int [] indices, ReactionStringParser parser,
            master.Model model,
            master.ReactionGroup group) throws ParseException {
        
        if (depth==indices.length) {
            
            List<master.Population> reactants = getEntityList(indices,
                    parser.getReactantPops(), parser.reactantLocs,
                    parser.variableNames, model.getPopulationTypes());
            List<master.Population> products = getEntityList(indices,
                    parser.getProductPops(), parser.productLocs,
                    parser.variableNames, model.getPopulationTypes());
            

            if (group != null) {
                
                group.addReactantSchema(reactants.toArray(new master.Population[0]));
                group.addProductSchema(products.toArray(new master.Population[0]));
                group.addRate(rate);
                
            } else {
                
                Reaction reaction;
                if (name != null) {
                    if (reactionIndex>0)
                        reaction = new master.Reaction(name + reactionIndex);
                    else
                        reaction = new master.Reaction(name);
                } else
                    reaction = new master.Reaction();
                
                reaction.addReactantSchema(reactants.toArray(new master.Population[0]));
                reaction.addProductSchema(products.toArray(new master.Population[0]));
                reaction.setRate(rate);
                model.addReaction(reaction);
                
            }
            
            reactionIndex += 1;
            
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
                rangeLoop(depth+1, indices, parser, model, group);
            }

        }
    }

    /**
     * Obtain list containing this reaction, or the reactions implied
     * by the ranges given.
     * 
     * @return list of reactions
     */
    public List<NewReaction> getAllReactions() {
        List<NewReaction> reactions = Lists.newArrayList();
        
        if (ranges.isEmpty()) {
            calcDelta();
            reactions.add(this);
        } else {

        }
        
        return reactions;
    }
    
    /*
     * Methods for JSON object mapper
     */
    
    @Override
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