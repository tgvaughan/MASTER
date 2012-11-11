package hamlet;

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
                
                // Create root node:
                rootNode = new Node(spec.getRootPop(),
                        spec.getRootSubPopOffset(),
                        0.0);
                
                // Initialise lineage state:
                LineageState lineageState = new LineageState(rootNode);
                
	}
}