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

package master.model.iterators;

import beast.core.Description;
import beast.core.Input;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Facilitates iteration over ranges of iteration variables.")
public class Iteration extends AbstractIteration {
        
    public Input<List<Range>> rangesInput = new Input<>("range",
            "Define multiple reactions for different values of a variable.",
            new ArrayList<>());
    
    private List<Range> ranges;
    private List<String> variableNames;
    private List<Integer> rangeFromValues, rangeToValues;
    private List<int[]> variableValuesList;
    
    public Iteration() {  }
    
    @Override
    public void initAndValidate() throws Exception {
        ranges = rangesInput.get();
        variableNames = new ArrayList<>();
        rangeFromValues = new ArrayList<>();
        rangeToValues = new ArrayList<>();
        variableValuesList = new ArrayList<>();
        
        for (Range range : ranges)
            variableNames.add(range.getVariableName());

        for (Range range : ranges) {
            String fromStr = range.fromInput.get();
            if(variableNames.contains(fromStr))
                rangeFromValues.add(-(variableNames.indexOf(fromStr)+1));
            else
                rangeFromValues.add(Integer.parseInt(fromStr));

            String toStr = range.toInput.get();
            if (variableNames.contains(toStr))
                rangeToValues.add(-(variableNames.indexOf(toStr)+1));
            else
                rangeToValues.add(Integer.parseInt(toStr));
        }
        
        int[] indices = new int[ranges.size()];
        

        rangeLoop(0, indices);
    }
    
    /**
     * Recursion used to loop over range variables and assemble reaction
     * for each combination.
     * 
     * @param depth current recursion depth
     * @param indices list of range variable values 
     */
    private void rangeLoop(int depth, int [] indices) {
        

        if (depth==indices.length) {
            variableValuesList.add(Arrays.copyOf(indices, indices.length));

        } else {
            int from;
            if (rangeFromValues.get(depth)<0)
                from = indices[-rangeFromValues.get(depth)-1];
            else
                from = rangeFromValues.get(depth);

            int to;
            if (rangeToValues.get(depth)<0)
                to = indices[-rangeToValues.get(depth)-1];
            else
                to = rangeToValues.get(depth);
            
            for (int i=from; i<=to; i++) {
                indices[depth] = i;
                rangeLoop(depth+1, indices);
            }

        }
    }

    @Override
    public List<int[]> getVariableValuesList() {
        return variableValuesList;
    }

    @Override
    public String[] getVariableNames() {
        return variableNames.toArray(new String[variableNames.size()]);
    }
    
}
