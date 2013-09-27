package master.inheritance;

import beast.util.Randomizer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexei Drummond
 */
public class SampleLineages {

    /**
     * Sample (without replacement) nSamples lineages crossing the chosen
     * samplingTime.  The graph is truncated beyond this time and the nodes
     * belonging to these sampled lineages are marked.  If markOnly is false,
     * nodes belonging to unsampled lineages are removed.
     * 
     * Depending on the time direction, the list of startNodes may be modified
     * by this method.
     * 
     * @param itraj Inheritance trajectory object
     * @param samplingTime Time at which sampling will occur
     * @param nSamples Number of lineages to sample
     * @param markOnly If true, sampled lineages are only marked not pruned.
     * @param reverseTime If true, times increases in the opposite direction
     * to the Markov process which generated the graph.
     */
    public static void process(InheritanceTrajectory itraj,
            double samplingTime, int nSamples, String markAnnotation,
            boolean reverseTime) {
        
        boolean markOnly;
        if (markAnnotation != null) {
            markOnly = true;
        } else {
            markAnnotation = "__mark__";
            markOnly = false;
        }
        
        // Get list of root nodes (using chosen time direction):
        List<Node> rootNodes;
        if (reverseTime)
            rootNodes = itraj.getEndNodes();
        else
            rootNodes = itraj.getStartNodes();
        
        // Assemble list of nodes at sampling time:
        List<Node> nodesAtSamplingTime = new ArrayList<Node>();
        for (Node root : rootNodes)
            collectNodes(root, samplingTime, nodesAtSamplingTime, reverseTime,
                    itraj);

        // Update startNodes if necessary:
        if (reverseTime) {
            itraj.getStartNodes().clear();
            itraj.getStartNodes().addAll(nodesAtSamplingTime);
        }
        
        // Sample a subset of these lineages:
        List<Node> sampledNodes = new ArrayList<Node>();
        while (nSamples > 0 && !nodesAtSamplingTime.isEmpty()) {
            int index = Randomizer.nextInt(nodesAtSamplingTime.size());
            Node sampledNode = nodesAtSamplingTime.remove(index);
            sampledNodes.add(sampledNode);
            nSamples -= 1;
        }
        
        // Label nodes belonging to sampled lineages:
        for (Node node : sampledNodes)
            mark(node, reverseTime, markAnnotation);
        
        // Set complimentary annotation on unsampled lineages:
        for (Node node: rootNodes)
            markFalse(node, reverseTime, markAnnotation);
        
        if (markOnly)
            return;
        
        // Get list of leaf nodes (using chosen time direction):
        List<Node> leafNodes;
        if (reverseTime)
            leafNodes = itraj.getStartNodes();
        else
            leafNodes = itraj.getEndNodes();
        
        // Discard unmarked lineages:
        for (Node leaf : leafNodes) {
            if (!isMarked(leaf, markAnnotation)) {
                pruneLineage(leaf, reverseTime, markAnnotation);
                if (reverseTime && itraj.getStartNodes().contains(leaf))
                    itraj.getStartNodes().remove(leaf);
            }
        }
        
        // Remove any remaining unmarked root nodes:
        List<Node> oldRootNodes = new ArrayList<Node>();
        oldRootNodes.addAll(rootNodes);
        for (Node root : oldRootNodes) {
            if (!isMarked(root, markAnnotation))
                rootNodes.remove(root);
        }
        
    }

    /**
     * Determine whether node belongs to a sampled lineage.
     * 
     * @param node
     * @return true if lineage has been sampled
     */
    private static boolean isMarked(Node node, String markAnnotation) {
        Boolean mark = (Boolean) node.getAttribute(markAnnotation);
        return mark != null && mark;
    }

    /**
     * Mark node and its ancestors as belonging to a sampled lineage.
     * 
     * @param node 
     * @param reverseTime 
     */
    private static void mark(Node node, boolean reverseTime, String markAnnotation) {

        node.setAttribute(markAnnotation, true);
        List<Node> prevNodes = getPrev(node, reverseTime);
        for (Node prev : prevNodes)
            mark(prev, reverseTime, markAnnotation);
    }
    
    /**
     * Explicitly unmark node and its descendents which do not
     * belong to sampled lineages.
     * 
     * @param node
     * @param reverseTime 
     */
    private static void markFalse(Node node, boolean reverseTime, String markAnnotation) {
        if (!isMarked(node, markAnnotation))
            node.setAttribute(markAnnotation, false);
        
        List<Node> nextNodes = getNext(node, reverseTime);
        for (Node next : nextNodes)
            markFalse(next, reverseTime, markAnnotation);
    }

    /**
     * Discard ancestral nodes until a node marked as belonging to a
     * sampled lineage is encountered.
     * 
     * @param node
     * @param reverseTime 
     */
    private static void pruneLineage(Node node, boolean reverseTime, String markAnnotation) {
        List<Node> prevNodes = new ArrayList<Node>();
        prevNodes.addAll(getPrev(node, reverseTime));
        
        for (Node prev : prevNodes) {
            getNext(prev, reverseTime).remove(node);
            if (!isMarked(prev, markAnnotation))
                pruneLineage(prev, reverseTime, markAnnotation);
        }
    }

    /**
     * Assemble a list of nodes terminating lineages at the sampling time.
     * 
     * @param node
     * @param samplingTime
     * @param nodesAtSamplingTime 
     */
    private static void collectNodes(Node node, double samplingTime,
            List<Node> nodesAtSamplingTime, boolean reverseTime,
            InheritanceTrajectory itraj) {

        double timeFactor = reverseTime ? -1.0 : 1.0;
        
        List<Node> nextNodes = new ArrayList<Node>();
        nextNodes.addAll(getNext(node, reverseTime));
        
        for (Node next : nextNodes) {
            if (next.getTime()*timeFactor >= samplingTime*timeFactor) {
                Node newNode = new Node(next.getPopulation(), samplingTime);
                getNext(node, reverseTime).remove(next);
                getPrev(next, reverseTime).remove(node);
                getNext(node, reverseTime).add(newNode);
                getPrev(newNode, reverseTime).add(node);
                nodesAtSamplingTime.add(newNode);
            } else {
                collectNodes(next, samplingTime, nodesAtSamplingTime, reverseTime,
                        itraj);
            }
        }
    }
    
    /**
     * Obtain nodes decending from node, respecting chosen time direction.
     * 
     * @param node
     * @param reverseTime
     * @return parents or children, depending on reverseTime
     */
    private static List<Node> getNext(Node node, boolean reverseTime) {
        if (reverseTime)
            return node.getParents();
        else
            return node.getChildren();
    }
    
    /**
     * Obtain direct ancestors of node, respecting chosen time direction.
     * 
     * @param node
     * @param reverseTime
     * @return parents or children, depending on reverseTime
     */
    private static List<Node> getPrev(Node node, boolean reverseTime) {
        if (reverseTime)
            return node.getChildren();
        else
            return node.getParents();
    }
    
}
