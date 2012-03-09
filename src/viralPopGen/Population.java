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
	double[] dims; // Structural space dimensions

	/**
	 * Define population.
	 * 
	 * @param name		Population name.
	 */
	public Population(String name, double[] dims) {
		this.name = name;
		this.dims = dims.clone();
	}
}