package hamlet.beast;

import beast.core.*;
import beast.evolution.tree.*;

/**
 * BEAST 2 plugin to generate random trees from birth-death models.
 *
 * @author Tim Vaughan
 */
@Description("Plugin to generate random trees from birth-death models.")
public class HamletTree extends Tree {

	public Input<Double> simulationTimeInput = new Input<Double>(
			"simulationTime", "Length of time to run simulation for.");
	public Input<Integer> nSamplesInput = new Input<Integer>(
			"nSamples", "Number of time points to include in tree.");
	public Input<Integer> seedInput = new Input<Integer>(
			"seed", "Seed for RNG.");

	public Input<Model> modelInput = new Input<Model>(
			"model", "Birth death model to use.");
	public Input<InitState> initState = new Input<InitState>(
			"initState", "Initial state of system.");

	public HamletTree() {};

	@Override
	public void initAndValidate() throws Exception {

		hamlet.Spec simulation = new hamlet.TreeSpec();

		// Read in simulation parameters from XML:
		simulation.setModel(modelInput.get().model);
		simulation.setSimulationTime(simulationTimeInput.get());
		simulation.setnSamples(nSamplesInput.get());
		simulation.setInitState(initState.get().initState);

		// Set seed explicitly if provided:
		if (seedInput.get() != null)
			simulation.setSeed(seedInput.get());

	}
	
}
