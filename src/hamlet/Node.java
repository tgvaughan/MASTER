package hamlet;

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
     * @param subPopOffset Sub-population to which node belongs.
     */
    Node(Population population, int subPopOffset, double time) {

        this.population = population;
        this.subPopOffset = subPopOffset;
        this.time = time;

        parent = null;
        children = new ArrayList<Node>();
    }
    
    /**
     * Constructor for inheritance map node. (No sub-population specification.)
     * 
     * @param population Population to which node belongs.
     */
    Node(Population population) {
        this.population = population;
        this.subPopOffset = -1;
        this.time = -1;
        
        parent = null;
        children = new ArrayList<Node>();
    }

    /**
     * Determine whether node is root.
     *
     * @return True if node has no parent.
     */
    boolean isRoot() {
        return (parent == null);
    }
}
