/*
 * Copyright (C) 2013 Tim Vaughan <tgvaughan@gmail.com>
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
package master.inheritance;

import java.util.ArrayList;
import java.util.List;
import master.Population;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class FilterLineages {
    
    public enum Rule {
        BY_REACTNAME, BY_POPTYPENAME,
        BY_REACTNAME_INV, BY_POPTYPENAME_INV
    };
    
    /**
     * Keep only those lineages terminating in a node satisfying the filter function.
     * 
     * @param itraj Inheritance trajectory object
     * @param filterFunc Function accepting node and string and returning boolean
     * @param markOnly If true, mark lineages rather than pruning them.
     * @param reverseTime If true, time increases in opposite direction
     * to the Markov process which generated the graph.
     */
    public static void filter(InheritanceTrajectory itraj,
            Rule rule, String name, boolean markOnly, boolean noClean, boolean reverseTime) {
        
        // Get list of root and leaf nodes:
        List<Node> rootNodes, leafNodes;
        if (reverseTime) {
            rootNodes = itraj.getEndNodes();
            leafNodes = itraj.getStartNodes();
        } else {
            rootNodes = itraj.getStartNodes();
            leafNodes = itraj.getEndNodes();
        }
        
        // Mark lineages ancestral to nodes with matching reactionGroup
        for (Node node : leafNodes) {
            switch (rule) {
                case BY_POPTYPENAME:
                    if (node.getPopulation().getType().getName().equals(name))
                        mark(node, reverseTime);
                    break;
                    
                case BY_POPTYPENAME_INV:
                    if (!node.getPopulation().getType().getName().equals(name))
                        mark(node, reverseTime);
                    break;
                    
                case BY_REACTNAME:
                    if (node.getReactionGroup().getName().equals(name))
                        mark(node, reverseTime);
                    break;
                    
                case BY_REACTNAME_INV:
                    if (!node.getReactionGroup().getName().equals(name))
                        mark(node, reverseTime);
                    break;
            }
        }
        
        // Explicitly unmark lineages decending from unmarked root nodes
        for (Node node : rootNodes)
            markFalse(node, reverseTime);
        
        if (markOnly)
            return;
        
        // Prune unmarked lineages:
        for (Node node : leafNodes) {
            if (!isMarked(node))
                pruneLineage(node, reverseTime);
        }
        
        // Remove any unmarked start nodes:
        List<Node> oldStartNodes = new ArrayList<Node>();
        oldStartNodes.addAll(itraj.getStartNodes());        
        for (Node node : oldStartNodes) {
            if (!isMarked(node))
                itraj.getStartNodes().remove(node);
        }
        
        if (noClean)
            return;
        
        // Clean graph of singleton nodes that don't represent state changes:
        for (Node node : itraj.getStartNodes())
            cleanSubGraph(node, reverseTime);
    }
    
    /**
     * Determine whether node belongs to a sampled lineage.
     * 
     * @param node
     * @return true if lineage has been sampled
     */
    private static boolean isMarked(Node node) {
        Boolean mark = (Boolean) node.getAttribute("marked");
        return mark != null && mark;
    }
    
    /**
     * Mark node and its ancestors as belonging to a sampled lineage.
     * 
     * @param node 
     * @param reverseTime 
     */
    private static void mark(Node node, boolean reverseTime) {

        node.setAttribute("marked", true);
        List<Node> prevNodes = getPrev(node, reverseTime);
        for (Node prev : prevNodes)
            mark(prev, reverseTime);
    }
    
    /**
     * Explicitly unmark node and its decendents which do not
     * belong to sampled lineages.
     * 
     * @param node
     * @param reverseTime 
     */
    private static void markFalse(Node node, boolean reverseTime) {
        if (!isMarked(node))
            node.setAttribute("marked", false);
        
        List<Node> nextNodes = getNext(node, reverseTime);
        for (Node next : nextNodes)
            markFalse(next, reverseTime);
    }
    
    /**
     * Discard ancestral nodes until a node marked as belonging to a
     * sampled lineage is encountered.
     * 
     * @param node
     * @param reverseTime 
     */
    private static void pruneLineage(Node node, boolean reverseTime) {
        List<Node> prevNodes = new ArrayList<Node>();
        prevNodes.addAll(getPrev(node, reverseTime));
        
        for (Node prev : prevNodes) {
            getPrev(node, reverseTime).remove(node);
            getNext(prev, reverseTime).remove(node);
            if (!isMarked(prev))
                pruneLineage(prev, reverseTime);
        }
    }
    
    /**
     * Clean graph below node of any singleton nodes which do not represent
     * state changes.
     * 
     * @param node
     * @param reverseTime 
     */
    private static void cleanSubGraph(Node node, boolean reverseTime) {
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
            cleanSubGraph(child, reverseTime);
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
