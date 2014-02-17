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

import master.steppers.Stepper;
import beast.core.Input;
import master.model.Model;
import master.model.PopulationState;
import master.model.Reaction;

/**
 * Deterministic rate equation stepper.  Currently uses an implicit Runge-Kutta method.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class RateEquationStepper extends Stepper {
    
    public Input<Double> stepSizeInput = new Input<Double>("stepSize",
            "Length of integration time step.", Input.Validate.REQUIRED);
    
    public Input<Integer> iterationsInput = new Input<Integer>("iterations",
            "Num of iterations used to solve for implicit term. (Default 3.)",
            3);
    
    private double dt;    
    private int maxIter;
    
    public RateEquationStepper() { }
    
    @Override
    public void initAndValidate() {
        dt = stepSizeInput.get();
        maxIter = iterationsInput.get();
    }
    
    /**
     * Construct a tau-leaping integrator
     * 
     * @param integrationTimeStep Size of time step to use in integration
     * algorithm. (Doesn't need to be a multiple of the desired sampling rate.)
     * @param maxIter
     */
    public RateEquationStepper(double integrationTimeStep, int maxIter) {
        this.dt = integrationTimeStep;
        this.maxIter = maxIter;
    }
    
    @Override
    public double step(PopulationState state, Model model, double t, double maxStepSize) {
        
        double thisdt = Math.min(dt, maxStepSize);
            
        PopulationState statePrime = state.getCopy();
        for (int i=0; i<maxIter; i++) {
            for (Reaction reaction : model.getReactions())
                reaction.calcPropensity(statePrime);
            
            if (i>0)
                statePrime = state.getCopy();
            
            for (Reaction reaction : model.getReactions()) {
                statePrime.implementReaction(reaction,
                        reaction.getPropensity()*0.5*thisdt);
            }
        }
        
        for (Reaction reaction : model.getReactions()) {
            reaction.calcPropensity(statePrime);
            state.implementReaction(reaction, reaction.getPropensity()*thisdt);
        }
        
        return thisdt;
    }

    @Override
    public String getAlgorithmName() {
        return "Deterministic Runge-Kutta integrator.";
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
     * Retrieve number of iterations used in the implicit step.
     * 
     * @return maxIter
     */
    public double getMaxIterations() {
        return maxIter;
    }
    
}
