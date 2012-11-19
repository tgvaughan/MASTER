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

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;

/**
 * Reaction group additionally specifying inheritance relationships between
 * reactants and products.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceReactionGroup extends ReactionGroup {
    
    List<List<Node>> reactNodes;
    List<Map<Population, Integer>> inheritanceReactCounts;
    
    /**
     * Constructor with name.
     * 
     * @param reactionGroupName 
     */
    public InheritanceReactionGroup(String reactionGroupName) {
        super(reactionGroupName);
        reactNodes = Lists.newArrayList();
        inheritanceReactCounts = Lists.newArrayList();
    }
    
    /**
     * Constructor without name.
     */
    public InheritanceReactionGroup() {
        super();
        reactNodes = Lists.newArrayList();
        inheritanceReactCounts = Lists.newArrayList();
    }
    
    /**
     * Define a particular inheritance schema by listing the reactant lineages
     * involved.  The children of each parent specify the product lineages
     * involved.
     * 
     * @param nodes Parent (reactant) lineages, themselves specifying
     * child (product) lineages.
     */
    public void addInheritanceSchema(Node ... nodes) {
        reactNodes.add(Lists.newArrayList(nodes));
        
        Population [] reactNodePops = new Population[nodes.length];
        for (int i=0; i<nodes.length; i++)
            reactNodePops[i] = nodes[i].population;
        
        inheritanceReactCounts.add(getPopCount(reactNodePops));
    }
}
