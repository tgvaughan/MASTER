package viralPopGen;

import java.util.*;
import cern.jet.random.Poisson;

/**
 * Class of objects describing the reactions which occur
 * between the various populations in the model.  Reactions
 * may involve both scalar and genetically-diverse
 * populations, and they may involve genetic mutation.
 * 
 * @author Tim Vaughan
 *
 */
/**
 * @author Tim Vaughan
 *
 */
public class Reaction {

	HashMap <Population, ArrayList<HashMap<Integer, Integer>>> reactants, products, deltas;
	double[] rates, propensities;
	
	int nSubReacts;
	
	/**
	 * Constructor.
	 */
	public Reaction() {
		reactants = new HashMap<Population, ArrayList<HashMap<Integer, Integer>>>();
		products = new HashMap<Population, ArrayList<HashMap<Integer, Integer>>>();
		deltas = new HashMap<Population, ArrayList<HashMap<Integer, Integer>>>();
		
		nSubReacts = 0;
	}

	/**
	 * Add reactant to reaction spec.
	 * 
	 * @param pop Reactant population.
	 * @param locs Specific reactant sub-populations.
	 */
	public void addReactant(Population pop, int[][] locs) {

		// Ensure number of sub-reactions is consistent:
		if (nSubReacts>0)
			assert(nSubReacts==locs.length);
		else
			nSubReacts = locs.length;

		// Check for multiple occurrences of reactant populations:
		if (!reactants.containsKey(pop)) {
			ArrayList<HashMap<Integer, Integer>> locVec = new ArrayList<HashMap<Integer, Integer>>();
			
			for (int i=0; i<locs.length; i++) {
				HashMap<Integer,Integer> locMap = new HashMap<Integer, Integer>();
				locMap.put(pop.locToOffset(locs[i]), 1);
				locVec.add(locMap);
			}
			reactants.put(pop, locVec);

		} else {

			for (int i=0; i<locs.length; i++) {

				int offset = pop.locToOffset(locs[i]);
				
				if (reactants.get(pop).get(i).containsKey(offset)) {
					int oldVal = reactants.get(pop).get(i).get(offset);
					reactants.get(pop).get(i).put(offset, oldVal+1);
				}
			}
		}
	}

	/**
	 * Add scalar reactant to reaction spec.
	 * 
	 * @param pop
	 */
	public void addReactant(Population pop) {

		if (!reactants.containsKey(pop)) {
			ArrayList<HashMap<Integer, Integer>> locVec = new ArrayList<HashMap<Integer,Integer>>();
			HashMap<Integer, Integer> locMap = new HashMap<Integer, Integer>();
			locMap.put(0, 1);
			locVec.add(locMap);
			reactants.put(pop, locVec);
		} else {
			int oldVal = reactants.get(pop).get(0).get(0);
			reactants.get(pop).get(0).put(0, oldVal+1);
		}
	}
	
	/**
	 * Add reactant to reaction spec.
	 * 
	 * @param pop Reactant population.
	 * @param locs Specific reactant sub-populations.
	 */
	public void addProduct(Population pop, int[][] locs) {

		// Ensure number of sub-reactions is consistent:
		if (nSubReacts>0)
			assert(nSubReacts==locs.length);
		else
			nSubReacts = locs.length;

		// Check for multiple occurrences of reactant populations:
		if (!products.containsKey(pop)) {
			ArrayList<HashMap<Integer, Integer>> locVec = new ArrayList<HashMap<Integer, Integer>>();
			
			for (int i=0; i<locs.length; i++) {
				HashMap<Integer,Integer> locMap = new HashMap<Integer, Integer>();
				locMap.put(pop.locToOffset(locs[i]), 1);
				locVec.add(locMap);
			}
			products.put(pop, locVec);

		} else {

			for (int i=0; i<locs.length; i++) {

				int offset = pop.locToOffset(locs[i]);
				
				if (products.get(pop).get(i).containsKey(offset)) {
					int oldVal = products.get(pop).get(i).get(offset);
					products.get(pop).get(i).put(offset, oldVal+1);
				}
			}
		}
	}

