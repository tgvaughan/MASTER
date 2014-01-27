/*
 * Copyright (C) 2014 Tim Vaughan <tgvaughan@gmail.com>
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

package master.model;

import beast.core.BEASTObject;
import beast.core.Input;
import beast.core.Input.Validate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class MomentGroup extends BEASTObject {
    
    public Input<String> nameInput = new Input<String>("momentGroupName",
            "Name of moment group. (Overrides moment name.)", Validate.REQUIRED);
    
    public Input<Boolean> factorialInput = new Input<Boolean>("factorial",
            "True causes factorial moments to be used.  (Overrides moment choice.)",
            false);
    
    public Input<Boolean> sumInput = new Input<Boolean>("sum",
            "Whether moments in group should be added together (Default false.)",
            false);
    
    public Input<List<Moment>> momentsInput = new Input<List<Moment>>(
            "moment", "Individual moments.", new ArrayList<Moment>());

    // Name of moment group - used in output file:
    String name;
    
    // Flag to mark whether these are factorial moments
    boolean factorialMoments;
    
    // Flag to mark whether product moments are to be added together
    boolean sum;
    
    public MomentGroup() { };
    
    @Override
    public void initAndValidate() {
        
        name = nameInput.get();
        factorialMoments = factorialInput.get();
        sum = sumInput.get();
        
        for (Moment moment : momentsInput.get()) {
            moment.factorialMoment = factorialInput.get();
        }
        
    }
    
    /**
     * @return name of moment group
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return true if this is a sum group
     */
    public boolean isSum() {
        return sum;
    }
    
    /**
     * @return true if moments are factorial moments
     */
    public boolean isFactorial() {
        return factorialMoments;
    }
    
    /**
     * Obtain summary of state defined by this moment group and record
     * in provided array.
     * 
     * @param state state to summarize
     * @param summary array in which to store summary
     */
    public void getSummary(PopulationState state, double [] summary) {
        
        if (sum) {
            summary[0] = 0.0;
            for (Moment moment : momentsInput.get()) {
                summary[0] += moment.getSummary(state);
            }
        } else {
            for (int midx=0; midx<momentsInput.get().size(); midx++)
                summary[midx] = momentsInput.get().get(midx).getSummary(state);
        }
    }
    
    /**
     * @return list of moments contained in group.
     */
    public List<Moment> getMoments() {
        return momentsInput.get();
    }
}
