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
import master.endconditions.PopulationEndCondition;
import master.model.PopulationSize;
import master.model.Model;
import master.model.PopulationState;
import master.steppers.GillespieStepper;
import master.steppers.Stepper;
import master.outputs.TrajectoryOutput;
import beast.core.Input;
import beast.core.Runnable;
import beast.util.Randomizer;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class of objects representing trajectories through the state space of the
 * birth-death model.
 *
 * @author Tim Vaughan
 *
 */
public class Trajectory extends Runnable {

    /*
     * XML inputs:
     */
    
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<Double>(
            "simulationTime",
            "The maximum length of time to simulate for. (Defaults to infinite.)");
    
    public Input<Integer> nSamplesInput = new Input<Integer>(
            "nSamples",
            "Number of evenly spaced time points to sample state at.");
    
    public Input<Integer> seedInput = new Input<Integer>(
            "seed",
            "Seed for RNG.");
    
    public Input<Stepper> stepperInput = new Input<Stepper>(
            "stepper",
            "State incrementing algorithm to use. (Default Gillespie.)",
            new GillespieStepper());
    
    public Input<Integer> verbosityInput = new Input<Integer> (
            "verbosity", "Level of verbosity to use (0-2).", 1);
    
    // Model:
    public Input<Model> modelInput = new Input<Model>("model",
            "The specific model to simulate.",
            Input.Validate.REQUIRED);
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<InitState>("initialState",
            "Initial state of system.",
            Input.Validate.REQUIRED);
    
    // End conditions:
    public Input<List<PopulationEndCondition>> endConditionsInput = new Input<List<PopulationEndCondition>>(
            "populationEndCondition",
            "Trajectory end condition based on population sizes.",
            new ArrayList<PopulationEndCondition>());    
    
    // Outputs:
    public Input<List<TrajectoryOutput>> outputsInput = new Input<List<TrajectoryOutput>>(
            "output",
            "Output writer used to write simulation output to disk.",
            new ArrayList<TrajectoryOutput>());
    
    // List of sampled states:
    List<PopulationState> sampledStates;
    List<Double> sampledTimes;
    
    // Simulation specification:
    private TrajectorySpec spec;

    public Trajectory() { }
    
    /**
     * Simulate a new trajectory with given specification.
     * 
     * @param spec trajectory specification
     */
    public Trajectory(TrajectorySpec spec) {
        this.spec = spec;
        
        simulate();
    }
    
    @Override
    public void initAndValidate() {
        spec = new master.TrajectorySpec();

        // Incorporate model:
        spec.setModel(modelInput.get());
        
        // Default to Gillespie stepper
        if (stepperInput.get() != null)
            spec.setStepper(stepperInput.get());
        else
            spec.setStepper(new master.steppers.GillespieStepper());
        
        // Default to unevenly spaced sampling times:
        if (nSamplesInput.get() != null)
            spec.setEvenSampling(nSamplesInput.get());
        else
            spec.setUnevenSampling();
        
        // Set maximum simulation time:
        if (simulationTimeInput.get() != null)
            spec.setSimulationTime(simulationTimeInput.get());
        else
            spec.setSimulationTime(Double.POSITIVE_INFINITY);
        
        // Assemble initial state:
        PopulationState initState = new PopulationState();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get())
            initState.set(popSize.getPopulation(), popSize.getSize());
        spec.setInitPopulationState(initState);
        
        // Incorporate any end conditions:
        for (PopulationEndCondition endCondition : endConditionsInput.get())
            spec.addPopSizeEndCondition(endCondition);

