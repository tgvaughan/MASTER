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
package master.model;

import beast.base.core.BEASTObject;
import beast.base.core.Input;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Class describing a birth-death viral population genetics model. Basically a
 * container class including a sets of population and reaction objects.
 *
 * @author Tim Vaughan
 *
 */
public class Model extends BEASTObject {
    
    public Input<List<PopulationType>> populationTypesInput = new Input<>(
            "populationType",
            "Population type involved in the birth-death process.",
            new ArrayList<>());
    
    public Input<List<Population>> populationsInput = new Input<>(
            "population",
            "Population involved in the birth-death process.",
            new ArrayList<>());

    public Input<List<Function>> functionsInput = new Input<>(
            "function",
            "Function for use in reaction predicate and "
                    + "rate multiplier expressions.",
            new ArrayList<>());
    
    public Input<List<ReactionGroup>> reactionGroupsInput = new Input<>(
            "reactionGroup",
            "Group of reactions involved in the birth-death process.",
            new ArrayList<>());
    
    public Input<List<Reaction>> reactionsInput = new Input<>(
            "reaction",
            "Individual reactions involved in the birth-death process.",
            new ArrayList<>());

    // Population types in model:
    List<PopulationType> types;
    
    // Reactions:
    List<Reaction> reactions;
    List<Double> reactionRateChangeTimes;

    // Functions:
    Map<String, Function> functionMap;

    /**
     * Model constructor.
     */
    public Model() {
        types = new ArrayList<>();
        reactions = new ArrayList<>();
        functionMap = new HashMap<>();
    }

    @Override
    public void initAndValidate() {

        System.out.println("Assembling model...");

        System.out.print("Setting up populations... ");

        // Add population types to model:
        for (PopulationType popType : populationTypesInput.get())
            types.add(popType);

        // Add population types corresponding to individual populations to model:
        for (Population pop : populationsInput.get())
            types.add(pop.type);

        System.out.println("done.");

        if (functionsInput.get().size()>0)
            System.out.print("Setting up functions... ");

        // Collect functions:
        for (Function function : functionsInput.get())
            functionMap.put(function.getID(), function);

        if (functionsInput.get().size()>0)
            System.out.println("done.");

        System.out.print("Setting up reactions...");

        // Add reaction groups to model:
        for (ReactionGroup reactGroup: reactionGroupsInput.get()) {
            for (Reaction react : reactGroup.getReactions()) {
                System.out.print(" " + react.getName() + "...");
                System.out.flush();
                reactions.addAll(react.getAllReactions(types, functionMap));
            }
        }

        // Add individual reactions to model:
        for (Reaction react : reactionsInput.get()) {
            System.out.print(" " + react.getName() + "...");
            System.out.flush();
            reactions.addAll(react.getAllReactions(types, functionMap));
        }

        System.out.println(" done.");
        System.out.println("Model assembled.");
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

    public Map<String, Function> getFunctionMap() {
        return functionMap;
    }

    /**
     * Obtain ordered list of times at which reaction rates change.
     * @return 
     */
    public List<Double> getReactionChangeTimes() {
        
        if (reactionRateChangeTimes != null)
            return reactionRateChangeTimes;
        
        List <Double> times = Lists.newArrayList();
        
        for (Reaction reaction : reactions) 
            times.addAll(reaction.getRateTimes());
        
        Collections.sort(times, new Comparator<Double>() {

            @Override
            public int compare(Double o1, Double o2) {
                if (o1<o2)
                    return -1;
                if (o1>o2)
                    return 1;
                
                return 0;
            }
        });
        
        List <Double> timesUnique = Lists.newArrayList();
        for (int i=0; i<times.size(); i++) {
            if (i==0) {
                timesUnique.add(times.get(0));
                continue;
            }
            
            if (times.get(i) != times.get(i-1))
                timesUnique.add(times.get(i));
        }
        reactionRateChangeTimes = timesUnique;
        
        return reactionRateChangeTimes;
    }
    
    public double getNextReactionChangeTime(double t) {
        
        int idx;
        for (idx=0; idx<getReactionChangeTimes().size(); idx++) {
            if (getReactionChangeTimes().get(idx)>t)
                break;
        }

        if (idx==getReactionChangeTimes().size())
            return Double.POSITIVE_INFINITY;
        
        return getReactionChangeTimes().get(idx);
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
        
        jsonObject.put("population_types", getPopulationTypes());
        jsonObject.put("reactions", getReactions());
        
        return jsonObject;
    }
}