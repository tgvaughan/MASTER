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
	HashMap<Population,Double[]> popSizes;
	
	
	/**
	 * Constructor
	 * 
	 * @param model Model defining the state space.
	 */
	public State (Model model) {
		
		this.model = model;
		
		// Initialise sub-population sizes:
		popSizes = new HashMap<Population, Double[]>(model.pops.size());
		for (Population p : model.pops)
			popSizes.put(p, new Double[p.nSubPops]);
		
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param oldState State to copy.
	 */
	public State (State oldState) {
		this.model = oldState.model;
		
		// Copy sub-population sizes:
		this.popSizes = new HashMap<Population, Double[]>(model.pops.size());
		for (Population p : model.pops) {
				popSizes.put(p, new Double[p.nSubPops]);
				for (int i=0; i<p.nSubPops; i++)
					popSizes.get(p)[i] = oldState.popSizes.get(p)[i];
		}
		
	}
	
	/**
	 * Copy make this (pre-initialised) state a copy of another state.
	 * 
	 * @param oldState State to make a copy of.
	 */
	public void makeCopy (State oldState) {
		
		// Copy sub-population sizes:
		for (Population p : model.pops) {
			for (int i=0; i<p.nSubPops; i++)
				popSizes.get(p)[i] = oldState.popSizes.get(p)[i];
		}
		
	}

	/**
	 * Get size of a particular sub-population.
	 * 
	 * @param p		Population to interrogate.
	 * @param loc	Specific sub-population location.
	 * @return Size of sub-population.
	 */
	public double get(Population p, int[] loc) {
		return popSizes.get(p)[p.locToOffset(loc)];
	}

	/**
	 * Get size of a population using pre-calculated offset.
	 * 
	 * @param p			Population to interrogate.
	 * @param offset	Offset into sub-population size array.
	 * @return Size of sub-population.
	 */
	public double get(Population p, int offset) {
		return popSizes.get(p)[offset];
	}

	/**
	 * Get size of structureless population.
	 * 
	 * @param p		Population to interrogate.
	 * @return Size of sub-population.
	 */
	public double get(Population p) {
		return popSizes.get(p)[0];
	}

	/**
	 * Set size of a particular sub-population.
	 * 
	 * @param p		Population to modify.
	 * @param loc	Specific sub-population location.
	 * @param value	Desired size.
	 */
	public void set(Population p, int[] loc, double value) {
		popSizes.get(p)[p.locToOffset(loc)] = value;
	}

	/**
	 * Set size of structureless population.
	 * 
	 * @param p		Population to modify.
	 * @param value Desired size.
	 */
	public void set(Population p, double value) {
		popSizes.get(p)[0] = value;
	}

	/**
	 * Add value to size of particular sub-population specified
	 * using a pre-calculated offset.
	 * 
	 * @param p
	 * @param offset
	 * @param increment
	 */
	public void add(Population p, int offset, double increment) {
		popSizes.get(p)[offset] += increment;
	}

	/**
	 * Dump representation of state to stdout.
	 */
	public void dump() {

		for (Population p : popSizes.keySet()) {
			for (int i=0; i<popSizes.get(p).length; i++)
				System.out.print(" " + String.valueOf(popSizes.get(p)[i]));
		}

		System.out.println();

	}
	
	/**
	 * Dump names of constituent populations to stdout.
	 */
	public void dumpNames() {
		
		for (Population p : popSizes.keySet()) {
			for (int i=0; i<popSizes.get(p).length; i++)
				System.out.print(" " + p.name + String.valueOf(i));
		}

		System.out.println();
	}
}
