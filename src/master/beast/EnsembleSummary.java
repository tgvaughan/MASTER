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

import master.InitState;
import master.PopulationSize;
import master.outputs.EnsembleSummaryOutput;
import beast.core.*;
import beast.core.Input.Validate;
import beast.core.Runnable;
import java.util.*;

/**
 * @author Tim Vaughan
 */
@Description("Simulates a number of trajectories under a stochastic"
        + " birth-death model, summarizing the results in terms of"
        + " moment estimates.")
@Citation("Tim Vaughan and Alexei Drummond, 'A Stochastic Simulator of "
        + "Birth–Death Master Equations with Application to Phylodynamics'. "
        + "Mol Biol Evol (2013) 30 (6): 1480-1493.")
public class EnsembleSummary extends Runnable {

    /*
     * XML inputs:
     */
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<Double>(
            "simulationTime",
            "The length of time to simulate.",
            Validate.REQUIRED);
    public Input<Integer> nSamplesInput = new Input<Integer>(
            "nSamples",
            "Number of evenly spaced time points to sample state at.", Validate.REQUIRED);
    public Input<Integer> nTrajInput = new Input<Integer>(
            "nTraj",
            "Number of trajectories to generate.",
            Validate.REQUIRED);
    public Input<Integer> seedInput = new Input<Integer>(
            "seed",
            "Seed for RNG.");
    public Input<Stepper> stepperInput = new Input<Stepper>(
            "stepper",
            "State stepping algorithm to use.");
    
    public Input<Integer> verbosityInput = new Input<Integer> (
            "verbosity", "Level of verbosity to use (0-3).", 1);
    
    // Model:
    public Input<Model> modelInput = new Input<Model>("model",
            "The specific model to simulate.");
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<InitState>("initialState",
            "Initial state of system.");
    
    // End conditions:
    public Input<List<PopulationEndCondition>> endConditionsInput =
            new Input<List<PopulationEndCondition>>("populationEndCondition",
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
    
    
    public Input<List<EnsembleSummaryOutput>> outputsInput = new Input<List<EnsembleSummaryOutput>>(
            "output",
            "Output writer used to write simulation output to disk.",
            new ArrayList<EnsembleSummaryOutput>());
    
    /*
     * Fields to populate using input values:
     */
    
    // Simulation specification:
    master.EnsembleSummarySpec spec;
    
    public EnsembleSummary() { }

    @Override
    public void initAndValidate() throws Exception {

        spec = new master.EnsembleSummarySpec();

        // Incorporate model:
        spec.setModel(modelInput.get().model);
        
        // Default to Gillespie stepper
        if (stepperInput.get() != null)
            spec.setStepper(stepperInput.get().getStepperObject());
        else
            spec.setStepper(new master.GillespieStepper());

        // Ensemble summaries must have finite end time and even sampling:
        spec.setSimulationTime(simulationTimeInput.get());
        spec.setEvenSampling(nSamplesInput.get());
        
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

        // Check for zero-lenght moment and moment group lists (no point to calculation!)
        if (momentGroupsInput.get().isEmpty() && momentsInput.get().isEmpty())
            throw new IllegalArgumentException("EnsembleSummary doesn't specfy any moments!");
        
        // Add moments and moment groups:
        for (MomentGroup momentGroup : momentGroupsInput.get())
            spec.addMomentGroup(momentGroup.momentGroup);
        
        for (Moment moment : momentsInput.get()) {
            if (moment.name == null)
                throw new RuntimeException("Moment doesn't specify name.");
            
            spec.addMoment(new master.Moment(moment.name,
                    moment.factorial, moment.factors));
        }

        // Set seed if provided, otherwise use default BEAST seed:
        if (seedInput.get()!=null)
            spec.setSeed(seedInput.get());
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());
    }

    @Override
    public void run() throws Exception {

        // Generate ensemble of stochastic trajectories and estimate
        // specified moments:
        master.EnsembleSummary ensemble =
                new master.EnsembleSummary(spec);

        // Write outputs:
        for (EnsembleSummaryOutput output : outputsInput.get())
            output.write(ensemble);
        
    }
}