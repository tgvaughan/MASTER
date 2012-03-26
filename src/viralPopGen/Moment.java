package viralPopGen;

import java.util.*;
import com.google.common.collect.*;

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
	List<Map<Population,Map<Integer,Integer>>> subSchemas;

	/**
	 * Constructor.
	 * 
	 * @param nSamples	Number of samples to record in time series.
	 */
	public Moment(String name, Population ... popOrder) {
		this.name = name;
		this.popSchema = popOrder;

		subSchemas = Lists.newArrayList();
	}

	/**
	 * Add specific sub-population resolution moment schema to moment object.
	 * 
	 * @param subs	Sub-population locations corresponding to populations
	 * 				given in constructor.
	 */
	public void addSubSchema (int[] ... subs) {

		if (subs.length != popSchema.length)
			throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");

		Map<Population, Map<Integer,Integer>> popMap = Maps.newHashMap();

		for (int pidx=0; pidx<popSchema.length; pidx++) {
			int offset = popSchema[pidx].subToOffset(subs[pidx]);

			if (!popMap.containsKey(popSchema[pidx])) {
				Map<Integer,Integer> offsetMap = Maps.newHashMap();
				offsetMap.put(offset, 1);
				popMap.put(popSchema[pidx], offsetMap);
			} else {
				if (popMap.get(popSchema[pidx]).containsKey(offset)) {
					int oldVal = popMap.get(popSchema[pidx]).get(offset);
					popMap.get(popSchema[pidx]).put(offset, oldVal+1);
				} else {
					popMap.get(popSchema[pidx]).put(offset, 1);
				}
			}
		}

		subSchemas.add(popMap);
	}

	/**
	 * Ensure sub-population moment schema exists,
	 * defaulting to a single scalar schema.
	 */
	public void init () {

		if (subSchemas.isEmpty()) {
			Map<Population, Map<Integer,Integer>> popMap = Maps.newHashMap();
			for (int pidx=0; pidx<popSchema.length; pidx++) {
				if (!popMap.containsKey(popSchema[pidx])) {
					Map<Integer,Integer> offsetMap = Maps.newHashMap();
					offsetMap.put(0,1);
					popMap.put(popSchema[pidx], offsetMap);
				} else {
					int oldVal = popMap.get(popSchema[pidx]).get(0);
					popMap.get(popSchema[pidx]).put(0, oldVal+1);
				}
			}
			subSchemas.add(popMap);
		}
	}

	/**
	 * Obtain estimate of moment for given state.
	 * 
	 * @param state	State to summarise.
	 * @param mean	Array to which to add moment estimates.
	 * @param std	Array to which to add squares of moment estimates.
	 */
	public void getEstimate (State state, double[] mean, double[] std) {

		for (int i=0; i<subSchemas.size(); i++) {
			double estimate = 1;
			for (Population pop : subSchemas.get(i).keySet()) {
				for (int offset : subSchemas.get(i).get(pop).keySet()) {
					for (int m=0; m<subSchemas.get(i).get(pop).get(offset); m++) {
						estimate *= state.get(pop, offset)-m;
					}
				}
			}
			mean[i] += estimate;
			std[i] += estimate*estimate;
		}
	}
}
