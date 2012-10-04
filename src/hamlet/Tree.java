package hamlet;

import java.util.*;
import com.google.common.collect.*;

/**
 * Inheritance tree generated from a birth-death model.
 *
 * @author Tim Vaughan
 */
public class Tree {

	// Possible root nodes of tree.
	List<Node> rootNodes;

	// Spec specification.
	TreeSpec spec;

	/**
	 * Generate new inheritance tree.
	 * 
	 * @param spec Simulation specification.
	 */
	public Tree(TreeSpec spec) {

		this.spec = spec;

		// Initialise system state:
		State state = new State(spec.initState);

		// Populate root and active nodes:
		rootNodes = Lists.newArrayList();
		Map<Population, List<Set<Node>>> activeNodes = Maps.newHashMap();
		for (Population pop : spec.treePops) {

			activeNodes.put(pop, new ArrayList<Set<Node>>());

			// Add each individual of included populations as an active
			// node, as well as a possible root node.
			for (int offset=0; offset<state.popSizes.get(pop).length; offset++) {

				activeNodes.get(pop).add(new HashSet<Node>());

				for (int i=0; i<state.popSizes.get(pop)[i]; i++) {
					Node thisNode = new Node(pop, offset);
					rootNodes.add(thisNode);
					activeNodes.get(pop).get(offset).add(thisNode);
				}
			}
		}

		for (int tidx = 0; tidx<spec.nTimeSteps; tidx++) {

		}

	}
}