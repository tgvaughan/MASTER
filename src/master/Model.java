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

import beast.core.BEASTObject;
import beast.core.Input;
import com.google.common.collect.Lists;
import java.util.*;

/**
 * Class describing a birth-death viral population genetics model. Basically a
 * container class including a sets of population and reaction objects.
 *
 * @author Tim Vaughan
 *
 */
public class Model extends BEASTObject {
    
    public Input<List<PopulationType>> populationTypesInput = new Input<List<PopulationType>>(
            "populationType",
            "Population type involved in the birth-death process.",
            new ArrayList<PopulationType>());
    
    public Input<List<Population>> populationsInput = new Input<List<Population>>(
            "population",
            "Population involved in the birth-death process.",
            new ArrayList<Population>());
    
    public Input<List<NewReactionGroup>> reactionGroupsInput = new Input<List<NewReactionGroup>>(
            "reactionGroup",
            "Group of reactions involved in the birth-death process.",
            new ArrayList<NewReactionGroup>());
    
    public Input<List<NewReaction>> reactionsInput = new Input<List<NewReaction>>(
            "reaction",
            "Individual reactions involved in the birth-death process.",
            new ArrayList<NewReaction>());

    // Population types in model:
    List<PopulationType> types;
    
    // Reactions:
    List<NewReaction> reactions;
    
    @Override
    public void initAndValidate() throws Exception {

        // Add population types to model:
        for (PopulationType popType : populationTypesInput.get())
            addPopulationType(popType);
        
        // Add population types corresponding to individual populations to model:
        for (Population pop : populationsInput.get())
            addPopulation(pop);

        // Add reaction groups to model:

        // Add individual reactions to model:
        for (NewReaction react : reactionsInput.get())
            addReaction(react);
    }

    /**
     * Model constructor.
     */
    public Model() {
        types = Lists.newArrayList();
        reactions = Lists.newArrayList();
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
     * Add reaction to model.
     * 
     * @param react 
     */
    public void addReaction(NewReaction react) {
        reactions.addAll(react.getAllReactions());
    }       

    /*
     * Getters:
     */
    public List<PopulationType> getPopulationTypes() {
        return types;
    }

    public List<NewReaction> getReactions() {
        return reactions;
    }
}