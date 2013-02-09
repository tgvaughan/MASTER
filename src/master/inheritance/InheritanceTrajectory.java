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
package master.inheritance;

import beast.util.Randomizer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import master.Population;
import master.PopulationEndCondition;
import master.PopulationState;
import master.Trajectory;

/**
 * A class representing a stochastic inheritance trajectory generated under
 * a particular stochastic population dynamics model. Inheritance trajectories
 * are trajectories which additionally contain a time graph reprepresenting
 * the lineages decending from members of a subset of the population.
 *
 * <p>Things to keep in mind if you're reading this code:
 *
 * <ol> <li>Construction of the graph proceeds in a top-down fashion beginning
 * with the earliest nodes (provided in spec.initNodes), implementing changes as
 * reactions occur and finishing when one of the end conditions is met.</li>
 *
 * <li>During graph construction, activeLineages maintains a list of node
 * objects that represent single lineages extant at the current time. The fact
 * that they represent lineages rather than regular graph nodes is important, as
 * it means that the node objects contained in activeLineages may have only one
 * parent, as having more would mean the node represents more than one
 * lineage.</li> </ol>
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceTrajectory extends Trajectory {

    // List of nodes present at the start of the simulation
    public List<Node> startNodes;
    
    // Simulation specification.
    private InheritanceTrajectorySpec spec;    
    
    // Lenght of time taken by calculation
    private double calculationTime;
    
    // Simulation state variables
    private Map<Population,List<Node>> activeLineages;
    private Map<Node, Node> nodesInvolved, nextLevelNodes;
    private List<Node> inactiveLineages;
    private PopulationState currentPopState;
    private double t;
    private int sidx;
    
    /**
     * Build an inheritance graph corrsponding to a set of lineages embedded
     * within populations evolving under a birth-death process.
     *
     * @param spec Inheritance trajectory simulation specification.
     */
    public InheritanceTrajectory(InheritanceTrajectorySpec spec) {

        // Keep a record of the simulation spec and the starting nodes.
        this.spec = spec;

        // Initialise graph with copy of specification init nodes:
        // (Can't use initNodes themselves when multiple graphs are being
        // generated from the same spec by InheritanceEnsemble.)
        startNodes = Lists.newArrayList();
        for (Node startNode : spec.initNodes)
            startNodes.add(startNode.getCopy());
        
        // Create HashMaps:
        nodesInvolved = Maps.newHashMap();
        nextLevelNodes = Maps.newHashMap();

        // Set seed if defined:
        if (spec.getSeed()>=0 && !spec.isSeedUsed()) {
            Randomizer.setSeed(spec.getSeed());
            spec.setSeedUsed();
        }
        
        // Record time at start of calculation:
        double startTime = (new Date()).getTime();
        
        // Don't want to calculate this more than once:
        double sampleDt = 0.0;
        if (spec.isSamplingEvenlySpaced())
            sampleDt = spec.getSampleDt();

        initialiseSimulation();
        
        // Simulation loop:
        while (true) {

            // Check whether a lineage end condition is met:
            // (Note that end points can only be reached when all lineages
            // are in play.  Presumably you don't want to cut the simulation
            // short if we still have lineages to add.)
            boolean conditionMet = false;
            boolean isRejection = false;
            if (inactiveLineages.isEmpty()) {
                for (LineageEndCondition lineageEC : spec.getLineageEndConditions()) {
                    if (lineageEC.isMet(activeLineages)) {
                        conditionMet = true;
                        isRejection = lineageEC.isRejection();
                        break;
                    }
                }
            }
            
            // Check whether a population end condition is met:
            if (!conditionMet) {
                for (PopulationEndCondition popEC : spec.getPopulationEndConditions()) {
                    if (popEC.isMet(currentPopState)) {
                        conditionMet = true;
                        isRejection = popEC.isRejection();
                        break;
                    }
                }
            }
            
            // Act on arrival at any end condition:
            if (conditionMet) {
                if (isRejection) {
                    // Rejection: Abort and start a new simulation
                    if (spec.getVerbosity()>0)
                        System.err.println("Rejection end condition met "
                                + "at time " + t);   
                    initialiseSimulation();
                    continue;
                } else {
                    // Stopping point: Truncate existing simulation
                    if (spec.getVerbosity()>0)
                        System.err.println("Truncation end condition met "
                                + "at time " + t);   
                    break;
                }                
            }
            
            // Report trajectory progress:
            if (spec.getVerbosity()>1)
                System.err.println("Simulation arrived at time "
                        + String.valueOf(t));

            // Calculate propensities
            double totalPropensity = 0.0;
            for (InheritanceReactionGroup reactionGroup :
                    spec.getModel().getInheritanceReactionGroups()) {
                reactionGroup.calcPropensities(currentPopState);
                for (double propensity : reactionGroup.propensities)
                    totalPropensity += propensity;
            }

            // Draw time of next reactionGroup
            if (totalPropensity > 0)
                t += Randomizer.nextExponential(totalPropensity);
            else
                t = Double.POSITIVE_INFINITY;
            
            // Check whether new time exceeds node seed time or simulation time
            boolean seedTimeExceeded = false;
            boolean simulationTimeExceeded = false;
            if (!inactiveLineages.isEmpty() &&
                    inactiveLineages.get(0).getTime()<spec.getSimulationTime()) {
                if (t>inactiveLineages.get(0).getTime()) {
                    t = inactiveLineages.get(0).getTime();
                    seedTimeExceeded = true;
                }
            } else {
                if (t>spec.getSimulationTime()) {
                    t = spec.getSimulationTime();
                    simulationTimeExceeded = true;
                }
            }

            // Sample population sizes (evenly) if necessary:
            if (spec.samplePopSizes && spec.isSamplingEvenlySpaced()) {
                while (sidx*spec.getSampleDt() < t) {
                    sampleState(currentPopState, sampleDt*sidx);
                    sidx += 1;
                }
            }
            
            // Continue to on to next reaction if lineage has been seeded
            if (seedTimeExceeded) {
                Node seedNode = inactiveLineages.get(0);
                inactiveLineages.remove(0);
                Node child = new Node(seedNode.population);
                seedNode.addChild(child);
                
                if (!activeLineages.containsKey(child.getPopulation()))
                    activeLineages.put(child.getPopulation(), new ArrayList<Node>());
                activeLineages.get(child.getPopulation()).add(child);
                
                currentPopState.add(seedNode.population, 1.0);                
                continue;
            }
            
            // Break if new time exceeds end time:
            if (simulationTimeExceeded) {
                if (spec.samplePopSizes)
                    sampleState(currentPopState, t);
                break;
            }

            // Choose reaction to implement
            double u = Randomizer.nextDouble()*totalPropensity;
            boolean found = false;
            InheritanceReactionGroup chosenReactionGroup = null;
            int chosenReaction = 0;
            for (InheritanceReactionGroup reactionGroup :
                    spec.getModel().getInheritanceReactionGroups()) {

                for (int ridx = 0; ridx<reactionGroup.propensities.size(); ridx++) {
                    u -= reactionGroup.propensities.get(ridx);
                    if (u<0) {
                        found = true;
                        chosenReactionGroup = reactionGroup;
                        chosenReaction = ridx;
                        break;
                    }
                }

                if (found)
                    break;
            }

            // Select lineages involved in chosen reaction:
            selectLineagesInvolved(chosenReactionGroup, chosenReaction);
            
            // Implement changes to inheritance graph:
            implementInheritanceReaction(chosenReactionGroup);

            // Implement state change due to reaction:
            currentPopState.implementReaction(chosenReactionGroup, chosenReaction, 1);
                         
            // Sample population sizes (unevenly) if necessary:
            if (spec.samplePopSizes && !spec.isSamplingEvenlySpaced()) {              
                if (!spec.sampleStateAtNodes || !nodesInvolved.isEmpty())
                    sampleState(currentPopState, t);
            }

        }

        // Fix final time of any remaining active lineages.
        for (Population nodePop : activeLineages.keySet())
            for (Node node : activeLineages.get(nodePop))
                node.setTime(t);
        
        // Record total time of calculation:
        calculationTime = Double.valueOf((new Date()).getTime() - startTime)/1e3;
    }
    
    /**
     * Initialise the state variables involved in the simulation.  Makes
     * sense to subroutine this as it has to be repeated whenever a
     * simulation is rejected due to some end condition.
     */
    private void initialiseSimulation() {
        // Initialise time
        t = 0.0;

        // Initialise population size state:
        currentPopState = new PopulationState(spec.getInitPopulationState());
        
        // Initialise active lineages with nodes present at start of simulation
        if (activeLineages == null)
            activeLineages = Maps.newHashMap();
        else
            activeLineages.clear();
        
        if (inactiveLineages == null)
            inactiveLineages = Lists.newArrayList();
        else
            inactiveLineages.clear();
        
        for (Node node : startNodes) {
            node.getChildren().clear();

            if (node.getTime()>0.0) {
                inactiveLineages.add(node);
            } else {
                Population nodePop = node.getPopulation();
                
                node.setTime(0.0);
                Node child = new Node(nodePop);
                node.addChild(child);

                if (!activeLineages.containsKey(nodePop))
                    activeLineages.put(nodePop, new ArrayList<Node>());
                activeLineages.get(nodePop).add(child);

                currentPopState.add(nodePop, 1.0);
            }
        }

        // Initialise sampling index and sample first state
        sidx = 1;
        if (spec.samplePopSizes)
            sampleState(currentPopState, 0.0);
        
        // Sort inactive lineages nodes in order of increasing seed time:
        Collections.sort(inactiveLineages, new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                double dt = node1.getTime()-node2.getTime();
                
                if (dt<0)
                    return -1;
                
                if (dt>0)
                    return 1;
                
                return 0;
            }
        });
    }

    
    /**
     * Select lineages involved in reaction.  This is done by sampling
     * without replacement from the individuals present in the current state.
     * 
     * @param activeLineages population-partitioned list of active lineages
     * @param currentPopState current state of population sizes
     * @param chosenReactionGroup reaction group selected
     * @param chosenReaction integer index into reaction group specifying reactionGroup
     * @return Map from nodes involved to the corresponding reactant nodes.
     */
    private void selectLineagesInvolved(InheritanceReactionGroup chosenReactionGroup, int chosenReaction) {

        nodesInvolved.clear();
        for (Population reactPop : chosenReactionGroup.reactNodes.get(chosenReaction).keySet()) {
            
            if (!activeLineages.containsKey(reactPop))
                continue;
            
            // Total size of this population (including active lineages)
            double N = currentPopState.get(reactPop);
            
            for (Node reactNode : chosenReactionGroup.reactNodes.get(chosenReaction).get(reactPop)) {
                            
                double l = Randomizer.nextDouble()*N;
                if (l<activeLineages.get(reactPop).size()) {
                    int nodeIdx = (int)l;
                    Node lineageNode = activeLineages.get(reactPop).get(nodeIdx);
                    nodesInvolved.put(lineageNode, reactNode);
                    activeLineages.get(reactPop).remove(nodeIdx);
                    if (activeLineages.get(reactPop).isEmpty())
                        activeLineages.remove(reactPop);
                }

                // Stop here if we have no active lineages of this population
                if (!activeLineages.containsKey(reactPop))
                    break;

                N -= 1;
            }
        }
    }

    /**
     * Update the graph and the activeLineages list according to the chosen
     * reactionGroup.
     * 
     * @param nodesInvolved map from active lineages involved to reactant nodes
     */
    private void implementInheritanceReaction(InheritanceReactionGroup reactionGroup) {
       
        // Attach reactionGroup graph to inheritance graph
        nextLevelNodes.clear();
        for (Node node : nodesInvolved.keySet()) {
            Node reactNode = nodesInvolved.get(node);

            for (Node reactChild : reactNode.children) {
                if (!nextLevelNodes.containsKey(reactChild))
                    nextLevelNodes.put(reactChild, new Node(reactChild.population));
                
                node.addChild(nextLevelNodes.get(reactChild));
            }
        }
        
        // Graph cleaning and activeLineages maintenance:
        for (Node node : nodesInvolved.keySet()) {
            
            if (node.children.size()==1
                    &&(node.population.equals(node.children.get(0).population)
                    || node.children.get(0).parents.size()>1)) {
                // Node is not needed to represent a state change: delete it
                // from the graph

                // Active lineages are nodes having exactly one parent:
                Node parent = node.parents.get(0);
                Node child = node.children.get(0);

                // Prune from graph
                int nodeIdx = parent.children.indexOf(node);
                parent.children.set(nodeIdx, node.children.get(0));

                nodeIdx = child.parents.indexOf(node);
                child.parents.set(nodeIdx, parent);
            } else {
                // Node is here to stay
                                
                // Ensure node has current time.
                node.setTime(t);
                
                // Annotate node with reaction group
                node.setReactionGroup(reactionGroup);
            }

            // Ensure any children are in active nodes list
            for (Node child : node.children) {
                Population pop = child.getPopulation();
                if (!child.flagIsSet()) {
                    if (!activeLineages.containsKey(pop))
                        activeLineages.put(pop, new ArrayList<Node>());
                    activeLineages.get(pop).add(child);
                    child.setFlag(true);
                }
            }
        }

        // Deal with multi-parent nodes:
        for (Node node : nextLevelNodes.values()) {
            if (node.parents.size()>1) {
                node.setTime(t);
                node.setReactionGroup(reactionGroup);
                
                activeLineages.get(node.getPopulation()).remove(node);
                if (activeLineages.get(node.getPopulation()).isEmpty())
                    activeLineages.remove(node.getPopulation());

                Node child = new Node(node.population);
                node.addChild(child);
                
                if (!activeLineages.containsKey(child.getPopulation()))
                    activeLineages.put(child.getPopulation(), new ArrayList<Node>());
                activeLineages.get(child.getPopulation()).add(child);
            }
        
        }

    }
        
    /**
     * Retrieve inheritance trajectory simulation specification.
     *
     * @return InheritanceTrajectorySpec object.
     */
    @Override
    public InheritanceTrajectorySpec getSpec() {
        return spec;
    }
    
    /**
     * Retrieve total calculation time in seconds.
     * 
     * @return wall time of calculation
     */
    @Override
    public double getCalculationTime() {
        return calculationTime;
    }
    
    /**
     * Retrieve start nodes of inheritance graph.
     * 
     * @return list of start nodes
     */
    public List<Node> getStartNodes() {
        return startNodes;
    }
    
    /**
     * Retrieve end nodes of inheritance graph.
     * 
     * @return list of end nodes
     */
    public List<Node> getEndNodes() {
        List<Node> endNodes = new ArrayList<Node>();
        for (Node startNode : startNodes)
            getEndNodesRecurse(startNode, endNodes);
        
        return endNodes;
    }
    
    private void getEndNodesRecurse(Node node, List<Node> endNodes) {
        for (Node child : node.getChildren()) {
            if (child.getChildren().isEmpty()) {
                if (!endNodes.contains(child))
                    endNodes.add(child);
            } else
                getEndNodesRecurse(child, endNodes);
        }
    }

    /**
     * Construct graph component of inheritance trajectory from existing
     * network of nodes.
     *
     * @param startNodes
     */
    public InheritanceTrajectory(Node... startNodes) {
        this.spec = null;
        this.startNodes = Lists.newArrayList(startNodes);
    }
}
