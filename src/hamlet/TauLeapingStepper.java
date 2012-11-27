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

import hamlet.math.Poisson;

/**
 * Implementation of Gillespie's tau-leaping stochastic integrator.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TauLeapingStepper extends Stepper {
    
    private double dt;
    
    /**
     * Construct a tau-leaping integrator
     * 
     * @param integrationTimeStep Size of time step to use in integration
     * algorithm. (Doesn't need to be a multiple of the desired sampling rate.)
     */
    public TauLeapingStepper(double integrationTimeStep) {
        dt = integrationTimeStep;
    }

    /**
     * Generate appropriate random state change according to Gillespie's
     * tau-leaping algorithm.
     *
     * @param state PopulationState to modify.
     * @param spec	Simulation spec.
     */
    public void leap(ReactionGroup reaction, PopulationState state, Model model, double thisdt) {
        
        for (int i = 0; i<reaction.propensities.size(); i++) {

            // Draw number of reactions to fire within time tau:
            double q = Poisson.nextDouble(reaction.propensities.get(i)*thisdt);

            // Implement reactions:
            state.implementReaction(reaction, i, q);
        }

    }
    
    @Override
    public double step(PopulationState state, Model model, double maxStepSize) {
        

        double thisdt = Math.min(dt, maxStepSize);
            
        // Calculate transition rates based on starting state:
        for (ReactionGroup reaction : model.reactionGroups)
            reaction.calcPropensities(state);

        // Update state according to these rates:
        for (ReactionGroup reaction : model.reactionGroups)
            leap(reaction, state, model, thisdt);
            
        return thisdt;
    }

    @Override
    public String getAlgorithmName() {
        return "Gillespie's tau-leaping algorithm";
    }
    
    /**
     * Retrieve integration step size.
     * 
     * @return dt
     */
    public double getTimeStepSize() {
        return dt;
    }
    
}
