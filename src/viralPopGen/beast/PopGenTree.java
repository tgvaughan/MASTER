package viralPopGen.beast;

import beast.core.*;
import beast.evolution.tree.*;

/**
 * BEAST 2 plugin to generate random trees from birth-death models.
 *
 * @author Tim Vaughan
 */
@Description("Plugin to generate random trees from birth-death models.")
public class PopGenTree extends Tree {

	public Input<Double> simulationTimeInput = new Input<Double>(
			"simulationTime", "Length of time to run simulation for.");
	public Input<Integer> nTimeStepsInput = new Input<Integer>(
			"nTimeSteps", "Number of time steps to use in integration.");
	public Input<Integer> nSamplesInput = new Input<Integer>(
			"nSamples", "Number of time points to include in tree.");
	public Input<Integer> seedInput = new Input<Integer>(
			"seed", "Seed for RNG.");

	public Input<Model> modelInput = new Input<Model>(
			"model", "Birth death model to use.");
	public Input<InitState> modelInitState = new Input<InitState>(
			"initState", "Initial state of system.");

	public PopGenTree() {};

	@Override
	public void initAndValidate() {

	}
	
}
