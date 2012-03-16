package viralPopGen.beast;

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

	// Simulation parameters:
	public Input<Double> simulationTimeInput = new Input<Double>("simulationTime",
			"The length of time to simulate.");
	public Input<Integer> nTimeStepsInput = new Input<Integer>("nTimeSteps",
			"Number integration time steps to use.");
	public Input<Integer> nSamplesInput = new Input<Integer>("nSamples",
			"Number of time points to sample state at.");
	public Input<Integer> nTrajInput = new Input<Integer>("nTraj",
			"Number of trajectories to generate.");
	
	// Model:
	public Input<Model> modelInput = new Input<Model>("model",
			"The specific model to simulate.");
	
	// Initial state:
	public Input<InitState> initialStateInput = new Input<InitState>("initialState",
			"Initial state of system.");
	
	/**
	 * Empty default constructor required.
	 */
	public StochasticSimulation() {}
	
	@Override
	public void initAndValidate() throws Exception {
		
		System.out.println("Hello, world!");
		System.out.println("simulationTime = " + String.valueOf(simulationTimeInput.get()));
		
	}
	
	@Override
	public void run() throws Exception {
		System.out.println("Off and running!");
	}
}