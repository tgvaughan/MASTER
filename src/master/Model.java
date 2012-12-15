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

import java.util.*;

/**
 * Class describing a birth-death viral population genetics model. Basically a
 * container class including a sets of population and reaction objects.
 *
 * @author Tim Vaughan
 *
 */
public class Model {

    // Population types in model:
    List<PopulationType> types;
    
    // Reaction groups:
    List<ReactionGroup> reactionGroups;

    /**
     * Model constructor.
     */
    public Model() {
        types = new ArrayList<PopulationType>();
        reactionGroups = new ArrayList<ReactionGroup>();
    }

    /**
     * Add population type to model.
     *
     * @param popType Population type to add.
     */
    public void addPopulationType(PopulationType popType) {
        types.add(popType);
    }
    
    /**
     * Add a number of population types to model.
     * 
     * @param popTypes vararg array of population type objects
     */
    public void addPopulationTypes(PopulationType ... popTypes) {
        for (PopulationType popType : popTypes)
            addPopulationType(popType);
    }
    
    /**
     * Add a type corresponding to given population to model.
     * 
     * @param pop 
     */
    public void addPopulation(Population pop) {
        types.add(pop.type);
    }
    
    /**
     * Add a types corresponding to given populations to model.
     * 
     * @param pops 
     */
    public void addPopulations(Population... pops) {
        for (Population pop : pops)
            addPopulation(pop);
    }

    /**
     * Add reaction group to model.
     *
     * @param reactGroup Reaction to add.
     */
    public void addReactionGroup(ReactionGroup reactGroup) {
        reactGroup.postSpecInit();
        reactionGroups.add(reactGroup);
    }
    
    /**
     * Add reaction to model.
     * 
     * @param react 
     */
    public void addReaction(Reaction react) {
        react.postSpecInit();
        reactionGroups.add(react);
    }       

    /*
     * Getters:
     */
    public List<PopulationType> getPopulationTypes() {
        return types;
    }

    public List<ReactionGroup> getReactionGroups() {
        return reactionGroups;
    }
}