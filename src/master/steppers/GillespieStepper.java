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
package master.steppers;

import beast.util.Randomizer;
import master.Model;
import master.PopulationState;
import master.Reaction;

/**
 * Implementation of Gillespie's foundational SSA.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class GillespieStepper extends Stepper {
    
    private double eventCount = 0;
    
    @Override
    public double step(PopulationState state, Model model, double maxDt) {
        
        // Calculate propensities
        double totalPropensity = 0.0;
        for (Reaction reaction : model.getReactions()) {
            reaction.calcPropensity(state);
            totalPropensity += reaction.getPropensity();
        }

        // Draw time of next reaction
        double dt;
        if (totalPropensity>0.0)
            dt = Randomizer.nextExponential(totalPropensity);
        else
            dt = Double.POSITIVE_INFINITY;
        
        // Stop here if maxDt exceeded:
        if (dt>maxDt)
            return maxDt;
            
        // Choose reaction to implement
        double u = Randomizer.nextDouble()*totalPropensity;

        Reaction chosenReaction = null;
        for (Reaction reaction : model.getReactions()) {                
            u -= reaction.getPropensity();
            if (u<0) { 
                chosenReaction = reaction;
                break;
            }
        }
            
        // Implement chosen reaction:
        state.implementReaction(chosenReaction, 1);
        
        // Increment event counter:
        eventCount += 1;

        return dt;
    }

    @Override
    public String getAlgorithmName() {
        return "Gillespie's stochastic simulation algorithm";
    }
    
    /**
     * Retrieve total number of events which have been simulated.
     * 
     * @return eventCount
     */
    public double getEventCount() {
        return eventCount;
    }
    
}
