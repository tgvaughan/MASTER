package viralPopGen;

import java.util.*;

/**
 * Class of objects describing distinct states of the
 * simulated system.
 * 
 * @author Tim Vaughan
 *
 */
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
		for (Population p : model.geneticPops)
			genPopSizes.put(p, new Double[model.typeNum]);
		
		scalarPopSizes = new HashMap<Population, Double>(model.scalarPops.size());
		
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param oldState State to copy.
	 */
	public State (State oldState) {
		this.model = oldState.model;
		
		// Copy genetic population sizes:
		this.genPopSizes = new HashMap<Population, Double[]>(model.geneticPops.size());
		for (Population p : model.geneticPops) {
				genPopSizes.put(p, new Double[model.typeNum]);
				for (int i=0; i<model.typeNum; i++)
					genPopSizes.get(p)[i] = oldState.genPopSizes.get(p)[i];
		}
		
		// Copy scalar population sizes:
		scalarPopSizes = new HashMap<Population, Double>(model.scalarPops.size());
		for (Population p : model.scalarPops)
			scalarPopSizes.put(p, oldState.getScalar(p));
	}
	
	/**
	 * Copy make this (pre-initialised) state a copy of another state.
	 * 
	 * @param oldState State to make a copy of.
	 */
	public void makeCopy (State oldState) {
		
		// Copy genetic population sizes:
		for (Population p : model.geneticPops) {
			for (int i=0; i<model.typeNum; i++)
				genPopSizes.get(p)[i] = oldState.genPopSizes.get(p)[i];
		}
		
		// Copy scalar population sizes:
		for (Population p : model.scalarPops)
			scalarPopSizes.put(p,  oldState.getScalar(p));
		
	}

	/**
	 * Get size of a particular genetic sub-population.
	 * 
	 * @param p		Population to interrogate.
	 * @param loc	Specific sub-population location.
	 * @return Size of sub-population.
	 */
	public double getGen(Population p, int[] loc) {
		return genPopSizes.get(p)[model.locToOffset(loc)];
	}
	
	/**
	 * Set size of a particular genetic sub-population.
	 * 
	 * @param p		Population to modify.
	 * @param loc	Specific sub-population location.
	 * @param value	Desired size.
	 */
	public void setGen(Population p, int[] loc, double value) {
		genPopSizes.get(p)[model.locToOffset(loc)] = value;
	}
	
	/**
	 * Get size of particular scalar population.
	 * 
	 * @param p	Population to interrogate.
	 * @return Size of population.
	 */
	public double getScalar(Population p) {
		return scalarPopSizes.get(p);
	}
	
	/**
	 * Set size of a scalar population.
	 * 
	 * @param p			Population to modify.
	 * @param value		Desired size.
	 */
	public void setScalar(Population p, double value) {
		scalarPopSizes.put(p, value);
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
