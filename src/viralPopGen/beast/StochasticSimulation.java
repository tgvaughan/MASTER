package viralPopGen.beast;

import java.io.*;

import viralPopGen.EnsembleSummary;
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
	
	// Output file name:
	public Input<String> outFileNameInput = new Input<String>("outFileName",
			"Name of output file.");
	
	/*
	 * Fields to populate with parameter values:
	 */
	
	double simulationTime;
	int nTimeSteps, nSamples, nTraj;
	int seed;
	PrintStream outStream;
	
	viralPopGen.Model model;
	viralPopGen.State initState;
	
	public StochasticSimulation() {}

	@Override
	public void initAndValidate() throws Exception {
		
		// Read parameters from XML:
		simulationTime = simulationTimeInput.get();
		nTimeSteps = nTimeStepsInput.get();
		nSamples = nSamplesInput.get();
		nTraj = nTrajInput.get();
		seed = seedInput.get();
		
		// Read model and state specification from XML:
		model = modelInput.get().model;
		initState = initialStateInput.get().initState;
		
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
		EnsembleSummary ensemble = new EnsembleSummary(model, initState,
				simulationTime, nTimeSteps, nSamples, nTraj, seed);
		
		// Format results using JSON:
		ensemble.dump(outStream);
		
		// Close output file:
		outStream.close();
	}
}