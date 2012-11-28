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

import hamlet.Population;
import java.util.List;

/**
 * A condition which is met when the simulation includes a specific
 * number of lineages.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class LineageEndCondition {
    
    private Population pop;    
    private int nlineages;
    private boolean rejection;
    
    /**
     * Create an inheritance graph end condition which is met when the
     * given number of lineages is reached.
     * 
     * @param nlineages number of lineages constituting end condition
     * @param rejection true causes graphs meeting condition to be discarded
     */
    public LineageEndCondition(int nlineages, boolean rejection) {
        this.pop = null;
        this.nlineages = nlineages;
        this.rejection = rejection;
    }
    
    /**
     * Create an inheritance graph end condition which is met when the
     * number of lineages matching the given population equals nlineages.
     * 
     * @param pop
     * @param nlineages
     * @param rejection 
     */
    public LineageEndCondition(Population pop, int nlineages, boolean rejection) {
        this.pop = pop;
        this.nlineages = nlineages;
        this.rejection = rejection;
    }

    /**
     * @return true if this end condition is a rejection.
     */
    public boolean isRejection() {
        return this.rejection;
    }

    /**
     * Returns true iff the given activeLineages list meets the end condition.
     * 
     * @param activeLineages
     * @return true if the end condition is met.
     */
    public boolean isMet(List<Node> activeLineages) {
        int size;
        if (pop == null)
            size = activeLineages.size();
        else {
            size = 0;
            for (Node node : activeLineages) {
                if (node.population == pop)
                    size += 1;
            }            
        }
        
        return size == nlineages;
    }

    /**
     * @return Description of the end condition.
     */
    public String getConditionDescription() {
        return "Condition met when number of lineages reaches " + nlineages;
    }
    
}
