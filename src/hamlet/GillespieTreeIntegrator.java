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
 * Integrator which uses Gillespie's SSA as the basis for tree generation.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class GillespieTreeIntegrator extends TreeIntegrator {
    
    @Override
    public void stepTree(Model model, State state,
            LineageState lineageState, double maxT) {
        
        // Calculate propensities
        double totalPropensity = 0.0;
        for (Reaction reaction : model.reactions) {
            reaction.calcPropensities(state);
            for (double propensity : reaction.propensities)
                totalPropensity += propensity;
        }
            
        // Draw time of next reaction
        lineageState.time += Randomizer.nextExponential(totalPropensity);
            
        // Stop here if new time exceeds maximum time.
        if (lineageState.time>maxT) {
            lineageState.time = maxT;
            return;
        }
        
        // Choose specific reaction to implement
        double u = Randomizer.nextDouble()*totalPropensity;
        boolean found = false;
        Reaction thisReact = null;
        int thisSub = 0;
        for (int ridx=0; ridx<model.reactions.size(); ridx++) {
            thisReact = model.reactions.get(ridx);
            
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
        
        // Modify lineage state according to chosen reaction:        
        
        // Alter system state according to chosen reaction
        state.implementReaction(thisReact, thisSub, 1);
    }
    
}
