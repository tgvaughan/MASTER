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
public class Reaction {

    String reactionName;
    List<Population> reactPopSchema, prodPopSchema;
    List<Map<SubPopulation,Integer>> reactSubCounts, prodSubCounts, deltaCounts;
    List<Double> rates, propensities;
    int nSubSchemas;
    
    /**
     * Constructor (with name).
     */
    public Reaction(String reactionName) {
        
        // Ensure lists are defined:
        reactSubCounts = Lists.newArrayList();
        prodSubCounts = Lists.newArrayList();

        rates = Lists.newArrayList();
        
        this.reactionName = reactionName;
    }
    
    /**
     * Constructor (no name).
     */
    public Reaction() {
        
        // Ensure lists are defined:
        reactSubCounts = Lists.newArrayList();
        prodSubCounts = Lists.newArrayList();

        rates = Lists.newArrayList();
        
        this.reactionName = "Nameless Reaction";
    }

    /**
     * Define reactant population-level schema by choosing which populations
     * appear in subsequent calls to addReactantLocSchema and in which order
     * they appear.
     *
     * @param reactantPopSchema	varargs list of reactant populations.
     */
    public void setReactantSchema(Population... reactantPopSchema) {
        this.reactPopSchema = Lists.newArrayList(reactantPopSchema);
    }

    /**
     * Define product population-level schema by choosing which populations
     * appear in subsequent calls to addProductLocSchema and in which order
     * those populations appear.
     *
     * @param productPopSchema	varargs list of product populations.
     */
    public void setProductSchema(Population... productPopSchema) {
        this.prodPopSchema = Lists.newArrayList(productPopSchema);
    }
    

    /**
     * Define a particular sub-population-level schema by listing the individual
     * sub-population reactants involved in a reaction. Subsequent calls to
     * addReactantLocSchema() define a list of such schemas that must be aligned
     * with a similar list created by corresponding calls to
     * addProductLocSchema().
     *
     * Note that the "sub-population" corresponding to an unstructured
     * population in the reactant schema should be set to null.
     *
     * @param subs	varargs list of reactant sub-populations.
     */
    public void addReactantSubSchema(SubPopulation ... subs) {

        // Check for consistent number of sub-populations:
        if (subs.length!=reactPopSchema.size())
            throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");
        
        // Check that sub-populations are of the right population type:
        for (int i=0; i<subs.length; i++) {
            if (subs[i] != null) {
                if (subs[i].pop != reactPopSchema.get(i))
                    throw new IllegalArgumentException("Incorrect subpopulation type in schema.");
            } else {
                if (reactPopSchema.get(i).nSubPops>1)
                    throw new IllegalArgumentException("Null subpopulation given for non-scalar population in schema.");
                
                subs[i] = new SubPopulation(reactPopSchema.get(i));
            }
        }
        
        // Record unique sub-population counts:
        reactSubCounts.add(getSubCount(subs));

    }
    
    /**
     * Define a particular sub-population-level schema by listing the individual
     * sub-population products involved in a reaction. Subsequent calls to
     * addProductLocSchema() define an array of such schemas that must be
     * aligned with a similar array created by corresponding calls to
     * addReactantLocSchema().
     *
     * Note that the "sub-population" corresponding to an unstructured
     * population in the product schema should be set to null.
     *
     * @param subs	varargs list of product sub-populations.
     */
    public void addProductSubSchema(SubPopulation ... subs) {

        // Check for consistent number of sub-populations:
        if (subs.length!=prodPopSchema.size())
            throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");
        
        // Check that sub-populations are of the right population type:
        for (int i=0; i<subs.length; i++) {
            if (subs[i] != null) {
                if (subs[i].pop != prodPopSchema.get(i))
                    throw new IllegalArgumentException("Incorrect subpopulation type in schema.");
            } else {
                if (prodPopSchema.get(i).nSubPops>1)
                    throw new IllegalArgumentException("Null subpopulation given for non-scalar population in schema.");
            }
        }
        
        // Record unique sub-population counts:
        prodSubCounts.add(getSubCount(subs));
    }
    
    /**
     * Internal method which takes a list of sub-populations and constructs
     * a map from the sub-populations to their multiplicity in the list.
     * 
     * @param subs List of sub-populations.
     * @return Map from subpopulations to their list multiplicity.
     */
    private Map<SubPopulation, Integer> getSubCount(SubPopulation ... subs) {

        // Condense provided schema into a map of the form
        // SubPop->count, where count is the number of times
        // that specific offset appears as a reactant/product in this schema.
        Map<SubPopulation, Integer> subCount = Maps.newHashMap();
        
        for (SubPopulation sub : subs) {   
            
            if (!subCount.containsKey(sub))
                subCount.put(sub, 1);
            else {
                int val = subCount.get(sub);
                subCount.put(sub, val+1);
            }
        }

        return subCount;
    }

