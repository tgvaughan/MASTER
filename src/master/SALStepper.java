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

import beast.core.Input;
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
    
    public Input<Double> stepSizeInput = new Input<Double>("stepSize",
            "Length of integration time step.", Input.Validate.REQUIRED);

    
    private double dt;    
    private double eventCount = 0;
    
    private HashMap<NewReaction, Double> corrections;
    private HashMap<Population, Double> derivs;
    
    public SALStepper() { }
    
    @Override
    public void initAndValidate() {
        dt = stepSizeInput.get();
    }
    
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
     * @param reaction
     * @param state PopulationState to modify.
     * @param model
     * @param thisdt
     */
    public void leap(NewReaction reaction, PopulationState state, Model model, double thisdt) {
        
        // Calculate corrected rate
        double rho = reaction.propensity*thisdt
                + 0.5*corrections.get(reaction)*thisdt*thisdt;

        // Draw number of reactions to fire within time tau:
        double q = Randomizer.nextPoisson(rho);

        // Implement reactions:
        state.implementReaction(reaction, q);
        
        // Increment event counter:
        eventCount += q;

    }
    
    @Override
    public double step(PopulationState state, Model model, double maxStepSize) {

        double thisdt = Math.min(dt, maxStepSize);
            
        // Calculate propensities based on starting state:
        for (NewReaction reaction : model.reactions)
            reaction.calcPropensity(state);
        
        // Estimate second order corrections:
        calcCorrections(model, state);
        
        // Update state according to these rates:
        for (NewReaction reaction : model.reactions)
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
        for (NewReaction reaction : model.getReactions()) {
            for (Population pop : reaction.deltaCount.keySet()) {
                double old = 0;
                if (derivs.containsKey(pop))
                    old = derivs.get(pop);
                derivs.put(pop, old
                        + reaction.propensity
                                *reaction.deltaCount.get(pop));
            }
        }
        
        // Ensure that corrections map is initialised
        if (corrections.isEmpty()) {
            for (NewReaction reaction : model.reactions)
                corrections.put(reaction, 0.0);
        }
        
        // Incoporate propensity derivatives:
        for (NewReaction reaction : model.reactions) {
            double thisCorr = 0.0;
            for (Population pop : reaction.reactCount.keySet()) {
                if (derivs.containsKey(pop))
                    thisCorr += propensityDeriv(state, reaction, pop)*derivs.get(pop);
            }
            corrections.put(reaction, thisCorr);
        }
    }
    
    /**
     * Calculates the derivative with respect to a particular state element
     * of the propensity of a particular reaction.
     * 
     * @param state State at which the derivative is to be evaluated
     * @param reaction Reaction group whose propensity to differentiate
     * @param r Member of reaction group
     * @param pop Population dimension to take derivative in
     * @return Derivative
     */
    private double propensityDeriv(PopulationState state,
            NewReaction reaction, Population pop) {
        
        // Can stop here if reaction doesn't involve population:
        if (!reaction.reactCount.containsKey(pop))
            return 0.0;
        
        // Short-cut if propensity is non-zero:
        if (reaction.propensity>0.0) {
            double sum = 0.0;
            for (int m=0; m<reaction.reactCount.get(pop); m++)
                sum += 1.0/(state.get(pop)-m);
            return sum*reaction.propensity;
        }
        
        // Initialise accumulator:
        double acc = reaction.rate;
        
        // TODO: This calculation involves a lot of loops - can we optimize?
        for (Population popPrime : reaction.reactCount.keySet()) {
            if (popPrime != pop) {
                
                for (int m=0; m<reaction.reactCount.get(popPrime); m++)
                    acc *= state.get(popPrime)-m;
                
            } else {
                double sum = 0.0;
                
                for (int m=0; m<reaction.reactCount.get(pop); m++) {
                    double factor = 1.0;
                    for (int mp=0; mp<reaction.reactCount.get(pop); mp++) {
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
