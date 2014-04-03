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
package master.conditions;

import master.model.Population;
import master.model.PopulationState;
import beast.core.Input;
import java.util.ArrayList;
import java.util.List;

/**
 * Population end condition which is met when (depending on the construction)
 * a population exceeds or dips below some threshold threshold.
 * May be either a rejection or a truncation condition.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationEndCondition extends EndCondition {
    
    public Input<Double> thresholdInput = new Input<Double>(
            "threshold",
            "Population size threshold at which condition is met.",
            Input.Validate.REQUIRED);
    
    public Input<Boolean> exceedCondInput = new Input<Boolean>(
            "exceedCondition",
            "Whether condition is size>=threshold. False implies <=threshold.",
            true);
    
    double threshold;
    boolean exceed;
    
    public PopulationEndCondition() { }
    
    @Override
    public void initAndValidate() {
        threshold = thresholdInput.get();
        exceed = exceedCondInput.get();
    }
    
    public boolean isMet(PopulationState currentState) {

        double size = 0;
        if (populationInput.get().isEmpty()) {
            for (Population pop : currentState.getPopSet())
                size += currentState.get(pop);
        } else {
            for (Population pop : populationInput.get())
                size += currentState.get(pop);
        }
        
        if (exceed)
            return size >= threshold;
        else
            return size <= threshold;
    }
    

    public String getConditionDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Condition is met when ");
        if (populationInput.get().isEmpty())
            sb.append("total population size");
        else {
            for (int i=0; i<populationInput.get().size(); i++) {
                if (i>0)
                    sb.append(" + ");
                sb.append(populationInput.get().get(i));
            }
        }

        if (exceed)
            sb.append(" >= ");
        else
            sb.append(" <= ");
        
        sb.append(threshold);
        
        return sb.toString();
    }
    
}
