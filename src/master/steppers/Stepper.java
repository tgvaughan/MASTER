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

import beast.core.BEASTObject;
import master.model.Model;
import master.model.PopulationState;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Abstract base class for algorithms which increment the system state
 * under the chosen reaction scheme.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public abstract class Stepper extends BEASTObject {
    
    public Stepper() { }
    
    @Override
    public void initAndValidate() { }

    /**
     * Use the stochastic integration algorithm to increment the state
     * forward in time under the given model.
     * 
     * @param state
     * @param model
     * @param t
     * @param maxStepSize 
     * @return Length of time increment.
     */
    public abstract double step (PopulationState state, Model model,
            double t, double maxStepSize);
    
    /**
     * Retrieve descriptive name of this integrator as a string.
     * 
     * @return integrator name
     */
    public abstract String getAlgorithmName();
    
    @Override
    @JsonValue
    public String toString() {
        return getAlgorithmName();
    }
}
