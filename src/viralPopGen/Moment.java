package viralPopGen;

import java.util.*;

/**
 * Class of objects representing moments to be estimated from
 * system state ensemble.  Very similar to Reaction class in design.
 * 
 * @author Tim Vaughan
 *
 */
public class Moment {
	
	// Name of moment - used in output file:
	String name;
	
	// Specification of moment:
	HashMap<Population,ArrayList<HashMap<Integer,Integer>>> components;
	
	// Estimates:
	double[] mean, var;
	
	/**
	 * Record sample of moment calculated from state.
	 * 
	 * @param state
	 */
	public void record (State state) {
		
	}
	
	/**
	 * Normalise
	 */
	public void normalise() {
		
	}
}
