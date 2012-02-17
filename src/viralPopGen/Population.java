package viralPopGen;

public class Population {
	
	String name; // Population name
	boolean genetic; // True if population has genetic component.

	/**
	 * Define population.
	 * 
	 * @param name		Population name.
	 * @param genetic	True if population has genetic component.
	 */
	public Population(String name, boolean genetic) {
		this.name = name;
		this.genetic = genetic;
	}
}