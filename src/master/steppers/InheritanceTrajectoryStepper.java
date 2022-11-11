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

import master.model.Model;
import master.model.PopulationState;

/**
 * A dummy stepper to include in spec for inheritance graphs. (May actually
 * move the graph building code over here though at some point.)
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceTrajectoryStepper extends Stepper {
    
    private double eventCount = 0;

    @Override
    public double step(PopulationState state, Model model, boolean calcLogP,
            double t, double maxStepSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAlgorithmName() {
        return "Built-in graph stepper.";
    }
    
    /**
     * Retrieve total number of events computed.
     * 
     * @return eventCount;
     */
    public double getEventCount() {
        return eventCount;
    }
    
    /**
     * Increment counter of total number of events computed during simulation\
     * for inclusion in output files.
     */
    public void incrementEventCount() {
        eventCount += 1;
    }
}
