package viralPopGen;

import java.util.*;

public class Reaction {
	
	ArrayList<Population> reactants, products;
	ArrayList<int[]> reactantLocs, productLocs;
	double[] rate;

	/**
	 * Add reactant to reaction spec.
	 * 
	 * @param pop Reactant population.
	 * @param loc Specific reactant subpopulation.
	 */
	public void addReactant(Population pop, int[] loc) {

	}
	
	/**
	 * Add reactant product to reaction spec.
	 * 
	 * @param pop Product population.
	 * @param loc Specific product subpopulation.
	 */
	public void addProduct(Population pop, int[] loc) {
		
	}
	
	/**
	 * Set average rate that reaction will occur at.
	 * 
	 * @param rate
	 */
	public void setRate(double[] rate) {
		this.rate = rate;
	}

}