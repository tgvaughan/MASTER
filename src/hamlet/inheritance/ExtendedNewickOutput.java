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
import java.io.PrintStream;
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
        
        if (!reverseTime)
            writeOutForwardTime(graph, includeRootBranches, pstream);
        else
            writeOutReverseTime(graph, includeRootBranches, pstream);
    }
    
    private static void writeOutForwardTime(InheritanceGraph graph,
            boolean includeRootBranches,
            PrintStream pstream) {
        
        Map<Node, Integer> hybridLabels = labelHybridNodes(graph);
        Set<Node> visitedHybrids = Sets.newHashSet();
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Node node : graph.startNodes) {
            if (!first)
                sb.append(",");
            else
                first = false;

            if (includeRootBranches)
                subTreeToExtendedNewick(node, null, hybridLabels, visitedHybrids, sb);
            else
                subTreeToExtendedNewick(node.getChildren().get(0), null, hybridLabels, visitedHybrids, sb);
        }
        
        pstream.println(sb.append(";"));
    }
    
    private static void subTreeToExtendedNewick(Node node, Node last,
            Map<Node,Integer> hybridLabels, Set<Node> visitedHybrids,
            StringBuilder sb) {
        
        double branchLength;
        if (last == null)
            branchLength = 0;
        else
            branchLength = node.getTime() - last.getTime();
        
        if (visitedHybrids.contains(node)) {
            sb.append("#H").append(hybridLabels.get(node));
            sb.append(":").append(branchLength);
            return;
        }
        
        visitedHybrids.add(node);
        
        if (node.getChildren().size()>0) {
            sb.append("(");
            boolean first = true;
            for (Node child : node.getChildren()) {
                if (!first)
                    sb.append(",");
                else
                    first = false;
                
                subTreeToExtendedNewick(child, node, hybridLabels, visitedHybrids, sb);
            }
            sb.append(")");
        } else {
            sb.append(node.hashCode());
        }
        
        if (hybridLabels.containsKey(node))
            sb.append("#H").append(hybridLabels.get(node));
        
        sb.append(":").append(branchLength);
    }
    
    private static Map<Node, Integer> labelHybridNodes(InheritanceGraph graph) {
        Set<Node> visited = Sets.newHashSet();
        Map <Node,Integer> hybridLabels = Maps.newHashMap();
        
        for (Node node : graph.startNodes)
            findHybridNodesInSubTree(node, visited, hybridLabels);
        
        return hybridLabels;
    }
    
    private static void findHybridNodesInSubTree(Node node, Set<Node> visited,
            Map<Node,Integer> hybridLabels) {
        if (visited.contains(node)) {
            if (!hybridLabels.containsKey(node))
                hybridLabels.put(node, hybridLabels.size()+1);
            return;
        } else
            visited.add(node);
        
        for (Node child : node.getChildren())
            findHybridNodesInSubTree(child, visited, hybridLabels);
    }
 
    private static void writeOutReverseTime(InheritanceGraph graph,
            boolean includeRootBranches,
            PrintStream pstream) {
        
        Set<Node> endNodes = findEndNodes(graph);
        Map<Node, Integer> hybridLabels = labelHybridNodesReverseTime(endNodes);
        Set<Node> visitedHybrids = Sets.newHashSet();
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Node node : endNodes) {
            if (!first)
                sb.append(",");
            else
                first = false;

            if (includeRootBranches)
                subTreeToExtendedNewickReverseTime(node, null, hybridLabels, visitedHybrids, sb);
            else
                subTreeToExtendedNewickReverseTime(node.getParents().get(0), null, hybridLabels, visitedHybrids, sb);
        }
        
        pstream.println(sb.append(";"));
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
    
    private static Map<Node, Integer> labelHybridNodesReverseTime(Set<Node> endNodes) {
        Set<Node> visited = Sets.newHashSet();
        Map <Node,Integer> hybridLabels = Maps.newHashMap();
        
        for (Node node : endNodes)
            findHybridNodesInSubTreeReverseTime(node, visited, hybridLabels);
        
        return hybridLabels;
    }
    
    private static void findHybridNodesInSubTreeReverseTime(Node node, Set<Node> visited,
            Map<Node,Integer> hybridLabels) {
        if (visited.contains(node)) {
            if (!hybridLabels.containsKey(node))
                hybridLabels.put(node, hybridLabels.size()+1);
            return;
        } else
            visited.add(node);
        
        for (Node parent : node.getParents())
            findHybridNodesInSubTreeReverseTime(parent, visited, hybridLabels);
    }
    
    private static void subTreeToExtendedNewickReverseTime(Node node, Node last,
            Map<Node,Integer> hybridLabels, Set<Node> visitedHybrids,
            StringBuilder sb) {
        
        double branchLength;
        if (last == null)
            branchLength = 0;
        else
            branchLength = last.getTime() - node.getTime();
        
        if (visitedHybrids.contains(node)) {
            sb.append("#H").append(hybridLabels.get(node));
            sb.append(":").append(branchLength);
            return;
        }
        
        visitedHybrids.add(node);
        
        if (node.getParents().size()>0) {
            sb.append("(");
            boolean first = true;
            for (Node parent : node.getParents()) {
                if (!first)
                    sb.append(",");
                else
                    first = false;
                
                subTreeToExtendedNewickReverseTime(parent, node, hybridLabels, visitedHybrids, sb);
            }
            sb.append(")");
        } else {
            sb.append(node.hashCode());
        }
        
        if (hybridLabels.containsKey(node))
            sb.append("#H").append(hybridLabels.get(node));
        
        sb.append(":").append(branchLength);
    }
}
