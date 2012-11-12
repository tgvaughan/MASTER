package hamlet;

import com.google.common.collect.*;
import java.util.*;

/**
 * Class of objects representing moments to be estimated from system state
 * ensemble. Very similar to Reaction class in design.
 *
 * @author Tim Vaughan
 *
 */
public class Moment {

    // Name of moment - used in output file:
    String name;
    // Specification of moment:
    List<Population> popSchema;
    List<Map<SubPopulation, Integer>> subCounts;
    List<List<Integer>> summationGroups;
    // Flag to mark whether this is a factorial moment:
    boolean factorialMoment;

    /**
     * Constructor.
     *
     * @param name Moment name to use for output.
     * @param popSchema Population-level moment schema.
     */
    public Moment(String name, Population... popSchema) {
        this.name = name;
        this.popSchema = Lists.newArrayList(popSchema);
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
    public Moment(String name, boolean factMoment, Population... popSchema) {
        this.name = name;
        this.popSchema = Lists.newArrayList(popSchema);
        this.factorialMoment = factMoment;

        subCounts = Lists.newArrayList();
        summationGroups = Lists.newArrayList();
    }

    /**
     * Add sub-population level moment specification schema.
     *
     * @param subs Sub-population locations corresponding to populations given
     * in constructor.
     */
    public void addSubSchema(SubPopulation... subs) {
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
     * Add sub-population-level moment specification schema to current summation
     * group.
     *
     * @param subs Sub-populations corresponding to populations given
     * in constructor.
     */
    public void addSubSchemaToSum(SubPopulation... subs) {

        if (subs.length!=popSchema.size())
            throw new IllegalArgumentException("Inconsistent number of sub-populations specified.");
        
        // Check that sub-populations are of the right population type:
        for (int i=0; i<subs.length; i++) {
            if (subs[i].pop != popSchema.get(i))
                throw new IllegalArgumentException("Incorrect subpopulation type in schema.");
        }
        
        // Record unique sub-population counts
        subCounts.add(getSubCount(subs));
        
        // Add subCounts index to summation group list:
        summationGroups.get(summationGroups.size()-1).add(subCounts.size()-1);
    }
    
    /**
     * Add default sub-population level schema involving only those
     * sub-populations which exist in "unstructured" populations.
     */
    public void addScalarSubSchema() {

        newSum();
        
        SubPopulation[] subs = new SubPopulation[popSchema.size()];
        for (int pidx=0; pidx<popSchema.size(); pidx++)
            subs[pidx] = new SubPopulation(popSchema.get(pidx));
        subCounts.add(getSubCount(subs));

        // Add sub-population schema index to summation group list:
        summationGroups.get(summationGroups.size()-1).add(subCounts.size()-1);
    }
    
    /**
     * Internal method which takes a list of sub-populations and constructs
     * a map from the sub-populations to their multiplicity in the list.
     * 
     * @param subs List of sub-populations.
     * @return Map from subpopulations to their list multiplicity.
     */
    private Map<SubPopulation, Integer> getSubCount(SubPopulation ... subs) {
        Map<SubPopulation, Integer> subCount = Maps.newHashMap();

        for (SubPopulation sub : subs) {
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

        if (subCounts.isEmpty())
            addScalarSubSchema();
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
                for (SubPopulation sub : subCounts.get(i).keySet())
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
