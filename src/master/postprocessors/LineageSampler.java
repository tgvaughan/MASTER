package master.postprocessors;

import beast.core.BEASTObject;
import beast.core.Input;
import beast.util.Randomizer;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import master.InheritanceTrajectory;
import master.model.Node;
import master.model.Population;
import master.model.PopulationSize;

/**
 * @author Alexei Drummond
 */
public class LineageSampler extends BEASTObject implements InheritancePostProcessor {
    
    public Input<Integer> nSamplesInput = new Input<Integer>("nSamples",
            "Number of lineages to sample");

    public Input<Double> sampleProbabilityInput = new Input<Double>("pSample",
            "Probability with which to sample each lineage.");

    public Input<List<PopulationSize>> popSpecificSamplesInput =
            new Input<List<PopulationSize>>("populationSize",
            "Population size specifying number of samples to draw "
                    + "from individual populations.",
                    new ArrayList<PopulationSize>());
    
    public Input<Double> samplingTimeInput = new Input<Double>("samplingTime",
            "Time at which sampling is to take place");
  
    public Input<String> markAnnotationInput = new Input<String>("markAnnotation",
            "Mark using this annotation rather than pruning.");
    
    public Input<Boolean> reverseTimeInput = new Input<Boolean>("reverseTime",
            "Process inheritance graph in reverse time.  Default false.", false);

    public Input<Boolean> noCleanInput = new Input<Boolean>("noClean",
            "Do not remove no-state-change nodes.", false);
    
    Multiset<Population> populationSizes;
    
