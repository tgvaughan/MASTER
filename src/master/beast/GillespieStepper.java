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
import beast.core.BEASTObject;

/**
 * Object for specifying Gillespie's stochastic simulation algorithm in Hamlet.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Gillespie's original stochastic simulation algorithm.")
public class GillespieStepper extends BEASTObject implements Stepper {
    
    public GillespieStepper() { };
    
    @Override
    public void initAndValidate() { };

    @Override
    public master.Stepper getStepperObject() {
        return new master.GillespieStepper();
    }
    
}
