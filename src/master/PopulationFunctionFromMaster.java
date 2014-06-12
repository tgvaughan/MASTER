/*
 * Copyright (C) 2014 Tim Vaughan <tgvaughan@gmail.com>
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
import beast.core.parameter.RealParameter;
import beast.evolution.tree.coalescent.PopulationFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import master.conditions.PopulationEndCondition;
import master.conditions.PostSimCondition;
import master.model.InitState;
import master.model.Model;
import master.outputs.TrajectoryOutput;
import master.steppers.GillespieStepper;
import master.steppers.Stepper;
import master.utilities.pfe.PFEVisitor;
import master.utilities.pfe.PFExpressionLexer;
import master.utilities.pfe.PFExpressionParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Uses a MASTER Trajectory simulation as the basis for a BEAST population
 * function.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationFunctionFromMaster extends PopulationFunction.Abstract {
    
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
    
    // Post-simulation conditioning:
    public Input<List<PostSimCondition>> postSimConditionsInput =
            new Input<List<PostSimCondition>>("postSimCondition",
                    "A post-simulation condition.",
                    new ArrayList<PostSimCondition>());
    
    // Outputs:
    public Input<List<TrajectoryOutput>> outputsInput = new Input<List<TrajectoryOutput>>(
            "output",
            "Output writer used to write simulation output to disk.",
            new ArrayList<TrajectoryOutput>());
    
    // Inputs specific to population function generation    

    public Input<String> popSizeExpressionInput = new Input<String>("popSizeExpression",
            "Either the name of a population or a simple mathematical expression"
            + "involving such names. e.g. I/(2*S) if S and I are population names.",
            Input.Validate.REQUIRED);
    
    public Input<RealParameter> originInput = new Input<RealParameter>("origin",
            "Maps population time onto time before most recent tree sample. "
                    + "Think of this as specifying the time of the most recent "
                    + "sample in the population size trajectory time scale.",
            Input.Validate.REQUIRED);
    
    public Input<Double> popSizeEndInput = new Input<Double>("popSizeEnd",
            "Population size to use beyond the end of the simulated trajectory.",
            0.0);
    
    public Input<Double> popSizeStartInput = new Input<Double>("popSizeStart",
            "Population size to use before the start of the simulated trajectory.",
            0.0);
    
    ParseTree expressionParseTree;
    PFEVisitor expressionVisitor;
    
    Trajectory traj;

    Double[] times, popSizes, intensities, intensitiesRev;
    
    double tIntensityTrajStart, dt;
    
    int peakIdx;
    
    public PopulationFunctionFromMaster() { }

    @Override
    public void initAndValidate() throws Exception {

        // Set up trajectory simulation inputs
        
        traj = new Trajectory();
        
        traj.setInputValue("simulationTime", simulationTimeInput.get());
        traj.setInputValue("nSamples", nSamplesInput.get());
        traj.setInputValue("seed", seedInput.get());
        traj.setInputValue("stepper", stepperInput.get());
        traj.setInputValue("verbosity", verbosityInput.get());
        traj.setInputValue("model", modelInput.get());
        traj.setInputValue("initialState", initialStateInput.get());
        
        for (PopulationEndCondition endCondition : endConditionsInput.get())
            traj.setInputValue("populationEndCondition", endCondition);        
                
        for (PostSimCondition postSimCondition : postSimConditionsInput.get())
            traj.setInputValue("postSimCondition", postSimCondition);
        
        for (TrajectoryOutput output : outputsInput.get())
            traj.setInputValue("output", output);

        traj.initAndValidate();
        
        // Build parse tree from of population size expression
        ANTLRInputStream input = new ANTLRInputStream(popSizeExpressionInput.get());
        PFExpressionLexer lexer = new PFExpressionLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PFExpressionParser parser = new PFExpressionParser(tokens);
        expressionParseTree = parser.start();
        
        // Visitor for interpreting population size expression
        expressionVisitor = new PFEVisitor();
        
        // Perform the simulation, calculate intensities etc
        prepare();

    }
    
    /**
     * Run the simulation itself and update the intensity arrays.
     */
    @Override
    public void prepare() {
                
        // Run simulation
        try {
            traj.run();
        } catch (Exception ex) { }
        
        // Collate times:
        times = new Double[traj.sampledTimes.size()];
        traj.sampledTimes.toArray(times);

        
        // Calculate population sizes
        expressionVisitor.setTraj(traj);
        popSizes = expressionVisitor.visit(expressionParseTree);
        
        // Find peak population size
        peakIdx=-1;
        double peakVal = 0.0;
        for (int i=0; i<times.length; i++) {
            if (popSizes[i]>peakVal) {
                peakIdx = i;
                peakVal = popSizes[i];
            }
        }

        // Numerically integrate intensities from peak population size
        intensities = new Double[times.length];
        intensities[peakIdx] = 0.0;
        for (int i=peakIdx; i>0; i--) {
            intensities[i-1] = intensities[i]
                    + (times[i]-times[i-1])/popSizes[i-1];
        }
        for (int i=peakIdx; i<times.length-1; i++) {
            intensities[i+1] = intensities[i]
                    + (times[i]-times[i+1])/popSizes[i];
        }

       // Copy to reversed intensities array needed for binary search
        intensitiesRev = new Double[times.length];
        for (int i=0; i<times.length/2; i++) {
            int j = times.length-1-i;
            intensitiesRev[i] = intensities[j];
            intensitiesRev[j] = intensities[i];
        }
        if (times.length%2>0)
            intensitiesRev[times.length/2] = intensities[times.length/2];
    }

    @Override
    public List<String> getParameterIds() {
        return new ArrayList<String>();
    }

    @Override
    public double getPopSize(double t) {
        double tforward = originInput.get().getValue() - t;
        
        if (tforward>times[times.length-1])
            return popSizeEndInput.get();
        
        if (tforward<0)
            return popSizeStartInput.get();
        
        // Choose which index into integration lattice to use:
        int tidx = Arrays.binarySearch(times, originInput.get().getValue()-t);
        if (tidx<0)
            tidx = -(tidx + 1) - 1;

        return popSizes[tidx];
    }

    @Override
    public double getIntensity(double t) {
        double tforward = originInput.get().getValue() - t;
        
        if (tforward>times[times.length-1]) {
            if (popSizeEndInput.get()>0.0) {
                return intensities[times.length-1] + (times[times.length-1]-tforward)/popSizeEndInput.get();
            } else
                return Double.NEGATIVE_INFINITY;
        }
        
        if (tforward<0.0) {
            if (popSizeStartInput.get()>0.0) {
                return intensities[0]
                        + (-tforward)/popSizeStartInput.get();
            } else
                return Double.POSITIVE_INFINITY;
        }
        
        int tidx = Arrays.binarySearch(times, tforward);
        if (tidx<0) {
            tidx = -(tidx + 1);  // index of first element greater than key
            
            // Integrate from different sides depending on location wrt peakIdx
            if (tidx<=peakIdx) {
                return (times[tidx]-tforward)/(popSizes[tidx-1]) + intensities[tidx];
            } else {
                return intensities[tidx-1] - (tforward-times[tidx-1])/popSizes[tidx-1];
            }
        } else
            return intensities[tidx]; // Exact match can happen at boundaries.
    }

    @Override
    public double getInverseIntensity(double intensity) {
        
        if (intensity<intensities[times.length-1])
            return convertTime(times[times.length-1]) + popSizeEndInput.get()*(intensity-intensities[times.length-1]);

        if (intensity>intensities[0])
            return convertTime(times[0]) + popSizeStartInput.get()*(intensity-intensities[0]);
        
        int idx = Arrays.binarySearch(intensitiesRev, intensity);
        if (idx<0) {
            idx = -(idx+1);
            int tidx = times.length - 1 - idx;  // index into forward-time array
            return convertTime(times[tidx]) + (intensity-intensities[tidx])*popSizes[tidx];
        } else
            return convertTime(times[times.length-1-idx]);
    }
    
    /**
     * Convert between simulation time and tree age time.
     * @param t
     * @return origin - t
     */
    private double convertTime(double t) {
        return originInput.get().getValue() - t;
    }
    
    
}
