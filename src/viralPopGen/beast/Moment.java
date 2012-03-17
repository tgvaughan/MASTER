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

	public Input<String> nameInput = new Input<String>("momentName", "Moment name.");

	public Input<List<Population>> popSchemaInput = new Input<List<Population>>(
			"population",
			"Population to incorporate into moment.",
			new ArrayList<Population>());
	
	// True moment object:
	viralPopGen.Moment moment;

	// TODO: Implement moments comprised of structured populations.

	public Moment() {};

	@Override
	public void initAndValidate() throws Exception {
		
		// Assemble population schema:
		int schemaSize = popSchemaInput.get().size();
		viralPopGen.Population[] popSchema = new viralPopGen.Population[schemaSize];
		for (int pidx=0; pidx<schemaSize; pidx++)
			popSchema[pidx] = popSchemaInput.get().get(pidx).pop;
		
		// Assign schema to true moment object:
		moment = new viralPopGen.Moment(nameInput.get(), popSchema);
		
	}

}
