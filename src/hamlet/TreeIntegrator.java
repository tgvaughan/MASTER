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
 * Abstract base class for tree-specific integrators.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public abstract class TreeIntegrator {
    
    /**
     * Integrate system and lineage states until the lineage state changes or
     * the time maxT is exceeded.
     * 
     * @param model Stochastic population dynamics model
     * @param state State of system
     * @param lineageState state of lineages existing at a particular time
     * @param maxT maximum length of step
     */
    public abstract void stepTree(Model model, State state,
            LineageState lineageState, double maxT);
    
}
