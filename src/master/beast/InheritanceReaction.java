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
import beast.core.Plugin;
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
public class InheritanceReaction extends Plugin {
            
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

    public InheritanceReaction() { }
    
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

            boolean fromFound = false;
            boolean toFound = false;
            
            for (int vidx=0; vidx<i; vidx++) {
                if (fromString.equals(variableNames.get(vidx))) {
                    fromValues.set(i, -vidx);
                    fromFound = true;
                }
                
                if (toString.equals(variableNames.get(vidx))) {
                    toValues.set(i, -vidx);
                    toFound = true;
                }
            }
            
            if (!fromFound)
                fromValues.set(i, Integer.parseInt(fromString));

            if (!toFound)
                toValues.set(i, Integer.parseInt(toString));
        }
        
    }
    
    private void loopOverVariables(int depth, int [] indices, ReactionStringParser parser,
            master.inheritance.InheritanceModel model) throws ParseException {
        if (depth==indices.length) {
            
            List<master.inheritance.Node> reactants = Lists.newArrayList();

            for (int r=0; r<parser.reactantPopNames.size(); r++) {
                // Substitute variable names for values:
                String popTypeName = parser.reactantPopNames.get(r);
                List<Integer> loc = parser.reactantLocs.get(r);

                master.PopulationType popType = null;
                for (master.PopulationType thisPopType : model.getPopulationTypes())
                    if (thisPopType.getName().equals(popTypeName))
                        popType = thisPopType;
                
                if (popType == null)
                    throw new ParseException("Unidentified reactant population type '"
                            + popTypeName + "'.", 0);
                
                int [] flattenedLoc = new int[loc.size()];
                for (int locIdx=0; locIdx<loc.size(); locIdx++) {
                    if (loc.get(locIdx)>0)
                        flattenedLoc[locIdx] = loc.get(locIdx);
                    else {
                        String variableName = parser.variableNames.get(-loc.get(locIdx));
                        if (variableNames.contains(variableName)) {
                            flattenedLoc[locIdx] = indices[variableNames.indexOf(variableName)];
                        } else {
                            throw new ParseException("Undefined range variable '"
                                    + variableName + "'.", 0);
                        }
                    }
                }
                
                master.Population population = new master.Population(popType, flattenedLoc);
                reactants.add(new master.inheritance.Node(population));
            }
            
            List<master.inheritance.Node> products = Lists.newArrayList();

            for (int p=0; p<parser.productPopNames.size(); p++) {
                // Substitute variable names for values:
                String popTypeName = parser.productPopNames.get(p);
                List<Integer> loc = parser.productLocs.get(p);

                master.PopulationType popType = null;
                for (master.PopulationType thisPopType : model.getPopulationTypes())
                    if (thisPopType.getName().equals(popTypeName))
                        popType = thisPopType;
                
                if (popType == null)
                    throw new ParseException("Unidentified product population type '"
                            + popTypeName + "'.", 0);
                
                int [] flattenedLoc = new int[loc.size()];
                for (int locIdx=0; locIdx<loc.size(); locIdx++) {
                    if (loc.get(locIdx)>0)
                        flattenedLoc[locIdx] = loc.get(locIdx);
                    else {
                        String variableName = parser.variableNames.get(-loc.get(locIdx));
                        if (variableNames.contains(variableName)) {
                            flattenedLoc[locIdx] = indices[variableNames.indexOf(variableName)];
                        } else {
                            throw new ParseException("Undefined range variable '"
                                    + variableName + "'.", 0);
                        }
                    }
                }
                
                master.Population population = new master.Population(popType, flattenedLoc);
                products.add(new master.inheritance.Node(population));
                
                
            }
            
        } else {
            int from;
            if (fromValues.get(depth)<0)
                from = indices[-fromValues.get(depth)];
            else
                from = fromValues.get(depth);

            int to;
            if (toValues.get(depth)<0)
                to = indices[-toValues.get(depth)];
            else
                to = toValues.get(depth);
            
            for (int i=from; i<=to; i++) {
                indices[depth] = i;
                loopOverVariables(depth+1, indices, parser, model);
            }

        }
    }
    
    public void addToModel(master.inheritance.InheritanceModel model) {
        
        
    }
    
    public void addToGroup(master.inheritance.InheritanceModel model,
            master.inheritance.InheritanceReactionGroup group) {
        
        try {
            ReactionStringParser parser =
                    new ReactionStringParser(reactionStringInput.get(),
                    model.getPopulationTypes(), rangesInput.get());
        } catch (ParseException ex) {
            Logger.getLogger(InheritanceReaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
        
    public double getRate() {
        return rate;
    }
    
    public String getName() {
        return name;
    }
}
