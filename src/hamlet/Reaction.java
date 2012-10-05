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
    List<Map<Population, Map<Integer, Integer>>> reactSubSchemas, prodSubSchemas, deltas;
    List<Double> rates, propensities;
    int nSubSchemas;

    /**
     * Constructor (with name).
     */
    public Reaction(String reactionName) {
        reactSubSchemas = Lists.newArrayList();
        prodSubSchemas = Lists.newArrayList();

        rates = Lists.newArrayList();
        
        this.reactionName = reactionName;
    }
    
    /**
     * Constructor (no name).
     */
    public Reaction() {
        reactSubSchemas = Lists.newArrayList();
        prodSubSchemas = Lists.newArrayList();

        rates = Lists.newArrayList();
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
    public void addReactantSubSchema(int[]... subs) {

        // Check for consistent number of sub-populations:
        if (subs.length!=reactPopSchema.size())
            throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");

        int[] offsets = new int[reactPopSchema.size()];
        for (int pidx = 0; pidx<reactPopSchema.size(); pidx++)
            if (subs[pidx]!=null)
                offsets[pidx] = reactPopSchema.get(pidx).subToOffset(subs[pidx]);
            else
                offsets[pidx] = 0;

        // Call internal method to complete addition of schema:
        addSubSchemaOffsets(reactPopSchema, reactSubSchemas, offsets);

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
    public void addProductSubSchema(int[]... subs) {

        // Check for consistent number of sub-populations:
        if (subs.length!=prodPopSchema.size())
            throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");

        // Calculate offsets from sub-population vectors:
        int[] offsets = new int[subs.length];
        for (int i = 0; i<subs.length; i++)
            if (subs[i]!=null)
                offsets[i] = prodPopSchema.get(i).subToOffset(subs[i]);
            else
                offsets[i] = 0;

        // Call internal method to complete addition of schema:
        addSubSchemaOffsets(prodPopSchema, prodSubSchemas,offsets);
    }
    
    /**
     * Internal method which handles the task of adding the sub-population level
     * reaction reactant/product schema. Takes a list of offsets into the State
     * sub-population size arrays rather than the sub-population specification
     * vectors.
     *
     * @param popSchema Population-level reactant or product schema.
     * @param subSchemaList List of sub-population-level schema.
     * @param offsets list of product sub-population offsets.
     */
    private void addSubSchemaOffsets(
            List<Population> popSchema,
            List<Map<Population, Map<Integer, Integer>>> subSchemaList,
            int... offsets) {

        // Condense provided schema into a map of the form
        // pop->offset->count, where count is the number of times
        // that specific offset appears as a reactant/product in this schema.
        Map<Population, Map<Integer, Integer>> subSchema = Maps.newHashMap();
        for (int pidx = 0; pidx<popSchema.size(); pidx++) {

            Population pop = popSchema.get(pidx);

            if (!subSchema.containsKey(pop)) {
                Map<Integer, Integer> offsetMap = Maps.newHashMap();
                offsetMap.put(offsets[pidx], 1);
                subSchema.put(pop, offsetMap);
            } else
                if (!subSchema.get(pop).containsKey(offsets[pidx]))
                    subSchema.get(pop).put(offsets[pidx], 1);
                else {
                    int val = subSchema.get(pop).get(offsets[pidx]);
                    subSchema.get(pop).put(offsets[pidx], val+1);
                }
        }

        // Add newly-condensed map to reactLocSchemas,
        // which is a list of such maps:
        subSchemaList.add(subSchema);
    }

    /**
     * Add default reactant and product sub-population schemas which refer only
     * to those sub-populations at zero offset in the sub-population size arrays
     * contained in the state vector. This allows for easy specification of
     * reactions involving structureless populations.
     */
    private void addScalarSubSchemas() {

        int[] reactOffsets = new int[reactPopSchema.size()];
        for (int pidx = 0; pidx<reactPopSchema.size(); pidx++)
            reactOffsets[pidx] = 0;
        addSubSchemaOffsets(reactPopSchema, reactSubSchemas, reactOffsets);

        int[] prodOffsets = new int[prodPopSchema.size()];
        for (int pidx = 0; pidx<prodPopSchema.size(); pidx++)
            prodOffsets[pidx] = 0;
        addSubSchemaOffsets(prodPopSchema, prodSubSchemas, prodOffsets);
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
        if (reactSubSchemas.isEmpty())
            addScalarSubSchemas();

        for (int i = 0; i<reactSubSchemas.size(); i++)
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
        if (reactSubSchemas.isEmpty())
            addScalarSubSchemas();

        // Perform sanity check on schema:
        if ((reactSubSchemas.size()!=prodSubSchemas.size())
                ||(reactSubSchemas.size()!=rates.size()))
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

        deltas = Lists.newArrayList();

        // (Loosely, calculate deltas=prodLocSchema-reactLocSchema.)

        for (int i = 0; i<nSubSchemas; i++) {
            Map<Population, Map<Integer, Integer>> popMap =
                    new HashMap<Population, Map<Integer, Integer>>();

            for (Population pop : reactSubSchemas.get(i).keySet()) {
                Map<Integer, Integer> offsetMap = Maps.newHashMap();
                for (int offset : reactSubSchemas.get(i).get(pop).keySet())
                    offsetMap.put(offset, -reactSubSchemas.get(i).get(pop).get(offset));

                popMap.put(pop, offsetMap);
            }

            for (Population pop : prodSubSchemas.get(i).keySet())
                if (!popMap.containsKey(pop)) {
                    Map<Integer, Integer> offsetMap = Maps.newHashMap();
                    for (int offset : prodSubSchemas.get(i).get(pop).keySet())
                        offsetMap.put(offset, prodSubSchemas.get(i).get(pop).get(offset));

                    popMap.put(pop, offsetMap);

                } else
                    for (int offset : prodSubSchemas.get(i).get(pop).keySet())
                        if (!popMap.get(pop).containsKey(offset)) {
                            int val = prodSubSchemas.get(i).get(pop).get(offset);
                            popMap.get(pop).put(offset, val);
                        } else {
                            int val = popMap.get(pop).get(offset);
                            val += prodSubSchemas.get(i).get(pop).get(offset);
                            popMap.get(pop).put(offset, val);
                        }

            deltas.add(popMap);
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

            for (Population pop : reactSubSchemas.get(i).keySet())
                for (int offset : reactSubSchemas.get(i).get(pop).keySet())
                    for (int m = 0; m<reactSubSchemas.get(i).get(pop).get(offset); m++)
                        thisProp *= state.get(pop, offset)-m;

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
            sb.append(reactionName + ": ");

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