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
package master;

import beast.base.core.Citation;
import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.inference.StateNode;
import beast.base.inference.StateNodeInitialiser;
import beast.base.inference.parameter.RealParameter;
import beast.base.evolution.alignment.Alignment;
import beast.base.evolution.tree.Tree;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import master.conditions.LeafCountEndCondition;
import master.conditions.LineageEndCondition;
import master.conditions.PopulationEndCondition;
import master.conditions.PostSimCondition;
import master.model.InitState;
import master.model.Model;
import master.outputs.InheritanceTrajectoryOutput;
import master.postprocessors.InheritancePostProcessor;
import master.steppers.GillespieStepper;
import master.steppers.Stepper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Plugin which uses a MASTER simulation to construct a BEAST 2 tree."
        + " This only works when MASTER output is tree-like.")
@Citation("Tim Vaughan and Alexei Drummond, 'A Stochastic Simulator of "
        + "Birth–Death Master Equations with Application to Phylodynamics'. "
        + "Mol Biol Evol (2013) 30 (6): 1480-1493.")
public class BeastTreeFromMaster extends Tree implements StateNodeInitialiser {
    
    /*
     * XML inputs:
     */
    
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<>(
            "simulationTime",
            "The maximum length of time to simulate for. (Defaults to infinite.)");
    
    public Input<Integer> nSamplesInput = new Input<>(
            "nSamples",
            "Number of evenly spaced time points to sample state at.");
    
    public Input<Integer> seedInput = new Input<>(
            "seed",
            "Seed for RNG.");
        
    public Input<Stepper> stepperInput = new Input<>(
            "stepper",
            "State incrementing algorithm to use. (Default Gillespie.)",
            new GillespieStepper());
    
    public Input<Integer> verbosityInput = new Input<>(
            "verbosity", "Level of verbosity to use (0-2).", 1);
    
    // Model:
    public Input<Model> modelInput = new Input<>("model",
            "The specific model to simulate.",
            Input.Validate.REQUIRED);
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<>("initialState",
            "Initial state of system.",
            Input.Validate.REQUIRED);
    
    // End conditions:
    public Input<List<PopulationEndCondition>> popEndConditionsInput =
            new Input<>(
                    "populationEndCondition",
                    "Trajectory end condition based on population sizes.",
                    new ArrayList<>());
    
    // Post-simulation conditioning:
    public Input<List<PostSimCondition>> postSimConditionsInput =
            new Input<>("postSimCondition",
                    "A post-simulation condition.",
                    new ArrayList<>());

    public Input<Integer> maxConditionRejectsInput =
            new Input<>("maxConditionRejects",
                    "Maximum number of condition failures" +
                            "before aborting.  (Default is no limit.)");

    public Input<Boolean> samplePopulationSizesInput = new Input<>(
            "samplePopulationSizes",
            "Sample population sizes together with inheritance graph. (Default false.)",
            false);
    
    public Input<Boolean> sampleAtNodesOnlyInput = new Input<>(
            "sampleAtNodesOnly",
            "Sample population sizes only at graph node times. (Default false.)",
            false);
    
    // Lineage end conditions:
    public Input<List<LineageEndCondition>> lineageEndConditionsInput =
            new Input<>("lineageEndCondition",
                    "Trajectory end condition based on remaining lineages.",
                    new ArrayList<>());
    
        
    // Leaf count end conditions:
    public Input<List<LeafCountEndCondition>> leafCountEndConditionsInput =
            new Input<>("leafCountEndCondition",
                    "Trajectory end condition based on number of terminal nodes generated.",
                    new ArrayList<>());
    
    // Post-processors:
    public Input<List<InheritancePostProcessor>> inheritancePostProcessorsInput =
            new Input<>("inheritancePostProcessor",
                    "Post processor for inheritance graph.",
                    new ArrayList<>());
    
    // Outputs:
    public Input<List<InheritanceTrajectoryOutput>> outputsInput
            = new Input<>("output",
            "Output writer used to write results of simulation to disk.",
            new ArrayList<>());
    
    // BEAST Tree construction:
    public Input<Boolean> reverseTimeInput = new Input<>("reverseTime",
            "Read time in reverse when assembling tree. (Default true.)", true);
    
    public Input<Boolean> collapseSingletonsInput = new Input<>(
            "collapseSingletons",
            "Whether to join branches of singleton nodes together. Default true.",
            true);
    
    public Input<Alignment> alignmentInput = new Input<>("alignment",
            "If provided, nodes are equated with taxons having the same label.");
        

    public Input<RealParameter> originInput = new Input<>("origin",
            "if provided, this is used to set the correct origin for BeastTreeFromMaster tree");

    public Double origin;

    public BeastTreeFromMaster() { }

