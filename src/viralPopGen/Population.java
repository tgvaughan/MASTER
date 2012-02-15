package viralPopGen;

/**
 * @author Tim Vaughan
 *
 */
public class Population {
	
	String name;
	
	int[] seqDims;
	int[] otherDims;
	
	int subPops;
	
	public Population(String name, int[] seqDims, int[] otherDims) {
		super();
		this.name = name;
		this.seqDims = seqDims;
		this.otherDims = otherDims;
		
		subPops = 1;
		for (int d : seqDims)
			subPops *= d;
		for (int d : otherDims)
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