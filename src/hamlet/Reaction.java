package hamlet;

import java.util.*;
import com.google.common.collect.*;

import hamlet.math.Poisson;

/**
 * Class of objects describing the reactions which occur
 * between the various populations in the model.  Reactions
 * may involve both scalar and structured populations.
 * 
 * @author Tim Vaughan
 *
 */
public class Reaction {

	Population [] reactPopSchema, prodPopSchema;
	List<Map<Population,Map<Integer,Integer>>> reactSubSchemas, prodSubSchemas, deltas;

	List<Double> rates, propensities;
	int nSubSchemas;

	/**
	 * Constructor.
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
	public void setReactantSchema(Population ... reactantPopSchema) {
		this.reactPopSchema = reactantPopSchema;
	}

	/**
	 * Define product population-level schema by choosing which populations
	 * appear in subsequent calls to addProductLocSchema and in which order
	 * those populations appear.
	 * 
	 * @param productPopSchema	varargs list of product populations.
	 */
	public void setProductSchema(Population ... productPopSchema) {
		this.prodPopSchema = productPopSchema;
	}

	/**
	 * Define a particular sub-population-level schema by listing the
	 * individual sub-population reactants involved in a reaction. Subsequent
	 * calls to addReactantLocSchema() define a list of such schemas
	 * that must be aligned with a similar list created by corresponding
	 * calls to addProductLocSchema().
	 * 
	 * Note that the "sub-population" corresponding to an unstructured
	 * population in the reactant schema should be set to null.
	 * 
	 * @param subs	varargs list of reactant sub-populations.
	 */
	public void addReactantSubSchema(int[] ... subs) {

		// Check for consistent number of sub-populations:
		if (subs.length != reactPopSchema.length)
			throw new IllegalArgumentException
					("Inconsistent number of sub-populations specified.");

		int[] offsets = new int[reactPopSchema.length];
		for (int pidx=0; pidx<reactPopSchema.length; pidx++) {
			if (subs[pidx] != null)
				offsets[pidx] = reactPopSchema[pidx].subToOffset(subs[pidx]);
			else
				offsets[pidx] = 0;
		}

		// Call internal method to complete addition of schema:
		addReactantSubSchemaOffsets(offsets);

	}

	/**
	 * Internal method which handles the task of adding the sub-population
	 * level reaction reactant schema.  Takes a list of offsets into the
	 * State sub-population size arrays rather than the sub-population
	 * specification vectors.
	 * 
	 * @param offsets	list of product sub-population offsets.
	 */
	private void addReactantSubSchemaOffsets(int ... offsets) {

		// Condense provided schema into a map of the form
		// pop->offset->count, where count is the number of times
		// that specific offset appears as a reactant in this schema.
		Map<Population,Map<Integer,Integer>> subSchema = Maps.newHashMap();
		for (int pidx=0; pidx<reactPopSchema.length; pidx++) {

			Population pop = reactPopSchema[pidx];

			if (!subSchema.containsKey(pop)) {
				Map<Integer,Integer> offsetMap = Maps.newHashMap();
				offsetMap.put(offsets[pidx], 1);
				subSchema.put(pop, offsetMap);
			} else {
				if (!subSchema.get(pop).containsKey(offsets[pidx]))
					subSchema.get(pop).put(offsets[pidx], 1);
				else {
					int val = subSchema.get(pop).get(offsets[pidx]);
					subSchema.get(pop).put(offsets[pidx], val+1);
				}
			}
		}

		// Add newly-condensed map to reactLocSchemas,
		// which is a list of such maps:
		reactSubSchemas.add(subSchema);
	}

