/*
 * Copyright (C) 2014 Tim Vaughan <tgvaughan@gmail.com>
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

package master.conditions;

import beast.core.BEASTObject;
import com.fasterxml.jackson.annotation.JsonValue;
import master.InheritanceTrajectory;
import master.Trajectory;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public abstract class PostSimCondition extends BEASTObject {
    
    /**
     * Returns true if trajectory traj meets post-simulation
     * acceptance condition.
     *
     * @param traj
     * @return true if condition is met
     */
    public abstract boolean accept(Trajectory traj);
    
    /**
     * Returns true if inheritance trajectory itraj meets post-simulation
     * acceptance condition.
     *
     * @param traj
     * @return true if condition is met
     */
    public abstract boolean accept(InheritanceTrajectory traj);
    
    /**
     * @return String description of condition.
     */
    @JsonValue
    public abstract String getConditionDescription();
    
    @Override
    public String toString() {
        return getConditionDescription();
    }
}
