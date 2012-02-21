package viralPopGen;

/**
 * Class to extend to construct classes of objects
 * tailored to calculating particular moments.
 * 
 * @author Tim Vaughan
 *
 */
public class Moment {
	
	String name;
	int[] dims;
	double []data;
	int n;
	
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
