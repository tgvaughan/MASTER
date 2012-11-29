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

import beast.util.Randomizer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import hamlet.Population;
import hamlet.PopulationEndCondition;
import hamlet.PopulationState;
import hamlet.Trajectory;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    
    
    // Simulation state variables
    List<Node> activeLineages, inactiveLineages;
    PopulationState currentPopState;
    double t;
    int sidx;
    
    /**
     * Build an inheritance graph corrsponding to a set of lineages embedded
     * within populations evolving under a birth-death process.
     *
     * @param spec Inheritance trajectory simulation specification.
     */
    public InheritanceTrajectory(InheritanceTrajectorySpec spec) {

        // Keep a record of the simulation spec and the starting nodes.
        this.spec = spec;
        startNodes = spec.initNodes;
        
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
            
            // Continue to on to next reactionGroup if lineage has been seeded
            if (seedTimeExceeded) {
                Node seedNode = inactiveLineages.get(0);
                inactiveLineages.remove(0);
                Node child = new Node(seedNode.population);
                seedNode.addChild(child);
                activeLineages.add(child);
                currentPopState.add(seedNode.population, 1.0);
                
                continue;
            }
            
            // Break if new time exceeds end time:
            if (simulationTimeExceeded)
                break;

            // Choose reactionGroup to implement
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
                    }
                }

                if (found)
                    break;
            }

            // Select lineages involved in chosen reactionGroup:
            Map<Node, Node> nodesInvolved = selectLineagesInvolved(activeLineages,
                currentPopState, chosenReactionGroup, chosenReaction);
            
            // Implement changes to inheritance graph:
            implementInheritanceReaction(activeLineages, nodesInvolved,
                    chosenReactionGroup, t);

            // Implement state change due to reactionGroup:
            currentPopState.implementReaction(chosenReactionGroup, chosenReaction, 1);
                         
            // Sample population sizes (unevenly) if necessary:
            if (spec.samplePopSizes && !spec.isSamplingEvenlySpaced()) {              
                if (!spec.sampleStateAtNodes || !nodesInvolved.isEmpty())
                    sampleState(currentPopState, t);
            }

        }

        // Fix final time of any remaining active lineages.
        for (Node node : activeLineages)
            node.setTime(t);
        
        // Perform final sample
        if (spec.samplePopSizes)
            sampleState(currentPopState, t);
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
            activeLineages = Lists.newArrayList();
        else
            activeLineages.clear();
        
        if (inactiveLineages == null)
            inactiveLineages = Lists.newArrayList();
        else
            inactiveLineages.clear();
        
        for (Node node : spec.initNodes) {
            node.getChildren().clear();
            if (node.getTime()>0.0) {
                inactiveLineages.add(node);
            } else {
                node.setTime(0.0);
                Node child = new Node(node.population);
                node.addChild(child);
                activeLineages.add(child);
                currentPopState.add(node.population, 1.0);
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
     * Select lineages involved in reactionGroup.  This is done by sampling
     * without replacement from the individuals present in the current state.
     * 
     * @param activeLineages list of active lineages
     * @param currentPopState current state of population sizes
     * @param chosenReactionGroup reactionGroup group selected
     * @param chosenReaction integer index into reactionGroup group specifying reactionGroup
     * @return Map from nodes involved to the corresponding reactant nodes.
     */
    private Map<Node,Node> selectLineagesInvolved(
            List<Node> activeLineages, PopulationState currentState,
            InheritanceReactionGroup chosenReactionGroup, int chosenReaction) {

        Map<Population, Integer> popsSeen = Maps.newHashMap();
        Map<Population, Integer> popsChosen = Maps.newHashMap();
        Map<Node, Node> nodesInvolved = Maps.newHashMap();
        for (Node node : activeLineages) {
            if (!chosenReactionGroup.reactCounts
                    .get(chosenReaction).containsKey(node.population))
                continue;

            // Calculate probability that lineage is involved in reactionGroup:
            int m = chosenReactionGroup.reactCounts
                    .get(chosenReaction).get(node.population);
            double N = currentState.get(node.population);

            if (popsChosen.containsKey(node.population))
                m -= popsChosen.get(node.population);

            if (popsSeen.containsKey(node.population))
                N -= popsSeen.get(node.population);

            // Decide whether lineage is involved
            if (Randomizer.nextDouble()<m/N) {

                // Node is involved, select particular reactant node to use:
                int idx = Randomizer.nextInt(m);
                for (Node reactNode :
                        chosenReactionGroup.reactNodes.get(chosenReaction)) {
                    if (reactNode.population!=node.population
                            || nodesInvolved.containsValue(reactNode))
                        continue;

                    if (idx==0) {
                        nodesInvolved.put(node, reactNode);
                        break;
                    } else
                        idx -= 1;
                }

                // Update popsChosen and popsSeen
                if (popsChosen.containsKey(node.population))
                    popsChosen.put(node.population,
                            popsChosen.get(node.population)+1);
                else
                    popsChosen.put(node.population, 1);
            }

            // Keep track of populations seen for future lineage selections:
            if (popsSeen.containsKey(node.population))
                popsSeen.put(node.population, popsSeen.get(node.population)+1);
            else
                popsSeen.put(node.population, 1);
        }
        
        return nodesInvolved;
    }

    /**
     * Update the graph and the activeLineages list according to the chosen
     * reactionGroup.
     * 
     * @param activeLineages list of active lineages
     * @param nodesInvolved map from active lineages involved to reactant nodes
     * @param t current time in simulation
     */
    private void implementInheritanceReaction(List<Node> activeLineages,
            Map<Node,Node> nodesInvolved,
            InheritanceReactionGroup reactionGroup, double t) {
       
        // Attach reactionGroup graph to inheritance graph
        Map<Node, Node> nextLevelNodes = Maps.newHashMap();
        for (Node node : nodesInvolved.keySet()) {
            Node reactNode = nodesInvolved.get(node);

            for (Node reactChild : reactNode.children) {
                if (!nextLevelNodes.containsKey(reactChild))
                    nextLevelNodes.put(reactChild, new Node(reactChild.population));
                
                node.addChild(nextLevelNodes.get(reactChild));
            }
        }
        
        // Update activeLineages:
        for (Node node : nodesInvolved.keySet()) {
            
            if (node.children.size()==1
                    &&(node.parents.get(0).population==node.children.get(0).population)) {
                // Node does not represent a state change: delete it

                // Active lineages are nodes having exactloy one parent:
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

            // Remove from active lineage list
            activeLineages.remove(node);

            // Ensure any children are in active nodes list
            for (Node child : node.children)
                if (!activeLineages.contains(child))
                    activeLineages.add(child);

        }

        // Deal with multi-parent nodes:
        for (Node node : nextLevelNodes.values()) {
            if (node.parents.size()>1) {
                node.setTime(t);
                activeLineages.remove(node);

                Node child = new Node(node.population);
                node.addChild(child);
                activeLineages.add(child);
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
