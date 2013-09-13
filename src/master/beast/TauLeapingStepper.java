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
 * Plugin for specifying use of the tau-leaping algorithm.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("The original tau-leaping stochastic integration algorithm.")
public class TauLeapingStepper extends BEASTObject implements Stepper {
    
    public Input<Double> stepSizeInput = new Input<Double>("stepSize",
            "Length of integration time step.", Validate.REQUIRED);

    double stepSize;
    
    public TauLeapingStepper() { }
    
    @Override
    public void initAndValidate() {
        stepSize = stepSizeInput.get();
    }
    
    @Override
    public master.Stepper getStepperObject() {
        return new master.TauLeapingStepper(stepSize);
    }
    
}
