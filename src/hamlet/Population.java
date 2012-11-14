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

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Specifies a particular population/deme of a given type.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class Population {
    PopulationType type;
    int offset;
    
    /**
     * Create a new population specifier.
     * 
     * @param populationType Type of this population.
     * @param location Location/identifier of population.
     */
    public Population(PopulationType populationType, int ... location) {
        this.type = populationType;
        
        if (populationType.nPops==1)
            this.offset = 0;
        else
            this.offset = populationType.locToOffset(location);
    }
    
    /**
     * Create new scalar population with its own type.
     * 
     * @param populationName 
     */
    public Population(String populationName) {
        this.type = new PopulationType(populationName);
        this.offset = 0;
    }
    
    /**
     * @return true if population is the sole member of its type.
     */
    public boolean isScalar() {
        return this.type.nPops==1;
    }
    
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        
        if (other instanceof Population) {
            Population otherPop = (Population)other;
            result = ((type == otherPop.type) && (offset == otherPop.offset));
        }
        
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89*hash+(this.type!=null ? this.type.hashCode() : 0);
        hash = 89*hash+this.offset;
        return hash;
    }    
}
