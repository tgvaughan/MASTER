package hamlet;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * Inheritance tree generated from a birth-death model.
 *
 * @author Tim Vaughan
 */
public class Tree {

	// Root nodes of tree.
	Node rootNode;

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
                
                // Create list of active nodes:
                List<Node> activeNodes = Lists.newArrayList();
                
                // Create root node:
                rootNode = new Node(spec.getRootSubPop(), 0.0);
                
                // Add to active node list:
                activeNodes.add(rootNode);
                
                
	}
}