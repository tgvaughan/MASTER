package viralPopGen.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing initial system state.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Specification of initial system state.")
public class InitState extends Plugin {
	
	public Input<List<PopulationSize>> popSizesInput = new Input<List<PopulationSize>>(
			"populationSize",
			"Initial population size.",
			new ArrayList<PopulationSize>());
	
	public Input<Model> modelInput = new Input<Model>("model", "Model defining state space.");
	
	// True state object:
	viralPopGen.State initState;
	
	public InitState() {};
	
	@Override
	public void initAndValidate() throws Exception {
		
		// Instantiate true state object:
		initState = new viralPopGen.State(modelInput.get().model);
		
		// Assign sizes to state object:
		for (PopulationSize popSizeInput : popSizesInput.get())
			initState.set(popSizeInput.pop, popSizeInput.size);
		
	}
	
}
