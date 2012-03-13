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
	
	// Number of samples to record in time series:
	int nSamples;
	
	// Estimates:
	double[][] mean, var;
	
	/**
	 * Constructor.
	 * 
	 * @param nSamples	Number of samples to record in time series.
	 */
	public Moment(String name, int nSamples) {
		this.name = name;
		this.nSamples = nSamples;
		
		mean = new double[nSamples][];
		var = new double[nSamples][];
		
		components = new HashMap<Population,ArrayList<HashMap<Integer,Integer>>>();
	}
	
	/**
	 * Add component population to moment specification.
	 * 
	 * @param pop		Population to add.
	 * @param locArray	Array of sub-populations.
	 */
	public void addComponent (Population pop, int[][] locs) {
		
		if (!components.containsKey(pop)) {
			
			ArrayList<HashMap<Integer,Integer>> locVec = new ArrayList<HashMap<Integer,Integer>>();
			for (int i=0; i<locs.length; i++) {
				HashMap<Integer,Integer> locMap = new HashMap<Integer,Integer>();
				locMap.put(pop.locToOffset(locs[i]), 1);
				locVec.add(locMap);
			}
			components.put(pop, locVec);
			
		} else {
			
			if (locs.length != components.get(pop).size())
				throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");
			
			for (int i=0; i<locs.length; i++) {
				int offset = pop.locToOffset(locs[i]);
				if (components.get(pop).get(i).containsKey(offset)) {
					int oldVal = components.get(pop).get(i).get(offset);
					components.get(pop).get(i).put(offset, oldVal+1);
				} else {
					components.get(pop).get(i).put(offset, 1);
				}
			}
			
		}
	}
	
	/**
	 * Record sample of moment calculated from state.
	 * 
	 * @param state			State to record.
	 * @param sampleIndex	Index of estimate to contribute to.
	 */
	public void record (State state, int sampleIndex) {
		
	}
	
	/**
	 * Normalise
	 */
	public void normalise() {
		
	}
}
