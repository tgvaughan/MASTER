package hamlet.beast;

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
	public Input<Schema> schemaInput = new Input<Schema>("momentSchema", "Moment schema.");

	// True moment object:
	hamlet.Moment moment;

	public Moment() {};

	@Override
	public void initAndValidate() throws Exception {

		moment = new hamlet.Moment(nameInput.get(), schemaInput.get().popSchema);
		for (int[][] subPopSchema : schemaInput.get().subPopSchemas)
			moment.addSubSchema(subPopSchema);

	}

}
