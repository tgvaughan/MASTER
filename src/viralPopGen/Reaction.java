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
public class Reaction {
	
	HashMap<Population,Integer> reactants, products, deltas;
	HashMap<Population,Boolean> mutateProduct;
	double[] rates, propensities;
	
	boolean genetic, mutation;
	
	/**
	 * Constructor.
	 */
	public Reaction() {
		reactants = new HashMap<Population,Integer>();
		products = new HashMap<Population,Integer>();
		deltas = new HashMap<Population,Integer>();
		mutateProduct = new HashMap<Population,Boolean>();
	}

	/**
	 * Add reactant to reaction spec.
	 * 
	 * @param pop Reactant population.
	 */
	public void addReactant(Population pop) {
		if (reactants.containsKey(pop))
			reactants.put(pop,reactants.get(pop)+1);
		else
			reactants.put(pop, 1);
		
		if (pop.genetic)
			genetic = true;
	}

	/**
	 * Add reactant product to reaction spec.
	 * 
	 * @param pop Product population.
	 * @param mutate True if genetic population mutates.
	 */
	public void addProduct(Population pop, boolean mutate) {
		if (products.containsKey(pop))
			products.put(pop, products.get(pop)+1);
		else
			products.put(pop, 1);
		
		if (pop.genetic) {
			genetic = true;
			if(mutate) {
				mutateProduct.put(pop, true);
				mutation = true;
			}
		} else
			mutateProduct.put(pop, false);
	}

	/**
	 * Set average rate that reaction will occur at.
	 * 
	 * @param rate
	 */
	public void setRate(double[] rate) {
		this.rates = rate;

		// Same number of propensities as rates:
		propensities = new double[rate.length];
	}
	
	/**
	 * Use existing reactant and product HashMaps to pre-calculate
	 * deltas for populations involved in the reaction.
	 */
	public void calcDeltas() {
		
		for (Population p : reactants.keySet())
			deltas.put(p, -1*reactants.get(p));
		
		for (Population p : products.keySet())
			deltas.put(p, deltas.get(p)+products.get(p));
	}
	
	/**
	 * Calculate instantaneous transition rates (propensities)
	 * for the given state.
	 * 
	 * @param state State vector used to calculate propensities.
	 */
	public void calcPropensities(State state) {

		// Simple scalar propensity:
		if (!genetic) {

			propensities[0] = rates[0];

			for (Population r : reactants.keySet()) {
				for (int m=0; m<reactants.get(r); m++)
					propensities[0] *= state.getScalar(r)-m;
			}

			return;
		}

		// Genetic propensity, without mutation:
		if (genetic && !mutation) {

			return;
		}

		// Genetic propensity with mutation:

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

		// Simple scalar leap:
		if (!genetic) {
			
			double nReacts = poissonian.nextInt(dt*propensities[0]);
			
			for (Population p : deltas.keySet()) {
				double oldSize = state.getScalar(p);
				state.setScalar(p, oldSize + nReacts*deltas.get(p));
			}

			return;
		}

		// Genetic reaction leap, without mutation:
		if (genetic && !mutation) {

			return ;
		}

		// Genetic reaction leap with mutation:

	}

}