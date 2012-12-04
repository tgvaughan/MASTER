/*
 * Copyright (C) 2012 Tim Vaughan <tgvaughan@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package hamlet.beast;

import beast.core.Description;
import beast.core.Input;
import beast.core.StateNode;
import beast.core.StateNodeInitialiser;
import beast.evolution.alignment.Alignment;
import beast.evolution.tree.Tree;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Plugin which uses a Hamlet simulation to construct a BEAST 2 tree.")
public class HamletTree extends Tree implements StateNodeInitialiser {
    
    /*
     * XML inputs:
     */
    
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<Double>(
            "simulationTime",
            "The maximum length of time to simulate for. (Defaults to infinite.)");
    
    public Input<Integer> verbosityInput = new Input<Integer> (
            "verbosity", "Level of verbosity to use (0-3).", 1);
    
    // Model:
    public Input<InheritanceModel> modelInput = new Input<InheritanceModel>("model",
            "The specific model to simulate.", Input.Validate.REQUIRED);
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<InitState>("initialState",
            "Initial state of system.", Input.Validate.REQUIRED);
    
    // Population end conditions:
    public Input<List<PopulationEndCondition>> popEndConditionsInput = new Input<List<PopulationEndCondition>>(
            "populationEndCondition",
            "Trajectory end condition based on population sizes.",
            new ArrayList<PopulationEndCondition>());
    
    // Lineage end conditions:
    public Input<List<LineageEndCondition>> lineageEndConditionsInput = new Input<List<LineageEndCondition>>(
            "lineageEndCondition",
            "Trajectory end condition based on remaining lineages.",
            new ArrayList<LineageEndCondition>());
    
    // Outputs (May want to record the tree separately.)
    public Input<List<InheritanceTrajectoryOutput>> outputsInput = new Input<List<InheritanceTrajectoryOutput>>(
            "output",
            "Output writer used to write results of simulation to disk.",
            new ArrayList<InheritanceTrajectoryOutput>());
    
    // BEAST Tree construction:
    public Input<Boolean> reverseTimeInput = new Input<Boolean>("reverseTime",
            "Read time in reverse when assembling tree. (Default true.)", true);
    
    public Input<Boolean> collapseSingletonsInput = new Input<Boolean>(
            "collapseSingletons",
            "Whether to join branches of singleton nodes together. Default true.",
            true);
    
    public Input<Alignment> alignmentInput = new Input<Alignment>("alignment",
            "If provided, nodes are equated with taxons having the same label.");
        
    hamlet.inheritance.InheritanceTrajectorySpec spec;
    
    public HamletTree() { }
    
    @Override
    public void initAndValidate() throws Exception {
    
        spec = new hamlet.inheritance.InheritanceTrajectorySpec();
               
        // Incorporate model:
        spec.setModel(modelInput.get().model);        
        
        // No need to sample population sizes here:
        spec.setNoSampling();
        
        // Set maximum simulation time:
        if (simulationTimeInput.get() != null)
            spec.setSimulationTime(simulationTimeInput.get());
        else
            spec.setSimulationTime(Double.POSITIVE_INFINITY);
        
        // Assemble initial state:
        hamlet.PopulationState initState = new hamlet.PopulationState();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get())
            initState.set(popSize.pop, popSize.size);
        spec.setInitPopulationState(initState);        
        spec.setInitNodes(initialStateInput.get().initNodes);
        
        // Incorporate any end conditions:
        for (PopulationEndCondition endCondition : popEndConditionsInput.get())
            spec.addPopSizeEndCondition(endCondition.endConditionObject);
        
        for (LineageEndCondition endCondition : lineageEndConditionsInput.get())
            spec.addLineageEndCondition(endCondition.endConditionObject);

        // BEAST controls seed here:
        spec.setSeed(-1);
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());
        
        // Generate stochastic trajectory:
        hamlet.inheritance.InheritanceTrajectory itraj =
                new hamlet.inheritance.InheritanceTrajectory(spec);
        
        // Write any outputs:
        for (InheritanceTrajectoryOutput output : outputsInput.get())
            output.write(itraj);
        
        // Assemble BEAST tree:
        assembleTree(itraj);        
        
        initStateNodes();        
    }
    
    /**
     * Use inheritance trajectory to assemble BEAST tree.
     * 
     * @param itraj inheritance trajectory object
     * @throws Exception when inheritance graph is not tree-like in preferred
     * time direction.
     */
    private void assembleTree(hamlet.inheritance.InheritanceTrajectory itraj) throws Exception {

        boolean reverseTime = reverseTimeInput.get();
        
        Map<hamlet.inheritance.Node,String> leafLabels = Maps.newHashMap();
        List<hamlet.inheritance.Node> rootNodes, leafNodes;
        
        
        // Identify root and leaf nodes
        if (reverseTime) {
            rootNodes = findEndNodes(itraj);
            leafNodes = itraj.startNodes;
        } else {
            rootNodes = itraj.startNodes;
            leafNodes = findEndNodes(itraj);
        }
        
        if (rootNodes.size()!=1)
            throw new Exception("Cannot assemble BEAST tree with multiple roots.");

        hamlet.inheritance.Node hamletRoot = rootNodes.get(0);
        
        // Assign a unique integer label to each unnamed leaf node and
        // identify time of node furthest from root:
        leafLabels = Maps.newHashMap();
        double largestHeightDiff = 0.0;
        double youngestLeafTime = 0.0;
        int label = 1;
        for (hamlet.inheritance.Node leaf : leafNodes) {
            if (leaf.getName() == null)
                leafLabels.put(leaf, String.valueOf(label++));
            else
                leafLabels.put(leaf, leaf.getName());
            
            double thisHeightDiff = Math.abs(leaf.getTime()-hamletRoot.getTime());
            if (thisHeightDiff>largestHeightDiff) {
                largestHeightDiff = thisHeightDiff;
                youngestLeafTime = leaf.getTime();
            }
        }
        
        // Create BEAST tree root node:
        beast.evolution.tree.Node beastRoot = new beast.evolution.tree.Node();
        
        // Assemble Tree
        assembleSubtree(hamletRoot, beastRoot, youngestLeafTime, leafLabels);
        
        // Set node numbers:
        initNodeNumbers(beastRoot);
                
        // Tell BEAST tree what it's root is:
        setRoot(beastRoot);
    }
    
    /**
     * Find "leaves" of inheritance graph.
     * 
     * @param graph
     * @return Set containing leaf nodes.
     */
    private List<hamlet.inheritance.Node> findEndNodes(hamlet.inheritance.InheritanceTrajectory graph) {
        List<hamlet.inheritance.Node> endNodes = Lists.newArrayList();
        
        for (hamlet.inheritance.Node startNode : graph.startNodes)
            findEndNodesOnSubGraph(startNode, endNodes);
        
        return endNodes;
    }
    
    /**
     * Method used for recursive traversal of graph when finding leaf nodes.
     * 
     * @param node Current node in traversal.
     * @param endNodes Set of leaf nodes already found.
     */
    private void findEndNodesOnSubGraph(hamlet.inheritance.Node node,
            List<hamlet.inheritance.Node> endNodes) {
        
        if (node.getChildren().isEmpty()) {
            if (!endNodes.contains(node))
                endNodes.add(node);
            return;
        }
        
        for (hamlet.inheritance.Node child : node.getChildren())
            findEndNodesOnSubGraph(child, endNodes);
    }
    
    /**
     * Assemble beast tree corresponding to Hamlet tree below hamletParent
     * and attach to beastParent. 
     * 
     * @param hamletNode
     * @param beastNode 
     * @param timeOffset 
     * @param leafLabels 
     * @return 
     */
    private void assembleSubtree(hamlet.inheritance.Node hamletNode,
            beast.evolution.tree.Node beastNode, double timeOffset,
            Map<hamlet.inheritance.Node,String> leafLabels) {
        
        // Deal with potential time reversal:
        List<hamlet.inheritance.Node> hamletChildren;
        if (reverseTimeInput.get())
            hamletChildren = hamletNode.getParents();
        else
            hamletChildren = hamletNode.getChildren();
        
        // Skip over single-child nodes:
        if (collapseSingletonsInput.get() && hamletChildren.size() == 1) {
            assembleSubtree(hamletChildren.get(0), beastNode, timeOffset, leafLabels);
            return;
        }
 
        // Set BEAST node height:
        beastNode.setHeight(Math.abs(hamletNode.getTime()-timeOffset));
        
        // Add children:
        for (hamlet.inheritance.Node hamletChild : hamletChildren) {
            beast.evolution.tree.Node beastChild = new beast.evolution.tree.Node();
            assembleSubtree(hamletChild, beastChild, timeOffset, leafLabels);
            beastNode.addChild(beastChild);
        }        
                
        // Label leaves by setting appropriate node IDs:
        if (hamletChildren.isEmpty())
            beastNode.setID(leafLabels.get(hamletNode));

    }
    
    /**
     *  Assign integer node numbers to BEAST tree nodes.  If an alignment
     * is supplied, leaf node numbers are selected so that the Hamlet node
     * labels match the taxon names.  An exception is thrown if no such
     * taxon exists.
     * 
     * @param beastRoot Root of BEAST tree.
     */
    public void initNodeNumbers(beast.evolution.tree.Node beastRoot) {
        
        List<beast.evolution.tree.Node> leaves = getBeastLeaves(beastRoot);

        int nodeNr = 0;
        for (beast.evolution.tree.Node leaf : leaves) {
            if (alignmentInput.get() != null) {
                if (!alignmentInput.get().getTaxaNames().contains(leaf.getID()))
                    throw new IllegalArgumentException("Alignment does not contain taxon named " + leaf.getID());

                leaf.setNr(alignmentInput.get().getTaxonIndex(leaf.getID()));
            } else
                leaf.setNr(nodeNr++);
        }
        
        nodeNr = leaves.size();
        
        List<beast.evolution.tree.Node> nodes = Lists.newArrayList();
        List<beast.evolution.tree.Node> nodesPrime = Lists.newArrayList();

        while(nodes.size()>1) {
            nodesPrime.clear();
            for (beast.evolution.tree.Node node : nodes) {
                if (!nodesPrime.contains(node.getParent())) {
                    node.getParent().setNr(nodeNr++);
                    nodesPrime.add(node.getParent());
                }
            }
            nodes.clear();
            nodes.addAll(nodesPrime);
        }
        
    }
    
    /**
     * Obtain list of leaves contained in clade below node.
     * 
     * @param node
     * @return 
     */
    List <beast.evolution.tree.Node> getBeastLeaves(beast.evolution.tree.Node node) {        
        List<beast.evolution.tree.Node> nodeList = Lists.newArrayList();
        
        if (node.isLeaf()) {
            nodeList.add(node);
        } else {
            for (beast.evolution.tree.Node child : node.getChildren())
                nodeList.addAll(getBeastLeaves(child));
        }
        return nodeList;
    }

    @Override
    public void initStateNodes() throws Exception {
        if (m_initial.get() != null) {
            m_initial.get().assignFrom(this);
        }
    }

    @Override
    public List<StateNode> getInitialisedStateNodes() {
        List<StateNode> stateNodes = new ArrayList<StateNode>();
        if (m_initial.get() != null) {
            stateNodes.add(m_initial.get());
        }
        return stateNodes;
    }

}
