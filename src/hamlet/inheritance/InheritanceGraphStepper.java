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
package hamlet.inheritance;

import hamlet.Model;
import hamlet.State;
import hamlet.Stepper;

/**
 * A dummy stepper to include in spec for inheritance graphs. (May actually
 * move the graph building code over here though at some point.)
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceGraphStepper extends Stepper {

    @Override
    public double step(State state, Model model, double maxStepSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAlgorithmName() {
        return "Built-in graph stepper.";
    }
    
}
