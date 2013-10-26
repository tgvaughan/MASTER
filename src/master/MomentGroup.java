/*
 * Copyright (C) 2012 Tim Vaughan <tgvaughan@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package master;

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
    List<Map<Population, Integer>> popCounts;
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

        popCounts = Lists.newArrayList();
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

        popCounts = Lists.newArrayList();
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
     * @param pops Sub-populations corresponding to populations given
     * in constructor.
     */
    public void addSubSchemaToSum(Population... pops) {
        
        // Record unique sub-population counts
        popCounts.add(getPopCount(pops));
        
        // Add popCounts index to summation group list:
        summationGroups.get(summationGroups.size()-1).add(popCounts.size()-1);
    }
    
    
    /**
     * Internal method which takes a list of populations and constructs
     * a map from the populations to their multiplicity in the list.
     * 
     * @param pops List of populations.
     * @return Map from subpopulations to their list multiplicity.
     */
    private Map<Population, Integer> getPopCount(Population ... pops) {
        Map<Population, Integer> popCount = Maps.newHashMap();

        for (Population sub : pops) {
            if (!popCount.containsKey(sub))
                popCount.put(sub, 1);
            else {
                int val = popCount.get(sub);
                popCount.put(sub, val+1);
            }
        }
        
        return popCount;
    }

    /**
     * Initialisation which can only be accomplished after the moment schema is
     * specified. Called by Simulation::addMoment().
     */
    public void postSpecInit() {

    }

    /**
     * Obtain summary for given state.
     *
     * @param state	PopulationState to summarise.
     * @param mean	Array in which to store summary.
     */
    public void getSummary(PopulationState state, double[] summary) {

        for (int s = 0; s<summationGroups.size(); s++) {
            double estimate = 0;
            for (int i : summationGroups.get(s)) {
                double x = 1;
                for (Population pop : popCounts.get(i).keySet())
                    for (int m = 0; m<popCounts.get(i).get(pop); m++)
                        if (factorialMoment)
                            x *= state.get(pop)-m;
                        else
                            x *= state.get(pop);
                estimate += x;
            }

            summary[s] = estimate;
        }
    }

    /**
     * Retrieve name of moment group.
     * 
     * @return moment group name
     */
    public String getName() {
        return name;
    }
}
