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

import beast.core.Description;
import beast.core.Input;
import com.google.common.collect.Multiset;
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
@Description("Leaf count end condition for an inheritance trajectory.")
public class LeafCountEndCondition extends EndCondition {
    
    public Input<Integer> nLeavesInput = new Input<Integer>(
            "nLeaves",
            "Leaf count threshold for end condition.",
            Input.Validate.REQUIRED);
    
    public Input<Boolean> includeExtantInput = new Input<Boolean>(
            "includeExtant",
            "Whether to include extant lineages in terminal node count. (Default false.)",
            false);
    
    public Input<Boolean> exactMatchInput = new Input<Boolean>(
            "exactMatch",
            "If true, the number of lineages must exactly match "
                    + "the value of nLineages.",
            true);
    
    public Input<Boolean> exceedCondInput = new Input<Boolean>(
            "exceedCondition",
            "If exactMatchRequired is false, determines whether condition "
                    + "is size>=threshold. False implies <=threshold.",
            true);
    
    private int nTerminalNodes;
    private boolean includeExtant, exact, exceed;

    public LeafCountEndCondition() { }
    
    @Override
    public void initAndValidate() {
        
        nTerminalNodes = nLeavesInput.get();
        includeExtant = includeExtantInput.get();
        exact = exactMatchInput.get();
        exceed = exceedCondInput.get();
    }
    
    /**
     * Returns true iff the given leafCounts and activeLineages
     * meet the end condition.
     * 
     * @param leafCounts Multiset containing all leaf populations so far
     * @param activeLineages
     * @return true if the end condition is met.
     */
    public boolean isMet(Multiset<Population> leafCounts,
            Map<Population,List<Node>> activeLineages) {

        if (isPost())
            return false;
        
        int size;
        if (populationInput.get().isEmpty())
            size = leafCounts.size();
        else {
            size = 0;
            for (Population pop : populationInput.get())
                size += leafCounts.count(pop);
        }
        if (includeExtant) {
            for (List<Node> nodeList : activeLineages.values())
                size += nodeList.size();
        }
        
        if (exact)
            return size == nTerminalNodes;
        
        if (exceed)
            return size >= nTerminalNodes;
        else
            return size <= nTerminalNodes;
    }
    
    /**
     * Returns true iff the given leafCounts and activeLineages
     * meet the end condition.
     * 
     * @param leafCounts Multiset containing all leaf populations
     * @return true if the end condition is met.
     */
    public boolean isMetPost(Multiset<Population> leafCounts) {

        if (!isPost())
            return false;
        
        int size;
        if (populationInput.get().isEmpty())
            size = leafCounts.size();
        else {
            size = 0;
            for (Population pop : populationInput.get())
                size += leafCounts.count(pop);
        }
        
        if (exact)
            return size == nTerminalNodes;
        
        if (exceed)
            return size >= nTerminalNodes;
        else
            return size <= nTerminalNodes;
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
