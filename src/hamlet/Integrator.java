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
package hamlet;

/**
 * Abstract base class for integration algorithms.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public abstract class Integrator {

    /**
     * Use the stochastic integration algorithm to ove the state an
     * amount T forward in time under the given model.
     * 
     * @param state
     * @param model
     * @param T

     */
    public abstract void step (State state, Model model, double T);
    
    /**
     * Retrieve descriptive name of this integrator as a string.
     * 
     * @return integrator name
     */
    public abstract String getAlgorithmName();
    
    @Override
    public String toString() {
        return getAlgorithmName();
    }
    
}
