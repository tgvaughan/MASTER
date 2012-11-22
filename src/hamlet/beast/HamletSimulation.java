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

import beast.core.*;
import beast.core.Input.Validate;
import beast.core.Runnable;
import hamlet.JsonOutput;
import java.io.*;
import java.util.*;

/**
 * BEAST 2 plugin representing a general stochastic simulation.
 *
 * @author Tim Vaughan
 *
 */
@Description("A stochastic simulation of a birth-death population dynamics model.")
public class HamletSimulation extends Runnable {

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
            "Number of time points to sample state at.",
            Validate.REQUIRED);
    public Input<Integer> nTrajInput = new Input<Integer>(
            "nTraj",
            "Number of trajectories to generate.",
            Validate.REQUIRED);
    public Input<Integer> seedInput = new Input<Integer>(
            "seed",
            "Seed for RNG.");
    public Input<Integrator> integratorInput = new Input<Integrator>(
            "integrator",
            "Integration algorithm to use.",
            Validate.REQUIRED);
    
    public Input<Integer> verbosityInput = new Input<Integer> (
            "verbosity", "Level of verbosity to use (0-3).", 1);
    
    // Model:
    public Input<Model> modelInput = new Input<Model>("model",
            "The specific model to simulate.");
    
    // Initial state:
    public Input<InitState> initialStateInput = new Input<InitState>("initialState",
            "Initial state of system.");
    
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
    
    // Output file name:
    public Input<String> outFileNameInput = new Input<String>("outFileName",
            "Name of output file.");

    /*
     * Fields to populate with parameter values:
     */
    
    // Spec specification:
    hamlet.EnsembleSummarySpec spec;
    
    // Stream object to write JSON output to:
    PrintStream outStream;

    public HamletSimulation() {
    }

    @Override
    public void initAndValidate() throws Exception {

        // Assemble spec object from XML parameters:

        spec = new hamlet.EnsembleSummarySpec();

        spec.setModel(modelInput.get().model);
        spec.setStepper(integratorInput.get().getIntegratorObject());
        spec.setSimulationTime(simulationTimeInput.get());
        spec.setnSamples(nSamplesInput.get());
        spec.setnTraj(nTrajInput.get());
        
        hamlet.State initState = new hamlet.State();
        for (PopulationSize popSize : initialStateInput.get().popSizesInput.get())
            initState.set(popSize.pop, popSize.size);
        spec.setInitState(initState);
        
        for (MomentGroup momentGroup : momentGroupsInput.get())
            spec.addMomentGroup(momentGroup.momentGroup);
        
        for (Moment moment : momentsInput.get()) {
            if (moment.name == null)
                throw new RuntimeException("Moment doesn't specify name.");
            
            spec.addMoment(new hamlet.Moment(moment.name,
                    moment.factorial, moment.factors));
        }

        // Set seed if provided, otherwise use default BEAST seed:
        if (seedInput.get()!=null)
            spec.setSeed(seedInput.get());
        
        // Set the level of verbosity:
        spec.setVerbosity(verbosityInput.get());

        // Open specified file to use as output PrintStream
        // for JSON-formated results.  If no file specified,
        // dump to stdout.
        String outFileName = outFileNameInput.get();
        if (outFileName!=null)
            outStream = new PrintStream(outFileName);
        else
            outStream = System.out;
    }

    @Override
    public void run() throws Exception {

        // Generate ensemble of stochastic trajectories and estimate
        // specified moments:
        hamlet.EnsembleSummary ensemble =
                new hamlet.EnsembleSummary(spec);

        // Format results using JSON:
        JsonOutput.write(ensemble, outStream);

        // Close output file:
        outStream.close();
    }
}