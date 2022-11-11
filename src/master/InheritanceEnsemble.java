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

import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.core.Input.Validate;
import beast.base.util.Randomizer;
import beast.base.inference.Runnable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import master.conditions.LeafCountEndCondition;
import master.conditions.LineageEndCondition;
import master.conditions.PopulationEndCondition;
import master.conditions.PostSimCondition;
import master.model.*;
import master.outputs.InheritanceEnsembleOutput;
import master.postprocessors.InheritancePostProcessor;
import master.postprocessors.LineageSampler;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Simulates a multiple trajectories under a stochastic birth-death"
        + " model, keeping track of lineages which descend from a chosen set"
        + " of individuals.")
public class InheritanceEnsemble extends Runnable {
    
    /*
    * XML inputs:
     */
    
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<>(
            "simulationTime",
            "The maximum length of time to simulate for. (Defaults to infinite.)");

    public Input<Boolean> samplePopulationSizesInput = new Input<>(
            "samplePopulationSizes",
            "Sample population sizes together with inheritance graph. (Default false.)",
            false);
    
    public Input<Integer> nSamplesInput = new Input<>(
            "nSamples",
            "Number of evenly spaced population size samples to record. (Default 0: uneven sampling)",
            0);
    
    public Input<Boolean> sampleAtNodesOnlyInput = new Input<>(
            "sampleAtNodesOnly",
            "Sample population sizes only at graph node times. (Default false.)",
            false);
    
    public Input<Integer> nTrajInput = new Input<>(
            "nTraj",
            "Number of trajectories to generate.",
            Input.Validate.REQUIRED);
            
    public Input<Integer> seedInput = new Input<>(
            "seed",
            "Seed for RNG.");
    
    public Input<Integer> verbosityInput = new Input<>(
            "verbosity", "Level of verbosity to use (0-3).", 1);
    
    // Model:
    public Input<Model> modelInput = new Input<>("model",
            "The specific model to simulate.", Validate.REQUIRED);
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<>("initialState",
            "Initial state of system.", Validate.REQUIRED);
    
    // Population end conditions:
    public Input<List<PopulationEndCondition>> popEndConditionsInput = new Input<>(
            "populationEndCondition",
            "Trajectory end condition based on population sizes.",
            new ArrayList<>());

    // Lineage end conditions:
    public Input<List<LineageEndCondition>> lineageEndConditionsInput = new Input<>(
            "lineageEndCondition",
            "Trajectory end condition based on remaining lineages.",
            new ArrayList<>());
    
    // Leaf count end conditions:
    public Input<List<LeafCountEndCondition>> leafCountEndConditionsInput = new Input<>(
            "leafCountEndCondition",
            "Trajectory end condition based on number of terminal nodes generated.",
            new ArrayList<>());
    
    // Post-processors:
    public Input<List<InheritancePostProcessor>> inheritancePostProcessorsInput =
            new Input<>("inheritancePostProcessor",
                    "Post processor for inheritance graph.",
                    new ArrayList<>());
    
    // Post-simulation conditioning:
    public Input<List<PostSimCondition>> postSimConditionsInput =
            new Input<>("postSimCondition",
                    "A post-simulation condition.",
                    new ArrayList<>());

    public Input<Integer> maxConditionRejectsInput =
            new Input<>("maxConditionRejects",
                    "Maximum number of post simulation condition failures" +
                            "before aborting.  (Default is no limit.)");

    // Outputs:
    public Input<List<InheritanceEnsembleOutput>> outputsInput
            = new Input<>("output",
            "Output writer used to write results of simulation to disk.",
            new ArrayList<>());
    
    // The ensemble is a large number of trajectories
    ArrayList<InheritanceTrajectory> itrajectories;
    
    // Record of simulation spec:
    InheritanceEnsembleSpec spec;
    
    public InheritanceEnsemble() { }
    
     @Override
    public void initAndValidate() {
        spec = new InheritanceEnsembleSpec();
               
        // Incorporate model:
        spec.setModel(modelInput.get());
        
        // Set population size options:
        if (samplePopulationSizesInput.get()) {
            if (nSamplesInput.get()>=2)
                spec.setEvenSampling(nSamplesInput.get());
            else
                spec.setUnevenSampling(sampleAtNodesOnlyInput.get());
        }
        
        // Set maximum simulation time:
        if (simulationTimeInput.get() != null)
            spec.setSimulationTime(simulationTimeInput.get());
        else {
            if (popEndConditionsInput.get() == null
                    && lineageEndConditionsInput.get() == null
                    && leafCountEndConditionsInput.get() == null) {
                throw new IllegalArgumentException("Must specify either a final simulation "
                        + "time or one or more end conditions.");
            } else
                spec.setSimulationTime(Double.POSITIVE_INFINITY);
        }
        
        // Specify number of trajectories to generate:
        spec.setnTraj(nTrajInput.get());        
        
        // Assemble initial state:
        PopulationState initState = new PopulationState();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get()) {
            popSize.computePopulationSizes(modelInput.get());
            for (Population pop : popSize.getPopSizes().keySet())
                initState.set(pop, popSize.getPopSizes().get(pop));
        }
        spec.setInitPopulationState(initState);        
        spec.setInitNodes(initialStateInput.get().getInitNodes());
        
        // Incorporate any end conditions:
        for (PopulationEndCondition endCondition : popEndConditionsInput.get())
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

        if (maxConditionRejectsInput.get() != null)
            spec.setMaxConditionRejects(maxConditionRejectsInput.get());

        // Set seed if provided, otherwise use default BEAST seed:
        if (seedInput.get()!=null)
            spec.setSeed(seedInput.get());
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());
        
    }

    @Override
    public void run() {
        
        // Generate stochastic trajectories:
        simulate();
        
        // Write outputs:
        for (InheritanceEnsembleOutput output : outputsInput.get())
            output.write(this);

        System.out.println("Done.");
    }
    
    private void simulate() {
        
        // Set seed if defined:
        if (spec.getSeed()>=0 && !spec.isSeedUsed()) {
            Randomizer.setSeed(spec.getSeed());
            spec.setSeedUsed();
        }
        
        // Initialise inheritance trajectory list
        itrajectories = new ArrayList<>();
        
                
        // Record time at start of simulation:
        double startTime = (new Date()).getTime();
        
        // Generate trajectories:
        for (int traj=0; traj<spec.nTraj; traj++) {
            
            // Report ensemble progress if verbosity is high enough:
            if (spec.getVerbosity()>0)
                System.err.println("Generating inheritance trajectory "
                        + String.valueOf(traj+1) + " of "
                        + String.valueOf(spec.nTraj));

            try {
                InheritanceTrajectory thisTraj = new InheritanceTrajectory(spec);
                itrajectories.add(thisTraj);
            } catch (RejectCountExceeded ex) {
                System.err.println("Maximum number of post-simulation " +
                        "condition rejections exceeded.  Skipping simulation.");
            }
        }
        
        // Record length of time taken by calculation:
        spec.setWallTime(((new Date()).getTime() - startTime)/1e3);
    }
    
    /**
     * Obtain inheritance ensemble simulation specification.
     * 
     * @return InheritanceEnsembleSpec object
     */
    public InheritanceEnsembleSpec getSpec() {
        return spec;
    }
    
    /**
     * Obtain inheritance trajectories contained in ensemble.
     * 
     * @return list of inheritance trajectories
     */
    public List<InheritanceTrajectory> getTrajectories() {
        return itrajectories;
    }

}
