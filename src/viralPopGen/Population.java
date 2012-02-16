package viralPopGen;

public class Population {
	
	String name;	 // Population name

	int[] seqDims;	 // Sequence space dimensions

	int subPops; // Total number of sub-populations.

	/**
	 * Define population.
	 * 
	 * @param name		Population name.
	 * @param seqDims	Sequence space dimensions.
	 * @param otherDims	Other dimensions (spatial, etc).
	 */
	public Population(String name, int[] seqDims) {

		this.name = name;
		this.seqDims = seqDims;

		subPops = 1;
		for (int d : seqDims)
			subPops *= d;
	}

	/**
	 * Retrieve the total number of sub-populations.
	 * 
	 * @return Sub-population number.
	 */
	public int getSubPops () {
		return subPops;
	}

}