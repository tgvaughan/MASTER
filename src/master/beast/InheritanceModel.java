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
import beast.core.Plugin;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Specification of a birth-death model with inheritance relationships.")
public class InheritanceModel extends Plugin {
    
    public Input<List<PopulationType>> populationTypesInput = new Input<List<PopulationType>>(
            "populationType",
            "Population type involved in the birth-death process.",
            new ArrayList<PopulationType>());
    
    public Input<List<Population>> populationsInput = new Input<List<Population>>(
            "population",
            "Population involved in the birth-death process.",
            new ArrayList<Population>());
    
    public Input<List<InheritanceReactionGroup>> inheritanceReactionGroupsInput =
            new Input<List<InheritanceReactionGroup>>("reactionGroup",
            "Specifies an inheritance reaction group.",
            new ArrayList<InheritanceReactionGroup>());
    
    public Input<List<InheritanceReaction>> inheritanceReactionsInput = 
            new Input<List<InheritanceReaction>>("reaction",
            "Specifies an individual inheritance reaction.",
            new ArrayList<InheritanceReaction>());
    
    master.inheritance.InheritanceModel model;
    
    public InheritanceModel() { }
    
    @Override
    public void initAndValidate() throws Exception {

        model = new master.inheritance.InheritanceModel();

        // Add population types to model:
        for (PopulationType popType : populationTypesInput.get())
            model.addPopulationType(popType.popType);
        
        // Add population types corresponding to individual populations to model:
        for (Population pop : populationsInput.get())
            model.addPopulation(pop.pop);

        // Add reaction groups to model:
        for (InheritanceReactionGroup reactGroup : inheritanceReactionGroupsInput.get()) {
            reactGroup.postProcessing(model.getPopulationTypes());
            model.addInheritanceReactionGroup(reactGroup.inheritanceReactionGroup);
        }

        // Add individual reactions to model:
        for (InheritanceReaction react : inheritanceReactionsInput.get()) {

            master.inheritance.InheritanceReaction reaction;
            if (react.getName()!=null)
                reaction = new master.inheritance.InheritanceReaction(react.getName());
            else
                reaction = new master.inheritance.InheritanceReaction();

            react.parseStrings(model.getPopulationTypes());
            reaction.setInheritanceReactantSchema(react.getReactants());
            reaction.setInheritanceProductSchema(react.getProducts());

            if (react.getRate()>=0)
                reaction.addRate(react.getRate());
            else
                throw new RuntimeException("Reaction does not specify reaction rate.");
            
            model.addInheritanceReaction(reaction);
        }
        
    }
}
