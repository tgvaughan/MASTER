package viralPopGen;

import java.util.*;

public class State {
	
	Model model;
	HashMap<Population,Double[]> genPopSizes;
	HashMap<Population,Double> scalarPopSizes;
	
	
	/**
	 * Constructor
	 * 
	 * @param model Model defining the state space.
	 */
	public State (Model model) {
		
		this.model = model;
		
		// Initialise genPopSizes and scalarPopSizes:
		
		genPopSizes = new HashMap<Population, Double[]>(model.geneticPops.size());
		for (Population p : genPopSizes.keySet())
			genPopSizes.put(p, new Double[model.typeNum]);
		
		scalarPopSizes = new HashMap<Population, Double>(model.scalarPops.size());
		
	}

	/**
	 * Get offset into popSizes vector.
	 * 
	 * @param location Array specification of sub-population.
	 * @return Offset into popSizes vector.
	 */
	private int locToOffset(int[] loc) {
		
		int offset = 0;
		int mul = 1;

		for (int d=0; d<model.seqDims.length; d++) {
			offset += loc[d]*mul;
			mul *= model.seqDims[d];
		}
		
		return offset;
	}

	/**
	 * Get size of a particular genetic sub-population.
	 * 
	 * @param name		Name of sub-population.
	 * @param loc	Specific sub-population location.
	 * @return Size of sub-population.
	 */
	public double getGen(Population p, int[] loc) {
		return genPopSizes.get(p)[locToOffset(loc)];
	}
	
	/**
	 * Set size of a particular genetic sub-population.
	 * 
	 * @param name		Name of sub-population to modify.
	 * @param loc	Specific sub-population location.
	 */
	public void setGen(Population p, int[] loc, double value) {
		genPopSizes.get(p)[locToOffset(loc)] = value;
	}
	

	/**
	 * Add two states together.
	 * 
	 * @param arg	State to add.
	 * @return		Result of addition.
	 */
	public State add(State arg) {

		for (Population p : genPopSizes.keySet()) {
			for (int i=0; i<genPopSizes.get(p).length; i++)
				genPopSizes.get(p)[i] += arg.genPopSizes.get(p)[i];
		}
		
		for (Population p : scalarPopSizes.keySet()) {
			double thisSize = scalarPopSizes.get(p);
			scalarPopSizes.put(p, thisSize + arg.scalarPopSizes.get(p));
		}

		return this;
	}

	/**
	 * Multiply state by an integer.
	 * 
	 * @param arg	Integer to multiply.
	 * @return		Result of multiplication.
	 */
	public State mul(int arg) {
		
		for (Population p : genPopSizes.keySet()) {
			for (int i=0; i<genPopSizes.get(p).length; i++)
				genPopSizes.get(p)[i] *= arg;
		}
		
		for (Population p : scalarPopSizes.keySet()) {
			double thisSize = scalarPopSizes.get(p);
			scalarPopSizes.put(p, thisSize*arg);
		}

		return this;
	}

}
