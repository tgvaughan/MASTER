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

import java.util.ArrayList;
import java.util.List;

/**
 * State of lineages in tree.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class LineageState {
    
    double time;
    List<Node> activeNodes;
    
    /**
     * Initialise lineage state at top of tree with chosen root node.
     * 
     * @param rootNode 
     */
    public LineageState(Node rootNode) {
        activeNodes = new ArrayList<Node>();
        activeNodes.add(rootNode);
        time = 0.0;
    }
    
    /**
     * @return true if any lineages remain.
     */
    public boolean isAlive() {
        return !activeNodes.isEmpty();
    }
    /**
     * Alter lineage state according to q occurrences of a particular reaction.
     * 
     * Note that the multiplier is provided here to allow us to construct
     * approximate trees even when a very large number of lineages are involved.
     * 
     * @param reaction
     * @param subReaction
     * @param q Number of times for reaction to fire.
     */
    public void implementReaction(Reaction reaction, int subReaction, double q,
            State state) {
        
        // Choose lineages to involve in reaction(s):
        List<Node> chosenLineages = new ArrayList<Node>();
        PopulationMap<Integer> reactivesRemaining = reaction.reactSubSchemas
                .get(subReaction).copy();
        
        for (Node node : activeNodes) {
            if (!reaction.reactPopSchema.contains(node.population))
                continue;
            
        }
        
    }
}
