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
	
	ArrayList <Population> reactants, products;
	ArrayList <Integer[]> reactantLocs, productLocs;
	double[] rates, propensities;

	/**
	 * Constructor.
	 */
	public Reaction() {
		reactants = new ArrayList<Population>();
		products = new ArrayList<Population>();
		
		reactantLocs = new ArrayList<Integer[]>();
		productLocs = new ArrayList<Integer[]>();
	}

	/**
	 * Add reactant to reaction spec.
	 * 
	 * @param pop Reactant population.
	 * @param loc Specific reactant sub-population.
	 */
	public void addReactant(Population pop, Integer[] loc) {
		reactants.add(pop);
		reactantLocs.add(loc);
	}
	
	/**
	 * Add scalar reactant to reaction spec.
	 * 
	 * @param pop
	 */
	public void addReactant(Population pop) {
		Integer[] loc = new Integer[1];
		loc[0] = 0;
		addReactant(pop, loc);
	}

	/**
	 * Add reactant product to reaction spec.
	 * 
	 * @param pop Product population.
	 * @param loc Specific product sub-population.
	 */
	public void addProduct(Population pop, Integer[] loc) {
		products.add(pop);
		productLocs.add(loc);
	}

	/**
	 * Add scalar product to reaction spec.
	 * 
	 * @param pop
	 */
	public void addProduct(Population pop) {
		Integer[] loc = new Integer[1];
		loc[0] = 0;
		addProduct(pop, loc);
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
	 * Calculate instantaneous transition rates (propensities)
	 * for the given state.
	 * 
	 * @param state State vector used to calculate propensities.
	 */
	public void calcPropensities(State state) {
		
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

	}

}
