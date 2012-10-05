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
import java.util.HashMap;
import java.util.Map;

/**
 * Class for mapping combinations of population and sub-population identifiers
 * onto a generic type.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationMap<T> {
    
    Map<Population,Map<Integer,T>> map;
    
    /**
     * Create a new population map.
     */
    public PopulationMap() {
        map = Maps.newHashMap();
    }
    
    public T get(Population pop, int subPopOffset) {
        return map.get(pop).get(subPopOffset);
    }
    
    public void put(Population pop, int subPopOffset, T value) {
        if (!map.containsKey(pop))
            map.put(pop, new HashMap<Integer,T>());
        
        map.get(pop).put(subPopOffset, value);
    }
    
    /**
     * Return a deep copy of the population map contents.
     * 
     * @return copy of map
     */
    public PopulationMap<T> copy() {
        PopulationMap<T> newMap = new PopulationMap<T>();
        
        for (Population pop : map.keySet()) {
            for (Integer offset : map.get(pop).keySet())
                newMap.put(pop, offset, get(pop,offset));
        }
        
        return newMap;
    }
}
