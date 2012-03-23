package viralPopGen.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing a general birth-death model.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Specification of a birth-death model.")
public class Model extends Plugin {

	public Input<List<Population>> populationsInput = new Input<List<Population>>(
			"population",
			"Population involved in the birth-death process.",
			new ArrayList<Population>());

	public Input<List<Reaction>> reactionsInput = new Input<List<Reaction>>(
			"reaction",
			"Reaction involved in the birth-death process.",
			new ArrayList<Reaction>());

	public Input<List<Moment>> momentsInput = new Input<List<Moment>>(
			"moment",
			"Moment to sample from birth-death process.",
			new ArrayList<Moment>());

	// True model object:
	viralPopGen.Model model;

	public Model() {};

	@Override
	public void initAndValidate() throws Exception {

		model = new viralPopGen.Model();

		// Add populations to model:
		for (Population popInput : populationsInput.get())
			model.addPopulation(popInput.pop);

		// Add reactions to model:
		for (Reaction reactInput : reactionsInput.get())
			model.addReaction(reactInput.reaction);

		// Add moments to model:
		for (Moment momentInput : momentsInput.get())
			model.addMoment(momentInput.moment);

	}

}
