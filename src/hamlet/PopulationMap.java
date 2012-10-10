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
import java.util.Set;

/**
 * Class for mapping combinations of population and sub-population identifiers
 * onto a generic type.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationMap<T> {
    
    Map<Population,Map<Integer,T>> map;
    
    T emptyValue;
    
    /**
     * Create a new population map.
     */
    public PopulationMap(T emptyValue) {
        this.emptyValue = emptyValue;
        map = Maps.newHashMap();
    }
    
    /**
     * Obtain value of map at (pop, subPopOffset), returning
     * the empty value in the case that the chosen key does not
     * exist.
     * 
     * @param pop
     * @param subPopOffset
     * @return 
     */
    public T get(Population pop, int subPopOffset) {
        if (!containsKey(pop, subPopOffset))
            return emptyValue;
        else
            return map.get(pop).get(subPopOffset);
    }
    
    /**
     * Remove map item at key (pop, subPopOffset).
     * 
     * @param pop
     * @param subPopOffset 
     */
    public void remove(Population pop, int subPopOffset) {
        map.get(pop).remove(subPopOffset);
        if (map.get(pop).isEmpty())
            map.remove(pop);
    }
    
    /**
     * Set the value of the map at (pop, subPopOffset).
     * 
     * @param pop
     * @param subPopOffset
     * @param value 
     */
    public void put(Population pop, int subPopOffset, T value) {
        
        // Setting to emtpyValue is the same as removing the entry:
        if (value==emptyValue) {
            if (containsKey(pop, subPopOffset))
                remove(pop, subPopOffset);
            
            return;
        }
        
        if (!map.containsKey(pop))
            map.put(pop, new HashMap<Integer,T>());
        
        map.get(pop).put(subPopOffset, value);
    }
    
    /**
     * Determine whether map contains an element with
     * the key (pop, subPopOffset).
     * 
     * @param pop
     * @param subPopOffset
     * @return True if map contains specified key.
     */
    public boolean containsKey(Population pop, int subPopOffset) {
        if (!map.containsKey(pop))
            return false;
        
        if (!map.get(pop).containsKey(subPopOffset))
            return false;
        
        return true;
    }
    
    /**
     * Get set of population-level keys.
     * 
     * @return key set
     */
    public Set<Population> getPopulationKeySet() {
        return map.keySet();
    }
    
    /**
     * Get set of sub-population-level keys (offset format).
     * 
     * @param population
     * @return key set
     */
    public Set<Integer> getOffsetKeySet(Population population) {
        return map.get(population).keySet();
    }
    
    /**
     * Return a deep copy of the population map contents.
     * 
     * @return copy of map
     */
    public PopulationMap<T> copy() {
        PopulationMap<T> newMap = new PopulationMap<T>(emptyValue);
        
        for (Population pop : map.keySet()) {
            for (Integer offset : map.get(pop).keySet())
                newMap.put(pop, offset, get(pop,offset));
        }
        
        return newMap;
    }
}
