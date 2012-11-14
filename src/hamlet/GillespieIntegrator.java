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

import beast.util.Randomizer;

/**
 * Implementation of Gillespie's foundational SSA.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class GillespieIntegrator extends Integrator {

    @Override
    public void step(State state, Model model, double T) {
        
        double t = 0;
        
        while (true) {
            
            // Calculate propensities
            double totalPropensity = 0.0;
            for (ReactionGroup reaction : model.reactionGroups) {
                reaction.calcPropensities(state);
                for (double propensity : reaction.propensities)
                    totalPropensity += propensity;
            }

            // Draw time of next reaction
            t += Randomizer.nextExponential(totalPropensity);
            
            // Break if new time > tEnd
            if (t>T)
                break;
            
            // Choose reaction to implement
            double u = Randomizer.nextDouble()*totalPropensity;
            boolean found = false;
            ReactionGroup thisReact = null;
            int thisSub = 0;
            for (int ridx=0; ridx<model.reactionGroups.size(); ridx++) {
                thisReact = model.reactionGroups.get(ridx);
                
                for (thisSub=0; thisSub<thisReact.propensities.size(); thisSub++) {
                    u -= thisReact.propensities.get(thisSub);
                    if (u<0) { 
                        found = true;
                        break;
                    }
                }
                if (found)
                    break;
            }
            
            // Implement chosen reaction:
            state.implementReaction(thisReact, thisSub, 1);
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Gillespie's stochastic simulation algorithm";
    }
    
}