	/**
	 * Define a particular sub-population-level schema by listing the
	 * individual sub-population products involved in a reaction. Subsequent
	 * calls to addProductLocSchema() define an array of such schemas
	 * that must be aligned with a similar array created by corresponding
	 * calls to addReactantLocSchema().
	 * 
	 * Note that the "sub-population" corresponding to an unstructured
	 * population in the product schema should be set to null.
	 * 
	 * @param subs	varargs list of product sub-populations.
	 */
	public void addProductSubSchema(int[] ... subs) {

		// Check for consistent number of sub-populations:
		if (subs.length != prodPopSchema.length)
			throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");

		// Calculate offsets from sub-population vectors:
		int[] offsets = new int[subs.length];
		for (int i=0; i<subs.length; i++) {
			if (subs[i] != null)
				offsets[i] = prodPopSchema[i].subToOffset(subs[i]);
			else
				offsets[i] = 0;
		}

		// Call internal method to complete addition of schema:
		addProductSubSchemaOffsets(offsets);
	}

	/**
	 * Internal method which handles the task of adding the sub-population
	 * level reaction product schema.  Takes a list of offsets into the
	 * State sub-population size arrays rather than the sub-population
	 * specification vectors.
	 * 
	 * @param offsets	list of product sub-population offsets.
	 */
	private void addProductSubSchemaOffsets(int ... offsets) {

		// Condense provided schema into a map of the form
		// pop->offset->count, where count is the number of times
		// that specific offset appears as a product in this schema:
		Map<Population,Map<Integer,Integer>> subSchema = Maps.newHashMap();
		for (int pidx=0; pidx<prodPopSchema.length; pidx++) {

			Population pop = prodPopSchema[pidx];

			if (!subSchema.containsKey(pop)) {
				Map<Integer,Integer> offsetMap = Maps.newHashMap();
				offsetMap.put(offsets[pidx], 1);
				subSchema.put(pop, offsetMap);
			} else {
				if (!subSchema.get(pop).containsKey(offsets[pidx]))
					subSchema.get(pop).put(offsets[pidx], 1);
				else {
					int val = subSchema.get(pop).get(offsets[pidx]);
					subSchema.get(pop).put(offsets[pidx], val+1);
				}
			}
		}

		// Add newly-condensed map to prodLocSchemas,
		// which is a list of such maps:
		prodSubSchemas.add(subSchema);
	}

