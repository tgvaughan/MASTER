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
 * End condition for trajectories.  End conditions may result in either
 * truncation or complete rejection of a trajectory.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public interface EndCondition {
    
    /**
     * @param currentState
     * @return true if end condition is met.
     */
    public boolean isMet(State currentState);
}
