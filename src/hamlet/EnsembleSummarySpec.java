package hamlet;

import java.util.*;

/**
 * Specification for generating a summarised ensemble of birth-death
 * trajectories.
 *
 * @author Tim Vaughan
 */
public class EnsembleSummarySpec extends EnsembleSpec {

	// Moments estimates to record:
	List<MomentGroup> momentGroups;

	public EnsembleSummarySpec() {
		super();

		// Create empty moment list:
		momentGroups = new ArrayList<MomentGroup>();
	}

	/**
	 * Add group of moments to estimate.
	 * 
	 * @param momentGroup 
	 */
	public void addMomentGroup(MomentGroup momentGroup) {
		momentGroup.postSpecInit();
		momentGroups.add(momentGroup);
	}
	
        /**
         * Add a single moment to estimate.
         * @param moment 
         */
        public void addMoment(Moment moment) {
            moment.postSpecInit();
            momentGroups.add(moment);
        }
}
