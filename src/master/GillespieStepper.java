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

import beast.util.Randomizer;

/**
 * Implementation of Gillespie's foundational SSA.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class GillespieStepper extends Stepper {

    @Override
    public double step(PopulationState state, Model model, double maxDt) {
        
        // Calculate propensities
        double totalPropensity = 0.0;
        for (ReactionGroup reactionGroup : model.reactionGroups) {
            reactionGroup.calcPropensities(state);
            for (double propensity : reactionGroup.propensities)
                totalPropensity += propensity;
        }

        // Draw time of next reaction
        double dt = Randomizer.nextExponential(totalPropensity);
        
        // Stop here if maxDt exceeded:
        if (dt>maxDt)
            return maxDt;
            
        // Choose reaction to implement
        double u = Randomizer.nextDouble()*totalPropensity;
        boolean found = false;
        ReactionGroup chosenReactionGroup = null;
        int chosenReaction = 0;
        for (ReactionGroup reactionGroup : model.reactionGroups) {
                
            for (int reaction=0; reaction<reactionGroup.propensities.size(); reaction++) {
                u -= reactionGroup.propensities.get(reaction);
                if (u<0) { 
                    found = true;
                    chosenReactionGroup = reactionGroup;
                    chosenReaction = reaction;
                    break;
                }
            }
            if (found)
                break;
        }
            
        // Implement chosen reaction:
        state.implementReaction(chosenReactionGroup, chosenReaction, 1);

        return dt;
    }

    @Override
    public String getAlgorithmName() {
        return "Gillespie's stochastic simulation algorithm";
    }
    
}
