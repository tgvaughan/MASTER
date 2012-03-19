package viralPopGen;

/**
 * Class of objects describing distinct populations within the model.
 * These populations may be scalar or may involve genetically distinct
 * sub-populations.
 * 
 * @author Tim Vaughan
 *
 */
public class Population {
	
	String name; // Population name
	int[] dims; // Structural space dimensions
	
	int nSubPops; // Total number of sub-populations

	/**
	 * Define a structured population.
	 * 
	 * @param name		Population name.
	 */
	public Population(String name, int[] dims) {
		this.name = name;
		this.dims = dims.clone();
		
		nSubPops = 1;
		for (int i=0; i<dims.length; i++)
			nSubPops *= dims[i];
	}

	/**
	 * Define an unstructured population.
	 * 
	 * @param name
	 */
	public Population(String name) {
		this.name = name;
		dims = new int[1];
		dims[0] = 1;
		
		nSubPops = 1;
	}

	/**
	 * Get offset into sub-population sizes vector.
	 * 
	 * @param p Population.
	 * @param loc Location of sub-population.
	 * @return Offset.
	 */
	public int subToOffset(int[] loc) {
		int offset = 0;
		
		int m=1;
		for (int i=0; i<loc.length; i++) {
			offset += m*loc[i];
			m *= dims[i];
		}
		
		return offset;
	}
}