    @Override
    public void initAndValidate() {
    
        // Initialise inheritance trajectory simulation
        
        InheritanceTrajectory itraj = new InheritanceTrajectory();
        
        itraj.setInputValue("simulationTime", simulationTimeInput.get());
        itraj.setInputValue("nSamples", nSamplesInput.get());
        itraj.setInputValue("seed", seedInput.get());
        itraj.setInputValue("stepper", stepperInput.get());
        itraj.setInputValue("verbosity", verbosityInput.get());
        itraj.setInputValue("model", modelInput.get());
        itraj.setInputValue("initialState", initialStateInput.get());
        itraj.setInputValue("samplePopulationSizes", samplePopulationSizesInput.get());
        itraj.setInputValue("sampleAtNodesOnly", sampleAtNodesOnlyInput.get());
        
        for (PopulationEndCondition endCondition : popEndConditionsInput.get())
            itraj.setInputValue("populationEndCondition", endCondition);
        
        for (LineageEndCondition endCondition : lineageEndConditionsInput.get())
            itraj.setInputValue("lineageEndCondition", endCondition);
                
        for (LeafCountEndCondition endCondition : leafCountEndConditionsInput.get())
            itraj.setInputValue("leafCountEndCondition", endCondition);
        
        for (InheritancePostProcessor postProcessor : inheritancePostProcessorsInput.get())
            itraj.setInputValue("inheritancePostProcessor", postProcessor);
                
        for (PostSimCondition postSimCondition : postSimConditionsInput.get())
            itraj.setInputValue("postSimCondition", postSimCondition);

        itraj.setInputValue("maxConditionRejects", maxConditionRejectsInput.get());
        
        for (InheritanceTrajectoryOutput output : outputsInput.get())
            itraj.setInputValue("output", output);

        itraj.initAndValidate();
        
        // Run simulation and generate output
        itraj.run();
        
        // Assemble BEAST tree:
        assembleTree(itraj);

        origin = itraj.t;
        // DEBUG: can check BEAST's newick tree against ours
        //log(0, System.out);
        
        initStateNodes();        
    }
    
    /**
     * Use inheritance trajectory to assemble BEAST tree.
     * 
     * @param itraj inheritance trajectory object
     * @throws Exception when inheritance graph is not tree-like in preferred
     * time direction.
     */
    private void assembleTree(master.InheritanceTrajectory itraj) {

        boolean reverseTime = reverseTimeInput.get();
        
        Map<master.model.Node,String> leafLabels;
        List<master.model.Node> rootNodes, leafNodes;
        
        
        // Identify root and leaf nodes
        // (Use the "child" of the root node to account for the fact that
        // master always generates trees having an explicit end node.)
        master.model.Node masterRoot;
        if (reverseTime) {
            rootNodes = findEndNodes(itraj);
            masterRoot = rootNodes.get(0).getParents().get(0);
            leafNodes = itraj.startNodes;
        } else {
            rootNodes = itraj.startNodes;
            masterRoot = rootNodes.get(0).getChildren().get(0);
            leafNodes = findEndNodes(itraj);
        }
        
        if (rootNodes.size()!=1)
            throw new RuntimeException("Cannot assemble BEAST tree with multiple roots.");

        // Assign a unique integer label to each unnamed leaf node and
        // identify time of node furthest from root:
        leafLabels = Maps.newHashMap();
        double largestHeightDiff = 0.0;
        double youngestLeafTime = 0.0;
        int label = 1;
        for (master.model.Node leaf : leafNodes) {
            if (leaf.getName() == null)
                leafLabels.put(leaf, String.valueOf(label++));
            else
                leafLabels.put(leaf, leaf.getName());
            
            double thisHeightDiff = Math.abs(leaf.getTime()-masterRoot.getTime());
            if (thisHeightDiff>largestHeightDiff) {
                largestHeightDiff = thisHeightDiff;
                youngestLeafTime = leaf.getTime();
            }
        }

        // Reset node array
        m_nodes = null;
        
        // Create BEAST tree root node:
        beast.base.evolution.tree.Node beastRoot = new beast.base.evolution.tree.Node();
        
        // Assemble Tree
        assembleSubtree(masterRoot, beastRoot, youngestLeafTime, leafLabels);
        
        // Set node numbers:
        initNodeNumbers(beastRoot);
                
        // Tell BEAST tree what it's root is:
        setRoot(beastRoot);

        // and init array representation!
        initArrays();

    }
    
    /**
     * Find "leaves" of inheritance graph.
     * 
     * @param graph
     * @return Set containing leaf nodes.
     */
    private List<master.model.Node> findEndNodes(master.InheritanceTrajectory graph) {
        List<master.model.Node> endNodes = Lists.newArrayList();
        
        for (master.model.Node startNode : graph.startNodes)
            findEndNodesOnSubGraph(startNode, endNodes);
        
        return endNodes;
    }
    
    /**
     * Method used for recursive traversal of graph when finding leaf nodes.
     * 
     * @param node Current node in traversal.
     * @param endNodes Set of leaf nodes already found.
     */
    private void findEndNodesOnSubGraph(master.model.Node node,
            List<master.model.Node> endNodes) {
        
        if (node.getChildren().isEmpty()) {
            if (!endNodes.contains(node))
                endNodes.add(node);
            return;
        }
        
        for (master.model.Node child : node.getChildren())
            findEndNodesOnSubGraph(child, endNodes);
    }
    
