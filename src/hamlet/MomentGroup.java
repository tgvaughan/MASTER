package hamlet;

import com.google.common.collect.*;
import java.util.*;

/**
 * Class of objects representing a group of moments to be estimated from
 * system state ensemble. Very similar to ReactionGroup class in design.
 *
 * @author Tim Vaughan
 *
 */
public class MomentGroup {

    // Name of moment - used in output file:
    String name;
    
    // Specification of moment:
    List<Map<Population, Integer>> subCounts;
    List<List<Integer>> summationGroups;
    
    // Flag to mark whether this is a factorial moment:
    boolean factorialMoment;

    /**
     * Constructor.
     *
     * @param name Moment name to use for output.
     * @param popSchema Population-level moment schema.
     */
    public MomentGroup(String name) {
        this.name = name;
        this.factorialMoment = true;

        subCounts = Lists.newArrayList();
        summationGroups = Lists.newArrayList();
    }

    /**
     * Alternative constructor.
     *
     * @param name Moment name to use for output.
     * @param factMoment True if this is a factorial moment.
     * @param popSchema Population-level moment schema.
     */
    public MomentGroup(String name, boolean factMoment) {
        this.name = name;
        this.factorialMoment = factMoment;

        subCounts = Lists.newArrayList();
        summationGroups = Lists.newArrayList();
    }

    /**
     * Add sub-population level moment specification schema.
     *
     * @param pops Populations whose sizes will be multiplied together
     * to produce moment.
     */
    public void addSchema(Population... pops) {
        newSum();
        addSubSchemaToSum(pops);
    }

    /**
     * Create new summation group.
     */
    public void newSum() {
        summationGroups.add(new ArrayList<Integer>());
    }

    /**
     * Add sub-population-level moment specification schema to current summation
     * group.
     *
     * @param subs Sub-populations corresponding to populations given
     * in constructor.
     */
    public void addSubSchemaToSum(Population... subs) {
        
        // Record unique sub-population counts
        subCounts.add(getSubCount(subs));
        
        // Add subCounts index to summation group list:
        summationGroups.get(summationGroups.size()-1).add(subCounts.size()-1);
    }
    
    
    /**
     * Internal method which takes a list of populations and constructs
     * a map from the populations to their multiplicity in the list.
     * 
     * @param pops List of populations.
     * @return Map from subpopulations to their list multiplicity.
     */
    private Map<Population, Integer> getSubCount(Population ... pops) {
        Map<Population, Integer> subCount = Maps.newHashMap();

        for (Population sub : pops) {
            if (!subCount.containsKey(sub))
                subCount.put(sub, 1);
            else {
                int val = subCount.get(sub);
                subCount.put(sub, val+1);
            }
        }
        
        return subCount;
    }

    /**
     * Initialisation which can only be accomplished after the moment schema is
     * specified. Called by Simulation::addMoment().
     */
    public void postSpecInit() {

    }

    /**
     * Obtain estimate of moment for given state.
     *
     * @param state	State to summarise.
     * @param mean	Array to which to add moment estimates.
     * @param std	Array to which to add squares of moment estimates.
     */
    public void getEstimate(State state, double[] mean, double[] std) {

        for (int s = 0; s<summationGroups.size(); s++) {
            double estimate = 0;
            for (int i : summationGroups.get(s)) {
                double x = 1;
                for (Population sub : subCounts.get(i).keySet())
                    for (int m = 0; m<subCounts.get(i).get(sub); m++)
                        if (factorialMoment)
                            x *= state.get(sub)-m;
                        else
                            x *= state.get(sub);
                estimate += x;
            }

            mean[s] += estimate;
            std[s] += estimate*estimate;
        }
    }
}
