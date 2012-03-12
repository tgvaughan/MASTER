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
	ArrayList <Integer[][]> reactantLocs, productLocs;
	double[] rates, propensities;
	
	HashMap <Population, Integer[]> cReactantOffsets, cProductOffsets;
	HashMap <Population, Integer[]> cReactantN, cProductN;
	
	int nSubReacts;

	/**
	 * Constructor.
	 */
	public Reaction() {
		reactants = new ArrayList<Population>();
		products = new ArrayList<Population>();
		
		reactantLocs = new ArrayList<Integer[][]>();
		productLocs = new ArrayList<Integer[][]>();
		
		// Use zero to indicate a scalar reaction:
		nSubReacts = 0;
	}

	/**
	 * Add reactant to reaction spec.
	 * 
	 * @param pop Reactant population.
	 * @param locs Specific reactant sub-populations.
	 */
	public void addReactant(Population pop, Integer[][] locs) {
		reactants.add(pop);
		reactantLocs.add(locs);
		
		if (locs != null) {
			if (nSubReacts == 0)
				nSubReacts = locs.length;
			else
				assert(nSubReacts == locs.length);
		}
		
		if (cReactantOffsets.containsKey(pop)) {
			
		}
	}

	/**
	 * Add scalar reactant to reaction spec.
	 * 
	 * @param pop
	 */
	public void addReactant(Population pop) {
		addReactant(pop, null);
	}

	/**
	 * Add reactant product to reaction spec.
	 * 
	 * @param pop Product population.
	 * @param locs Specific product sub-populations.
	 */
	public void addProduct(Population pop, Integer[][] locs) {
		products.add(pop);
		productLocs.add(locs);
		
		if (locs != null) {
			if (nSubReacts == 0)
				nSubReacts = locs.length;
			else
				assert(nSubReacts == locs.length);
		}
	}

	/**
	 * Add scalar product to reaction spec.
	 * 
	 * @param pop
	 */
	public void addProduct(Population pop) {
		addProduct(pop, null);
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
	 * Calculate instantaneous transition rates (propensities)
	 * for the given state.
	 * 
	 * @param state State vector used to calculate propensities.
	 */
	public void calcPropensities(State state) {
		
		for (int r=0; r<nSubReacts; r++) {
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

	}

}
