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

import java.util.*;
import beast.core.*;
import beast.core.parameter.*;

/**
 * @author Tim Vaughan
 */
@Description("Group of reactions in the birth-death model.")
public class ReactionGroup extends Plugin {

    public Input<String> nameInput = new Input<String>("name",
            "Reaction group name");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Group reaction rate. (Overrides individual reaction rates.)");
    
    public Input<List<Reaction>> reactionsInput = new Input<List<Reaction>>(
            "reaction",
            "Individual reaction within group.",
            new ArrayList<Reaction>());

    // True reaction object:
    hamlet.ReactionGroup reactionGroup;

    public ReactionGroup() { };
    
	@Override
    public void initAndValidate() throws Exception {

        if (nameInput.get()==null)
            reactionGroup = new hamlet.ReactionGroup();
        else
            reactionGroup = new hamlet.ReactionGroup(nameInput.get());

        // Add reactions to reaction group:
        for (Reaction react : reactionsInput.get()) {
            
            reactionGroup.addReactantSchema(react.reactants);
            reactionGroup.addProductSchema(react.products);
            
            if (rateInput.get() != null)
                reactionGroup.addRate(rateInput.get());
            else {
                if (react.rate>=0)
                    reactionGroup.addRate(react.rate);
                else
                    throw new RuntimeException("Neither reaction group nor reaction specify reaction rate.");
            }
        }
    }
}