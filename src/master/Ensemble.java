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

import master.model.InitState;
import master.conditions.PopulationEndCondition;
import master.model.Population;
import master.model.PopulationSize;
import master.model.Model;
import master.steppers.Stepper;
import beast.core.Input;
import beast.util.Randomizer;
import beast.core.Runnable;
import java.util.*;
import master.conditions.PostSimCondition;
import master.outputs.EnsembleOutput;

/**
 * A class representing an ensemble of stochastic trajectories through the state
 * space of a population genetics model of viral dynamics.
 *
 * @author Tim Vaughan
 *
 */
public class Ensemble extends Runnable {
    
    /*
    * XML inputs:
    */
    
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<>(
            "simulationTime",
            "The length of time to simulate. (Defaults to infinite.)");
    
    public Input<Boolean> useEvenSamplingInput = new Input<>(
            "useEvenSampling",
            "Whether to use evenly spaced samples. (Defaults to false.)",
            false);
    
    public Input<Integer> nSamplesInput = new Input<>(
            "nSamples",
            "Number of evenly spaced time points to sample state at.");
    
    public Input<Integer> nTrajInput = new Input<>(
            "nTraj",
            "Number of trajectories to generate.",
            Input.Validate.REQUIRED);
    
    public Input<Integer> seedInput = new Input<>(
            "seed",
            "Seed for RNG.");
    
    public Input<Stepper> stepperInput = new Input<>(
            "stepper",
            "State incrementing algorithm to use. (Default Gillespie.)");
    
    public Input<Integer> verbosityInput = new Input<>(
            "verbosity", "Level of verbosity to use (0-3).", 1);
    
    // Model:
    public Input<Model> modelInput = new Input<>("model",
            "The specific model to simulate.",
            Input.Validate.REQUIRED);
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<>("initialState",
            "Initial state of system.",
            Input.Validate.REQUIRED);
    
    // End conditions:
    public Input<List<PopulationEndCondition>> endConditionsInput = new Input<>(
            "populationEndCondition",
            "Trajectory end condition based on population sizes.",
            new ArrayList<>());
    
    // Post-simulation conditioning:
    public Input<List<PostSimCondition>> postSimConditionsInput =
            new Input<>("postSimCondition",
                    "A post-simulation condition.",
                    new ArrayList<>());
    
    // Outputs to write:
    public Input<List<EnsembleOutput>> outputsInput = new Input<>(
            "output",
            "Output writer used to write simulation output to disk.",
            new ArrayList<>());

    // The ensemble is a large number of trajectories:
    ArrayList<Trajectory> trajectories;
    
    // Local record of simulation spec:
    EnsembleSpec spec;
    
    public Ensemble() { }
    
    @Override
    public void initAndValidate() {
        spec = new master.EnsembleSpec();

        // Incorporate model:
        spec.setModel(modelInput.get());
        
        // Default to Gillespie stepper
        if (stepperInput.get() != null)
            spec.setStepper(stepperInput.get());
        else
            spec.setStepper(new master.steppers.GillespieStepper());
        
        // Default to unevenly spaced sampling times:
        if (useEvenSamplingInput.get())
            spec.setEvenSampling(nSamplesInput.get());
        else
            spec.setUnevenSampling();
        
        // Set maximum simulation time:
        if (simulationTimeInput.get() != null)
            spec.setSimulationTime(simulationTimeInput.get());
        else
            spec.setSimulationTime(Double.POSITIVE_INFINITY);
        
        // Specify number of trajectories to generate:
        spec.setnTraj(nTrajInput.get());
        
        // Assemble initial state:
        master.model.PopulationState initState = new master.model.PopulationState();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get()) {
            popSize.computePopulationSizes(modelInput.get());
            for (Population pop : popSize.getPopSizes().keySet())
                initState.set(pop, popSize.getPopSizes().get(pop));
        }
        spec.setInitPopulationState(initState);
                        
        // Incorporate any end conditions:
        for (PopulationEndCondition endCondition : endConditionsInput.get())
            spec.addPopSizeEndCondition(endCondition);

        // Incorporate post-simulation conditions:
        for (PostSimCondition condition : postSimConditionsInput.get())
            spec.addPostSimCondition(condition);
        
        // Set seed if provided, otherwise use default BEAST seed:
        if (seedInput.get()!=null)
            spec.setSeed(seedInput.get());
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());
    }
    
    @Override
    public void run() throws Exception {

        // Perform simulations.
        simulate();
        
        // Write outputs:
        for (EnsembleOutput output : outputsInput.get())
            output.write(this);        
    }
    
    /**
     * Simulate ensemble trajectories.
     */
    private void simulate() {
        
        // Set RNG seed unless seed<0:
        if (spec.seed>=0 && !spec.seedUsed) {
            Randomizer.setSeed(spec.seed);
            spec.seedUsed = true;
        }

        // Initialise trajectory list:
        trajectories = new ArrayList<>();
        
        // Record time at start of simulation:
        double startTime = (new Date()).getTime();

        // Generate trajectories:
        for (int traj = 0; traj<spec.nTraj; traj++) {
            
            // Report ensemble progress if verbosity high enough:
            if (spec.verbosity>0)
                System.err.println("Integrating trajectory "
                        +String.valueOf(traj+1)+" of "
                        +String.valueOf(spec.nTraj));

            Trajectory thisTraj = new Trajectory(spec);
            trajectories.add(thisTraj);

        }
        
        // Record total time (in seconds) taken by calculation:
        spec.setWallTime(((new Date()).getTime() - startTime)/1e3);
    }

    /**
     * Retrieve ensemble simulation specification.
     * 
     * @return EnsembleSpec object.
     */
    public EnsembleSpec getSpec() {
        return spec;
    }

    /**
     * @return trajectories
     */
    public List<Trajectory> getTrajectories() {
        return trajectories;
    }
    
}