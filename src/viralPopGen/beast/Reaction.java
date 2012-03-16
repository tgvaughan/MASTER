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
			"Reactant population.");
	
	public Input<List<Population>> productsInput = new Input<List<Population>>(
			"product",
			"Product population.");
	
	public Input<Double> rateInput = new Input<Double>("rate", "Reaction rate.");
	
	// True reaction object:
	viralPopGen.Reaction reaction;
	
	public Reaction() {};
	
	@Override
	public void initAndValidate() throws Exception {
		
		reaction = new viralPopGen.Reaction();
		
		for (Population reactantInput : reactantsInput.get())
			reaction.addReactant(reactantInput.pop);
		
		for (Population productInput : productsInput.get())
			reaction.addProduct(productInput.pop);
		
		reaction.setRate(rateInput.get());
	}

}