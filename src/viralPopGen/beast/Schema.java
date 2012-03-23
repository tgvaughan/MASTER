package viralPopGen.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing a combined population and sub-population schema.
 * @author Tim Vaughan
 *
 */
@Description("Schema specification used in the definition of reactions/moments.")
public class Schema extends Plugin {

	public Input<PopSchema> popSchemaInput = new Input<PopSchema>("popSchema", "Population level schema.");

	public Input<List<SubPopSchema>> subPopSchemasInput = new Input<List<SubPopSchema>>("subPopSchema",
			"A single sub-population-level schema.",
			new ArrayList<SubPopSchema>());

	// Population schema:
	viralPopGen.Population[] popSchema;

	// Array of sub-population-level schemas:
	List<int[][]> subPopSchemas;

	public Schema() {};

	@Override
	public void initAndValidate() throws Exception {

		popSchema = popSchemaInput.get().popSchema;

		subPopSchemas = new ArrayList<int[][]>();
		for (SubPopSchema subPopSchemaInput : subPopSchemasInput.get())
			subPopSchemas.add(subPopSchemaInput.subPopSchema);

	}
}
