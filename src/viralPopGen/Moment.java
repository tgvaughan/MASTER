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
	Population[] popSchema;
	ArrayList<HashMap<Population,HashMap<Integer,Integer>>> locSchema;
	int schemaSize;

	// Number of samples to record in time series:
	int nSamples;
	
	// Number of trajectories incorporated into estimate:
	int nTraj;

	// Estimates:
	double[][] mean, std;
	double[] temp;
	
	/**
	 * Constructor.
	 * 
	 * @param nSamples	Number of samples to record in time series.
	 */
	public Moment(String name, int nSamples, Population ... popOrder) {
		this.name = name;
		this.nSamples = nSamples;
		this.popSchema = popOrder;
		schemaSize = popSchema.length;

		locSchema = new ArrayList<HashMap<Population,HashMap<Integer,Integer>>>();
	}
	
	/**
	 * Add specific sub-population resolution moment schema to moment object.
	 * 
	 * @param locs	Sub-population locations corresponding to populations
	 * 				given in constructor.
	 */
	public void addLocSchema (int[] ... locs) {
		
		if (locs.length != popSchema.length)
			throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");
		
		HashMap<Population, HashMap<Integer,Integer>> popMap =
				new HashMap<Population,HashMap<Integer,Integer>>();
		
		for (int pidx=0; pidx<popSchema.length; pidx++) {
			int offset = popSchema[pidx].locToOffset(locs[pidx]);
			
			if (!popMap.containsKey(popSchema[pidx])) {
				HashMap<Integer,Integer> offsetMap = new HashMap<Integer,Integer>();
				offsetMap.put(offset, 1);
				popMap.put(popSchema[pidx], offsetMap);
			} else {
				if (popMap.get(popSchema[pidx]).containsKey(locs[pidx])) {
					int oldVal = popMap.get(popSchema[pidx]).get(locs[pidx]);
					popMap.get(popSchema[pidx]).put(offset, oldVal+1);
				} else {
					popMap.get(popSchema[pidx]).put(offset, 1);
				}
			}
		}
		
		locSchema.add(popMap);
	}
	
	/**
	 * Prepare moment object. Call after last location schema has been
	 * added and before calling record().
	 */
	public void initialise () {
		
		// Allocate memory for storing moment estimates:
		mean = new double[nSamples][schemaSize];
		std = new double[nSamples][schemaSize];
		
		// Zero trajectory count:
		nTraj = 0;
		
	}

	/**
	 * Record sample of moment calculated from state.
	 * 
	 * @param state	State to record.
	 * @param sidx	Index of estimate to contribute to.
	 */
	public void record (State state, int sidx) {
		
		for (int i=0; i<schemaSize; i++) {
			double estimate = 1;
			for (Population pop : locSchema.get(i).keySet()) {
				for (int offset : locSchema.get(i).get(pop).keySet()) {
					for (int m=0; m<locSchema.get(i).get(pop).get(offset); m++) {
						estimate *= state.get(pop, offset)-m;
					}
				}
			}
			mean[sidx][i] += estimate;
			std[sidx][i] += estimate*estimate;
		}
		
		// Increment trajectory count:
		nTraj++;
	}
	
	/**
	 * Normalise moment estimates.
	 */
	public void normalise() {
		
		for (int sidx = 0; sidx<nSamples; sidx++) {
			for (int i=0; i<schemaSize; i++) {
				mean[sidx][i] /= (double)nTraj;
				std[sidx][i] /= (double)nTraj;
				std[sidx][i] = Math.sqrt(std[sidx][i] - mean[sidx][i]*mean[sidx][i]);
			}
		}
	}
}
