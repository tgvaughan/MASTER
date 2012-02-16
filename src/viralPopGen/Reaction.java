package viralPopGen;

import java.util.*;

public class Reaction {
	
	ArrayList<Population> reactants, products;
	ArrayList<int[]> reactantLocs, productLocs;
	double[] rate;
	
	/**
	 * Constructor.
	 */
	public Reaction() {
		reactants = new ArrayList<Population>(0);
		products = new ArrayList<Population>(0);
		reactantLocs = new ArrayList<int[]>(0);
		productLocs = new ArrayList<int[]>(0);
	}

	/**
	 * Add reactant to reaction spec.
	 * 
	 * @param pop Reactant population.
	 * @param loc Specific reactant subpopulation.
	 */
	public void addReactant(Population pop, int[] loc) {
		reactants.add(pop);
		reactantLocs.add(loc);
	}

	/**
	 * Add reactant product to reaction spec.
	 * 
	 * @param pop Product population.
	 * @param loc Specific product subpopulation.
	 */
	public void addProduct(Population pop, int[] loc) {
		products.add(pop);
		productLocs.add(loc);
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
		
		int ratesNeeded = 1;
		for (int r=0; r<reactants.size(); r++) {
			for (int i=0; i<reactantLocs.get(r).length; i++)
				if (reactantLocs.get(r)[i]<1)
					ratesNeeded *= reactants.get(r).seqDims[i];
		}
		assert(rate.length == ratesNeeded);
			
		this.rate = rate;
	}

}