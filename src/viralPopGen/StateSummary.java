package viralPopGen;

import java.util.*;

/**
 * Class representing an ensemble of states summarised in
 * terms of moment estimates.
 * 
 * @author Tim Vaughan
 *
 */
public class StateSummary {
	
	HashMap<Moment,double[]> mean, std;
	int nSamples;
	
	/**
	 * Create new state summary using a given list of moments.
	 * 
	 * @param moments List of moments to use to summarise states.
	 */
	public StateSummary (ArrayList<Moment> moments) {
		
		for (Moment moment : moments) {
			mean.put(moment, new double[moment.schemaSize]);
			std.put(moment, new double[moment.schemaSize]);
		}
		
		nSamples = 0;
	}
	
	/**
	 * Add a new state to the summary.
	 * 
	 * @param state
	 */
	public void record(State state) {
		
		for (Moment moment : mean.keySet()) {
			moment.getEstimate(state, mean.get(moment), std.get(moment));
		}
		
		nSamples++;
		
	}
	
	/**
	 * Normalise the summary.
	 */
	public void normalise() {
		for (Moment moment : mean.keySet()) {
			double[] thisMean = mean.get(moment);
			double[] thisStd = std.get(moment);
			for (int i=0; i<thisMean.length; i++) {
				thisMean[i] /= nSamples;
				thisStd[i] /= nSamples;
				thisStd[i] = Math.sqrt(thisStd[i] - thisMean[i]*thisMean[i]);
			}
		}
	}
}
