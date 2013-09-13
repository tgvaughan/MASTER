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
package master.beast;

import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.BEASTObject;

/**
 * Object for specifying deterministic integration of the rate equations.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Deterministic rate equation integrator.")
public class RateEquationStepper extends BEASTObject implements Stepper {
    
    public Input<Double> stepSizeInput = new Input<Double>("stepSize",
            "Length of integration time step.", Validate.REQUIRED);
    
    public Input<Integer> iterationsInput = new Input<Integer>("iterations",
            "Num of iterations used to solve for implicit term. (Default 3.)",
            3);

    double stepSize;
    int maxIter;
    
    public RateEquationStepper() { }
    
    @Override
    public void initAndValidate() {
        stepSize = stepSizeInput.get();
        maxIter = iterationsInput.get();
    }
    
    @Override
    public master.Stepper getStepperObject() {
        return new master.RateEquationStepper(stepSize, maxIter);
    }
    
}
