package viralPopGen;

import java.util.*;

public class Reaction {
	
	ArrayList<Population> reactants, products;
	ArrayList<Boolean> mutateProduct;
	double[] rate;
	
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
	}

	/**
	 * Set average rate that reaction will occur at.
	 * 
	 * @param rate
	 */
	public void setRate(double[] rate) {
		
		/* TODO:
		 * 
		 * Allow every element of each dimension of the
		 * reduced sequence space to have 3 rates associated
		 * with it -> 3*D rates per dimension.
		 * 
		 * Also allow a dimension to have just 1 rate associated
		 * with it, which is taken to be a uniform rate in that
		 * dimension.
		 */
	
	}

}