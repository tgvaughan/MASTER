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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Group of reactions in an inheritance-tracking birth-death model.")
public class InheritanceReactionGroup extends Plugin {
    
    public Input<String> nameInput = new Input<String>("reactionGroupName",
            "Reaction group name");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Group reaction rate. (Overrides individual reaction rates.)");
    
    public Input<List<InheritanceReaction>> reactionsInput = new Input<List<InheritanceReaction>>(
            "reaction",
            "Individual inheritance reaction within group.",
            new ArrayList<InheritanceReaction>());
    
    // True inheritance reaction object
    master.inheritance.InheritanceReactionGroup inheritanceReactionGroup;
    
    public InheritanceReactionGroup() { };
    
    @Override
    public void initAndValidate() { };
    
    public void addToModel(master.inheritance.InheritanceModel model) throws ParseException {
        
        if (nameInput.get()==null)
            inheritanceReactionGroup = new master.inheritance.InheritanceReactionGroup();
        else
            inheritanceReactionGroup = new master.inheritance.InheritanceReactionGroup(nameInput.get());

        // Add reactions to reaction group:
        for (InheritanceReaction react : reactionsInput.get())
            react.addToGroup(model, inheritanceReactionGroup, rateInput.get());
        
        // Add reaction group to model.
        model.addInheritanceReactionGroup(inheritanceReactionGroup);
    }
}
