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

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import master.math.Poisson;

/**
 * Implementation of Sehl et al.'s "step anticipation" tau-leaping
 * stochastic integrator.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class SALStepper extends Stepper {
    
    private double dt;    
    private double eventCount = 0;
    
    private HashMap<ReactionGroup, List<Double>> corrections;
    private HashMap<Population, Double> derivs;
    
    /**
     * Construct a tau-leaping integrator
     * 
     * @param integrationTimeStep Size of time step to use in integration
     * algorithm. (Doesn't need to be a multiple of the desired sampling rate.)
     */
    public SALStepper(double integrationTimeStep) {
        dt = integrationTimeStep;
        corrections = Maps.newHashMap();
        derivs = Maps.newHashMap();
        
    }

    /**
     * Generate appropriate random state change according to Sehl's
     * step anticipation tau-leaping algorithm.
     *
     * @param state PopulationState to modify.
     * @param spec Simulation spec.
     */
    public void leap(ReactionGroup reaction, PopulationState state, Model model, double thisdt) {
        
        for (int i = 0; i<reaction.propensities.size(); i++) {
            
            // Calculate corrected rate
            double rho = reaction.propensities.get(i)*thisdt
                    + 0.5*corrections.get(reaction).get(i)*thisdt*thisdt;

            // Draw number of reactions to fire within time tau:
            double q = Poisson.nextDouble(rho);

            // Implement reactions:
            state.implementReaction(reaction, i, q);
            
            // Increment event counter:
            eventCount += q;
        }

    }
    
    @Override
    public double step(PopulationState state, Model model, double maxStepSize) {

        double thisdt = Math.min(dt, maxStepSize);
            
        // Calculate propensities based on starting state:
        for (ReactionGroup reactionGroup : model.reactionGroups)
            reactionGroup.calcPropensities(state);
        
        // Estimate second order corrections:
        calcCorrections(model, state);
        
        // Update state according to these rates:
        for (ReactionGroup reaction : model.reactionGroups)
            leap(reaction, state, model, thisdt);
            
        return thisdt;
    }
    
    /**
     * Calculate second order corrections to Poisson process rates.
     * 
     * @param model
     * @param state 
     */
    private void calcCorrections(Model model, PopulationState state) {

        // Time derivatives of rate equations:
        derivs.clear();
        for (ReactionGroup reactionGroup : model.getReactionGroups()) {
            for (int i=0; i<reactionGroup.nReactions; i++) {
                for (Population pop : reactionGroup.deltaCounts.get(i).keySet()) {
                    double old = 0;
                    if (derivs.containsKey(pop))
                        old = derivs.get(pop);
                    derivs.put(pop, old
                            + reactionGroup.propensities.get(i)
                            *reactionGroup.deltaCounts.get(i).get(pop));
                }
            }
        }
        
        // Ensure map is initialised
        if (corrections.isEmpty()) {
            for (ReactionGroup reactionGroup : model.reactionGroups) {
                ArrayList<Double> list =  new ArrayList<Double>();
                for (int i=0; i<reactionGroup.nReactions; i++)
                    list.add(0.0);
                corrections.put(reactionGroup, list);
            }
        }
        
        // Calculate propensity corrections:
        for (ReactionGroup reactionGroup : model.getReactionGroups()) {
            for (int i=0; i<reactionGroup.nReactions; i++) {
                
                // Ensure correction is initially zero:
                corrections.get(reactionGroup).set(i, 0.0);
                
                // Abort if propensity zero:
                if (!(reactionGroup.propensities.get(i)>0.0))
                    continue;
                
                double acc = 0.0;
                for (Population pop : reactionGroup.reactCounts.get(i).keySet()) {
                    for (int m=0; m<reactionGroup.reactCounts.get(i).get(pop); m++) {
                        acc += (1.0-m)/(state.get(pop)-m)*derivs.get(pop);
                    }
                }
                acc *= reactionGroup.propensities.get(i);                
                corrections.get(reactionGroup).set(i, acc);
            }
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Fixed time-step SAL algorithm";
    }
    
    /**
     * Retrieve integration step size.
     * 
     * @return dt
     */
    public double getTimeStepSize() {
        return dt;
    }
    
    /**
     * Retrieve number of events calculated.
     * 
     * @return eventcount
     */
    public double getEventCount() {
        return eventCount;
    }
    
}
