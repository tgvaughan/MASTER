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
package master.beast;

import beast.core.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Vaughan
 */
@Description("Specification of a summary statistic to calculate.")
public class Moment extends Plugin {
    
    public Input<String> nameInput = new Input<String>("momentName",
            "Name of moment. (Overridden by moment group name.)");
    
    public Input<Boolean> factorialInput = new Input<Boolean>("factorial",
            "True causes factorial moments to be used.  (Overridden by moment group choice.)",
            false);
    
    public Input<List<Population>> factorsInput = new Input<List<Population>>(
            "factor",
            "Population whose size will be factored into moment calculation.",
            new ArrayList<Population>());

    master.Population [] factors;
    String name;
    boolean factorial;

    public Moment() { };

    @Override
    public void initAndValidate() throws Exception {
        int nFactors = factorsInput.get().size();
        factors = new master.Population[nFactors];
        
        for (int i=0; i<nFactors; i++)
            factors[i] = factorsInput.get().get(i).pop;
        
        name = nameInput.get();        
        factorial = factorialInput.get();
    }
}
