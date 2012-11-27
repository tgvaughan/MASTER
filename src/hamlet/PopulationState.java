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
package hamlet;

import com.google.common.collect.*;
import java.util.*;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Class of objects describing population states of the simulated system.
 *
 * @author Tim Vaughan
 *
 */
public class PopulationState {

    Map<Population, Double> popSizes;

    /**
     * Constructor
     *
     * @param model Model defining the state space.
     */
    public PopulationState() {

        // Initialise sub-population sizes:
        popSizes = Maps.newHashMap();
    }

    /**
     * Copy constructor
     *
     * @param oldState PopulationState to copy.
     */
    public PopulationState(PopulationState oldState) {

        // Copy sub-population sizes:
        this.popSizes = Maps.newHashMap();
        for (Population pop : oldState.popSizes.keySet()) {
            popSizes.put(pop, oldState.popSizes.get(pop));
        }
    }

    /**
     * Get size of a particular population.
     *
     * @param pop Specific population.
     * @return Size of population.
     */
    public double get(Population pop) {
        if (popSizes.containsKey(pop))
            return popSizes.get(pop);
        else
            return 0.0;
    }

    /**
     * Set size of a particular population.
     *
     * @param pop Population to modify.
     * @param value Desired size.
     */
    public void set(Population pop, double value) {
        popSizes.put(pop, value);
    }

    /**
     * Add value to size of particular population.
     *
     * @param pop
     * @param increment
     */
    public void add(Population pop, double increment) {
        if (popSizes.containsKey(pop))
            popSizes.put(pop, popSizes.get(pop) + increment);
        else
            popSizes.put(pop, increment);
    }

    /**
     * Add value to size of particular population specified using a
     * pre-calculated offset, truncating at zero if result is negative.
     *
     * @param pop
     * @param increment
     */
    public void addNoNeg(Population pop, double increment) {
        double newPopSize = get(pop)+increment;
        if (newPopSize<0.0)
            popSizes.put(pop, 0.0);
        else
            popSizes.put(pop, newPopSize);
    }

    /**
     * Alter state according to one ore more occurrences of a particular
     * reaction.
     * 
     * @param reactionGroup
     * @param reactionIndex
     * @param q Number of times for reaction to fire.
     */
    public void implementReaction(ReactionGroup reactionGroup, int reactionIndex, double q) {
        for (Population pop : reactionGroup.deltaCounts.get(reactionIndex).keySet())
            addNoNeg(pop, q*reactionGroup.deltaCounts.get(reactionIndex).get(pop));
    }
    
    @JsonValue
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        boolean first = true;
        for (Population pop : popSizes.keySet()) {
            if (!first)
                sb.append(" ");
            else
                first = false;
            
            sb.append(pop.getType().getName());
            if (!pop.isScalar()) {
                sb.append("[");
                int[] loc = pop.getLocation();
                for (int i=0; i<loc.length; i++) {
                    if (i>0)
                        sb.append(",");
                    sb.append(loc[i]);
                }
                sb.append("]");
            }
            sb.append(": ").append(popSizes.get(pop));
        }
        
        return sb.toString();
    }
    
}
