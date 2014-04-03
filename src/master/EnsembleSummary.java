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
import master.model.MomentGroup;
import master.model.PopulationSize;
import master.model.Model;
import master.model.StateSummary;
import master.model.Moment;
import master.model.PopulationState;
import master.steppers.GillespieStepper;
import master.steppers.Stepper;
import beast.core.Input;
import beast.core.Runnable;
import beast.util.Randomizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import master.conditions.PostSimCondition;
import master.outputs.EnsembleSummaryOutput;

/**
 * A class representing a collection of results obtained by estimating moments
 * from an ensemble of trajectories of a birth-death process. Use this class as
 * an alternative to Ensemble to avoid having to keep a record of every
 * trajectory generated during the calculation.
 *
 * @author Tim Vaughan
 *
 */
public class EnsembleSummary extends Runnable {
    
    /*
     * XML inputs:
     */
    
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<Double>(
            "simulationTime",
            "The length of time to simulate.",
            Input.Validate.REQUIRED);
    public Input<Integer> nSamplesInput = new Input<Integer>(
            "nSamples",
            "Number of evenly spaced time points to sample state at.", Input.Validate.REQUIRED);
    public Input<Integer> nTrajInput = new Input<Integer>(
            "nTraj",
            "Number of trajectories to generate.",
            Input.Validate.REQUIRED);
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
    
        
    // Post-simulation conditioning:
    public Input<List<PostSimCondition>> postSimConditionsInput =
            new Input<List<PostSimCondition>>("postSimCondition",
                    "A post-simulation condition.",
                    new ArrayList<PostSimCondition>());
    
    // Individual moments:
    public Input<List<Moment>> momentsInput = new Input<List<Moment>>(
            "moment",
            "Individual moment to estimate from birth-death process.",
            new ArrayList<Moment>());
    
    // Moments groups:
    public Input<List<MomentGroup>> momentGroupsInput = new Input<List<MomentGroup>>(
            "momentGroup",
            "Moment group to estimate from birth-death process.",
            new ArrayList<MomentGroup>());

    // Outputs to write:
    public Input<List<EnsembleSummaryOutput>> outputsInput = new Input<List<EnsembleSummaryOutput>>(
            "output",
            "Output writer used to write simulation output to disk.",
            new ArrayList<EnsembleSummaryOutput>());
    

    // Simulation specification:
    EnsembleSummarySpec spec;
    
    // Ensemble-averaged state summaries:
    StateSummary[] stateSummaries;
    
    public EnsembleSummary() { }

    @Override
    public void initAndValidate() {

        spec = new master.EnsembleSummarySpec();

        // Incorporate model:
        spec.setModel(modelInput.get());
        
        // Default to Gillespie stepper
        if (stepperInput.get() != null)
            spec.setStepper(stepperInput.get());
        else
            spec.setStepper(new GillespieStepper());

        // Ensemble summaries must have finite end time and even sampling:
        spec.setSimulationTime(simulationTimeInput.get());
        spec.setEvenSampling(nSamplesInput.get());
        
        // Specify number of trajectories to generate:
        spec.setnTraj(nTrajInput.get());
        
        // Assemble initial state:
        master.model.PopulationState initState = new master.model.PopulationState();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get())
            initState.set(popSize.getPopulation(), popSize.getSize());
        spec.setInitPopulationState(initState);
        
        // Incorporate any end conditions:
        for (PopulationEndCondition endCondition : endConditionsInput.get())
            spec.addPopSizeEndCondition(endCondition);

        // Incorporate post-simulation conditions:
        for (PostSimCondition condition : postSimConditionsInput.get())
            spec.addPostSimCondition(condition);
        
        // Check for zero-length moment and moment group lists (no point to calculation!)
        if (momentGroupsInput.get().isEmpty() && momentsInput.get().isEmpty())
            throw new IllegalArgumentException("EnsembleSummary doesn't specfy any moments!");
        
        // Add moments and moment groups:
        for (MomentGroup momentGroup : momentGroupsInput.get())
            spec.addMomentGroup(momentGroup);
        
        for (Moment moment : momentsInput.get()) {
            if (moment.getName() == null)
                throw new IllegalArgumentException("Moment doesn't specify name"
                        + " and is not part of a moment group.");
            
            spec.addMoment(moment);
        }

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
        for (EnsembleSummaryOutput output : outputsInput.get())
            output.write(this);
        
    }

    /**
     * Perform simulation.
     */
    private void simulate () {

        // Set seed if defined:
        if (spec.seed>=0 && !spec.seedUsed) {
            Randomizer.setSeed(spec.seed);
            spec.seedUsed = true;
        }
        
        // Record time at start of calculation:
        double startTime = (new Date()).getTime();

        // Derived spec parameters:
        double sampleDt = spec.getSampleDt();

        // Initialise state summaries:
        stateSummaries = new StateSummary[spec.nSamples];
        for (int sidx = 0; sidx<spec.nSamples; sidx++)
            stateSummaries[sidx] = new StateSummary(spec.moments, spec.momentGroups);

        // Loop over trajectories:
        for (int traj = 0; traj<spec.nTraj; traj++) {

            // Report ensemble progress if verbosity high enough:
            if (spec.verbosity>0)
                System.err.println("Integrating trajectory "
                        +String.valueOf(traj+1)+" of "
                        +String.valueOf(spec.nTraj));

            // Initialise system state:
            PopulationState currentState = new PopulationState(spec.initPopulationState);
            
            // Record initial sample
            stateSummaries[0].record(currentState);

            // Integration loop:
            double t = 0.0;
            for (int sidx = 1; sidx<spec.nSamples; sidx++) {
                
                double nextSampTime = sidx*sampleDt;
                
                // Check for end conditions:
                boolean endConditionMet = false;
                for (PopulationEndCondition endCondition : spec.populationEndConditions) {
                    if (endCondition.isMet(currentState)) {
                        
                        // Can immediately reject, as only rejection condtions
                        // allowed for ensemble summaries.
                        
                        // Report if necessary:
                        if (spec.verbosity>0)
                            System.err.println("Rejection end condition met"
                                    + " at time " + t);
                        
                        currentState = new PopulationState(spec.initPopulationState);
                        t = 0;
                        sidx = 0;
                        endConditionMet = true;
                        
                        break;
                    }
                }
                if (endConditionMet)
                    continue;

                // Report trajectory progress at all times:
                if (spec.verbosity>1)
                    System.err.println("Recording sample time point "
                            +String.valueOf(sidx+1)+" of "
                            +String.valueOf(spec.nSamples));

                // Integrate to next sample time:
                while (t<nextSampTime)
                    t += spec.stepper.step(currentState, spec.model,
                            t, nextSampTime-t);
                
                // Record sample:
                stateSummaries[sidx].record(currentState);
            }
            
            for (StateSummary summary : stateSummaries)
                summary.accept();
        }

        // Normalise state summaries:
        for (StateSummary summary : stateSummaries)
            summary.normalise();
        
        // Record total time of calculation:
        spec.setWallTime(((new Date()).getTime() - startTime)/1e3);
    }

    /**
     * Retrieve ensemble simulation specification.
     * 
     * @return EnsembleSummarySpec object.
     */
    public EnsembleSummarySpec getSpec() {
        return spec;
    }

    /**
     * Return array of state summaries.
     * 
     * @return StateSummary array.
     */
    public StateSummary[] getStateSummaries() {
        return stateSummaries;
    }
}