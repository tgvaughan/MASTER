package viralPopGen;

import java.util.HashMap;

public class State {
	
	Population[] populations;
	double[][] popSizes;
	
	HashMap<String,Integer> popIndex;
	
	
	/**
	 * Constructor
	 * 
	 * @param populations Vararg list of population objects.
	 */
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
	private int locToOffset(int idx, int[] location) {
		
		int offset = 0;
		int mul = 1;

		int i;
		for (i=0; i<populations[idx].seqDims.length; i++) {
			offset += location[i]*mul;
			mul *= populations[idx].seqDims[i];
		}

		for (int j=0; j<populations[idx].otherDims.length; j++) {
			offset += location[i+j]*mul;
			mul *= populations[idx].otherDims[j];
		}

		return offset;
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
		return popSizes[idx][locToOffset(idx,location)];
	}

	/**
	 * Set size of a particular population.
	 * 
	 * @param name		Name of population to modify.
	 * @param location	Specific population location.
	 */
	public void setSize(String name, int[] location, double value) {
		
		int idx = popIndex.get(name);
		popSizes[idx][locToOffset(idx,location)] = value;
		
	}
	
	/**
	 * Add two states together.
	 * 
	 * @param arg	State to add.
	 * @return		Result of addition.
	 */
	public State add(State arg) {
		
		for (int p=0; p<populations.length; p++)
			for (int i=0; i<popSizes[p].length; i++)
				popSizes[p][i] += arg.popSizes[p][i];
		
		return this;
	}
	
	/**
	 * Multiply state by an integer.
	 * 
	 * @param arg	Integer to multiply.
	 * @return		Result of multiplication.
	 */
	public State mul(int arg) {
		
		for (int p=0; p<populations.length; p++)
			for (int i=0; i<popSizes[p].length; i++)
				popSizes[p][i] *= arg;
		
		return this;
	}

}
