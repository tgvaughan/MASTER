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

    public Input<Population> populationInput = new Input<Population>(
            "population", "Population whose size to specify.",
            Input.Validate.REQUIRED);
    
    public Input<Double> sizeInput = new Input<Double>(
            "size", "Size of chosen population.",
            Input.Validate.REQUIRED);
    
    // True population object:
    hamlet.Population pop;
    
    // Size of this population:
    double size;

    // Note that viralPopGen uses doubles rather than integers
    // to represent population sizes.
    public PopulationSize() { };

	@Override
    public void initAndValidate() throws Exception {
        pop = populationInput.get().pop;
        size = sizeInput.get();
    }
}
