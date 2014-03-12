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
package master.outputs;

import beast.core.BEASTObject;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import master.InheritanceEnsemble;
import master.model.Node;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import master.model.Population;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import master.InheritanceTrajectory;

/**
 * Static methods for producing "extended Newick" representations of inheritance
 * graphs, as described by Cardona, Rossello and Valiente(BMC Bioinf., 2008).
 * If the graph is tree-like, this is just the standard Newick representation.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Output writer capable of writing inheritance graph to"
        + " disk in extended Newick format of Cardona et al, BMC Bioinf. (2008).")
public class NewickOutput extends BEASTObject implements 
        InheritanceTrajectoryOutput, InheritanceEnsembleOutput{
    
    InheritanceTrajectory graph;

    boolean reverseTime;
    boolean collapseSingleChildNodes;
    
    PrintStream ps;
    Set<Node> rootNodes, leafNodes;
    Map<Node, String> leafLabels;
    Map<Node, Integer> hybridIDs;

    public Input<String> fileNameInput = new Input<String>("fileName",
            "Name of file to write to.", Validate.REQUIRED);
    
    public Input<Boolean> reverseTimeInput = new Input<Boolean>("reverseTime",
            "Read graph in reverse time - useful for building coalescent trees.  (Default false.)",
            false);
    
    public Input<Boolean> collapseSingleChildNodesInput = new Input<Boolean>(
            "collapseSingleChildNodes",
            "Prune nodes having a single child from output. (Default false.)",
            false);
    
    public NewickOutput() { }
    
    @Override
    public void initAndValidate() { }

    @Override
    public void write(InheritanceTrajectory itraj) {
        
        PrintStream pstream = null;
        try {
            pstream = new PrintStream(fileNameInput.get());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewickOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (itraj.getSpec().getVerbosity()>0)
            System.out.println("Writing Newick output...");
        
        if (!itraj.getStartNodes().isEmpty())
            generateOutput(itraj, reverseTime, collapseSingleChildNodes, pstream);
        else {
            if (itraj.getSpec().getVerbosity()>0)
                System.out.println("Warning: Newick writer skipping empty graph.");
        }
    }

    @Override
    public void write(InheritanceEnsemble iensemble) {
        
        PrintStream pstream = null;
        try {
            pstream = new PrintStream(fileNameInput.get());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewickOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (iensemble.getSpec().getVerbosity()>0)
            System.out.println("Writing Newick output...");

        int skips=0;
        for (InheritanceTrajectory itraj : iensemble.getTrajectories()) {
            if (!itraj.getStartNodes().isEmpty())
                generateOutput(itraj, reverseTime, collapseSingleChildNodes, pstream);
            else {
                skips += 1;
                if (itraj.getSpec().getVerbosity()>0)
                    System.out.print("\rWarning: Newick writer skipping empty "
                            + "graph. (repeated " + skips + " times)");
            }
        }
        if (skips>0)
            System.out.println();
    }
       
    /**
     * Create an extended Newick string representation of graph.
     * 
     * @param graph Graph to represent.
     * @param reverseTime True causes the graph to be read in the direction
     * from the latest nodes to the earliest.  Useful for coalescent trees.
     * @param collapseSingleChildNodes 
     * @param pstream 
     */
    public void generateOutput(InheritanceTrajectory graph, boolean reverseTime,
            boolean collapseSingleChildNodes, PrintStream pstream) {

        this.graph = graph;
        this.reverseTime = reverseTime;
        this.collapseSingleChildNodes = collapseSingleChildNodes;
        this.ps = pstream;
        
        leafLabels = Maps.newHashMap();
        hybridIDs = Maps.newHashMap();
        
        // Identify root and leaf nodes
        if (reverseTime) {
            rootNodes = findEndNodes(graph);
            leafNodes = Sets.newHashSet(graph.startNodes);
        } else {
            rootNodes = Sets.newHashSet(graph.startNodes);
            leafNodes = findEndNodes(graph);
        }
        
        // Assign a unique integer label to each unnamed leaf node:
        leafLabels = Maps.newHashMap();
        int label = 1;
        for (Node leaf : leafNodes) {
            if (leaf.getName() == null)
                leafLabels.put(leaf, String.valueOf(label++));
            else
                leafLabels.put(leaf, leaf.getName());
        }
        
        // Identify hybrid nodes:
        Set<Node> hybridNodes = findHybridNodes(rootNodes, reverseTime);
        
        // Assign a unique integer label to each hybrid node:
        hybridIDs = Maps.newHashMap();
        label = 1;
        for (Node hybrid : hybridNodes)
            hybridIDs.put(hybrid, label++);
        
        Set<Node> visitedHybrids = Sets.newHashSet();        
        boolean first = true;
        for (Node node : rootNodes) {
            if (!first)
                ps.append(",");
            else
                first = false;

            Node next;
            if (reverseTime)
                next = node.getParents().get(0);
            else
                next = node.getChildren().get(0);
            
            subTreeToExtendedNewick(next, node, visitedHybrids);
        }
        
        ps.append(";\n");
    }
    

    /**
     * Recursive method to construct an extended newick string representing
     * a sub-tree or sub-graph.
     * 
     * @param node Root of this subtree
     * @param last Previous node in traversal (null if none)
     * @param visitedHybrids Set containing hybrids already visited
     */
    private void subTreeToExtendedNewick(Node node, Node last, Set<Node> visitedHybrids) {
        
        double branchLength;
        if (last == null)
            branchLength = 0;
        else
            branchLength = Math.abs(node.getTime() - last.getTime());
        
        if (visitedHybrids.contains(node)) {
            addLabel(node, branchLength);
            return;
        }
        
        visitedHybrids.add(node);
        
        List<Node> nextNodes;
        if (reverseTime)
            nextNodes = node.getParents();
        else
            nextNodes = node.getChildren();
        
        if (nextNodes.size()==1 && collapseSingleChildNodes) {
            subTreeToExtendedNewick(nextNodes.get(0), last, visitedHybrids);
            
        } else {
            if (nextNodes.size()>0) {
                ps.append("(");
                boolean first = true;
                for (Node next : nextNodes) {
                    if (!first)
                        ps.append(",");
                    else
                        first = false;
                    
                    subTreeToExtendedNewick(next, node, visitedHybrids);
                }
                ps.append(")");
            }
            
            addLabel(node, branchLength);
        }
    }
    
    /**
     * Add node label to Newick string.
     * 
     * @param node
     * @param branchLength 
     */
    protected void addLabel(Node node, double branchLength) {
        
        if (leafLabels.containsKey(node))
            ps.append(leafLabels.get(node));
        
        if (hybridIDs.containsKey(node))
            ps.append("#").append(String.valueOf(hybridIDs.get(node)));
        // note that we've omitted the optional "type" specifier
        
        ps.append(":").append(String.valueOf(branchLength));
    }
    
    /**
     * Identify hybrid nodes (nodes with more than one parent) and assign each
     * a unique integer ID.
     * 
     * @param rootNodes List of nodes at which to begin graph traversal.
     * @param reverseTime Whether traversal should occur in reverse time.
     * @return Map of hybrid nodes to their chosen IDs.
     */
    private Set<Node> findHybridNodes(Set<Node> rootNodes, boolean reverseTime) {
        Set<Node> visited = Sets.newHashSet();
        Set<Node> hybridNodes = Sets.newHashSet();
        
        for (Node node : rootNodes)
            findHybridNodesInSubTree(node, visited, hybridNodes, reverseTime);
        
        return hybridNodes;
    }
    
    /**
     * Recursive traversal method for identifying hybrid nodes.
     * 
     * @param node Node containing a subgraph.
     * @param visited Set containing nodes already seen.
     * @param hybridIDs Map of hybrid nodes already found to integer labels.
     * @param reverseTime Whether traversal is occuring in reverse time.
     */
    private void findHybridNodesInSubTree(Node node, Set<Node> visited,
            Set<Node> hybridLabels, boolean reverseTime) {
        if (visited.contains(node)) {
            if (!hybridLabels.contains(node))
                hybridLabels.add(node);
            return;
        } else
            visited.add(node);
        
        List<Node> nextNodes;
        if (reverseTime)
            nextNodes = node.getParents();
        else
            nextNodes = node.getChildren();
        
        for (Node next : nextNodes)
            findHybridNodesInSubTree(next, visited, hybridLabels, reverseTime);
    }
    
    /**
     * Find "leaves" of inheritance graph.
     * 
     * @param graph
     * @return Set containing leaf nodes.
     */
    private Set<Node> findEndNodes(InheritanceTrajectory graph) {
        Set<Node> endNodes = Sets.newHashSet();
        
        for (Node startNode : graph.startNodes)
            findEndNodesOnSubGraph(startNode, endNodes);
        
        return endNodes;
    }
    
    /**
     * Method used for recursive traversal of graph when finding leaf nodes.
     * 
     * @param node Current node in traversal.
     * @param endNodes Set of leaf nodes already found.
     */
    private void findEndNodesOnSubGraph(Node node, Set<Node> endNodes) {
        
        if (node.getChildren().isEmpty()) {
            endNodes.add(node);
            return;
        }
        
        for (Node child : node.getChildren())
            findEndNodesOnSubGraph(child, endNodes);
    }
    
    @Override
    public String toString() {
        return ps.toString();
    }
    
}
