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
package master.conditions;

import beast.core.Description;
import beast.core.Input;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.ArrayList;
import java.util.List;
import master.InheritanceTrajectory;
import master.Trajectory;
import master.model.Node;
import master.model.Population;

/**
 * A condition which is met when the simulation has generated a given
 * number of terminal nodes.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Leaf count post-simulation condition for an inheritance trajectory.")
public class LeafCountPostSimCondition extends PostSimCondition {
    
    public Input<List<Population>> populationInput = new Input<List<Population>>(
            "population",
            "Specific population to which end condition applies (Optional).",
            new ArrayList<Population>());
    
    public Input<Integer> nLeavesInput = new Input<Integer>(
            "nLeaves",
            "Leaf count threshold for end condition.",
            Input.Validate.REQUIRED);
    
    public Input<Boolean> includeExtantInput = new Input<Boolean>(
            "includeExtant",
            "Whether to include extant lineages in terminal node count. (Default false.)",
            false);
    
    public Input<Boolean> exactMatchInput = new Input<Boolean>(
            "exact",
            "If true, the number of lineages must exactly match "
                    + "the value of nLineages.",
            false);
    
    public Input<Boolean> exceedCondInput = new Input<Boolean>(
            "exceedCondition",
            "If exactMatchRequired is false, determines whether condition "
                    + "is size>=threshold. False implies <=threshold.",
            true);
    
    private int nTerminalNodes;
    private boolean exact, exceed;

    public LeafCountPostSimCondition() { }
    
    @Override
    public void initAndValidate() {
        
        nTerminalNodes = nLeavesInput.get();
        exact = exactMatchInput.get();
        exceed = exceedCondInput.get();
    }
    
    /**
     * Returns true iff the given inheritance trajectory
     * meet the post-simulation acceptance condition.
     * 
     * @param itraj inheritance trajectory
     * @return true if the end condition is met.
     */
    @Override
    public boolean accept(InheritanceTrajectory itraj) {
        
        // Assemble leaf counts:
        Multiset<Population> leafCounts = HashMultiset.create();
        for (Node leaf : itraj.getEndNodes())
            leafCounts.add(leaf.getPopulation());
        
        // Check whether condition is met:
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
    
    
    @Override
    public boolean accept(Trajectory traj) {
        return false;
    }
 
    /**
     * @return Description of the end condition.
     */
    @Override
    public String getConditionDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Condition met when final number of terminal nodes");
        if (!populationInput.get().isEmpty()) {
            sb.append(" with population ");
            for (int i=0; i<populationInput.get().size(); i++) {
                if (i>0) {
                    if (i==populationInput.get().size()-1)
                        sb.append(" or ");
                    else
                        sb.append(", ");
                }
            }
        }
        if (exact)
            sb.append(" equals ");
        else {
            if (exceed)
                sb.append(" is greater or equal to ");
            else
                sb.append(" is less than or equal to ");
        }
        sb.append(nTerminalNodes);
        
        return sb.toString();
    }

}
