/*
 * Copyright (C) 2013 Tim Vaughan <tgvaughan@gmail.com>
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
package master.endconditions;

import beast.core.BEASTObject;
import java.util.List;
import java.util.Map;
import master.model.Node;
import master.model.Population;

/**
 * A condition which is met when the simulation has generated a given
 * number of terminal nodes.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class LeafCountEndCondition extends BEASTObject {
    
    private int nTerminalNodes;
    private boolean includeExtant;
    private boolean rejection;

    /**
     * Create an inheritance graph end condition which is met when
     * the given number of terminal nodes is reached.
     * 
     * @param nTerminalNodes number of terminal nodes constituting end condition
     * @param rejection true causes graphs meeting condition to be discarded
     */
    public LeafCountEndCondition(int nTerminalNodes,
            boolean includeExtant, boolean rejection) {
        this.nTerminalNodes = nTerminalNodes;
        this.includeExtant = includeExtant;
        this.rejection = rejection;
    }
    
    /**
     * @return true if this end condition is a rejection.
     */
    public boolean isRejection() {
        return this.rejection;
    }
    
    /**
     * Returns true iff the given nTerminalsSoFar and activeLineages
     * meets the end condition.
     * 
     * @param nTerminalsSoFar
     * @param activeLineages
     * @return true if the end condition is met.
     */
    public boolean isMet(int nTerminalsSoFar,
            Map<Population,List<Node>> activeLineages) {

        int size = nTerminalsSoFar;
        if (includeExtant) {
            for (List<Node> nodeList : activeLineages.values())
                size += nodeList.size();
        }
        
        return size == nTerminalNodes;
    }
    
    /**
     * @return Description of the end condition.
     */
    public String getConditionDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Condition met when number of terminal nodes");
        if (includeExtant)
            sb.append(" (including extant lineages)");
        sb.append(" reaches ").append(nTerminalNodes);
        
        return sb.toString();
    }
}
