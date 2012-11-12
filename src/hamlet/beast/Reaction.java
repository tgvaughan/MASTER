package hamlet.beast;

import java.util.*;
import beast.core.*;
import beast.core.parameter.*;

/**
 * Beast 2 plugin representing a single reaction.
 *
 * @author Tim Vaughan
 *
 */
@Description("Component reaction of a birth-death model.")
public class Reaction extends Plugin {

    public Input<String> reactionNameInput = new Input<String>(
            "reactionName", "Reaction name");
    public Input<Schema> reactantSchemaInput = new Input<Schema>(
            "reactantSchema", "Reactant schema.");
    public Input<Schema> productSchemaInput = new Input<Schema>(
            "productSchema", "Product schema.");
    public Input<List<RealParameter>> ratesInput = new Input<List<RealParameter>>(
            "rate", "Reaction rate.",
            new ArrayList<RealParameter>());
    // True reaction object:
    hamlet.Reaction reaction;

    public Reaction() { };
    
	@Override
    public void initAndValidate() throws Exception {

        if (reactionNameInput.get()==null)
            reaction = new hamlet.Reaction();
        else
            reaction = new hamlet.Reaction(reactionNameInput.get());

        reaction.setReactantSchema(reactantSchemaInput.get().popSchema);        
        for (hamlet.SubPopulation[] subs : reactantSchemaInput.get().subPopSchemas)
            reaction.addReactantSubSchema(subs);
        
        reaction.setProductSchema(productSchemaInput.get().popSchema);        
        for (hamlet.SubPopulation[] subs : productSchemaInput.get().subPopSchemas)
            reaction.addProductSubSchema(subs);

        double[] rates = new double[ratesInput.get().size()];
        for (int i = 0; i<rates.length; i++)
            reaction.addSubRate(ratesInput.get().get(i).getValue());
    }
}