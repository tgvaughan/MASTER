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
 * Static methods for "extended Newick" representations of an inheritance
 * graphs, as described by Cardona, Rossello and Valiente(BMC Bioinf., 2008).
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class ExtendedNewickOutput {
    
    /**
     * Write extended Newick string representation of graph to pstream.
     * 
     * @param graph
     * @param reverseTime
     * @param includeRootBranches 
     * @param pstream 
     */
    public static void writeOut(InheritanceGraph graph,
            boolean reverseTime, boolean includeRootBranches,
            PrintStream pstream) {
        
        Set<Node> rootNodes;
        if (reverseTime)
            rootNodes = findEndNodes(graph);
        else
            rootNodes = Sets.newHashSet(graph.startNodes);
        
        Map<Node, Integer> hybridLabels = labelHybridNodes(rootNodes, reverseTime);
        Set<Node> visitedHybrids = Sets.newHashSet();
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Node node : rootNodes) {
            if (!first)
                sb.append(",");
            else
                first = false;

            if (includeRootBranches)
                subTreeToExtendedNewick(node, null,
                        hybridLabels, visitedHybrids, reverseTime, sb);
            else
                subTreeToExtendedNewick(node.getChildren().get(0), null,
                        hybridLabels, visitedHybrids, reverseTime, sb);
        }
        
        pstream.println(sb.append(";"));

    }
    
    
    private static void subTreeToExtendedNewick(Node node, Node last,
            Map<Node,Integer> hybridLabels, Set<Node> visitedHybrids,
            boolean reverseTime, StringBuilder sb) {
        
        double branchLength;
        if (last == null)
            branchLength = 0;
        else
            branchLength = Math.abs(node.getTime() - last.getTime());
        
        if (visitedHybrids.contains(node)) {
            sb.append("#H").append(hybridLabels.get(node));
            sb.append(":").append(branchLength);
            return;
        }
        
        visitedHybrids.add(node);
        
        List<Node> nextNodes;
        if (reverseTime)
            nextNodes = node.getParents();
        else
            nextNodes = node.getChildren();
        
        if (nextNodes.size()>0) {
            sb.append("(");
            boolean first = true;
            for (Node next : nextNodes) {
                if (!first)
                    sb.append(",");
                else
                    first = false;
                
                subTreeToExtendedNewick(next, node, hybridLabels, visitedHybrids, reverseTime, sb);
            }
            sb.append(")");
        } else {
            sb.append(node.hashCode());
        }
        
        if (hybridLabels.containsKey(node))
            sb.append("#H").append(hybridLabels.get(node));
        
        sb.append(":").append(branchLength);
    }
    
    private static Map<Node, Integer> labelHybridNodes(Set<Node> rootNodes, boolean reverseTime) {
        Set<Node> visited = Sets.newHashSet();
        Map <Node,Integer> hybridLabels = Maps.newHashMap();
        
        for (Node node : rootNodes)
            findHybridNodesInSubTree(node, visited, hybridLabels, reverseTime);
        
        return hybridLabels;
    }
    
    private static void findHybridNodesInSubTree(Node node, Set<Node> visited,
            Map<Node,Integer> hybridLabels, boolean reverseTime) {
        if (visited.contains(node)) {
            if (!hybridLabels.containsKey(node))
                hybridLabels.put(node, hybridLabels.size()+1);
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
    
    private static Set<Node> findEndNodes(InheritanceGraph graph) {
        Set<Node> endNodes = Sets.newHashSet();
        
        for (Node startNode : graph.startNodes)
            findEndNodesOnSubGraph(startNode, endNodes);
        
        return endNodes;
    }
    
    private static void findEndNodesOnSubGraph(Node node, Set<Node> endNodes) {
        
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
//        Node root = (new Node(X,0))
//                .addChild((new Node(X,1)).addChild(new Node(X,2)).addChild(new Node(X,2)))
//                .addChild(new Node(X,2));

        // Simple network:
        Node hybrid = (new Node(X,1)).addChild(new Node(X,2)).addChild(new Node(X,2));
        Node root = (new Node(X,0))
                .addChild(hybrid)
                .addChild((new Node(X,0.5)).addChild(hybrid).addChild(new Node(X,2)));
        
        InheritanceGraph graph = new InheritanceGraph(root);
        
        writeOut(graph, false, true, new PrintStream("out.tree"));
    }
}
