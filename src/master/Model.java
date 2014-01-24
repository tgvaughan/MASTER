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
import com.google.common.collect.Maps;
import java.util.*;
import org.codehaus.jackson.annotate.JsonValue;

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
    
    public Input<List<ReactionGroup>> reactionGroupsInput = new Input<List<ReactionGroup>>(
            "reactionGroup",
            "Group of reactions involved in the birth-death process.",
            new ArrayList<ReactionGroup>());
    
    public Input<List<Reaction>> reactionsInput = new Input<List<Reaction>>(
            "reaction",
            "Individual reactions involved in the birth-death process.",
            new ArrayList<Reaction>());

    // Population types in model:
    List<PopulationType> types;
    
    // Reactions:
    List<Reaction> reactions;
    
    @Override
    public void initAndValidate() throws Exception {

        // Add population types to model:
        for (PopulationType popType : populationTypesInput.get())
            addPopulationType(popType);
        
        // Add population types corresponding to individual populations to model:
        for (Population pop : populationsInput.get())
            addPopulation(pop);

        // Add reaction groups to model:
        for (ReactionGroup reactGroup: reactionGroupsInput.get()) {
            for (Reaction react : reactGroup.getReactions())
                reactions.addAll(react.getAllReactions(types));
        }

        // Add individual reactions to model:
        for (Reaction react : reactionsInput.get()) {
            //addReaction(react);
            reactions.addAll(react.getAllReactions(types));
        }
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
     * Add a type corresponding to given population to model.
     * 
     * @param pop 
     */
    public void addPopulation(Population pop) {
        types.add(pop.type);
    }
    
    /**
     * Add reaction to model.
     * 
     * @param react 
     */
    public void addReaction(Reaction react) {
        reactions.add(react);
    }       

    /*
     * Getters:
     */
    public List<PopulationType> getPopulationTypes() {
        return types;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }
    
    /**
     * Construct representation of specification to use in assembling
     * summary in JSON output file.
     * 
     * @return Map from strings to other objects which have a JSON rep
     */
    @JsonValue
    public Map<String, Object> getJsonValue() {
        
        Map<String, Object> jsonObject = Maps.newHashMap();
        
        jsonObject.put("population types", getPopulationTypes());
        jsonObject.put("reactions", getReactions());
        
        return jsonObject;
    }
}