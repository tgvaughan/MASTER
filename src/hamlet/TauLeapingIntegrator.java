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
 * Implementation of Gillespie's tau-leaping integrator.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TauLeapingIntegrator extends Integrator {
    
    private double dt;
    
    /**
     * Construct a tau-leaping integrator
     * 
     * @param integrationTimeStep Size of time step to use in integration
     * algorithm. (Doesn't need to be a multiple of the desired sampling rate.)
     */
    public TauLeapingIntegrator(double integrationTimeStep) {
        dt = integrationTimeStep;
    }

    /**
     * Generate appropriate random state change according to Gillespie's
     * tau-leaping algorithm.
     *
     * @param state State to modify.
     * @param spec	Simulation spec.
     */
    public void leap(Reaction reaction, State state, Model model, double thisdt) {
        
        for (int i = 0; i<reaction.propensities.size(); i++) {

            // Draw number of reactions to fire within time tau:
            double q = Poisson.nextDouble(reaction.propensities.get(i)*thisdt);

            // Implement reactions:
            for (Population pop : reaction.deltas.get(i).keySet())
                for (int offset : reaction.deltas.get(i).get(pop).keySet())
                    state.addNoNeg(pop, offset,
                            q*reaction.deltas.get(i).get(pop).get(offset));
        }

    }
    
    @Override
    public void step(State state, Model model, double T) {
        
        double t = 0.0;
        while (t<T) {
            
            double thisdt = Math.min(dt, T-t);
            
            // Calculate transition rates based on starting state:
            for (Reaction reaction : model.reactions)
                reaction.calcPropensities(state);

            // Update state according to these rates:
            for (Reaction reaction : model.reactions)
                leap(reaction, state, model, thisdt);
            
            t += dt;
        }

    }

    @Override
    public String getAlgorithmName() {
        return "Gillespie's tau-leaping algorithm";
    }
    
}
