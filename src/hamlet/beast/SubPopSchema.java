package hamlet.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin for specifying sub-population-level reaction/moment schema.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Specification of sub-population schema for defining reactions/moments.")
public class SubPopSchema extends Plugin {

	public Input<List<SubPopulation>> subPopsInput = new Input<List<SubPopulation>>("subPopulation",
			"Sub-population to include in schema.",
			new ArrayList<SubPopulation>());

	// Array of sub-population specifications:
	int[][] subPopLocations;

	public SubPopSchema() {};

	@Override
	public void initAndValidate() throws Exception {

		subPopLocations = new int[subPopsInput.get().size()][];
		for (int i=0; i<subPopsInput.get().size(); i++)
			subPopLocations[i] = subPopsInput.get().get(i).location;
	}

}
