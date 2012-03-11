package viralPopGen;

import java.util.*;

/**
 * Class describing a birth-death viral population genetics model.
 * Basically a container class including a sets of population and
 * reaction objects.
 * 
 * @author Tim Vaughan
 *
 */
public class Model {

	// Populations in model:
	ArrayList<Population> pops;

	// Reactions to model:
	ArrayList<Reaction> reactions;
	
	// Total number of included types:
	int typeNum;
	
	/**
	 * Model constructor.
	 */
	public Model () {
		pops = new ArrayList<Population>();
		reactions = new ArrayList<Reaction>();
		
		// Count reduced volume of sequence space:
		typeNum = 1;
	}

	/**
	 * Add population to model.
	 * 
	 * @param pop Population to add.
	 */
	public void addPopulation(Population pop) {
		pops.add(pop);
	}

	/**
	 * Add reaction to model.
	 * 
	 * @param react Reaction to add.
	 */
	public void addReaction(Reaction react) {
		reactions.add(react);
	}

}
