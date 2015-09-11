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

import beast.core.Input;
import beast.util.Randomizer;
import com.google.common.collect.*;
import master.conditions.LeafCountEndCondition;
import master.conditions.LineageEndCondition;
import master.conditions.PopulationEndCondition;
import master.conditions.PostSimCondition;
import master.model.*;
import master.outputs.InheritanceTrajectoryOutput;
import master.postprocessors.InheritancePostProcessor;
import master.postprocessors.LineageSampler;

import java.util.*;

/**
 * A class representing a stochastic inheritance trajectory generated under
 * a particular stochastic population dynamics model. Inheritance trajectories
 * are trajectories which additionally contain a time graph representing
 * the lineages descending from members of a subset of the population.
 *
 * <p>Things to keep in mind when reading this code:
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
    
    // List of nodes present at the start of the simulation
    public List<Node> startNodes;
    
    // Simulation specification.
    private InheritanceTrajectorySpec spec;    

    // Simulation state variables
    private Map<Population,List<Node>> activeLineages;
    private Map<Node, Node> nodesInvolved, nextLevelNodes;
    private List<Node> inactiveLineages;
    private PopulationState currentPopState;
    protected double t;
    private int sidx;
    private Multiset<Population> leafCounts;
    
    public InheritanceTrajectory() { }
    
    /**
     * Simulate a new inheritance trajectory with given specification.
     * 
     * @param spec inheritance trajectory specification
     */
    public InheritanceTrajectory(InheritanceTrajectorySpec spec) {
        this.spec = spec;
        
        simulate();
    }
    
    @Override
    public void initAndValidate() {
        spec = new master.InheritanceTrajectorySpec();
        
        // Incorporate model:
        spec.setModel(modelInput.get());        
        
        // Set population size options:
        if (samplePopulationSizesInput.get()) {
            if (nSamplesInput.get() != null && nSamplesInput.get()>=2)
                spec.setEvenSampling(nSamplesInput.get());
            else
                spec.setUnevenSampling(sampleAtNodesOnlyInput.get());
        }
        
        // Set maximum simulation time:
        if (simulationTimeInput.get() != null)
            spec.setSimulationTime(simulationTimeInput.get());
        else {
            if (endConditionsInput.get() == null
                    && lineageEndConditionsInput.get() == null
                    && leafCountEndConditionsInput.get() == null) {
                throw new IllegalArgumentException("Must specify either a final simulation "
                        + "time or one or more end conditions.");
            } else
                spec.setSimulationTime(Double.POSITIVE_INFINITY);
        }
        
        // Assemble initial state:
        master.model.PopulationState initState = new master.model.PopulationState();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get()) {
            popSize.computePopulationSizes(modelInput.get());
            for (Population pop : popSize.getPopSizes().keySet())
                initState.set(pop, popSize.getPopSizes().get(pop));
        }
        spec.setInitPopulationState(initState);        
        spec.setInitNodes(initialStateInput.get().getInitNodes());
        
        // Incorporate any end conditions:
        for (PopulationEndCondition endCondition : endConditionsInput.get())
            spec.addPopSizeEndCondition(endCondition);

        for (LineageEndCondition endCondition : lineageEndConditionsInput.get())
            spec.addLineageEndCondition(endCondition);

        for (LeafCountEndCondition endCondition : leafCountEndConditionsInput.get())
            spec.addLeafCountEndCondition(endCondition);

        // Incorporate post-processors:
        for (InheritancePostProcessor postProc : inheritancePostProcessorsInput.get()) {
            if (postProc instanceof LineageSampler)
                ((LineageSampler) postProc).computePopulationSizes(modelInput.get());
            spec.addInheritancePostProcessor(postProc);
        }
        
        // Incorporate post-simulation conditions:
        for (PostSimCondition condition : postSimConditionsInput.get())
            spec.addPostSimCondition(condition);
        
        // Set seed if provided, otherwise use default BEAST seed:
        if (seedInput.get()!=null)
            spec.setSeed(seedInput.get());
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());
        
        // Set trajectory probability recording status:
        spec.setTrajLogPRecording(recordTrajLogPInput.get());
    }
    
    @Override
    public void run() {
        
        // Generate stochastic trajectory:
        simulate();

        // Write outputs:
        for (InheritanceTrajectoryOutput output : outputsInput.get())
            output.write(this);

        System.out.println("Done.");
    }
    
    /**
     * Build an inheritance graph corresponding to a set of lineages embedded
     * within populations evolving under a birth-death process.
     */
    private void simulate() {

        // Create HashMaps:
        nodesInvolved = Maps.newLinkedHashMap();
        nextLevelNodes = Maps.newLinkedHashMap();


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

        boolean postSimReject;
        do { // Perform simulations until no post-simulation rejection occurs
            
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
                
                // Check whether a leaf count end condition is met:
                if (!conditionMet) {
                    for (LeafCountEndCondition leafEC : spec.getLeafCountEndConditions()) {
                        if (leafEC.isMet(leafCounts, activeLineages)) {
                            conditionMet = true;
                            isRejection = leafEC.isRejection();
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
                for (Reaction reaction :
                        spec.getModel().getReactions()) {
                    reaction.calcPropensity(currentPopState, t);
                    totalPropensity += reaction.getPropensity();
                }
                
                // Draw time of next reaction
                double tprime;
                if (totalPropensity > 0.0)
                    tprime = t + Randomizer.nextExponential(totalPropensity);
                else
                    tprime = Double.POSITIVE_INFINITY;
                
                // Check whether new time exceeds node seed time, simulation
                // time or rate change time
                boolean seedTimeExceeded = false;
                boolean simulationTimeExceeded = false;
                boolean rateChangeTimeExceeded = false;
                if (!inactiveLineages.isEmpty() &&
                        inactiveLineages.get(0).getTime()<spec.getSimulationTime()) {
                    if (tprime>inactiveLineages.get(0).getTime()) {
                        tprime = inactiveLineages.get(0).getTime();
                        seedTimeExceeded = true;
                    }
                } else {
                    double nextChangeTime = spec.getModel().getNextReactionChangeTime(t);
                    if (tprime>Math.min(spec.getSimulationTime(), nextChangeTime)) {
                        if (spec.getSimulationTime()<nextChangeTime) {
                            tprime = spec.getSimulationTime();
                            simulationTimeExceeded = true;
                        } else {
                            tprime = nextChangeTime;
                            rateChangeTimeExceeded = true;
                        }
                    }
                }
                
                // Calculate trajectory probability contribution of waiting time
                if (spec.isTrajLogPRecordingOn())
                    trajLogP += -(tprime-t)*totalPropensity;
                
                // Update time
                t = tprime;

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
                    Node child = new Node(seedNode.getPopulation());
                    seedNode.addChild(child);
                    
                    if (!activeLineages.containsKey(child.getPopulation()))
                        activeLineages.put(child.getPopulation(), new ArrayList<>());
                    activeLineages.get(child.getPopulation()).add(child);
                    
                    currentPopState.add(seedNode.getPopulation(), 1.0);                
                    continue;
                }
                
                // Continue to next reaction if reached reaction rate change time
                if (rateChangeTimeExceeded) {
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
                Reaction chosenReaction = null;
                for (Reaction reaction : spec.getModel().getReactions()) {
                    
                    u -= reaction.getPropensity();
                    if (u<0) {
                        chosenReaction = reaction;
                        break;
                    }
                }
                
                // Calculate trajectory probability contribution of event
                if (spec.isTrajLogPRecordingOn() && chosenReaction != null)
                    trajLogP += Math.log(chosenReaction.getPropensity());
                
                // Select lineages involved in chosen reaction:
                selectLineagesInvolved(chosenReaction);
                
                // Implement changes to inheritance graph:
                implementInheritanceReaction(chosenReaction);
                
                // Implement state change due to reaction:
                currentPopState.implementReaction(chosenReaction, 1);
                
                // Update event counter:
                spec.getStepper().incrementEventCount();
                
                // Sample population sizes (unevenly) if necessary:
                if (spec.samplePopSizes && !spec.isSamplingEvenlySpaced()) {              
                    if (!spec.sampleStateAtNodes || !nodesInvolved.isEmpty())
                        sampleState(currentPopState, t);
                }
                
            }
            
            // Fix final time of any remaining active lineages:
            for (Population nodePop : activeLineages.keySet()) {
                for (Node node : activeLineages.get(nodePop)) {
                    node.setTime(t);
                    leafCounts.add(nodePop);
                }
            }
            
            // Perform any requested post-processing:
            for (InheritancePostProcessor inheritancePostProc : spec.inheritancePostProcessors)
                inheritancePostProc.process(this);
            
            // Check for any post-simulation rejections
            postSimReject = false;
            for (PostSimCondition condition  : spec.postSimConditions) {
                if (!condition.accept(this)) {
                    postSimReject = true;
                    break;
                }
            }
            
            if (postSimReject) {
                // Rejection: Start a new simulation
                if (spec.getVerbosity()>0)
                    System.err.println("Post-simulation rejection condition met.");
            }
            
        } while (postSimReject);
        
        // Record total time of calculation:
        spec.setWallTime(((new Date()).getTime() - startTime)/1e3);
    }
    
    /**
     * Initialise the state variables involved in the simulation.  Makes
     * sense to subroutine this as it has to be repeated whenever a
     * simulation is rejected due to some end condition.
     */
    private void initialiseSimulation() {
        // Initialise time
        t = 0.0;
        
        // Initialise trajectory probability
        trajLogP = 0.0;
        
        // Initialise graph with copy of specification init nodes:
        // (Can't use initNodes themselves when multiple graphs are being
        // generated from the same spec by InheritanceEnsemble.)
        if (startNodes == null)
            startNodes = Lists.newArrayList();
        else
            startNodes.clear();

        for (Node startNode : spec.initNodes)
            startNodes.add(startNode.getCopy());

        // Initialise population size state:
        currentPopState = new PopulationState(spec.getInitPopulationState());
        
                
        // Initialise sampled state and time lists:
        if (sampledStates == null)
            sampledStates = Lists.newArrayList();
        else
            sampledStates.clear();
        
        if (sampledTimes == null)
            sampledTimes = Lists.newArrayList();
        else
            sampledTimes.clear();
        
        // Initialise active lineages with nodes present at start of simulation
        if (activeLineages == null)
            activeLineages = Maps.newLinkedHashMap();
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
                    activeLineages.put(nodePop, new ArrayList<>());
                activeLineages.get(nodePop).add(child);

                currentPopState.add(nodePop, 1.0);
            }
        }

        // Initialise sampling index and sample first state
        if (spec.samplePopSizes) {
            sidx = 1;
            clearSamples();
            sampleState(currentPopState, 0.0);
        }
        
        // Sort inactive lineages nodes in order of increasing seed time:
        Collections.sort(inactiveLineages, (node1, node2) -> {
            double dt = node1.getTime()-node2.getTime();

            if (dt<0)
                return -1;

            if (dt>0)
                return 1;

            return 0;
        });
        
        // Reset terminal node count:
        if (leafCounts == null)
            leafCounts = LinkedHashMultiset.create();
        else
            leafCounts.clear();
    }

    
    /**
     * Select lineages involved in reaction.  This is done by sampling
     * without replacement from the individuals present in the current state.
     * 
     * @param chosenReaction reaction selected
     */
    private void selectLineagesInvolved(Reaction chosenReaction) {

        nodesInvolved.clear();
        for (Population reactPop : chosenReaction.reactNodes.keySet()) {
            
            // Skip this population if no lineages remain:
            if (!activeLineages.containsKey(reactPop))
                continue;
            
            // Total size of this population (including active lineages)
            double N = currentPopState.get(reactPop);
            
            List<Node> lineages = activeLineages.get(reactPop);            
            for (Node reactNode : chosenReaction.reactNodes.get(reactPop)) {
                
                double l = Randomizer.nextDouble()*N;
                if (l<lineages.size()) {
                    int nodeIdx = (int)l;
                    Node lineageNode = lineages.get(nodeIdx);
                    nodesInvolved.put(lineageNode, reactNode);
                    
                    //Speedy alternative to lineages.remove(nodeIdx)
                    if (lineages.size()>1)
                        lineages.set(nodeIdx, lineages.get(lineages.size()-1));
                    lineages.remove(lineages.size()-1);
                    
                    if (lineages.isEmpty()) {
                        activeLineages.remove(reactPop);
                        break;
                    }
                }

                N -= 1;
            }
        }
    }

    /**
     * Update the graph and the activeLineages list according to the chosen
     * reactionGroup.
     */
    private void implementInheritanceReaction(Reaction reaction) {
       
        // Attach reactionGroup graph to inheritance graph
        nextLevelNodes.clear();
        for (Node node : nodesInvolved.keySet()) {
            Node reactNode = nodesInvolved.get(node);

            for (Node reactChild : reactNode.getChildren()) {
                if (!nextLevelNodes.containsKey(reactChild))
                    nextLevelNodes.put(reactChild, new Node(reactChild.getPopulation()));
                
                node.addChild(nextLevelNodes.get(reactChild));
            }
            
            // Increment terminal node counter as necessary:
            if (reactNode.getChildren().isEmpty())
                leafCounts.add(reactNode.getPopulation());
        }

        // Graph cleaning and activeLineages maintenance:
        for (Node node : nodesInvolved.keySet()) {
            
            if (node.getChildren().size()==1
                    && (node.getPopulation().equals(node.getChildren().get(0).getPopulation())
                    || node.getChildren().get(0).getParents().size()>1)) {
                // Node is not needed to represent a state change: delete it
                // from the graph

                // Active lineages are nodes having exactly one parent:
                Node parent = node.getParents().get(0);
                Node child = node.getChildren().get(0);

                // Prune from graph
                int nodeIdx = parent.getChildren().indexOf(node);
                parent.getChildren().set(nodeIdx, node.getChildren().get(0));

                nodeIdx = child.getParents().indexOf(node);
                child.getParents().set(nodeIdx, parent);
            } else {
                // Node is here to stay
                                
                // Ensure node has current time.
                node.setTime(t);
                
                // Annotate node with reaction group
                node.setReaction(reaction);
            }

            // Ensure any children are in active nodes list
            for (Node child : node.getChildren()) {
                Population pop = child.getPopulation();
                if (!child.flagIsSet()) {
                    if (!activeLineages.containsKey(pop))
                        activeLineages.put(pop, new ArrayList<>());
                    activeLineages.get(pop).add(child);
                    child.setFlag(true);
                }
            }
        }

        // Deal with multi-parent nodes:
        for (Node node : nextLevelNodes.values()) {
            if (node.getParents().size()>1) {
                node.setTime(t);
                node.setReaction(reaction);
                
                activeLineages.get(node.getPopulation()).remove(node);
                if (activeLineages.get(node.getPopulation()).isEmpty())
                    activeLineages.remove(node.getPopulation());

                Node child = new Node(node.getPopulation());
                node.addChild(child);
                
                if (!activeLineages.containsKey(child.getPopulation()))
                    activeLineages.put(child.getPopulation(), new ArrayList<>());
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
        List<Node> endNodes = new ArrayList<>();
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
     * @param startNodes start nodes for network
     */
    public InheritanceTrajectory(Node... startNodes) {
        this.spec = null;
        this.startNodes = Lists.newArrayList(startNodes);
    }
}
