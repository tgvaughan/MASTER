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
		
		int ratesNeeded = 1;
		for (int r=0; r<reactants.size(); r++) {
			for (int i=0; i<reactantLocs.get(r).length; i++)
				if (reactantLocs.get(r)[i]<1)
					ratesNeeded *= reactants.get(r).dims[i];
		}
		assert(rate.length == ratesNeeded);
			
		this.rate = rate;
	}

}