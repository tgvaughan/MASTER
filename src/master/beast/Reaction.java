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
package master.beast;

import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.BEASTObject;
import com.google.common.collect.Lists;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Individual reaction in an inheritance-tracking birth-death model.")
public class Reaction extends BEASTObject {
            
    public Input<String> nameInput = new Input<String>("reactionName",
            "Name of reaction. (Not used for grouped reactions.)");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Individual reaction rate. (Only used if group rate unset.)");
    
    public Input<List<Range>> rangesInput = new Input<List<Range>>("range",
            "Define multiple reactions for different values of a variable.",
            new ArrayList<Range>());
    
    public Input<String> reactionStringInput = new Input<String>(
            "value",
            "String description of reaction.", Validate.REQUIRED);

    
    private double rate;
    private String name;
    
    private List<String> variableNames;
    private List<Integer> fromValues, toValues;
    
    private int reactionIndex = 0;

    public Reaction() { }
    
    @Override
    public void initAndValidate() {
                    
        if (rateInput.get() != null)
            rate = rateInput.get();
        else
            rate = -1;

        if (nameInput.get() != null)
            name = nameInput.get();
        else
            name = null;
        
        variableNames = Lists.newArrayList();
        fromValues = Lists.newArrayList();
        toValues = Lists.newArrayList();
        
        for (int i=0; i<rangesInput.get().size(); i++) {
            variableNames.add(rangesInput.get().get(i).getVariableName());
        }
        
        for (int i=0; i<rangesInput.get().size(); i++) {
            String fromString = rangesInput.get().get(i).getFrom();
            String toString = rangesInput.get().get(i).getTo();
            
            if (variableNames.contains(fromString)) {
                if (variableNames.indexOf(fromString)<i) {
                    fromValues.add(-(1+variableNames.indexOf(fromString)));
                } else {
                    throw new RuntimeException("Range boundaries can only refer "
                            + "to variables of earlier ranges, not later ones.");
                }
            } else {
                try {
                    fromValues.add(Integer.parseInt(fromString));
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Range 'from' value must be "
                            + "a number or range variable.");
                }
            }
            
            if (variableNames.contains(toString)) {
                if (variableNames.indexOf(toString)<i) {
                    toValues.add(-(1+variableNames.indexOf(toString)));
                } else {
                    throw new RuntimeException("Range boundaries can only refer "
                            + "to variables of earlier ranges, not later ones.");
                }
            } else {
                try {
                    toValues.add(Integer.parseInt(toString));
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Range 'to' value must be "
                            + "a number or range variable.");
                }
            }

        }
        
    }
    
    /**
     * Assemble list of reactant or product nodes.
     * 
     * @param indices values of range variables
     * @param popNames list of population names identified by parser
     * @param locs list of locations (unflattened) identified by parser
     * @param reactionVariableNames list of variable names identified by parser
     * @param popTypes list of population types present in model
     * @return list of nodes
     * @throws ParseException 
     */
    private List<master.Population> getEntityList(int [] indices,
            List<String> popNames, List<List<Integer>> locs,
            List<String> reactionVariableNames,
            List<master.PopulationType> popTypes) throws ParseException {
        
        List<master.Population> entities = Lists.newArrayList();

        for (int entityIdx=0; entityIdx<popNames.size(); entityIdx++) {
            // Substitute variable names for values:
            String popTypeName = popNames.get(entityIdx);
            List<Integer> loc = locs.get(entityIdx);
            
            master.PopulationType popType = null;
            for (master.PopulationType thisPopType : popTypes)
                if (thisPopType.getName().equals(popTypeName))
                    popType = thisPopType;
            
            if (popType == null)
                throw new ParseException("Unidentified reactant population type '"
                        + popTypeName + "'.", 0);
            
            int [] flattenedLoc = new int[loc.size()];
            for (int locIdx=0; locIdx<loc.size(); locIdx++) {
                if (loc.get(locIdx)>=0)
                    flattenedLoc[locIdx] = loc.get(locIdx);
                else {
                    String variableName = reactionVariableNames.get(-loc.get(locIdx)-1);
                    if (variableNames.contains(variableName)) {
                        flattenedLoc[locIdx] = indices[variableNames.indexOf(variableName)];
                    } else {
                        throw new ParseException("Undefined range variable '"
                                + variableName + "'.", 0);
                    }
                }
            }
                
            entities.add(new master.Population(popType, flattenedLoc));
        }
            
        return entities;
    }
    

    /**
     * Recursion used to loop over range variables and assemble reaction
     * for each combination.
     * 
     * @param depth current recursion depth
     * @param indices list of range variable values
     * @param parser result of parsing reaction string
     * @param model model to which reactions are to be added
     * @param group reaction group to which reactions are to be added (may be null)
     * @throws ParseException 
     */
    private void rangeLoop(int depth, int [] indices, ReactionStringParser parser,
            master.Model model,
            master.ReactionGroup group) throws ParseException {
        
        if (depth==indices.length) {
            
            List<master.Population> reactants = getEntityList(indices,
                    parser.reactantPopNames, parser.reactantLocs,
                    parser.variableNames, model.getPopulationTypes());
            List<master.Population> products = getEntityList(indices,
                    parser.productPopNames, parser.productLocs,
                    parser.variableNames, model.getPopulationTypes());
            

            if (group != null) {
                
                group.addReactantSchema(reactants.toArray(new master.Population[0]));
                group.addProductSchema(products.toArray(new master.Population[0]));
                group.addRate(rate);
                
            } else {
                
                master.Reaction reaction;
                if (name != null) {
                    if (reactionIndex>0)
                        reaction = new master.Reaction(name + reactionIndex);
                    else
                        reaction = new master.Reaction(name);
                } else
                    reaction = new master.Reaction();
                
                reaction.addReactantSchema(reactants.toArray(new master.Population[0]));
                reaction.addProductSchema(products.toArray(new master.Population[0]));
                reaction.setRate(rate);
                model.addReaction(reaction);
                
            }
            
            reactionIndex += 1;
            
        } else {
            int from;
            if (fromValues.get(depth)<0)
                from = indices[-fromValues.get(depth)-1];
            else
                from = fromValues.get(depth);

            int to;
            if (toValues.get(depth)<0)
                to = indices[-toValues.get(depth)-1];
            else
                to = toValues.get(depth);
            
            for (int i=from; i<=to; i++) {
                indices[depth] = i;
                rangeLoop(depth+1, indices, parser, model, group);
            }

        }
    }
    

    /**
     * Add reactions specified by reaction string and range inputs directly
     * to model.
     * 
     * @param model Inheritance model
     * @throws ParseException 
     */
    public void addToModel(master.Model model) throws ParseException {
        
        if (rate<0) {
            throw new RuntimeException("No reaction rate specified.");
        }
        
        ReactionStringParser parser = null;
        try {
            parser = new ReactionStringParser(reactionStringInput.get(),
                    model.getPopulationTypes());
        } catch (ParseException ex) {
            Logger.getLogger(InheritanceReaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int [] indices = new int[rangesInput.get().size()];
        rangeLoop(0, indices, parser, model, null);
        
    }
    
    /**
     * Add reactions specified by reaction string and range inputs to chosen
     * reaction group inside of model.
     * 
     * @param model Inheritance model
     * @param group Reaction group
     * @param groupRate Optional fixed group rate for reactions.
     * @throws ParseException 
     */
    public void addToGroup(master.Model model,
            master.ReactionGroup group, Double groupRate) throws ParseException {
        
        if (groupRate != null) {
          rate = groupRate;
        } else {
            if (rate<0)
                throw new RuntimeException("Neither reaction nor its enclosing "
                        + "group specify rate.");
        }
        
        ReactionStringParser parser = null;
        try {
            parser = new ReactionStringParser(reactionStringInput.get(),
                    model.getPopulationTypes());
        } catch (ParseException ex) {
            Logger.getLogger(Reaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int [] indices = new int[rangesInput.get().size()];
        rangeLoop(0, indices, parser, model, group);
        
    }
        
    public double getRate() {
        return rate;
    }
    
    public String getName() {
        return name;
    }
}