        // Set seed if provided, otherwise use default BEAST seed:
        if (seedInput.get()!=null)
            spec.setSeed(seedInput.get());
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());
    }

    @Override
    public void run() throws Exception {
        
        // Generate stochastic trajectory:
        simulate();

        // Write outputs:
        for (TrajectoryOutput output : outputsInput.get())
            output.write(this);
    }
    
    /**
     * Generate trajectory of birth-death process.
     */
    private void simulate() {

        // Set seed if defined:
        if (spec.seed>=0 && !spec.seedUsed) {
            Randomizer.setSeed(spec.seed);
            spec.seedUsed = true;
        }
        
        // Record time at start of simulation:
        double startTime = (new Date()).getTime();

        // Initialise sampled state and time lists:
        sampledStates = Lists.newArrayList();
        sampledTimes = Lists.newArrayList();

        // Initialise system state:
        PopulationState currentState = new PopulationState(spec.initPopulationState);

        if (spec.evenlySpacedSampling) {
            // Sample at evenly spaced times

            double sampleDt = spec.getSampleDt();
            
            // Sample initial state:
            sampleState(currentState, 0.0);

            // Integration loop:
            double t = 0;
            for (int sidx = 1; sidx<spec.nSamples; sidx++) {                

                double nextSampTime = sidx*sampleDt;
                
                // Report trajectory progress:
                if (spec.verbosity>1)
                    System.err.println("Recording sample time point "
                            +String.valueOf(sidx+1)+" of "
                            +String.valueOf(spec.nSamples));    

                // Integrate to next sample time:
                while (t<nextSampTime)
                    t += spec.stepper.step(currentState, spec.model, t,
                            nextSampTime-t);
                
                // Sample state:
                sampleState(currentState, nextSampTime);
                
                // Check for end conditions:
                PopulationEndCondition endConditionMet = null;
                for (PopulationEndCondition endCondition : spec.populationEndConditions) {
                    if (endCondition.isMet(currentState)) {
                        endConditionMet = endCondition;
                        break;
                    }
                }
                if (endConditionMet != null) {
                    if (endConditionMet.isRejection()) {
                        if (spec.verbosity>0)
                            System.err.println("Rejection end condition met "
                                    + "at time " + t);   
                        currentState = new PopulationState(spec.initPopulationState);
                        clearSamples();
                        t = 0;
                        sidx = -1;
                    } else {
                        if (spec.verbosity>0)
                            System.err.println("Truncation end condition met "
                                    + "at time " + t);
                        break;
                    }
                }
            }
        } else {
            // Sample following every integration step
            
            // Sample initial state:
            sampleState(currentState, 0.0);

            double t = 0;
            while (t<spec.simulationTime) {
                
                // Increment time
                t += spec.stepper.step(currentState, spec.model, t,
                        spec.simulationTime-t);
                
                // Report trajectory progress:
                if (spec.verbosity>1)
                    System.err.println("Recording sample at time "
                            + String.valueOf(t)); 
                
                // Sample state
                sampleState(currentState, t);
                
                // Check for end conditions:
                PopulationEndCondition endConditionMet = null;
                for (PopulationEndCondition endCondition : spec.populationEndConditions) {
                    if (endCondition.isMet(currentState)) {
                        endConditionMet = endCondition;
                        break;
                    }
                }
                if (endConditionMet != null) {
                    if (endConditionMet.isRejection()) {
                        if (spec.verbosity>0)
                            System.err.println("Rejection end condition met "
                                    + "at time " + t);                        
                        
                        currentState = new PopulationState(spec.initPopulationState);
                        clearSamples();                        
                        t = 0;
                    } else {
                        if (spec.verbosity>0)
                            System.err.println("Truncation end condition met "
                                    + "at time " + t);
                        break;
                    }
                }
            }
        }
        
        // Record length of time (in seconds) taken by calculation:
        spec.setWallTime(Double.valueOf((new Date()).getTime() - startTime)/1e3);
    }
    

    
    /**
     * Retrieve trajectory simulation specification.
     * 
     * @return TrajectorySpec object.
     */
    public TrajectorySpec getSpec() {
        return spec;
    }
    
    /**
     *  Sample current population size state and time.
     * 
     * @param currentState
     * @param time 
     */
    public final void sampleState(PopulationState currentState, double time) {
        sampledStates.add(new PopulationState(currentState));
        sampledTimes.add(time);
    }
    
    /**
     * Clear sampled population size states and times.
     */
    public final void clearSamples() {
        sampledStates.clear();
        sampledTimes.clear();
    }

    
    /**
     * @return Sampled states.
     */
    public List<PopulationState> getSampledStates() {
        return sampledStates;
    }

    /**
     * @return Sampled times.
     */
    public List<Double> getSampledTimes() {
        return sampledTimes;
    }
}
