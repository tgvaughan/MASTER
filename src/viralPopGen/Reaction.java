package viralPopGen;

import java.util.*;

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
	
	ArrayList<Population> reactants, products;
	ArrayList<Boolean> mutateProduct;
	double[] rates, propensities;
	
	boolean genetic, mutation;
	
	/**
	 * Constructor.
	 */
	public Reaction() {
		reactants = new ArrayList<Population>(0);
		products = new ArrayList<Population>(0);
		mutateProduct = new ArrayList<Boolean>(0);
	}

	/**
	 * Add reactant to reaction spec.
	 * 
	 * @param pop Reactant population.
	 */
	public void addReactant(Population pop) {
		reactants.add(pop);
		
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
		products.add(pop);
		mutateProduct.add(mutate);
		
		if (pop.genetic) {
			genetic = true;
			if(mutate) {
				mutateProduct.add(true);
				mutation = true;
			}
		} else
			mutateProduct.add(false);
	}

	/**
	 * Set average rate that reaction will occur at.
	 * 
	 * @param rate
	 */
	public void setRate(double[] rate) {
		this.rates = rate;
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
			
			return;
		}
		
		// Genetic propensity, without mutation:
		if (genetic && !mutation) {
			
			return;
		}
		
		// Genetic propensity with mutation:
		
	}

}