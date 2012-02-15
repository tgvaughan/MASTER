package viralPopGen;

import java.util.*;

public class Model {

	// Populations in model:
	ArrayList<Population> populations;

	// Reactions to model:
	ArrayList<Reaction> reactions;

	/**
	 * Add population to model.
	 * 
	 * @param pop Population to add.
	 */
	public void addPopulation(Population pop) {
		populations.add(pop);
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
