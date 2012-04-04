package popgen;

import java.util.*;

/**
 * Inheritance tree generated from a birth-death model.
 *
 * @author Tim Vaughan
 */
public class Tree {

	List<Node> rootNodes;

	// Spec specification:
	TreeSpec spec;

	/**
	 * Generate new inheritance tree.
	 * 
	 * @param spec Simulation specification.
	 */
	public Tree(TreeSpec spec) {

		this.spec = spec;

	}
	
}