package popgen;

import java.util.*;

/**
 * Specification for generating a summarised ensemble of birth-death
 * trajectories.
 *
 * @author Tim Vaughan
 */
public class EnsembleSummarySpec extends EnsembleSpec {

	// Moments estimates to record:
	List<Moment> moments;

	public EnsembleSummarySpec() {
		super();

		// Create empty moment list:
		moments = new ArrayList<Moment>();
	}

	/**
	 * Add moment to list of moments to estimate:
	 * 
	 * @param moment 
	 */
	public void addMoment(Moment moment) {
		moment.postSpecInit();
		moments.add(moment);
	}
	
}
