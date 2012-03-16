package viralPopGen.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing a stochastic moment.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Specification of a summary statistic to calculate.")
public class Moment extends Plugin {
	
	public Input<List<Population>> popSchemaInput = new Input<List<Population>>(
			"population",
			"Population to incorporate into moment.",
			new ArrayList<Population>());
	
	// TODO: Implement moments comprised of structured populations.

}
