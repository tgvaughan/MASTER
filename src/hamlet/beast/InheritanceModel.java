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
package hamlet.beast;

import beast.core.Description;
import beast.core.Input;
import beast.core.Plugin;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("")
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
            new Input<List<InheritanceReactionGroup>>("inheritanceReactionGroup",
            "Specifies an inheritance reaction group.",
            new ArrayList<InheritanceReactionGroup>());
    
    public Input<List<InheritanceReaction>> inheritanceReactionsInput = 
            new Input<List<InheritanceReaction>>("inheritanceReaction",
            "Specifies an individual inheritance reaction.");
    
    hamlet.inheritance.InheritanceModel model;
    
    @Override
    public void initAndValidate() throws Exception {

        model = new hamlet.inheritance.InheritanceModel();

        // Add population types to model:
        for (PopulationType popType : populationTypesInput.get())
            model.addPopulationType(popType.popType);
        
        // Add population types corresponding to individual populations to model:
        for (Population pop : populationsInput.get())
            model.addPopulation(pop.pop);

        // Add reaction groups to model:
        for (InheritanceReactionGroup reactGroup : inheritanceReactionGroupsInput.get())
            model.addInheritanceReactionGroup(reactGroup.inheritanceReactionGroup);

        // Add individual reactions to model:
        for (InheritanceReaction react : inheritanceReactionsInput.get()) {

            hamlet.inheritance.InheritanceReaction reaction;
            if (react.name!=null)
                reaction = new hamlet.inheritance.InheritanceReaction(react.nameInput.get());
            else
                reaction = new hamlet.inheritance.InheritanceReaction();

            reaction.setInheritanceReactantSchema(react.reactants);
            reaction.setInheritanceProductSchema(react.products);

            if (react.rate>=0)
                reaction.addRate(react.rate);
            else
                throw new RuntimeException("Reaction does not specify reaction rate.");
            
            model.addInheritanceReaction(reaction);
        }
        
    }
}
