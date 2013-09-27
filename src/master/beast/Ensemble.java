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

import beast.core.Citation;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.Runnable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Simulates a number of trajectories under a stochastic"
        + " birth-death model.")
@Citation("Tim Vaughan and Alexei Drummond, 'A Stochastic Simulator of "
        + "Birth–Death Master Equations with Application to Phylodynamics'. "
        + "Mol Biol Evol (2013) 30 (6): 1480-1493.")
public class Ensemble extends Runnable {
    
    /*
     * XML inputs:
     */
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<Double>(
            "simulationTime",
            "The length of time to simulate. (Defaults to infinite.)");
    
    public Input<Boolean> useEvenSamplingInput = new Input<Boolean>(
            "useEvenSampling",
            "Whether to use evenly spaced samples. (Defaults to false.)",
            false);
    
    public Input<Integer> nSamplesInput = new Input<Integer>(
            "nSamples",
            "Number of evenly spaced time points to sample state at.");
    
    public Input<Integer> nTrajInput = new Input<Integer>(
            "nTraj",
            "Number of trajectories to generate.",
            Input.Validate.REQUIRED);
    
    public Input<Integer> seedInput = new Input<Integer>(
            "seed",
            "Seed for RNG.");
    
    public Input<Stepper> stepperInput = new Input<Stepper>(
            "stepper",
            "State incrementing algorithm to use. (Default Gillespie.)");
    
    public Input<Integer> verbosityInput = new Input<Integer> (
            "verbosity", "Level of verbosity to use (0-3).", 1);
    
    // Model:
    public Input<Model> modelInput = new Input<Model>("model",
            "The specific model to simulate.",
            Validate.REQUIRED);
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<InitState>("initialState",
            "Initial state of system.",
            Validate.REQUIRED);
    
    // End conditions:
    public Input<List<PopulationEndCondition>> endConditionsInput = new Input<List<PopulationEndCondition>>(
            "populationEndCondition",
            "Trajectory end condition based on population sizes.",
            new ArrayList<PopulationEndCondition>());
    
    // Moments groups:
    public Input<List<MomentGroup>> momentGroupsInput = new Input<List<MomentGroup>>(
            "momentGroup",
            "Moment group to estimate from birth-death process.",
            new ArrayList<MomentGroup>());
    
    // Individual moments:
    public Input<List<Moment>> momentsInput = new Input<List<Moment>>(
            "moment",
            "Individual moment to estimate from birth-death process.",
            new ArrayList<Moment>());
    
    
    public Input<List<EnsembleOutput>> outputsInput = new Input<List<EnsembleOutput>>(
            "output",
            "Output writer used to write simulation output to disk.",
            new ArrayList<EnsembleOutput>());
    
    /*
     * Fields to populate using input values:
     */
    
    // Simulation specification:
    master.EnsembleSpec spec;
    
    public Ensemble() { }
    
    @Override
    public void initAndValidate() {
        spec = new master.EnsembleSpec();

        // Incorporate model:
        spec.setModel(modelInput.get().model);
        
        // Default to Gillespie stepper
        if (stepperInput.get() != null)
            spec.setStepper(stepperInput.get().getStepperObject());
        else
            spec.setStepper(new master.GillespieStepper());
        
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
        master.PopulationState initState = new master.PopulationState();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get())
            initState.set(popSize.pop, popSize.size);
        spec.setInitPopulationState(initState);
                        
        // Incorporate any end conditions:
        for (PopulationEndCondition endCondition : endConditionsInput.get())
            spec.addPopSizeEndCondition(endCondition.endConditionObject);

        // Set seed if provided, otherwise use default BEAST seed:
        if (seedInput.get()!=null)
            spec.setSeed(seedInput.get());
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());
    }
    
    @Override
    public void run() throws Exception {

        // Generate stochastic trajectory:
        master.Ensemble ensemble =
                new master.Ensemble(spec);

        // Write outputs:
        for (EnsembleOutput output : outputsInput.get())
            output.write(ensemble);        
    }
        
}