    /**
     * Add default reactant and product sub-population schemas which refer only
     * to those sub-populations at zero offset in the sub-population size arrays
     * contained in the state vector. This allows for easy specification of
     * reactions involving only structureless populations.
     */
    private void addScalarSubSchemas() {
        
        SubPopulation [] reactSubs = new SubPopulation[reactPopSchema.size()];
        for (int pidx=0; pidx<reactPopSchema.size(); pidx++)
            reactSubs[pidx] = new SubPopulation(reactPopSchema.get(pidx));
        reactSubCounts.add(getSubCount(reactSubs));
        
        SubPopulation [] prodSubs = new SubPopulation[prodPopSchema.size()];
        for (int pidx=0; pidx<prodPopSchema.size(); pidx++)
            prodSubs[pidx] = new SubPopulation(prodPopSchema.get(pidx));
        prodSubCounts.add(getSubCount(prodSubs));
    }

    /**
     * Adds rate of sub-population-specific reaction.
     *
     * @param rate
     */
    public void addSubRate(double rate) {
        rates.add(rate);
    }

    /**
     * Uses a single value to populate the rate list.
     *
     * Useful for defining collections of sub-population reactions which occur
     * at the same rate, or for specifying the rate of reactions involving
     * un-structured populations only.
     *
     * @param rate
     */
    public void setRate(double rate) {

        // Ensure at least a single sub-population schema is present,
        // even if only unstructured populations are involved.
        if (reactSubCounts.isEmpty())
            addScalarSubSchemas();

        for (int i = 0; i<reactSubCounts.size(); i++)
            rates.add(rate);
    }

    /**
     * Perform that part of the initialization process which can only be
     * completed once the reaction schema is in place.
     *
     * Also performs validation of the specified schema.
     */
    public void postSpecInit() {

        // Ensure at least a single sub-population schema is present,
        // even if only unstructured populations are involved.
        if (reactSubCounts.isEmpty())
            addScalarSubSchemas();

        // Perform sanity check on schema:
        if ((reactSubCounts.size()!=prodSubCounts.size())
                ||(reactSubCounts.size()!=rates.size()))
            throw new IllegalArgumentException("Inconsistent number of schemas and/or rates.");

        // Central record of number of sub-population reaction schemas:
        nSubSchemas = rates.size();
        
        // Preallocate memory for reaction propensities:
        propensities = Lists.newArrayList(rates);

        // Pre-calculate reaction-induced changes to sub-population sizes:
        calcDeltas();

    }
    
    /**
     * Pre-calculate reaction-induced changes to sub-population sizes.
     *
     * Determines the difference between each reactant and product
     * sub-population level schema defined in reactSubSchema and prodSubSchema.
     */
    private void calcDeltas() {

        deltaCounts = Lists.newArrayList();

        // Loosely, calculate deltas=prodLocSchema-reactLocSchema.

        for (int i = 0; i<nSubSchemas; i++) {
            Map<SubPopulation, Integer> deltaCount = Maps.newHashMap();
            
            for (SubPopulation sub : reactSubCounts.get(i).keySet()) 
                deltaCount.put(sub, -reactSubCounts.get(i).get(sub));
            
            for (SubPopulation sub : prodSubCounts.get(i).keySet()) {
                if (!deltaCount.containsKey(sub))
                    deltaCount.put(sub, prodSubCounts.get(i).get(sub));
                else {
                    int val = deltaCount.get(sub);
                    val += prodSubCounts.get(i).get(sub);
                    deltaCount.put(sub, val);
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
        
        for (int i = 0; i<nSubSchemas; i++) {
            double thisProp = rates.get(i);

            for (SubPopulation sub : reactSubCounts.get(i).keySet()) {
                for (int m = 0; m<reactSubCounts.get(i).get(sub); m++)
                    thisProp *= state.get(sub)-m;
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

        // Count occurrences of populations in reactant schema
        Map<Population, Integer> reactants = Maps.newHashMap();
        for (Population reactant : reactPopSchema)
            if (reactants.containsKey(reactant)) {
                int oldVal = reactants.get(reactant);
                reactants.put(reactant, oldVal+1);
            } else
                reactants.put(reactant, 1);

        // Count occurrences of populations in product schema
        Map<Population, Integer> products = Maps.newHashMap();
        for (Population product : prodPopSchema)
            if (products.containsKey(product)) {
                int oldVal = products.get(product);
                products.put(product, oldVal+1);
            } else
                products.put(product, 1);

        // Construct reaction string
        StringBuilder sb = new StringBuilder();
        if (reactionName != null)
            sb.append(reactionName).append(": ");

        if (reactants.size()>0) {
            boolean first = true;
            for (Population reactant : reactants.keySet()) {
                if (!first)
                    sb.append(" + ");
                else
                    first = false;

                int count = reactants.get(reactant);
                if (count>1)
                    sb.append(count);
                sb.append(reactant.getName());
            }
        } else
            sb.append("0");

        sb.append(" -> ");

        if (products.size()>0) {
            boolean first = true;
            for (Population product : products.keySet()) {
                if (!first)
                    sb.append(" + ");
                else
                    first = false;

                int count = products.get(product);
                if (count>1)
                    sb.append(count);
                sb.append(product.getName());
            }
        } else
            sb.append("0");

        return sb.toString();

    }

}