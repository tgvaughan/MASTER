package viralPopGen;

public class Population {
	
	String name;	 // Population name

	int[] seqDims;	 // Sequence space dimensions
	int[] otherDims; // Other dimensions

	int[] dims;  // Sequence and other dims together.

	int subPops; // Total number of sub-populations.

	/**
	 * Define population.
	 * 
	 * @param name		Population name.
	 * @param seqDims	Sequence space dimensions.
	 * @param otherDims	Other dimensions (spatial, etc).
	 */
	public Population(String name, int[] seqDims, int[] otherDims) {

		this.name = name;
		this.seqDims = seqDims;
		this.otherDims = otherDims;

		dims = new int[seqDims.length + otherDims.length];

		subPops = 1;
		int i = 0;
		for (int d : seqDims) {
			dims[i++] = d;
			subPops *= d;
		}
		for (int d : otherDims) {
			dims[i++] = d;
			subPops *= d;
		}
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