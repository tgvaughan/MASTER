package viralPopGen;

import java.util.*;

/**
 * Class describing a birth-death viral population genetics model.
 * Basically a container class including a sets of population and
 * reaction objects.
 * 
 * @author Tim Vaughan
 *
 */
public class Model {

	// Populations in model:
	ArrayList<Population> geneticPops;
	ArrayList<Population> scalarPops;

	// Reactions to model:
	ArrayList<Reaction> reactions;
	
	// Sequence length:
	int L;
	
	// Sequence space dimensionality:
	int[] seqDims;
	
	// Total number of included types:
	int typeNum;
	
	/**
	 * Model constructor.  Note that each element of seqDims must be <= L+1.
	 * 
	 * @param L Genetic sequence length.
	 * @param seqDims Reduced sequence space dimensionality.
	 */
	public Model (int L, int[] seqDims) {
		this.L = L;
		this.seqDims = seqDims;
		
		geneticPops = new ArrayList<Population>();
		scalarPops = new ArrayList<Population>();
		
		reactions = new ArrayList<Reaction>();
		
		// Count reduced volume of sequence space:
		typeNum = 1;
		for (int d=0; d<seqDims.length; d++) {
			typeNum *= seqDims[d];
			assert(seqDims[d] <= L+1);
		}
	}

	/**
	 * Add population to model.
	 * 
	 * @param pop Population to add.
	 */
	public void addPopulation(Population pop) {
		if (pop.genetic)
			geneticPops.add(pop);
		else
			scalarPops.add(pop);
	}

	/**
	 * Add reaction to model.
	 * 
	 * @param react Reaction to add.
	 */
	public void addReaction(Reaction react) {
		reactions.add(react);
	}

	/**
	 * Convert sequence space location vector to
	 * integer offset into linear array.
	 * 
	 * @param location Array specification of sub-population.
	 * @return Offset into popSizes vector.
	 */
	public int locToOffset(int[] loc) {
		
		int offset = 0;
		int mul = 1;
	
		for (int d=0; d<seqDims.length; d++) {
			offset += loc[d]*mul;
			mul *= seqDims[d];
		}
		
		return offset;
	}

}