	/**
	 * Add default reactant and product sub-population schemas
	 * which refer only to those sub-populations at zero offset
	 * in the sub-population size arrays contained in the state
	 * vector.  This allows for easy specification of reactions
	 * involving structureless populations.
	 */
	private void addScalarSubSchemas() {

		int[] reactOffsets = new int[reactPopSchema.length];
		for (int pidx=0; pidx<reactPopSchema.length; pidx++)
			reactOffsets[pidx] = 0;
		addReactantSubSchemaOffsets(reactOffsets);

		int[] prodOffsets = new int[prodPopSchema.length];
		for (int pidx=0; pidx<prodPopSchema.length; pidx++)
			prodOffsets[pidx] = 0;
		addProductSubSchemaOffsets(prodOffsets);
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
	 * Useful for defining collections of sub-population reactions which
	 * occur at the same rate, or for specifying the rate of reactions
	 * involving un-structured populations only.
	 *
	 * @param rate
	 */
	public void setRate(double rate) {

		// Ensure at least a single sub-population schema is present,
		// even if only unstructured populations are involved.
		if (reactSubSchemas.isEmpty())
			addScalarSubSchemas();

		for (int i=0; i<reactSubSchemas.size(); i++)
			rates.add(rate);
	}

	/**
	 * Perform that part of the initialization process which can
	 * only be completed once the reaction schema is in place.
	 * 
	 * Also performs validation of the specified schema.
	 */
	public void postSpecInit() {

		// Ensure at least a single sub-population schema is present,
		// even if only unstructured populations are involved.
		if (reactSubSchemas.isEmpty())
			addScalarSubSchemas();

		// Perform sanity check on schema:
		if ((reactSubSchemas.size() != prodSubSchemas.size())
				|| (reactSubSchemas.size() != rates.size())) {
			throw new IllegalArgumentException
					("Inconsistent number of schemas and/or rates.");
		}

		// Central record of number of sub-population reaction schemas:
		nSubSchemas = rates.size();

		// Pre-allocate memory for propensity list:
		propensities = Lists.newArrayList(rates);

		// Pre-calculate reaction-induced changes to sub-population sizes:
		calcDeltas();

	}

	/**
	 * Pre-calculate reaction-induced changes to sub-population sizes.
	 * 
	 * Determines the difference between each reactant and product
	 * sub-population level schema defined in reactSubSchema and
	 * prodSubSchema.
	 */
	private void calcDeltas() {

		deltas = Lists.newArrayList();

		// (Loosely, calculate deltas=prodLocSchema-reactLocSchema.)

		for (int i=0; i<nSubSchemas; i++) {
			Map<Population,Map<Integer,Integer>> popMap =
					new HashMap<Population, Map<Integer,Integer>>();

			for (Population pop : reactSubSchemas.get(i).keySet()) {
				Map<Integer,Integer> offsetMap = Maps.newHashMap();
				for (int offset : reactSubSchemas.get(i).get(pop).keySet())
					offsetMap.put(offset, -reactSubSchemas.get(i).get(pop).get(offset));

				popMap.put(pop, offsetMap);
			}

			for (Population pop : prodSubSchemas.get(i).keySet()) {
				if (!popMap.containsKey(pop)) {
					Map<Integer,Integer> offsetMap = Maps.newHashMap();
					for (int offset : prodSubSchemas.get(i).get(pop).keySet())
						offsetMap.put(offset, prodSubSchemas.get(i).get(pop).get(offset));

					popMap.put(pop, offsetMap);

				} else {

					for (int offset : prodSubSchemas.get(i).get(pop).keySet()) {
						if (!popMap.get(pop).containsKey(offset)) {
							int val = prodSubSchemas.get(i).get(pop).get(offset);
							popMap.get(pop).put(offset, val);
						} else {
							int val = popMap.get(pop).get(offset);
							val += prodSubSchemas.get(i).get(pop).get(offset);
							popMap.get(pop).put(offset, val);
						}
					}
				}
			}

			deltas.add(popMap);
		}
	}

	/**
	 * Calculate instantaneous reaction rates (propensities) from
	 * a given system state.
	 * 
	 * @param state	State used to calculate propensities.
	 */
	public void calcPropensities(State state) {

		for (int i=0; i<nSubSchemas; i++) {
			double thisProp = rates.get(i);

			for (Population pop : reactSubSchemas.get(i).keySet()) {
				for (int offset : reactSubSchemas.get(i).get(pop).keySet()) {
					for (int m=0; m<reactSubSchemas.get(i).get(pop).get(offset); m++) {
						thisProp *= state.get(pop, offset)-m;
					}
				}
			}

			propensities.set(i, thisProp);
		}
	}

	/**
	 * Generate appropriate random state change according to
	 * Gillespie's tau-leaping algorithm.
	 * 
	 * @param state 		State to modify.
	 * @param spec			Simulation spec.
	 */
	public void leap(State state, Spec spec) {

		for (int i=0; i<nSubSchemas; i++) {

			// Draw number of reactions to fire within time tau:
			double q = Poisson.nextDouble(propensities.get(i)*spec.getDt());

			// Implement reactions:
			for (Population pop : deltas.get(i).keySet()) {
				for (int offset : deltas.get(i).get(pop).keySet())
					state.add(pop, offset, q*deltas.get(i).get(pop).get(offset));
			}
		}

	}

	/**
	 * Generate random modification to tree leaves according to
	 * Gillespie's tau-leaping algorithm.
	 * 
	 * @param leaves
	 * @param state
	 * @param spec 
	 */
	public void treeLeap(Map<Population,List<Set<Node>>> leaves,
			State state,
			TreeSpec spec) {

		for (int i=0; i<nSubSchemas; i++) {

			// Draw number of reactions to fire within time tau:
			double q = Poisson.nextDouble(propensities.get(i)*spec.getDt());

			// Implement reactions:
			for (int j=0; j<q; j++) {

				for (Population pop : reactPopSchema) {

					if (spec.treePops.contains(pop)) {


					}

					for (int offset : reactSubSchemas.get(i).get(pop).keySet()) {

					}

				}

			}
		}

	}
}