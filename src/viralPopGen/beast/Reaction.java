package viralPopGen.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing a single reaction.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Component reaction of a birth-death model.")
public class Reaction extends Plugin {
	
	public Input<List<Population>> reactantsInput = new Input<List<Population>>(
			"reactant",
			"Reactant population.",
			new ArrayList<Population>());
	
	public Input<List<Population>> productsInput = new Input<List<Population>>(
			"product",
			"Product population.",
			new ArrayList<Population>());
	
	public Input<Double> rateInput = new Input<Double>("rate", "Reaction rate.");
	
	// True reaction object:
	viralPopGen.Reaction reaction;
	
	public Reaction() {};
	
	@Override
	public void initAndValidate() throws Exception {
		
		reaction = new viralPopGen.Reaction();
		
		viralPopGen.Population[] reactPopSchema =
				new viralPopGen.Population[reactantsInput.get().size()];
		for (int i=0; i<reactantsInput.get().size(); i++)
			reactPopSchema[i] = reactantsInput.get().get(i).pop;
		
		reaction.setReactantSchema(reactPopSchema);
		
		viralPopGen.Population[] prodPopSchema =
				new viralPopGen.Population[productsInput.get().size()];
		for (int i=0; i<productsInput.get().size(); i++)
			prodPopSchema[i] = productsInput.get().get(i).pop;
		
		reaction.setProductSchema(prodPopSchema);
		
		reaction.setRate(rateInput.get());
	}

}