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

/**
 * Population end condition which is met when (depending on the construction)
 * a population exceeds or dips below some threshold size.
 * May be either a rejection or a truncation condition.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationEndCondition {
    
    Population pop;
    double size;
    boolean exceed, rejection;
    
    /**
     * Create new population end condition which is met when pop exceeds
     * or dips below size, depending on the value of exceedCond.
     * 
     * @param pop Population size to watch.
     * @param size Population size at which condition is met.
     * @param exceedCond True creates condition met when population >= size.
     * @param rejection True creates a rejection end condition.
     */
    public PopulationEndCondition(Population pop, double size, boolean exceedCond,
            boolean rejection) {
        this.pop = pop;
        this.size = size;
        this.rejection = rejection;
        this.exceed = exceedCond;
    }

    public boolean isMet(PopulationState currentState) {
        if (exceed)
            return currentState.get(pop) >= size;
        else
            return currentState.get(pop) <= size;
    }

    public boolean isRejection() {
        return rejection;
    }

    public String getConditionDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Condition is met when size of ")
                .append(pop)
                .append(" population ");
        
        if (exceed)
            sb.append(">=");
        else
            sb.append("<=");
        
        sb.append(size);
        
        return sb.toString();
    }
    
}
