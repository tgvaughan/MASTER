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

import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.Plugin;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Population size end condition for a stochastic trajectory.")
public class PopulationEndCondition extends Plugin {
    
    public Input<List<Population>> populationInput = new Input<List<Population>>(
            "population",
            "Population whose size to base end condition on.",
            new ArrayList<Population>());
    
    public Input<Double> thresholdInput = new Input<Double>(
            "threshold",
            "Population size threshold at which condition is met.",
            Validate.REQUIRED);
    
    public Input<Boolean> exceedCondInput = new Input<Boolean>(
            "exceedCondition",
            "Whether condition is size>=threshold. False implies <=threshold.",
            Validate.REQUIRED);
    
    public Input<Boolean> rejectionInput = new Input<Boolean>(
            "isRejection",
            "Whether condition causes trajectory rejection. (Default false.)",
            false);
    
    public master.PopulationEndCondition endConditionObject;
    
    public PopulationEndCondition() { }
    
    @Override
    public void initAndValidate() {
        int nPops = populationInput.get().size();
        
        if (nPops==0)
            throw new IllegalArgumentException("Need at least one population "
                    + "for population end condition.");
        

        master.Population[] pops = new master.Population[nPops];
        for (int i=0; i< nPops; i++)
            pops[i] = populationInput.get().get(i).pop;
        
        endConditionObject = new master.PopulationEndCondition(
                thresholdInput.get(),
                exceedCondInput.get(),
                rejectionInput.get(),
                pops);
    }
}
