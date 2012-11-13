package hamlet.beast;

import beast.core.*;
import beast.core.Input.Validate;

/**
 * Beast 2 plugin representing a stochastic moment.
 *
 * @author Tim Vaughan
 *
 */
@Description("Specification of a summary statistic to calculate.")
public class Moment extends Plugin {

    public Input<String> nameInput = new Input<String>("momentName",
            "Moment name.", Validate.REQUIRED);
    public Input<Schema> schemaInput = new Input<Schema>("momentSchema",
            "Moment schema.", Validate.REQUIRED);
    public Input<Boolean> sumInput = new Input<Boolean>("sum",
            "Set to true to sum sub-population moment values together.");
    
    // True moment object:
    hamlet.Moment moment;

    public Moment() { };

	@Override
    public void initAndValidate() throws Exception {

        moment = new hamlet.Moment(nameInput.get(), schemaInput.get().popSchema);
        
        if (sumInput.get() != null && sumInput.get() == true) {
            moment.newSum();
            for (hamlet.SubPopulation[] subPopSchema : schemaInput.get().subPopSchemas)
                moment.addSubSchemaToSum(subPopSchema);
        } else {
            for (hamlet.SubPopulation[] subPopSchema : schemaInput.get().subPopSchemas)
                moment.addSubSchema(subPopSchema);
        }
    }
}
