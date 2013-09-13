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

import beast.core.*;
import java.text.ParseException;
import java.util.*;

/**
 * @author Tim Vaughan
 */
@Description("Group of reactions in the birth-death model.")
public class ReactionGroup extends BEASTObject {

    public Input<String> nameInput = new Input<String>("reactionGroupName",
            "Reaction group name");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Group reaction rate. (Overrides individual reaction rates.)");
    
    public Input<List<Reaction>> reactionsInput = new Input<List<Reaction>>(
            "reaction",
            "Individual reaction within group.",
            new ArrayList<Reaction>());

    // True reaction object:
    master.ReactionGroup reactionGroup;

    public ReactionGroup() { };
    
    @Override
    public void initAndValidate() { };
        
    public void addToModel(master.Model model) throws ParseException {
        
        if (nameInput.get()==null)
            reactionGroup = new master.ReactionGroup();
        else
            reactionGroup = new master.ReactionGroup(nameInput.get());

        // Add reactions to reaction group:
        for (Reaction react : reactionsInput.get())
            react.addToGroup(model, reactionGroup, rateInput.get());

        // Add reaction group to model:
        model.addReactionGroup(reactionGroup);
    }
}