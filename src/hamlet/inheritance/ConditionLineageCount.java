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

import java.util.List;

/**
 * A condition which is met when the simulation includes a specific
 * number of lineages.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class ConditionLineageCount implements LineageEndCondition {
    
    private int nlineages;
    private boolean rejection;
    
    /**
     * Create an inheritance graph end condition which is met when the
     * given number of lineages is reached.
     * 
     * @param nlineages number of lineages constituting end condition
     * @param rejection true causes graphs meeting condition to be discarded
     */
    public ConditionLineageCount(int nlineages, boolean rejection) {
        this.nlineages = nlineages;
        this.rejection = rejection;
    }

    @Override
    public boolean isRejection() {
        return this.rejection;
    }

    @Override
    public boolean isMet(List<Node> activeLineages) {
        return (activeLineages.size() == nlineages);
    }

    @Override
    public String getConditionDescription() {
        return "Condition met when number of lineages reaches " + nlineages;
    }
    
}
