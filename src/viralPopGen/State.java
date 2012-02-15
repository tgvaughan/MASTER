package viralPopGen;

import java.util.HashMap;

public class State {
	
	Population[] populations;
	double[][] popSizes;
	
	HashMap<String,Integer> popIndex;
	
	
	public State (Population ... populations) {
		
		this.populations = populations;
		
		popSizes = new double[populations.length][];
		for (int i=0; i<populations.length; i++) {
			popSizes[i] = new double[populations[i].getSubPops()];
			
			// Add name to look-up table:
			popIndex.put(populations[i].name, i);
		}
		
		
	}
	
	/**
	 * Get offset into popSizes vector.
	 * 
	 * @param location Array specification of sub-population.
	 * @return Offset into popSizes vector.
	 */
	private int locToOffset(int[] location) {

		return 0;
	}

	/**
	 * Get size of a particular population.
	 * 
	 * @param name		Name of population.
	 * @param location	Specific population location.
	 * @return
	 */
	public double getSize(String name, int[] location) {
		
		int idx = popIndex.get(name);
		return popSizes[idx][locToOffset(location)];
	}

	/**
	 * Set size of a particular population.
	 * 
	 * @param name		Name of population to modify.
	 * @param location	Specific population location.
	 */
	public void setSize(String name, int[] location) {
		
	}
	
	/**
	 * Add two states together.
	 * 
	 * @param arg	State to add.
	 * @return		Result of addition.
	 */
	public State add(State arg) {
		
		return this;
	}
	
	/**
	 * Multiply state by an integer.
	 * 
	 * @param arg	Integer to multiply.
	 * @return		Result of multiplication.
	 */
	public State mul(int arg) {
		
		return this;
	}

}
