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
package master.inheritance;

import com.google.common.collect.Lists;
import master.Population;
import master.ReactionGroup;
import java.util.List;

/**
 * Reaction group additionally specifying inheritance relationships between
 * reactants and products.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceReactionGroup extends ReactionGroup {
    
    List<List<Node>> reactNodes, prodNodes;
    
    /**
     * Constructor with name.
     * 
     * @param reactionGroupName 
     */
    public InheritanceReactionGroup(String reactionGroupName) {
        super(reactionGroupName);
        reactNodes = Lists.newArrayList();
        prodNodes = Lists.newArrayList();
    }
    
    /**
     * Constructor without name.
     */
    public InheritanceReactionGroup() {
        super();
        reactNodes = Lists.newArrayList();
        prodNodes = Lists.newArrayList();
    }
    
    /**
     * Define a particular reactionGroup by listing nodes representing individual
     * reactants involved in that reactionGroup.  In contrast to the similarly
     * named method in the ReactionGroup class, this method takes <b>nodes</b>
     * which specify through their children an inheritance relationship to
     * products listed in the call to addProductSchema.
     * 
     * @param nodes Nodes representing individual reactants.
     */
    public void addInheritanceReactantSchema(Node ... nodes) {
        reactNodes.add(Lists.newArrayList(nodes));
        
        Population [] reactNodePops = new Population[nodes.length];
        for (int i=0; i<nodes.length; i++)
            reactNodePops[i] = nodes[i].population;
        super.addReactantSchema(reactNodePops);
    }
    
    /**
     * Define a particular reactionGroup by listing nodes representing individual
     * products involved in that reactionGroup.  In contrast to the similarly
     * named method in the ReactionGroup class, this method takes <b>nodes</b>
     * which specify through their parents an inheritance relationship to
     * products listed in the call to addReactantSchema.
     * 
     * @param nodes Nodes representing individual products.
     */
    public void addInheritanceProductSchema(Node ... nodes) {
        prodNodes.add(Lists.newArrayList(nodes));
        
        Population [] prodNodePops = new Population[nodes.length];
        for (int i=0; i<nodes.length; i++)
            prodNodePops[i] = nodes[i].population;
        super.addProductSchema(prodNodePops);
    }
    
    @Override
    public void addReactantSchema(Population ... pops) {
        throw new UnsupportedOperationException("Inheritance reaction schemas must be defined using nodes.");
    }
    
    @Override
    public void addProductSchema(Population ... pops) {
        throw new UnsupportedOperationException("Inheritance reaction schemas must be defined using nodes.");
    }
}
