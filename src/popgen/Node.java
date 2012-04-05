package popgen;

import java.util.*;

/**
 * Node in inheritance tree generated from birth-death model.
 *
 * @author Tim Vaughan
 */
public class Node {

	Node parent;
	List<Node> children;

	Population population;
	int subPopOffset;

	Node(Population population, int subPopOffset) {

		this.population = population;
		this.subPopOffset = subPopOffset;

		children = new ArrayList<Node>();
	}
	
}
