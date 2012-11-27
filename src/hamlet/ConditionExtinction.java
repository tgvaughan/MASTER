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
 * Simple population end condition which is met when a chosen population
 * has a size of zero.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class ConditionExtinction implements PopulationEndCondition {
    
    Population pop;
    boolean rejection;

    /**
     * Create an end condition that is met when population pop has a
     * size of zero.
     * 
     * @param pop Population to watch.
     * @param rejection True causes end condition to result in
     * trajectory rejection.
     */
    public ConditionExtinction(Population pop, boolean rejection) {
        this.pop = pop;
        this.rejection = rejection;
    }

    @Override
    public boolean isMet(State currentState) {
        return currentState.get(pop) == 0;
    }

    @Override
    public boolean isRejection() {
        return this.rejection;
    }
    
}
