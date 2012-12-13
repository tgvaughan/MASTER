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

import com.google.common.collect.Lists;
import java.util.List;

/**
 * Population end condition which is met when (depending on the construction)
 * a population exceeds or dips below some threshold threshold.
 * May be either a rejection or a truncation condition.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationEndCondition {
    
    List<Population> pops;
    double threshold;
    boolean exceed, rejection;
    
    /**
     * Create new population end condition which is met when pop exceeds
     * or dips below threshold, depending on the value of exceedCond.
     * 
     * @param pop Population threshold to watch.
     * @param threshold Population threshold at which condition is met.
     * @param exceedCond True creates condition met when population >= threshold.
     * @param rejection True creates a rejection end condition.
     */
    public PopulationEndCondition(Population pop, double threshold, boolean exceedCond,
            boolean rejection) {
        this.pops = Lists.newArrayList();
        pops.add(pop);
        this.threshold = threshold;
        this.rejection = rejection;
        this.exceed = exceedCond;
    }
    
    /**
     * Create a new multi-population end condition which is met when sum of
     * populations provided exceeds or dips below threshold, depending on
     * value of exceedCond.
     * 
     * @param threshold Threshold at which condition is met.
     * @param exceedCond True creates condition met when sum> >= threshold
     * @param rejection True creates a rejection end condition
     * @param pops Varargs array of populations to sum over.
     */
    public PopulationEndCondition(double threshold, boolean exceedCond, boolean rejection, Population ... pops) {
        this.pops = Lists.newArrayList(pops);
        this.threshold = threshold;
        this.rejection = rejection;
        this.exceed = exceedCond;
    }

    public boolean isMet(PopulationState currentState) {
        double size = 0;
        for (Population pop : pops)
            size += currentState.get(pop);
        
        if (exceed)
            return size >= threshold;
        else
            return size <= threshold;
    }

    public boolean isRejection() {
        return rejection;
    }

    public String getConditionDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Condition is met when ");
        for (int i=0; i<pops.size(); i++) {
            if (i>0)
                sb.append(" + ");            
            sb.append(pops.get(i));
        }

        if (exceed)
            sb.append(" >= ");
        else
            sb.append(" <= ");
        
        sb.append(threshold);
        
        return sb.toString();
    }
    
}
