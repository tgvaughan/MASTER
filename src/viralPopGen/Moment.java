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

	/**
	 * Constructor.
	 * 
	 * @param nSamples	Number of samples to record in time series.
	 */
	public Moment(String name, Population ... popOrder) {
		this.name = name;
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
	 * Ensure sub-population moment schema exists, defaulting to
	 * scalar schema.
	 */
	public void init () {
		
	}

	/**
	 * Obtain estimate of moment for given state.
	 * 
	 * @param state	State to summarise.
	 * @param mean	Array to which to add moment estimates.
	 * @param std	Array to which to add squares of moment estimates.
	 */
	public void getEstimate (State state, double[] mean, double[] std) {
		
		for (int i=0; i<schemaSize; i++) {
			double estimate = 1;
			for (Population pop : locSchema.get(i).keySet()) {
				for (int offset : locSchema.get(i).get(pop).keySet()) {
					for (int m=0; m<locSchema.get(i).get(pop).get(offset); m++) {
						estimate *= state.get(pop, offset)-m;
					}
				}
			}
			mean[i] += estimate;
			std[i] += estimate*estimate;
		}
	}
}
