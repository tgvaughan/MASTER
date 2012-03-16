package viralPopGen.beast;

import beast.core.*;

/**
 * Beast 2 plugin for specifying the size of a population.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Size of a particular population.")
public class PopulationSize extends Plugin {
	
	public Input<Population> populationInput = new Input<Population>("population",
			"Population whose size to specify.");
	public Input<Double> sizeInput = new Input<Double>("size",
			"Size of population.");
	
	// Note that viralPopGen uses doubles rather than integers
	// to represent population sizes.
	
	// TODO: Allow specification of structured population sizes.
}
