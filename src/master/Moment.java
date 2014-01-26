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

import beast.core.BEASTObject;
import beast.core.Input;
import com.google.common.collect.*;
import java.util.*;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Class of objects representing a group of moments to be estimated from
 * system state ensemble.
 *
 * @author Tim Vaughan
 *
 */
public class Moment extends BEASTObject {

    public Input<String> nameInput = new Input<String>("momentName",
            "Name of moment. (Overridden by moment group name.)");
    
    public Input<Boolean> factorialInput = new Input<Boolean>("factorial",
            "True causes factorial moments to be used.  (Overridden by moment group choice.)",
            false);
    
    public Input<List<Population>> factorsInput = new Input<List<Population>>(
            "factor",
            "Population whose size will be factored into moment calculation.",
            new ArrayList<Population>());
    
    // Name of moment - used in output file:
    String name;
    
    // Flag to mark whether this is a factorial moment:
    boolean factorialMoment;
        
    // Specification of moment:
    Map<Population, Integer> popCount;

    /**
     * Default constructor. (Used by BEAST)
     */
    public Moment() { }
    
    @Override
    public void initAndValidate() {
        name = nameInput.get();
        factorialMoment = factorialInput.get();
        
        popCount = Maps.newHashMap();

        for (Population pop : factorsInput.get()) {
            if (!popCount.containsKey(pop))
                popCount.put(pop, 1);
            else {
                int val = popCount.get(pop);
                popCount.put(pop, val+1);
            }
        }
    }

    /**
     * Obtain summary for given state.
     *
     * @param state PopulationState to summarize.
     * @return summary of state
     */
    public double getSummary(PopulationState state) {

        double x = 1;
        for (Population pop : popCount.keySet())
            for (int m = 0; m<popCount.get(pop); m++)
                if (factorialMoment)
                    x *= state.get(pop)-m;
                else
                    x *= state.get(pop);
        
        return x;
    }

    /**
     * Retrieve name of moment group.
     * 
     * @return moment group name
     */
    public String getName() {
        return name;
    }
    
    @Override
    @JsonValue
    public String toString() {

        // Construct reaction string
        StringBuilder sb = new StringBuilder();
        if (name != null)
            sb.append(name).append(": ");
        
        if (!popCount.isEmpty()) {
            boolean first = true;
            for (Population pop : popCount.keySet()) {
                if (!first)
                    sb.append("*");
                else
                    first = false;
                

                sb.append(pop.type.name);
                if (!pop.isScalar()) {
                    sb.append("[");
                    int [] loc = pop.getLocation();
                    for (int i=0; i<loc.length; i++) {
                        if (i>0)
                            sb.append(',');
                        sb.append(loc[i]);
                    }
                    sb.append("]");
                }
                if (popCount.get(pop)>1)
                    sb.append("^").append(popCount.get(pop));
            }
        } else
            sb.append("0");  // This would be weird.
        
        return sb.toString();

    }
}
