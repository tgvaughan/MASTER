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
package hamlet.inheritance;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import hamlet.Population;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Static methods for producing "extended Newick" representations of inheritance
 * graphs, as described by Cardona, Rossello and Valiente(BMC Bioinf., 2008).
 * If the graph is tree-like, this is just the standard Newick representation.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class NewickOutput {
    
    InheritanceGraph graph;
    boolean reverseTime, includeRootBranches;
    
    StringBuilder newickStr;
    Set<Node> rootNodes, leafNodes;
    Map<Node, Integer> leafLabels, hybridLabels;
    
    
    /**
     * Constructor.
     * 
     * @param graph Graph to represent.
     * @param reverseTime True causes the graph to be read in the direction
     * from the latest nodes to the earliest.  Useful for coalescent trees.
     * @param includeRootBranches Hamlet always creates rooted graphs/trees
     * starting with a singleton node.  Some software (I'm looking at you,
     * APE) doesn't like this.  Use false to exclude the root branch.
     */
    public NewickOutput(InheritanceGraph graph,
            boolean reverseTime, boolean includeRootBranches) {

        this.graph = graph;
        this.reverseTime = reverseTime;
        this.includeRootBranches = includeRootBranches;
        
        leafLabels = Maps.newHashMap();
        hybridLabels = Maps.newHashMap();
        
        // Identify root and leaf nodes
        if (reverseTime) {
            rootNodes = findEndNodes(graph);
            leafNodes = Sets.newHashSet(graph.startNodes);
        } else {
            rootNodes = Sets.newHashSet(graph.startNodes);
            leafNodes = findEndNodes(graph);
        }
        
        // Assign a unique integer label to each leaf node:
        leafLabels = Maps.newHashMap();
        int label = 1;
        for (Node leaf : leafNodes)
            leafLabels.put(leaf, label++);
        
        // Identify hybrid nodes:
        Set<Node> hybridNodes = findHybridNodes(rootNodes, reverseTime);
        
        // Assign a unique integer label to each hybrid node:
        hybridLabels = Maps.newHashMap();
        label = 1;
        for (Node hybrid : hybridNodes)
            hybridLabels.put(hybrid, label++);
        
        Set<Node> visitedHybrids = Sets.newHashSet();        
        newickStr = new StringBuilder();
        boolean first = true;
        for (Node node : rootNodes) {
            if (!first)
                newickStr.append(",");
            else
                first = false;

            if (includeRootBranches)
                subTreeToExtendedNewick(node, null, visitedHybrids);
            else {
                Node next;
                if (reverseTime)
                    next = node.getParents().get(0);
                else
                    next = node.getChildren().get(0);
                subTreeToExtendedNewick(next, null, visitedHybrids);
            }
        }
        
        newickStr.append(";");
    }
    
    public void writeOut(PrintStream pstream) {              
        pstream.println(newickStr);
    }
    
    /**
     * Recursive method to construct an extended newick string representing
     * a sub-tree or sub-graph.
     * @param node Root of this subtree
     * @param last Previous node in traversal (null if none)
     * @param leafLabels Map of leaf nodes to their chosen labels
     * @param hybridLabels Map of hybrid nodes to their chosen labels
     * @param visitedHybrids Set containing hybrids already visited
     * @param reverseTime Whether the traversal is in reverse time
     * @param sb StringBuilder that output is sent to
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
        
        if (nextNodes.size()>0) {
            newickStr.append("(");
            boolean first = true;
            for (Node next : nextNodes) {
                if (!first)
                    newickStr.append(",");
                else
                    first = false;
                
                subTreeToExtendedNewick(next, node, visitedHybrids);
            }
            newickStr.append(")");
        }

        addLabel(node, branchLength);
    }
    
    protected void addLabel(Node node, double branchLength) {
        
        if (leafLabels.containsKey(node))
            newickStr.append(leafLabels.get(node));
        
        if (hybridLabels.containsKey(node))
            newickStr.append("#H").append(hybridLabels.get(node));
        
        newickStr.append(":").append(branchLength);
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
     * @param hybridLabels Map of hybrid nodes already found to integer labels.
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
    private Set<Node> findEndNodes(InheritanceGraph graph) {
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
    
    
    /**
     * Main method for testing.
     * 
     * @param args 
     */
    public static void main (String[] args) throws FileNotFoundException {
       
        // Create artificial inheritance graph:
        
        Population X = new Population("X");
        
        // Basic 3-taxon tree: (:1,:1):1,:2):0;
        Node root = (new Node(X,0))
                .addChild((new Node(X,1)).addChild(new Node(X,2)).addChild(new Node(X,2)))
                .addChild(new Node(X,2));

        // Simple network:
//        Node hybrid = (new Node(X,1)).addChild(new Node(X,2)).addChild(new Node(X,2));
//        Node root = (new Node(X,0))
//                .addChild(hybrid)
//                .addChild((new Node(X,0.5)).addChild(hybrid).addChild(new Node(X,2)));
        
        InheritanceGraph graph = new InheritanceGraph(root);
        
        (new NewickOutput(graph, false, true)).writeOut(new PrintStream("out.tree"));
    }
}
