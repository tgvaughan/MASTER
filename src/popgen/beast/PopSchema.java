package popgen.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing population-level reaction/moment schema.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Specification of population schema for defining reactions and moments.")
public class PopSchema extends Plugin {

	public Input<List<Population>> populationsInput = new Input<List<Population>>("population",
			"Population in schema.", new ArrayList<Population>());

	// Population-level schema:
	popgen.Population[] popSchema; 

	public PopSchema() {};

	@Override
	public void initAndValidate() throws Exception {

		popSchema = new popgen.Population[populationsInput.get().size()];
		for (int i=0; i<populationsInput.get().size(); i++)
			popSchema[i] = populationsInput.get().get(i).pop;
	}

}
