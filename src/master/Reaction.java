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
package master;

/**
 * A Reaction is a special ReactionGroup containing only a single reaction
 * schema.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class Reaction extends ReactionGroup {
    
    /**
     * Create Reaction with name.
     * 
     * @param reactionName 
     */
    public Reaction(String reactionName) {
        super(reactionName);
    }
    
    /**
     * Create Reaction with no name.
     */
    public Reaction() {
        super();
    }
    
    /**
     * Define reaction reactant schema.
     * 
     * @param pops 
     */
    public void setReactantSchema(Population ... pops) {
        reactCounts.clear();
        addReactantSchema(pops);
    }
    
    /**
     * Define reaction product schema.
     * 
     * @param pops 
     */
    public void setProductSchema(Population ... pops) {
        prodCounts.clear();
        addProductSchema(pops);
    }
    
    /**
     * Set reaction rate.
     * 
     * @param rate 
     */
    public void setRate(double rate) {
        rates.clear();
        rates.add(rate);
    }
}
