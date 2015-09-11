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
package master.postprocessors;

import beast.core.BEASTObject;
import beast.core.Input;
import java.util.ArrayList;
import java.util.List;
import master.InheritanceTrajectory;
import master.model.Node;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class LineageFilter extends BEASTObject implements InheritancePostProcessor {
    
    public Input<String> reactNameInput = new Input<>("reactionName",
            "Name of reaction used to filter lineages.");
    
    public Input<String> populationNameInput = new Input<>("populationName",
            "Name of population used to filter lineages.");
    
    public Input<Boolean> discardInput = new Input<>("discard",
            "Discard (instead of keep) lineages that match the name.", false);
    
    public Input<Boolean> leavesOnlyInput = new Input<>("leavesOnly",
            "Only alter leaves.", false);
    
    public Input<String> markAnnotationInput = new Input<>("markAnnotation",
            "Mark using this annotation rather than pruning.");
    
    public Input<Boolean> noCleanInput = new Input<>("noClean",
            "Do not remove no-state-change nodes.", false);
    
    public Input<Boolean> reverseTimeInput = new Input<>("reverseTime",
            "Process inheritance graph in reverse time.  Default false.", false);

    private String string;
    private Rule rule;
    
    @Override
    public void initAndValidate() {
        
        if ((reactNameInput.get() != null) && (populationNameInput.get() != null))
            throw new IllegalArgumentException("Only ONE of the reaction or population "
                    + "name inputs to LineageFilter may be specified.");
        
        if ((reactNameInput.get() == null) && (populationNameInput.get() == null))
            throw new IllegalArgumentException("Either reaction or population "
                    + "name input to LineageFilter must be specified.");
        
        if (reactNameInput.get() != null) {
            string = reactNameInput.get();
            if (discardInput.get())
                rule = Rule.BY_REACTNAME_DISCARD;
            else
                rule = Rule.BY_REACTNAME;
        }
        
        if (populationNameInput.get() != null) {
            string = populationNameInput.get();
            if (discardInput.get())
                rule = Rule.BY_POPTYPENAME_DISCARD;
            else
                rule = Rule.BY_POPTYPENAME;
        }
        
    }

    @Override
    public void process(InheritanceTrajectory itraj) {
        LineageFilter.filter(itraj, rule, string,
                markAnnotationInput.get(), leavesOnlyInput.get(),
                noCleanInput.get(), reverseTimeInput.get());
    }
    
    public enum Rule {
        BY_REACTNAME, BY_POPTYPENAME,
        BY_REACTNAME_DISCARD, BY_POPTYPENAME_DISCARD
    };
    
    /**
     * Keep only those lineages terminating in a node satisfying
     * the filter rule.
     * 
     * @param itraj inheritance trajectory containing graph to filter
     * @param rule filtering rule
     * @param name name of reaction or population used in rule
     * @param markAnnotation annotation to use if graph is to be marked only
     * @param leavesOnly only filter the leaf nodes
     * @param noClean do not clean resulting singleton nodes
     * @param reverseTime interpret the graph in reverse time
     */
    public static void filter(InheritanceTrajectory itraj,
            Rule rule, String name,
            String markAnnotation, boolean leavesOnly,
            boolean noClean, boolean reverseTime) {
        
        boolean markOnly;
        if (markAnnotation != null)
            markOnly = true;
        else {
            markAnnotation = "__mark__";
            markOnly = false;
        }
        
        // Get list of root and leaf nodes:
        List<Node> rootNodes, leafNodes;
        if (reverseTime) {
            rootNodes = itraj.getEndNodes();
            leafNodes = itraj.getStartNodes();
        } else {
            rootNodes = itraj.getStartNodes();
            leafNodes = itraj.getEndNodes();
        }
        
        // Pin existing singleton nodes:
        for (Node node : rootNodes)
            pinSingletons(node, reverseTime);
        
        // Mark lineages ancestral to nodes with matching reactionGroup
        for (Node node : leafNodes) {
            switch (rule) {
                case BY_POPTYPENAME:
                    if (node.getPopulation() != null
                            && node.getPopulation().getType().getName().equals(name))
                        mark(node, reverseTime, markAnnotation);
                    else
                        if (leavesOnly) {
                            for(Node prev : getPrev(node, reverseTime))
                                mark(prev, reverseTime, markAnnotation);
                            
                        }
                    break;
                    
                case BY_POPTYPENAME_DISCARD:
                    if (!(node.getPopulation() != null
                            && node.getPopulation().getType().getName().equals(name)))
                        mark(node, reverseTime, markAnnotation);
                    else
                        if (leavesOnly) {
                            for(Node prev : getPrev(node, reverseTime))
                                mark(prev, reverseTime, markAnnotation);
                            
                        }
                    break;
                    
                case BY_REACTNAME:
                    if ((node.getReaction() != null && node.getReaction().getName().equals(name))
                            || (node.getReaction() == null && name.equals("NONE")))
                        mark(node, reverseTime, markAnnotation);
                    else
                        if (leavesOnly) {
                            for(Node prev : getPrev(node, reverseTime))
                                mark(prev, reverseTime, markAnnotation);
                            
                        }
                    break;
                    
                case BY_REACTNAME_DISCARD:
                    if (!((node.getReaction() != null && node.getReaction().getName().equals(name))
                            || (node.getReaction() == null && name.equals("NONE"))))
                        mark(node, reverseTime, markAnnotation);
                    else
                        if (leavesOnly) {
                            for(Node prev : getPrev(node, reverseTime))
                                mark(prev, reverseTime, markAnnotation);
                            
                        }
                    break;
            }
        }
        
        // Explicitly unmark lineages decending from unmarked root nodes
        for (Node node : rootNodes)
            markFalse(node, reverseTime, markAnnotation);
        
        if (markOnly)
            return;
        
        // Prune unmarked lineages:
        for (Node node : leafNodes) {
            if (!isMarked(node, markAnnotation))
                pruneLineage(node, reverseTime, markAnnotation);
        }
        
        // Remove any unmarked start nodes:
        List<Node> oldStartNodes = new ArrayList<>();
        oldStartNodes.addAll(itraj.getStartNodes());        
        for (Node node : oldStartNodes) {
            if (!isMarked(node, markAnnotation))
                itraj.getStartNodes().remove(node);
        }
        
        // Clean graph of singleton nodes that don't represent state changes:
        if (!noClean)
            for (Node node : rootNodes)
                cleanSubGraph(node, reverseTime);
        
        // Remove pins and marks
        for (Node node : rootNodes)
            unPinSingletons(node, reverseTime, markAnnotation);
        
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
     * Explicitly unmark node and its descendants which do not
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
        List<Node> prevNodes = new ArrayList<>();
        prevNodes.addAll(getPrev(node, reverseTime));
        
        for (Node prev : prevNodes) {
            getPrev(node, reverseTime).remove(node);
            getNext(prev, reverseTime).remove(node);
            if (!isMarked(prev, markAnnotation))
                pruneLineage(prev, reverseTime, markAnnotation);
        }
    }
    
    /**
     * Pin existing singleton nodes in place.
     * 
     * @param node
     * @param reverseTime 
     */
    private static void pinSingletons(Node node, boolean reverseTime) {
        List<Node> nextNodes = getNext(node, reverseTime);
        List<Node> prevNodes = getPrev(node, reverseTime);
        
        if (nextNodes.size() == 1 && prevNodes.size() == 1)
            node.setAttribute("__pinned__", true);
        
        for (Node child : nextNodes)
            pinSingletons(child, reverseTime);
    }
    
    /**
     * Remove node pins and marks.
     * 
     * @param node
     * @param reverseTime 
     * @param markAnnotation 
     */
    private static void unPinSingletons(Node node, boolean reverseTime, String markAnnotation) {

        if (node.getAttributeNames().contains("__pinned__"))
            node.removeAttribute("__pinned__");
        
        if (node.getAttributeNames().contains(markAnnotation))
            node.removeAttribute(markAnnotation);

        for (Node child : getNext(node, reverseTime))
            unPinSingletons(child, reverseTime, markAnnotation);
    }
    
    /**
     * Clean graph below node of any unpinned singleton nodes which do not represent
     * state changes.
     * 
     * @param node
     * @param reverseTime 
     */
    private static void cleanSubGraph(Node node, boolean reverseTime) {
        List<Node> nextNodes = getNext(node, reverseTime);
        List<Node> prevNodes = getPrev(node, reverseTime);
        
        if (nextNodes.size() == 1 && prevNodes.size() == 1
                && !node.getAttributeNames().contains("__pinned__")) {
            Node parent = prevNodes.get(0);
            Node child = nextNodes.get(0);
            
            if (node.getPopulation().equals(child.getPopulation())) {
                
                int idx = getPrev(child, reverseTime).indexOf(node);
                getPrev(child, reverseTime).set(idx, parent);
                
                idx = getNext(parent, reverseTime).indexOf(node);
                getNext(parent, reverseTime).set(idx, child);
            }
        }
        
        List<Node> nextNodesCopy = new ArrayList<>();
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
