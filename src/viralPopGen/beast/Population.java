package viralPopGen.beast;

import beast.core.*;

/**
 * Beast 2 plugin representing a generic population.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Population involved in a birth-death process.")
public class Population extends Plugin {
	
	public Input<String> nameInput = new Input<String>("name", "Name of population");

	// TODO: Dimensionality specification.
}
