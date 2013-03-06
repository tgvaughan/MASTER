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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;

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
    public void leap(ReactionGroup reactionGroup, PopulationState state, Model model, double thisdt) {
        
        for (int i = 0; i<reactionGroup.propensities.size(); i++) {
            
            // Calculate corrected rate
            double rho = reactionGroup.propensities.get(i)*thisdt
                    + 0.5*corrections.get(reactionGroup).get(i)*thisdt*thisdt;

            // Draw number of reactions to fire within time tau:
            double q = Randomizer.nextPoisson(rho);

            // Implement reactions:
            state.implementReaction(reactionGroup, i, q);
            
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
        
        // Ensure that corrections map is initialised
        if (corrections.isEmpty()) {
            for (ReactionGroup reactionGroup : model.reactionGroups) {
                List<Double> corrList = Lists.newArrayList();
                for (int reaction=0; reaction<reactionGroup.nReactions; reaction++)
                    corrList.add(0.0);
                corrections.put(reactionGroup, corrList);
            }
        }
        
        // Incoporate propensity derivatives:
        for (ReactionGroup reactionGroup : model.reactionGroups) {
            List<Double> corrList = corrections.get(reactionGroup);
            for (int reaction=0; reaction<reactionGroup.nReactions; reaction++) {
                double thisCorr = 0.0;
                for (Population pop : reactionGroup.reactCounts.get(reaction).keySet()) {
                    if (derivs.containsKey(pop))
                        thisCorr += propensityDeriv(state, reactionGroup, reaction, pop)*derivs.get(pop);
                }
                corrList.set(reaction, thisCorr);
            }
        }
        
    }
    
    /**
     * Calculates the derivative with respect to a particular state element
     * of the propensity of a particular reaction.
     * 
     * @param state State at which the derivative is to be evaluated
     * @param reactionGroup Reaction group whose propensity to differentiate
     * @param reaction Member of reaction group
     * @param pop Population dimension to take derivative in
     * @return Derivative
     */
    private double propensityDeriv(PopulationState state,
            ReactionGroup reactionGroup, int reaction,
            Population pop) {
        
        // Can stop here if reaction doesn't involve population:
        if (!reactionGroup.reactCounts.get(reaction).containsKey(pop))
            return 0.0;
        
        // Short-cut if propensity is non-zero:
        if (reactionGroup.propensities.get(reaction)>0.0) {
            double sum = 0.0;
            for (int m=0; m<reactionGroup.reactCounts.get(reaction).get(pop); m++)
                sum += 1.0/(state.get(pop)-m);
            return sum*reactionGroup.propensities.get(reaction);
        }
        
        // Initialise accumulator:
        double acc = reactionGroup.rates.get(reaction);
        
        // TODO: This calculation involves a lot of loops - can we optimize?
        for (Population popPrime : reactionGroup.reactCounts.get(reaction).keySet()) {
            if (popPrime != pop) {
                
                for (int m=0; m<reactionGroup.reactCounts.get(reaction).get(popPrime); m++)
                    acc *= state.get(popPrime)-m;
                
            } else {
                double sum = 0.0;

                for (int m=0; m<reactionGroup.reactCounts.get(reaction).get(pop); m++) {
                    double factor = 1.0;
                    for (int mp=0; mp<reactionGroup.reactCounts.get(reaction).get(pop); mp++) {
                        if (mp != m)
                            factor *= state.get(pop)-mp;
                        // else factor *= 1.0
                    }
                    sum += factor;
                }
                
                acc *= sum;
            }
        }
        
        return acc;
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
