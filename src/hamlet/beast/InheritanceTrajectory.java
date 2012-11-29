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
package hamlet.beast;

import beast.core.Description;
import beast.core.Input;
import beast.core.Runnable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Simulates a single trajectory under a stochastic birth-death"
        + " model, keeping track of lineages decendent form a chosen set"
        + " of individuals.")
public class InheritanceTrajectory extends Runnable {
    
    /*
     * XML inputs:
     */
    
    // Spec parameters:
    public Input<Double> simulationTimeInput = new Input<Double>(
            "simulationTime",
            "The maximum length of time to simulate for. (Defaults to infinite.)");
    public Input<Boolean> useEvenSamplingInput = new Input<Boolean>(
            "useEvenSampling",
            "Whether to use evenly spaced samples. (Defaults to false.)",
            false);
    public Input<Integer> nSamplesInput = new Input<Integer>(
            "nSamples",
            "Number of evenly spaced time points to sample state at.");
    public Input<Integer> seedInput = new Input<Integer>(
            "seed",
            "Seed for RNG.");
    
    public Input<Integer> verbosityInput = new Input<Integer> (
            "verbosity", "Level of verbosity to use (0-3).", 1);
    
    // Model:
    public Input<Model> modelInput = new Input<Model>("model",
            "The specific model to simulate.");
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<InitState>("initialState",
            "Initial state of system.");
    
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
    
    
    public Input<List<TrajectoryOutput>> outputsInput = new Input<List<TrajectoryOutput>>(
            "output",
            "Output writer used to write simulation output to disk.",
            new ArrayList<TrajectoryOutput>());
    
    public InheritanceTrajectory() { }
    
    @Override
    public void initAndValidate() {
        
    }
    
}
