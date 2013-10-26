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
package master.beast;

import master.outputs.InheritanceTrajectoryOutput;
import beast.core.Citation;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.Runnable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Simulates a single trajectory under a stochastic birth-death"
        + " model, keeping track of lineages decendent form a chosen set"
        + " of individuals.")
@Citation("Tim Vaughan and Alexei Drummond, 'A Stochastic Simulator of "
        + "Birth–Death Master Equations with Application to Phylodynamics'. "
        + "Mol Biol Evol (2013) 30 (6): 1480-1493.")
public class InheritanceTrajectory extends Runnable {
    
    /*
     * XML inputs:
     */
    
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<Double>(
            "simulationTime",
            "The maximum length of time to simulate for. (Defaults to infinite.)");

    public Input<Boolean> samplePopulationSizesInput = new Input<Boolean>(
            "samplePopulationSizes",
            "Sample population sizes together with inheritance graph. (Default false.)",
            false);
    
    public Input<Integer> nSamplesInput = new Input<Integer>(
            "nSamples",
            "Number of evenly spaced population size samples to record. (Default 0: uneven sampling)",
            0);
    
    public Input<Boolean> sampleAtNodesOnlyInput = new Input<Boolean>(
            "sampleAtNodesOnly",
            "Sample population sizes only at graph node times. (Default false.)",
            false);
            
    public Input<Integer> seedInput = new Input<Integer>(
            "seed",
            "Seed for RNG.");
    
    public Input<Integer> verbosityInput = new Input<Integer> (
            "verbosity", "Level of verbosity to use (0-3).", 1);
    
    // Model:
    public Input<InheritanceModel> modelInput = new Input<InheritanceModel>("model",
            "The specific model to simulate.", Validate.REQUIRED);
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<InitState>("initialState",
            "Initial state of system.", Validate.REQUIRED);
    
    // Population end conditions:
    public Input<List<PopulationEndCondition>> popEndConditionsInput = new Input<List<PopulationEndCondition>>(
            "populationEndCondition",
            "Trajectory end condition based on population sizes.",
            new ArrayList<PopulationEndCondition>());
    
    // Lineage end conditions:
    public Input<List<LineageEndCondition>> lineageEndConditionsInput = new Input<List<LineageEndCondition>>(
            "lineageEndCondition",
            "Trajectory end condition based on remaining lineages.",
            new ArrayList<LineageEndCondition>());
    
    // Leaf count end conditions:
    public Input<List<LeafCountEndCondition>> leafCountEndConditionsInput = new Input<List<LeafCountEndCondition>>(
            "leafCountEndCondition",
            "Trajectory end condition based on number of terminal nodes generated.",
            new ArrayList<LeafCountEndCondition>());
    
    // Post-processors:
    public Input<List<InheritancePostProcessor>> inheritancePostProcessorsInput =
            new Input<List<InheritancePostProcessor>>(
            "inheritancePostProcessor",
            "Post processor for inheritance graph.",
            new ArrayList<InheritancePostProcessor>());
    
    // Outputs:
    public Input<List<InheritanceTrajectoryOutput>> outputsInput
            = new Input<List<InheritanceTrajectoryOutput>>("output",
            "Output writer used to write results of simulation to disk.",
            new ArrayList<InheritanceTrajectoryOutput>());
    
    
    master.inheritance.InheritanceTrajectorySpec spec;
    
    public InheritanceTrajectory() { }
    
    @Override
    public void initAndValidate() {
        spec = new master.inheritance.InheritanceTrajectorySpec();
               
        // Incorporate model:
        spec.setModel(modelInput.get().model);        
        
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
        
        // Assemble initial state:
        master.PopulationState initState = new master.PopulationState();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get())
            initState.set(popSize.pop, popSize.size);
        spec.setInitPopulationState(initState);        
        spec.setInitNodes(initialStateInput.get().initNodes);
        
        // Incorporate any end conditions:
        for (PopulationEndCondition endCondition : popEndConditionsInput.get())
            spec.addPopSizeEndCondition(endCondition.endConditionObject);
        
        for (LineageEndCondition endCondition : lineageEndConditionsInput.get())
            spec.addLineageEndCondition(endCondition.endConditionObject);
        
        for (LeafCountEndCondition endCondition : leafCountEndConditionsInput.get())
            spec.addLeafCountEndCondition(endCondition.endConditionObject);

        // Set seed if provided, otherwise use default BEAST seed:
        if (seedInput.get()!=null)
            spec.setSeed(seedInput.get());
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());
    }
    
    @Override
    public void run() {
        
        // Generate stochastic trajectory:
        master.inheritance.InheritanceTrajectory itraj =
                new master.inheritance.InheritanceTrajectory(spec);
        
        // Perform any requested post-processing:
        for (InheritancePostProcessor inheritancePostProc : inheritancePostProcessorsInput.get())
            inheritancePostProc.process(itraj);

        // Write outputs:
        for (InheritanceTrajectoryOutput output : outputsInput.get())
            output.write(itraj);
    }
}
