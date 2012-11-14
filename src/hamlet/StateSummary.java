package hamlet;

import java.util.*;
import com.google.common.collect.*;

/**
 * Class representing an ensemble of states summarised in
 * terms of moment estimates.
 * 
 * @author Tim Vaughan
 *
 */
public class StateSummary {

	Map<MomentGroup,double[]> mean, std;
	int sampleNum;

	/**
	 * Create new state summary using a given list of moments.
	 * 
	 * @param moments List of moments to use to summarise states.
	 */
	public StateSummary (List<MomentGroup> moments) {

		mean = Maps.newHashMap();
		std = Maps.newHashMap();

		for (MomentGroup moment : moments) {
			mean.put(moment, new double[moment.summationGroups.size()]);
			std.put(moment, new double[moment.summationGroups.size()]);
		}

		sampleNum = 0;
	}

	/**
	 * Add a new state to the summary.
	 * 
	 * @param state
	 */
	public void record(State state) {

		for (MomentGroup moment : mean.keySet())
			moment.getEstimate(state, mean.get(moment), std.get(moment));

		sampleNum += 1;

	}

	/**
	 * Normalise the summary.
	 */
	public void normalise() {
		for (MomentGroup moment : mean.keySet()) {
			double[] thisMean = mean.get(moment);
			double[] thisStd = std.get(moment);
			for (int i=0; i<thisMean.length; i++) {
				thisMean[i] /= sampleNum;
				thisStd[i] /= sampleNum;
				thisStd[i] = Math.sqrt(thisStd[i] - thisMean[i]*thisMean[i]);
			}
		}
	}
}