    @Override
    public void initAndValidate() {
        if (nSamplesInput.get() == null
                && sampleProbabilityInput.get() == null
                && popSpecificSamplesInput.get().isEmpty())
            throw new IllegalArgumentException("Either nSamples or pSample or at least "
                    + "one populationSize must be specified.");
        
        populationSizes = HashMultiset.create();
        for (PopulationSize popSize : popSpecificSamplesInput.get())
            populationSizes.setCount(popSize.getPopulation(),
                    (int)Math.round(popSize.getSize()));
    }
    
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
     * @param samplingTime Time at which sampling will occur (-1 implies no specific time)
     * @param nSamples Number of lineages to sample (or -1 if not used)
     * @param pSample Proportion of lineages to sample (or -1 if not used)
     * @param populationSizes Lineages to sample from individual populations
     * @param markAnnotation
     * @param noClean Do not remove non-state-change nodes after processing
     * @param reverseTime If true, times increases in the opposite direction
     * to the Markov process which generated the graph.
     */
    public static void process(InheritanceTrajectory itraj,
                               double samplingTime, int nSamples, double pSample,
                               Multiset<Population> populationSizes,
                               String markAnnotation,
                               boolean noClean, boolean reverseTime) {
        
        boolean markOnly;
        if (markAnnotation != null) {
            markOnly = true;
        } else {
            markAnnotation = "__mark__";
            markOnly = false;
        }
        
        // Get lists of root and leaf nodes (using chosen time direction):
        List<Node> rootNodes, leafNodes;
        if (reverseTime) {
            rootNodes = itraj.getEndNodes();
            leafNodes = itraj.getStartNodes();
        } else {
            rootNodes = itraj.getStartNodes();
            leafNodes = itraj.getEndNodes();
        }
        
        // Assemble list of nodes from which to sample:
        List<Node> nodesToSample = new ArrayList<Node>();
        if (samplingTime>=0.0) {
            for (Node root : rootNodes)
                collectNodes(root, samplingTime, nodesToSample, reverseTime,
                        itraj);
            
            // Update startNodes if necessary:
            if (reverseTime) {
                itraj.getStartNodes().clear();
                itraj.getStartNodes().addAll(nodesToSample);
            }
        } else {
            nodesToSample.addAll(leafNodes);
        }

        // Sample a subset of these lineages:
        List<Node> sampledNodes;
        if (nSamples>=0) {
            sampledNodes = getSampledLineages(nodesToSample, nSamples);
        } else if (pSample>0) {
            sampledNodes = getSampledLineages(nodesToSample, pSample);
        } else {
            sampledNodes = getSampledLineages(nodesToSample, populationSizes);
        }

        
        // Label nodes belonging to sampled lineages:
        for (Node node : sampledNodes)
            mark(node, reverseTime, markAnnotation);
        
        // Set complimentary annotation on unsampled lineages:
        for (Node node: rootNodes)
            markFalse(node, reverseTime, markAnnotation);
        
        if (markOnly)
            return;
        
        // Update list of leaf nodes (using chosen time direction):
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
        
        // Clean graph of singleton nodes that don't represent state changes:
        if (!noClean)
            for (Node node : rootNodes)
                cleanSubGraph(node, reverseTime, markAnnotation);

        // Remove marks:
        for (Node root : rootNodes)
            unMark(root, reverseTime, markAnnotation);
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
     * Remove mark from node and its ancestors.
     * 
     * @param node 
     * @param reverseTime 
     */
    private static void unMark(Node node, boolean reverseTime, String markAnnotation) {

        if (node.getAttributeNames().contains(markAnnotation)) {
            node.removeAttribute(markAnnotation);
        }

        List<Node> prevNodes = getNext(node, reverseTime);
        for (Node prev : prevNodes)
            unMark(prev, reverseTime, markAnnotation);
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
     * Assemble a list of at most nSamples nodes sampled without replacement
     * from nodesToSample.
     * 
     * @param nodesToSample
     * @param nSamples
     * @return list of sampled nodes
     */
    private static List<Node> getSampledLineages(List<Node> nodesToSample, int nSamples) {
        List<Node> sampledNodes = Lists.newArrayList();
        while (nSamples > 0 && !nodesToSample.isEmpty()) {
            int index = Randomizer.nextInt(nodesToSample.size());
            Node sampledNode = nodesToSample.remove(index);
            sampledNodes.add(sampledNode);
            nSamples -= 1;
        }
        
        return sampledNodes;
    }

    /**
     * Assemble a list of nodes sampled probabilistically from the given list of nodes.
     * Each node is sampled with probability rho.
     *
     * @param nodesToSample
     * @param rho the probability with which each of the nodes is sampled
     * @return list of sampled nodes
     */
    private static List<Node> getSampledLineages(List<Node> nodesToSample, double rho) {
        List<Node> sampledNodes = Lists.newArrayList();
        for (Node node : nodesToSample) {
            if (Randomizer.nextDouble() < rho) {
                Node sampledNode = node;
                sampledNodes.add(sampledNode);
            }
        }
        return sampledNodes;
    }


    /**
     * Assemble list of nodes sampled with replacement from nodesToSample.
     * 
     * @param nodesToSample list of nodes from which to sample
     * @param populationSizes multiset specifying number of nodes
     * corresponding to each population to sample.
     * 
     * @return list of sampled nodes
     */
    private static List<Node> getSampledLineages(List<Node> nodesToSample,
            Multiset<Population> populationSizes) {
        
        // Sort node list by population
        Map<Population, List<Node>> nodeMap = Maps.newHashMap();
        for (Node node : nodesToSample) {
            Population pop = node.getPopulation();
            if (!nodeMap.containsKey(pop))
                nodeMap.put(pop, new ArrayList<Node>());
            
            nodeMap.get(pop).add(node);
        }
        
        // Sample at most the chosen number of nodes of each population
        List<Node> sampledNodes = Lists.newArrayList();
        for (Population pop : populationSizes.elementSet()) {
            int nSamples = populationSizes.count(pop);
            sampledNodes.addAll(getSampledLineages(nodeMap.get(pop), nSamples));
        }
        
        return sampledNodes;
    }
    
    /**
     * Obtain nodes descending from node, respecting chosen time direction.
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
    
    /**
     * Clean graph below node of any unmarked singleton nodes which do not represent
     * state changes.
     * 
     * @param node
     * @param reverseTime 
     */
    private static void cleanSubGraph(Node node, boolean reverseTime, String markAnnotation) {
        List<Node> nextNodes = getNext(node, reverseTime);
        List<Node> prevNodes = getPrev(node, reverseTime);
        
        if (nextNodes.size() == 1 && prevNodes.size() == 1) {
            Node parent = prevNodes.get(0);
            Node child = nextNodes.get(0);
            
            if (node.getPopulation().equals(child.getPopulation())) {
                
                getPrev(child, reverseTime).remove(node);
                getPrev(child, reverseTime).add(parent);
                
                getNext(parent, reverseTime).remove(node);
                getNext(parent, reverseTime).add(child);
            }
        }
        
        List<Node> nextNodesCopy = new ArrayList<Node>();
        nextNodesCopy.addAll(nextNodes);

        for (Node child : nextNodesCopy)
            cleanSubGraph(child, reverseTime, markAnnotation);
    }

    @Override
    public void process(InheritanceTrajectory itraj) {
        LineageSampler.process(itraj,
                samplingTimeInput.get() == null ? -1 : samplingTimeInput.get(),
                nSamplesInput.get() == null ? -1 : nSamplesInput.get(),
                sampleProbabilityInput.get() == null ? -1.0 : sampleProbabilityInput.get(),
                populationSizes,
                markAnnotationInput.get(), noCleanInput.get(),
                reverseTimeInput.get());
    }
    
}
