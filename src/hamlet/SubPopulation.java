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
 * Specifies a particular subpopulation/deme within a population.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class SubPopulation {
    Population pop;
    int offset;
    
    /**
     * Create a new subpopulation specifier.
     * 
     * @param population Population this is a subpopulation of.
     * @param location Location/identifier of subpopulation.
     */
    public SubPopulation(Population population, int ... location) {
        this.pop = population;
        
        if (population.nSubPops==1)
            offset = 0;
        else
            this.offset = population.locToOffset(location);
    }
    
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        
        if (other instanceof SubPopulation) {
            SubPopulation otherSub = (SubPopulation)other;
            result = (pop == otherSub.pop) && (offset == otherSub.offset);
        }
        
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89*hash+(this.pop!=null ? this.pop.hashCode() : 0);
        hash = 89*hash+this.offset;
        return hash;
    }
    
}
