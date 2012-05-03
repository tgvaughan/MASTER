package hamlet.beast;

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
public class PopGenSimulation extends Runnable {

	/*
	 * XML inputs:
	 */

	// Spec parameters:
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

	// Spec specification:
	hamlet.EnsembleSummarySpec spec;

	// Stream object to write JSON output to:
	PrintStream outStream;

	public PopGenSimulation() {}

	@Override
	public void initAndValidate() throws Exception {

		// Assemble spec object from XML parameters:

		spec = new hamlet.EnsembleSummarySpec();

		spec.setModel(modelInput.get().model);
		spec.setSimulationTime(simulationTimeInput.get());
		spec.setnTimeSteps(nTimeStepsInput.get());
		spec.setnSamples(nSamplesInput.get());
		spec.setnTraj(nTrajInput.get());
		spec.setInitState(initialStateInput.get().initState);
		for (Moment momentInput : momentsInput.get())
			spec.addMoment(momentInput.moment);

		// Set seed if provided, otherwise use default BEAST seed:
		if (seedInput.get() != null)
			spec.setSeed(seedInput.get());

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
		hamlet.EnsembleSummary ensemble =
				new hamlet.EnsembleSummary(spec);

		// Format results using JSON:
		ensemble.dump(outStream);

		// Close output file:
		outStream.close();
	}
}