	/**
	 * Add scalar reactant to reaction spec.
	 * 
	 * @param pop
	 */
	public void addProduct(Population pop) {

		if (!products.containsKey(pop)) {
			ArrayList<HashMap<Integer, Integer>> locVec = new ArrayList<HashMap<Integer,Integer>>();
			HashMap<Integer, Integer> locMap = new HashMap<Integer, Integer>();
			locMap.put(0, 1);
			locVec.add(locMap);
			products.put(pop, locVec);
		} else {
			int oldVal = products.get(pop).get(0).get(0);
			products.get(pop).get(0).put(0, oldVal+1);
		}
	}


	/**
	 * Set average rate that reaction will occur at.
	 * 
	 * @param rates
	 */
	public void setRate(double[] rates) {
		
		assert(rates.length == nSubReacts);
		this.rates = rates.clone();

		// Same number of propensities as rates:
		propensities = new double[rates.length];
	}
	
	/**
	 * Pre-calculate changes in each sub-population due to reactions.
	 */
	public void getDeltas() {
		
		for (Population pop : reactants.keySet()) {
			
			ArrayList<HashMap<Integer,Integer>> locVec = new ArrayList<HashMap<Integer,Integer>>();
			for (int r = 0; r<reactants.get(pop).size(); r++) {
				
				HashMap<Integer,Integer> locMap = new HashMap<Integer,Integer>();
				for (int offset : reactants.get(pop).get(r).keySet())
					locMap.put(offset, -reactants.get(pop).get(r).get(offset));
				
				locVec.add(locMap);
			}
			deltas.put(pop, locVec);
		}
		
		for (Population pop : products.keySet()) {
			
			if (deltas.containsKey(pop)) {
				
				for (int r=0; r<products.get(pop).size(); r++) {
					for (int offset : products.get(pop).get(r).keySet()) {
						
						if (deltas.get(pop).get(r).containsKey(offset)) {
							int oldVal = deltas.get(pop).get(r).get(offset);
							int newVal = oldVal + products.get(pop).get(r).get(offset);
							deltas.get(pop).get(r).put(offset, newVal);
						} else {
							deltas.get(pop).get(r).put(offset, products.get(pop).get(r).get(offset));
						}
						
					}
				}
				
			} else {
				
				ArrayList<HashMap<Integer,Integer>> locVec = new ArrayList<HashMap<Integer,Integer>>();
				for (int r=0; r<products.get(pop).size(); r++) {
					
					HashMap<Integer,Integer> locMap = new HashMap<Integer,Integer>();
					for (int offset : products.get(pop).get(r).keySet())
						locMap.put(offset, products.get(pop).get(r).get(offset));
					
					locVec.add(locMap);
				}
				deltas.put(pop, locVec);
			}
		}
	}
	
	/**
	 * Calculate instantaneous transition rates (propensities)
	 * for the given state.
	 * 
	 * @param state State vector used to calculate propensities.
	 */
	public void calcPropensities(State state) {
		
		for (int r=0; r<nSubReacts; r++) {
			propensities[r] = rates[r];
			for (Population pop : reactants.keySet()) {
				for (int offset : reactants.get(pop).get(r).keySet()) {
					for (int m=0; m<reactants.get(pop).get(r).get(offset); m++)
						propensities[r] *= state.get(pop, offset)-m;
				}
			}
		}
	}

	/**
	 * Implement tau leap for specified dt.  Make sure that
	 * calcPropensities() has been called first!
	 * 
	 * @param state Current state with pre-calculated propensities.
	 * @param dt Time increment over which to leap.
	 * @param poissonian Poissonian RNG.
	 */
	public void leap(State state, double dt, Poisson poissonian) {
		
		for (int r=0; r<nSubReacts; r++) {
			
			double n = poissonian.nextInt(propensities[r]*dt);
			
			
		}
		
	}

}
