package popgen;

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
	List<List<Integer>> summationGroups;

	/**
	 * Constructor.
	 * 
	 * @param name Moment name to use for output.
	 * @param popSchema Population-level moment schema. 
	 */
	public Moment(String name, Population ... popSchema) {
		this.name = name;
		this.popSchema = popSchema;

		subSchemas = Lists.newArrayList();
		summationGroups = Lists.newArrayList();
	}

	/**
	 * Add sub-population level moment specification schema.
	 * 
	 * @param subs  Sub-population locations corresponding to populations
	 * 				given in constructor.
	 */
	public void addSubSchema(int[] ... subs) {
		newSum();
		addSubSchemaToSum(subs);
	}

	/**
	 * Create new summation group.
	 */
	public void newSum() {
		summationGroups.add(new ArrayList<Integer>());
	}

	/**
	 * Add sub-population-level moment specification schema to
	 * current summation group.
	 * 
	 * @param subs	Sub-population locations corresponding to populations
	 * 				given in constructor.
	 */
	public void addSubSchemaToSum (int[] ... subs) {

		if (subs.length != popSchema.length)
			throw new IllegalArgumentException
					("Inconsistent number of sub-populations specified.");

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

		// Add sub-population schema index to summation group list:
		summationGroups.get(summationGroups.size()-1).add(subSchemas.size()-1);
	}

	/**
	 * Add default sub-population level schema involving only
	 * those sub-populations which exist in "unstructured" populations.
	 */
	public void addScalarSubSchema() {

		newSum();

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

		// Add sub-population schema index to summation group list:
		summationGroups.get(summationGroups.size()-1).add(subSchemas.size()-1);
	}

	/**
	 * Initialisation which can only be accomplished after the moment
	 * schema is specified.  Called by Simulation::addMoment().
	 */
	public void postSpecInit () {

		if (subSchemas.isEmpty()) 
			addScalarSubSchema();
	}

	/**
	 * Obtain estimate of moment for given state.
	 * 
	 * @param state	State to summarise.
	 * @param mean	Array to which to add moment estimates.
	 * @param std	Array to which to add squares of moment estimates.
	 */
	public void getEstimate (State state, double[] mean, double[] std) {

		for (int s=0; s<summationGroups.size(); s++) {
			double estimate = 0;
			for (int i : summationGroups.get(s)) {
				double x = 1;
				for (Population pop : subSchemas.get(i).keySet()) {
					for (int offset : subSchemas.get(i).get(pop).keySet()) {
						for (int m=0; m<subSchemas.get(i).get(pop).get(offset); m++) {
							x *= state.get(pop, offset)-m;
						}
					}
				}
				estimate += x;
			}

			mean[s] += estimate;
			std[s] += estimate*estimate;
		}
	}
}
