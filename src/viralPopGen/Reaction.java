package viralPopGen;

import java.util.*;
import com.google.common.collect.*;

import cern.jet.random.Poisson;

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
	
	double[] rates, propensities;
	int nSubSchemas;
	
	/**
	 * Constructor.
	 */
	public Reaction() {
		reactSubSchemas = Lists.newArrayList();
		prodSubSchemas = Lists.newArrayList();
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
	 * appear in subsequent calls to addProductLocSchema and in wihch order
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
	 * calls to addReactantLocSchema() define an array of such schemas
	 * that must be aligned with a similar array created by corresponding
	 * calls to addProductLocSchema().
	 * 
	 * @param subs	varargs list of reactant sub-populations.
	 */
	public void addReactantSubSchema(int[] ... subs) {
		
		// Check for consistent number of sub-populations:
		if (subs.length != reactPopSchema.length)
			throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");
		
		int[] offsets = new int[reactPopSchema.length];
		for (int pidx=0; pidx<reactPopSchema.length; pidx++)
			offsets[pidx] = reactPopSchema[pidx].subToOffset(subs[pidx]);
		
		// Call internal method to complete addition of schema:
		addReactantSubSchema(offsets);
		
	}
	
	/**
	 * Internal method which handles the task of adding the sub-population
	 * level reaction reactant schema.  Takes a list of offsets into the
	 * State sub-population size arrays rather than the sub-population
	 * specification vectors.
	 * 
	 * @param offsets	list of product sub-population offsets.
	 */
	private void addReactantSubSchema(int ... offsets) {
		
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
	 * @param subs	varargs list of product sub-populations.
	 */
	public void addProductSubSchema(int[] ... subs) {
	
		// Check for consistent number of sub-populations:
		if (subs.length != prodPopSchema.length)
			throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");

		// Calculate offsets from sub-population vectors:
		int[] offsets = new int[subs.length];
		for (int i=0; i<subs.length; i++)
			offsets[i] = prodPopSchema[i].subToOffset(subs[i]);
		
		// Call internal method to complete addition of schema:
		addProductSubSchema(offsets);
	}
	
	/**
	 * Internal method which handles the task of adding the sub-population
	 * level reaction product schema.  Takes a list of offsets into the
	 * State sub-population size arrays rather than the sub-population
	 * specification vectors.
	 * 
	 * @param offsets	list of product sub-population offsets.
	 */
	private void addProductSubSchema(int ... offsets) {

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
		addReactantSubSchema(reactOffsets);
		
		int[] prodOffsets = new int[prodPopSchema.length];
		for (int pidx=0; pidx<prodPopSchema.length; pidx++)
			prodOffsets[pidx] = 0;
		addProductSubSchema(prodOffsets);
	}

	/**
	 * Set average rate that reaction will occur at.
	 * 
	 * @param rates	varargs list of rates to use.
	 */
	public void setRate(double ... rates) {
		
		if (reactSubSchemas.size() != prodSubSchemas.size())
			throw new IllegalArgumentException("Unequal numbers of reactant and product schemas.");
		nSubSchemas = reactSubSchemas.size();
		
		if (nSubSchemas == 0) {
			// Assume scalar reaction:
			addScalarSubSchemas();
			nSubSchemas++;
		}
		
		if (nSubSchemas != rates.length)
			throw new IllegalArgumentException("Inconsistent number of rates specified.");
		this.rates = rates.clone();

		// Same number of propensities as rates:
		propensities = new double[rates.length];
	}
	
	/**
	 * Pre-calculate reaction-induced changes to sub-population sizes.
	 */
	public void calcDeltas() {
		
		deltas = Lists.newArrayList();
		
		// Determine the difference between each reactant and product
		// sub-population level schema defined in reactLocSchema and
		// prodLocSchema.  (Loosely, deltas=prodLocSchema-reactLocSchema.)
		
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
			propensities[i] = rates[i];
			
			for (Population pop : reactSubSchemas.get(i).keySet()) {
				for (int offset : reactSubSchemas.get(i).get(pop).keySet()) {
					for (int m=0; m<reactSubSchemas.get(i).get(pop).get(offset); m++) {
						propensities[i] *= state.get(pop, offset)-m;
					}
				}
			}
		}
	}
	
	/**
	 * Generate appropriate random state change according to
	 * Gillespie's tau-leaping algorithm.
	 * 
	 * @param state 		State to modify.
	 * @param tau			Time increment over which to leap.
	 * @param poissonian	Poissonian RNG.
	 */
	public void leap(State state, double tau, Poisson poissonian) {
		
		for (int i=0; i<nSubSchemas; i++) {
			
			// Draw number of reactions to fire within time tau:
			double q = (double)poissonian.nextInt(propensities[i]*tau);
			
			// Implement reactions:
			for (Population pop : deltas.get(i).keySet()) {
				for (int offset : deltas.get(i).get(pop).keySet())
					state.add(pop, offset, q*deltas.get(i).get(pop).get(offset));
			}
		}
		
	}
}