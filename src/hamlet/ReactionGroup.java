package hamlet;

import com.google.common.collect.*;
import java.util.*;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Class of objects describing the reactions which occur between the various
 * populations in the model. Reactions may involve both scalar and structured
 * populations.
 *
 * @author Tim Vaughan
 *
 */
public class ReactionGroup {

    String reactionGroupName;
    List<Map<Population,Integer>> reactCounts, prodCounts, deltaCounts;
    List<Double> rates, propensities;
    int nReactions;
    
    /**
     * Constructor with name.
     */
    public ReactionGroup(String reactionGroupName) {
        
        // Ensure lists are defined:
        reactCounts = Lists.newArrayList();
        prodCounts = Lists.newArrayList();

        rates = Lists.newArrayList();
        
        this.reactionGroupName = reactionGroupName;
    }
    
    /**
     * Constructor without name.
     */
    public ReactionGroup() {
        
        // Ensure lists are defined:
        reactCounts = Lists.newArrayList();
        prodCounts = Lists.newArrayList();

        rates = Lists.newArrayList();
    }   

    /**
     * Define a particular schema by listing the individual
     * reactants involved in a reaction.
     *
     * @param pops	varargs list of reactant populations.
     */
    public void addReactantSchema(Population ... pops) {

        // Record unique sub-population counts:
        reactCounts.add(getPopCount(pops));

    }
    
    /**
     * Define a particular schema by listing the individual
     * products involved in a reaction.
     *
     * @param pops	varargs list of product populations.
     */
    public void addProductSchema(Population ... pops) {

        // Record unique population counts:
        prodCounts.add(getPopCount(pops));
    }
    
    /**
     * Internal method which takes a list of populations and constructs
     * a map from the populations to their multiplicity in the list.
     * 
     * @param pops List of populations.
     * @return Map from populations to their list multiplicity.
     */
    protected Map<Population, Integer> getPopCount(Population ... pops) {

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
    public void addRate(double rate) {
        rates.add(rate);
    }

    /**
     * Uses a single value to populate the rate list.
     *
     * Useful for defining collections of reactions which occur
     * at the same rate, or for specifying the rate of reactions involving
     * un-structured populations only.
     *
     * @param rate
     */
    public void setGroupRate(double rate) {

        rates.clear();
        for (int i = 0; i<reactCounts.size(); i++)
            rates.add(rate);
    }

    /**
     * Perform that part of the initialization process which can only be
     * completed once the reaction schema is in place.
     *
     * Also performs validation of the specified schema.
     */
    public void postSpecInit() {

        // Perform sanity check on schema:
        if ((reactCounts.size()!=prodCounts.size())
                ||(reactCounts.size()!=rates.size()))
            throw new IllegalArgumentException("Inconsistent number of schemas and/or rates.");

        // Central record of number of sub-population reaction schemas:
        nReactions = rates.size();
        
        // Preallocate memory for reaction propensities:
        propensities = Lists.newArrayList(rates);

        // Pre-calculate reaction-induced changes to sub-population sizes:
        calcDeltas();

    }
    
    /**
     * Pre-calculate reaction-induced changes to population sizes.
     *
     * Determines the difference between each reactant and product
     * schema defined in reactCounts and prodCounts.
     */
    private void calcDeltas() {

        deltaCounts = Lists.newArrayList();

        // Loosely, calculate deltas=prodLocSchema-reactLocSchema.

        for (int i = 0; i<nReactions; i++) {
            Map<Population, Integer> deltaCount = Maps.newHashMap();
            
            for (Population pop : reactCounts.get(i).keySet()) 
                deltaCount.put(pop, -reactCounts.get(i).get(pop));
            
            for (Population pop : prodCounts.get(i).keySet()) {
                if (!deltaCount.containsKey(pop))
                    deltaCount.put(pop, prodCounts.get(i).get(pop));
                else {
                    int val = deltaCount.get(pop);
                    val += prodCounts.get(i).get(pop);
                    deltaCount.put(pop, val);
                }
            }

            deltaCounts.add(deltaCount);
        }
    }

    /**
     * Calculate instantaneous reaction rates (propensities) for a given system
     * state.
     *
     * @param state	State used to calculate propensities.
     */
    public void calcPropensities(State state) {
        
        for (int i = 0; i<nReactions; i++) {
            double thisProp = rates.get(i);

            for (Population pop : reactCounts.get(i).keySet()) {
                for (int m = 0; m<reactCounts.get(i).get(pop); m++)
                    thisProp *= state.get(pop)-m;
            }

            propensities.set(i, thisProp);
        }        
    }
    
    /*
     * Methods for JSON object mapper
     */
    @JsonValue
    @Override
    public String toString() {

        // Construct reaction string
        StringBuilder sb = new StringBuilder();
        if (reactionGroupName != null)
            sb.append(reactionGroupName).append(": ");
        
        if (!reactCounts.get(0).isEmpty()) {
            boolean first = true;
            for (Population pop : reactCounts.get(0).keySet()) {
                if (!first)
                    sb.append(" + ");
                else
                    first = false;
                
                if (reactCounts.get(0).get(pop)>1)
                    sb.append(reactCounts.get(0).get(pop));
                sb.append(pop.type.name);
                if (!pop.isScalar())
                    sb.append("[]");
            }
        } else
            sb.append("0");
        
        sb.append(" -> ");
        
        if (!prodCounts.get(0).isEmpty()) {
            boolean first = true;
            for (Population pop : prodCounts.get(0).keySet()) {
                if (!first)
                    sb.append(" + ");
                else
                    first = false;
                
                if (prodCounts.get(0).get(pop)>1)
                    sb.append(prodCounts.get(0).get(pop));
                sb.append(pop.type.name);
                if (!pop.isScalar())
                    sb.append("[]");
            }
        } else
            sb.append("0");

        return sb.toString();

    }

}