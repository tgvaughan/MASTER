package master;

import beast.core.*;

/**
 * Beast 2 object for specifying the size of a population.
 *
 * @author Tim Vaughan
 */
@Description("Size of a particular population.")
public class PopulationSize extends BEASTObject {

    public Input<Population> populationInput = new Input<Population>(
            "population", "Population whose size to specify.",
            Input.Validate.REQUIRED);

    public Input<Double> sizeInput = new Input<Double>(
            "size", "Size of chosen population.",
            Input.Validate.REQUIRED);

    // True population object:
    Population pop;

    /**
     * Size of this population.  Note that MASTER uses doubles rather than
     * integers to represent population sizes.
     */
    double size;

    public PopulationSize() { };

    @Override
    public void initAndValidate() throws Exception {
        pop = populationInput.get();
        size = sizeInput.get();
    }
}
