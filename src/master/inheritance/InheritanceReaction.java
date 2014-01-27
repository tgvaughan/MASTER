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

import master.model.Node;

/**
 * An InheritanceReaction is a special InheritanceReactionGroup specifying
 * only a single reaction.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceReaction extends InheritanceReactionGroup {
    
    /**
     * Create inheritance reaction with name.
     * 
     * @param reactionName 
     */
    public InheritanceReaction(String reactionName) {
        super(reactionName);
    }
    
    /**
     * Create inheritance reaction with no name.
     * 
     */
    public InheritanceReaction() {
        super();
    }
    
    /**
     * Set inheritance reactant schema.
     * 
     * @param nodes 
     */
    public void setInheritanceReactantSchema(Node ... nodes) {
        clearReactantSchemas();
        reactNodes.clear();
        addInheritanceReactantSchema(nodes);
    }
    
    /**
     * Set inheritance reaction product schema.
     * 
     * @param nodes 
     */
    public void setInheritanceProductSchema(Node ... nodes) {
        clearProductSchemas();
        prodNodes.clear();
        addInheritanceProductSchema(nodes);
    }
    
    /**
     * Set inheritance reaction rate.
     * 
     * @param rate 
     */
    public void setRate(double rate) {
        clearRates();
        addRate(rate);
    }
    
}
