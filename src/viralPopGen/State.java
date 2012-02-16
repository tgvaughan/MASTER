package viralPopGen;

import java.util.*;

public class State {
	
	ArrayList<Population> populations;
	double[][] popSizes;
	
	HashMap<String,Integer> popIndex;
	
	
	/**
	 * Constructor
	 * 
	 * @param populations Vararg list of population objects.
	 */
	public State (Model model) {
		
		this.populations = model.populations;
		
		popSizes = new double[populations.size()][];
		for (int i=0; i<populations.size(); i++) {
			popSizes[i] = new double[populations.get(i).getSubPops()];
			
			// Add name to look-up table:
			popIndex.put(populations.get(i).name, i);
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
		for (i=0; i<populations.get(idx).dims.length; i++) {
			offset += location[i]*mul;
			mul *= populations.get(idx).dims[i];
		}

		return offset;
	}

	/**
	 * Get size of a particular sub-population.
	 * 
	 * @param name		Name of sub-population.
	 * @param location	Specific sub-population location.
	 * @return
	 */
	public double getSize(String name, int[] location) {
		
		int idx = popIndex.get(name);
		return popSizes[idx][locToOffset(idx,location)];
	}

	/**
	 * Set size of a particular sub-population.
	 * 
	 * @param name		Name of sub-population to modify.
	 * @param location	Specific sub-population location.
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
		
		for (int p=0; p<populations.size(); p++)
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
		
		for (int p=0; p<populations.size(); p++)
			for (int i=0; i<popSizes[p].length; i++)
				popSizes[p][i] *= arg;
		
		return this;
	}

}
