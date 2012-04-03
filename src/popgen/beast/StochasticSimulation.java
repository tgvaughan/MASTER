package popgen.beast;

import java.io.*;
import java.util.*;

import beast.core.*;
import beast.core.Runnable;

/**
 * BEAST 2 plugin representing a general stochastic simulation.
 * 
 * @author Tim Vaughan
 *
 */
@Description("A stochastic simulation of a birth-death population dynamics model.")
public class StochasticSimulation extends Runnable {

	/*
	 * XML inputs:
	 */

	// Simulation parameters:
	public Input<Double> simulationTimeInput = new Input<Double>("simulationTime",
			"The length of time to simulate.");
	public Input<Integer> nTimeStepsInput = new Input<Integer>("nTimeSteps",
			"Number integration time steps to use.");
	public Input<Integer> nSamplesInput = new Input<Integer>("nSamples",
			"Number of time points to sample state at.");
	public Input<Integer> nTrajInput = new Input<Integer>("nTraj",
			"Number of trajectories to generate.");
	public Input<Integer> seedInput = new Input<Integer>("seed",
			"Seed for RNG.");

	// Model:
	public Input<Model> modelInput = new Input<Model>("model",
			"The specific model to simulate.");

	// Initial state:
	public Input<InitState> initialStateInput = new Input<InitState>("initialState",
			"Initial state of system.");

	// Moments to estimate:
	public Input<List<Moment>> momentsInput = new Input<List<Moment>>(
			"moment",
			"Moment to sample from birth-death process.",
			new ArrayList<Moment>());

	// Output file name:
	public Input<String> outFileNameInput = new Input<String>("outFileName",
			"Name of output file.");

	/*
	 * Fields to populate with parameter values:
	 */

	// Simulation specification:
	popgen.Simulation simulation;

	// Stream object to write JSON output to:
	PrintStream outStream;

	public StochasticSimulation() {}

	@Override
	public void initAndValidate() throws Exception {

		// Assemble simulation object from XML parameters:

		simulation = new popgen.Simulation();

		simulation.setModel(modelInput.get().model);
		simulation.setSimulationTime(simulationTimeInput.get());
		simulation.setnTimeSteps(nTimeStepsInput.get());
		simulation.setnSamples(nSamplesInput.get());
		simulation.setnTraj(nTrajInput.get());
		simulation.setInitState(initialStateInput.get().initState);
		for (Moment momentInput : momentsInput.get())
			simulation.addMoment(momentInput.moment);

		// Set seed if provided, otherwise use default BEAST seed:
		if (seedInput.get() != null)
			simulation.setSeed(seedInput.get());

		// Open specified file to use as output PrintStream
		// for JSON-formated results.  If no file specified,
		// dump to stdout.
		String outFileName = outFileNameInput.get();
		if (outFileName != null)
			outStream = new PrintStream(outFileName);
		else 
			outStream = System.out;
	}

	@Override
	public void run() throws Exception {

		// Generate ensemble of stochastic trajectories and estimate
		// specified moments:
		popgen.EnsembleSummary ensemble =
				new popgen.EnsembleSummary(simulation);

		// Format results using JSON:
		ensemble.dump(outStream);

		// Close output file:
		outStream.close();
	}
}