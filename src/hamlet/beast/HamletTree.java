package hamlet.beast;

import beast.core.*;
import beast.evolution.tree.*;
import java.util.List;

/**
 * BEAST 2 plugin to generate random trees from birth-death models.
 *
 * @author Tim Vaughan
 */
@Description("Plugin to generate random trees from birth-death models.")
public class HamletTree extends Tree implements StateNodeInitialiser {

	public Input<Integer> seedInput = new Input<Integer>(
			"seed", "Seed for RNG.");

	public Input<Model> modelInput = new Input<Model>(
			"model", "Birth death model to use.");
	public Input<InitState> initState = new Input<InitState>(
			"initState", "Initial state of system.");

	public HamletTree() {};

	@Override
	public void initAndValidate() throws Exception {

		hamlet.TreeSpec simulation = new hamlet.TreeSpec();

		// Read in simulation parameters from XML:
		simulation.setModel(modelInput.get().model);
		simulation.setInitState(initState.get().initState);

		// Set seed explicitly if provided:
		if (seedInput.get() != null)
			simulation.setSeed(seedInput.get());

	}

    @Override
    public void initStateNodes() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StateNode> getInitialisedStateNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
}
