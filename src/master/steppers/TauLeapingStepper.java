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

import beast.core.Input;
import beast.util.Randomizer;
import master.model.Model;
import master.model.PopulationState;
import master.model.Reaction;

/**
 * Implementation of Gillespie's tau-leaping stochastic integrator.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TauLeapingStepper extends Stepper {
    
    public Input<Double> stepSizeInput = new Input<Double>("stepSize",
            "Length of integration time step.", Input.Validate.REQUIRED);
    
    private double dt;
    
    private double eventCount = 0;
    
    /**
     * Default constructor (required by BEAST).
     */
    public TauLeapingStepper() { }
    
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
    public TauLeapingStepper(double integrationTimeStep) {
        dt = integrationTimeStep;
    }

    /**
     * Generate appropriate random state change according to Gillespie's
     * tau-leaping algorithm.
     *
     * @param reaction
     * @param state PopulationState to modify.
     * @param model
     * @param thisdt
     */
    public void leap(Reaction reaction, PopulationState state, Model model, double thisdt) {
        
        // Draw number of reactions to fire within time tau:
        double q = Randomizer.nextPoisson(reaction.getPropensity()*thisdt);

        // Implement reactions:
        state.implementReaction(reaction, q);
            
        // Increment event counter:
        eventCount += q;
    }
    
    @Override
    public double step(PopulationState state, Model model, boolean calcLogP,
            double t, double maxStepSize) {
        
        double tend = t + Math.min(maxStepSize, dt);
        double tprime = t;

        do {
            
            double nextChangeTime = model.getNextReactionChangeTime(tprime);
            double smallerdt = Math.min(tend, nextChangeTime)-tprime;
        
            // Calculate transition rates based on starting state:
            for (Reaction reaction : model.getReactions())
                reaction.calcPropensity(state, tprime);
            
            // Update state according to these rates:
            for (Reaction reaction : model.getReactions())
                leap(reaction, state, model, smallerdt);
          
            tprime += smallerdt;
            
        } while (tprime<tend);
        
        return tend-t;
    }

    @Override
    public String getAlgorithmName() {
        return "Fixed time-step tau-leaping algorithm";
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
