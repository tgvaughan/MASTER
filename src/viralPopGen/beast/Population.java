package viralPopGen.beast;

//import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing a generic population.
 * 
 * @author Tim Vaughan
 *
 */
@Description("Population involved in a birth-death process.")
public class Population extends Plugin {
	
	public Input<String> nameInput = new Input<String>("popName", "Name of population");
	
	// TODO: Dimensionality specification.
	/*
	public Input<List<Integer>> dimsInput = new Input<List<Integer>>("dim",
			"Number of sub-populations in a single dimension.",
			new ArrayList<Integer>());
	*/
	
	// True population object:
	viralPopGen.Population pop;

	
	public Population() {};
	
	@Override
	public void initAndValidate() throws Exception {
		
		pop = new viralPopGen.Population(nameInput.get());
		
	}
}
