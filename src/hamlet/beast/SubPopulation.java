package hamlet.beast;

import java.util.*;
import beast.core.*;
import beast.core.parameter.*;

/**
 * Beast 2 plugin representing a specific sub-population.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Specific sub-population of a structured population.")
public class SubPopulation extends Plugin {

	public Input<List<IntegerParameter>> locationInput = new Input<List<IntegerParameter>>(
			"location",
			"Location of sub-population along a single dimention",
			new ArrayList<IntegerParameter>());

	// Sub-population location:
	int[] location;

	public SubPopulation() {};

	@Override
	public void initAndValidate() throws Exception {

		location = new int[locationInput.get().size()];

		for (int i=0; i<locationInput.get().size(); i++)
			location[i] = locationInput.get().get(i).getValue();
	}

}
