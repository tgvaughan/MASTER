package viralPopGen.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing a specific sub-population.
 * @author Tim Vaughan
 *
 */
@Description("Specific sub-population of a structured population.")
public class SubPopulation extends Plugin {
	
	Input<List<Integer>> locationInput = new Input<List<Integer>>("location",
			"Location of sub-population along a single dimention",
			new ArrayList<Integer>());
	
	// Sub-population specification:
	int[] sub;

	public SubPopulation() {};

	@Override
	public void initAndValidate() throws Exception {

		sub = new int[locationInput.get().size()];

		for (int i=0; i<locationInput.get().size(); i++)
			sub[i] = locationInput.get().get(i);
	}

}
