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

import beast.util.Randomizer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

/**
 * A class representing an inheritance graph generated under a particular
 * stochastic population dynamics model.  Inheritance trees are a special
 * case in which children have only one parent.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceGraph {

    // List of nodes present at the start of the simulation
    public List <Node> startNodes;

    // List of nodes/lineages currently present in simulation.
    public List<Node> activeNodes;
    
    // Simulation specification.
    InheritanceGraphSpec spec;
    
    /**
     * Build an inheritance graph corrsponding to a set of lineages
     * embedded within populations evolving under a birth-death process.
     * 
     * I'm sure there's a more efficient way of implementing this. Suggestions
     * are very welcome. (Tim)
     * 
     * @param spec Inheritanc graph simulation specification.
     */
    public InheritanceGraph(InheritanceGraphSpec spec) {
        this.spec = spec;
        startNodes = spec.initLineages;
        activeNodes = spec.initLineages;
        
        // Initialise time
        double t=0.0;
        
        // Initialise system state:
        State currentState = new State(spec.initState);
        
        // Integration loop:
        while (true) {
            
            // Calculate propensities
            double totalPropensity = 0.0;
            for (ReactionGroup reactionGroup : spec.model.reactionGroups) {
                reactionGroup.calcPropensities(currentState);
                for (double propensity : reactionGroup.propensities)
                    totalPropensity += propensity;
            }
            
            // Draw time of next reaction
            t += Randomizer.nextExponential(totalPropensity);
            
            // Break if new time exceeds end time:
            if (t>spec.simulationTime)
                break;
            
            // Choose reaction to implement
            double u = Randomizer.nextDouble()*totalPropensity;
            boolean found = false;
            InheritanceReactionGroup chosenReactionGroup = null;
            int chosenReaction = 0;
            for (InheritanceReactionGroup reactionGroup : spec.inheritanceModel.inheritanceReactionGroups) {
                
                for (int ridx = 0; ridx<reactionGroup.propensities.size(); ridx++) {
                    u -= reactionGroup.propensities.get(ridx);
                    if (u<0) {
                        found = true;
                        chosenReactionGroup = reactionGroup;
                        chosenReaction = ridx;
                    }
                }
                
                if (found)
                    break;
            }
            
            // Select lineages involved in reaction.  This is done by sampling
            // without replacement from the individuals present in the current
            // state.
            Map<Population, Integer> popsSeen = Maps.newHashMap();
            Map<Population, Integer> popsChosen = Maps.newHashMap();
            List<Node> nodesInvolved = Lists.newArrayList();
            List<Node> reactNodesInvolved = Lists.newArrayList();
            for (Node node : activeNodes) {
                if (!chosenReactionGroup.reactCounts.get(chosenReaction).containsKey(node.population))
                    continue;
                
                // Calculate probability that lineage is involved in reaction:
                int m = chosenReactionGroup.reactCounts.get(chosenReaction).get(node.population);
                double N = currentState.get(node.population);
                
                if (popsChosen.containsKey(node.population))
                    m -= popsChosen.get(node.population);
                
                if (popsSeen.containsKey(node.population))
                    N -= popsSeen.get(node.population);
                
                // Decide whether lineage is involved
                if (Randomizer.nextDouble() < m/N) {
                    nodesInvolved.add(node);
                    
                    // Select particular reactant node to use:
                    int idx = Randomizer.nextInt(m);
                    for (Node reactNode : chosenReactionGroup.reactNodes.get(chosenReaction)) {
                        if (reactNode.population == node.population) {
                            if (idx == 0) {
                                reactNodesInvolved.add(reactNode);
                                break;
                            } else
                                idx -= 1;
                        }
                    }
                    
                    // Update popsChosen and popsSeen
                    if (popsChosen.containsKey(node.population))
                        popsChosen.put(node.population, popsChosen.get(node.population)+1);
                    else
                        popsChosen.put(node.population, 1);
                }
                
                // Keep track of populations seen for future lineage selections:
                if (popsSeen.containsKey(node.population))
                    popsSeen.put(node.population, popsSeen.get(node.population)+1);
                else
                    popsSeen.put(node.population,1);
            }
            
            
            
            // Implement modifications to inheritance graph:
            
            // Implement state change due to reaction:
            
        }
    }
}