    /**
     * Assemble beast tree corresponding to MASTER tree below masterParent
     * and attach to beastParent. 
     * 
     * @param masterNode
     * @param beastNode 
     * @param timeOffset 
     * @param leafLabels 
     * @return 
     */
    private void assembleSubtree(master.model.Node masterNode,
            beast.base.evolution.tree.Node beastNode, double timeOffset,
            Map<master.model.Node,String> leafLabels) {
        
        // Deal with potential time reversal:
        List<master.model.Node> masterChildren;
        if (reverseTimeInput.get())
            masterChildren = masterNode.getParents();
        else
            masterChildren = masterNode.getChildren();
        
        // Skip over single-child nodes:
        if (collapseSingletonsInput.get() && masterChildren.size() == 1) {
            assembleSubtree(masterChildren.get(0), beastNode, timeOffset, leafLabels);
            return;
        }
 
        // Set BEAST node height:
        beastNode.setHeight(Math.abs(masterNode.getTime()-timeOffset));
        
        // Add children:
        for (master.model.Node masterChild : masterChildren) {
            beast.base.evolution.tree.Node beastChild = new beast.base.evolution.tree.Node();
            assembleSubtree(masterChild, beastChild, timeOffset, leafLabels);
            beastNode.addChild(beastChild);
        }        
                
        // Label leaves by setting appropriate node IDs:
        if (masterChildren.isEmpty())
            beastNode.setID(leafLabels.get(masterNode));
        
        annotateNode(masterNode, beastNode);
    }
    
    /**
     * Copy MASTER node annotations to BEAST node.
     * 
     * @param masterNode
     * @param beastNode 
     */
    private void annotateNode(master.model.Node masterNode,
            beast.base.evolution.tree.Node beastNode) {
        
        master.model.Node annotationNode;
        if (reverseTimeInput.get()) {
            annotationNode = masterNode.getChildren().get(0);
        } else {
            annotationNode = masterNode;
        }
        
        beastNode.setMetaData("type", annotationNode.getPopulation().getType().getName());
        int[] loc = annotationNode.getPopulation().getLocation();
        if (loc.length > 1) {
            beastNode.setMetaData("location", loc);
        } else if (loc.length == 1)
            beastNode.setMetaData("location", loc[0]);
    }
    
    /**
     *  Assign integer node numbers to BEAST tree nodes.  If an alignment
     * is supplied, leaf node numbers are selected so that the MASTER node
     * labels match the taxon names.  An exception is thrown if no such
     * taxon exists.
     * 
     * @param beastRoot Root of BEAST tree.
     */
    public void initNodeNumbers(beast.base.evolution.tree.Node beastRoot) {

        List<beast.base.evolution.tree.Node> leaves = getBeastLeaves(beastRoot);

        int nodeNr = 0;
        for (beast.base.evolution.tree.Node leaf : leaves) {
            if (alignmentInput.get() != null) {
                if (!alignmentInput.get().getTaxaNames().contains(leaf.getID()))
                    throw new IllegalArgumentException("Alignment does not contain taxon named " + leaf.getID());

                leaf.setNr(alignmentInput.get().getTaxonIndex(leaf.getID()));
            } else if (m_taxonset.get() != null) {
                if (!m_taxonset.get().getTaxaNames().contains(leaf.getID()))
                    throw new IllegalArgumentException("TaxonSet does not contain taxon named " + leaf.getID());

                leaf.setNr(m_taxonset.get().getTaxonIndex(leaf.getID()));
            } else {
                leaf.setNr(nodeNr);
                nodeNr += 1;
            }
        }

        nodeNr = leaves.size();
        
        List<beast.base.evolution.tree.Node> nodes = Lists.newArrayList();
        List<beast.base.evolution.tree.Node> nodesPrime = Lists.newArrayList();

        nodes.addAll(leaves);

        while(nodes.size()>1) {
            nodesPrime.clear();
            for (beast.base.evolution.tree.Node node : nodes) {

                if (node.getParent() != null && !nodesPrime.contains(node.getParent()) && node.getParent().getNr() == 0) {
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
    List <beast.base.evolution.tree.Node> getBeastLeaves(beast.base.evolution.tree.Node node) {
        List<beast.base.evolution.tree.Node> nodeList = Lists.newArrayList();
        
        if (node.isLeaf()) {
            nodeList.add(node);
        } else {
            for (beast.base.evolution.tree.Node child : node.getChildren())
                nodeList.addAll(getBeastLeaves(child));
        }
        return nodeList;
    }

    @Override
    public void initStateNodes() {
        if (m_initial.get() != null) {
            m_initial.get().assignFrom(this);
        }

        if (originInput.get() != null) {
            originInput.get().setValue(origin);
        }
    }

    @Override
    public void getInitialisedStateNodes(List<StateNode> stateNodes) {
        if (m_initial.get() != null) {
            stateNodes.add(m_initial.get());
        }
        if (originInput.get() != null) {
            stateNodes.add(originInput.get());
        }
    }

}
