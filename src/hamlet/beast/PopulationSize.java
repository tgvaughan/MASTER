package hamlet.beast;

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
	public Input<SubPopulation> subPopulationInput = new Input<SubPopulation>(
			"subPopulation", "Sub-population whose size to specify.");
	public Input<Double> sizeInput = new Input<Double>(
			"size", "Size of particular sub-population.");

	// True population object:
	hamlet.Population pop;

	// Sub-population specifier:
	hamlet.SubPopulation sub;

	// Size of particular sub-population:
	Double size;

	// Note that viralPopGen uses doubles rather than integers
	// to represent population sizes.

	public PopulationSize() {};

	@Override
	public void initAndValidate() throws Exception {

		pop = populationInput.get().pop;
		if (subPopulationInput.get() != null) {
                    sub = new hamlet.SubPopulation(pop, subPopulationInput.get().location);
                } else {
                    sub = new hamlet.SubPopulation(pop);
                }
		size = sizeInput.get();
	}
}
