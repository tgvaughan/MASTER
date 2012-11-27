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

import hamlet.PopulationState;
import java.util.List;

/**
 * End condition which fires when a single lineage remains.  Graph is
 * not rejected.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class ConditionMRCA implements LineageEndCondition {
    
    @Override
    public boolean isMet(List<Node> activeLineages) {
        return activeLineages.size()==1;
    }

    @Override
    public boolean isRejection() {
        return false;
    }
    
}
