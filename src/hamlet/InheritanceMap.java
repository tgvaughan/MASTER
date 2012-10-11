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
import java.util.ArrayList;
import java.util.List;

/**
 * Evil data structure describing the possible children resulting
 * from the involvement of a particular lineage in a reaction.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceMap {

    public PopulationMap<List<List<Population>>> populationLists;
    public PopulationMap<List<List<Integer>>> offsetLists;
    
    public InheritanceMap(
            List<Population> reactSchema,
            List<Population> prodSchema,
            List<Integer> reactOffsets,
            List<Integer> prodOffsets,
            List<Integer> reactantInheritance,
            List<Integer> productInheritance) {
        
        populationLists = new PopulationMap<List<List<Population>>>(null);
        offsetLists = new PopulationMap<List<List<Integer>>>(null);
        
        for (int i=0; i<reactSchema.size(); i++) {
            
            if (!populationLists.containsKey(reactSchema.get(i), reactOffsets.get(i))) {
                populationLists.put(reactSchema.get(i), reactOffsets.get(i),
                        new ArrayList<List<Population>>());
 
                offsetLists.put(reactSchema.get(i), reactOffsets.get(i),
                        new ArrayList<List<Integer>>());
            }
            
            int label=reactantInheritance.get(i);
            List<Population> populationList = Lists.newArrayList();
            List<Integer> offsetList = Lists.newArrayList();
                
            for (int j=0; j<prodSchema.size(); j++) {
                if (productInheritance.get(i) == label) {
                    populationList.add(prodSchema.get(i));
                    offsetList.add(prodOffsets.get(i));
                }
            }
            
            populationLists.get(reactSchema.get(i), reactOffsets.get(i)).add(populationList);
            offsetLists.get(reactSchema.get(i), reactOffsets.get(i)).add(offsetList);
        }
        
    }
}
