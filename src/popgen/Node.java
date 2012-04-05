package popgen;

import java.util.*;

/**
 * Node in inheritance tree generated from birth-death model.
 *
 * @author Tim Vaughan
 */
public class Node {

	// Parent of this node.  (Null if root.)
	Node parent;

	double time;

	List<Node> children;

	Population population;
	int subPopOffset;

	/**
	 * Constructor.
	 * 
	 * @param population	Population to which node belongs.
	 * @param subPopOffset  Sub-population to which node belongs.
	 */
	Node(Population population, int subPopOffset) {

		this.population = population;
		this.subPopOffset = subPopOffset;

		children = new ArrayList<Node>();
	}
	
}
