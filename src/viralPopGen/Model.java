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
	
	// Moments to calculate:
	ArrayList<Moment> moments;
	
	/**
	 * Model constructor.
	 */
	public Model () {
		pops = new ArrayList<Population>();
		reactions = new ArrayList<Reaction>();
		moments = new ArrayList<Moment>();
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
		react.calcDeltas();
		reactions.add(react);
	}
	
	/**
	 * Add moment to model.
	 * 
	 * @param moment Moment to add.
	 */
	public void addMoment(Moment moment) {
		moments.add(moment);
	}
}