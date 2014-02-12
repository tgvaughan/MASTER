/*
 * Copyright (C) 2013 Tim Vaughan <tgvaughan@gmail.com>
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

import beast.core.BEASTObject;
import beast.core.Input;
import java.util.ArrayList;
import java.util.List;

/**
 * Group of reactions.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class ReactionGroup extends BEASTObject {
    
    public Input<String> nameInput = new Input<String>("reactionGroupName",
            "Reaction group name");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Group reaction rate. (Overrides individual reaction rates.)");
    
    public Input<List<Reaction>> reactionsInput = new Input<List<Reaction>>(
            "reaction",
            "Individual reaction within group.",
            new ArrayList<Reaction>());
    
    public ReactionGroup() { }
    
    @Override
    public void initAndValidate() {
        
        if (rateInput.get() != null) {
            for (Reaction react : reactionsInput.get())
                react.setRate(rateInput.get());
        }
        
        if (nameInput.get() != null) {
            for (Reaction react : reactionsInput.get())
                react.setName(nameInput.get());
        }
    }
    
    /**
     * @return list of reactions in group
     */
    public List<Reaction> getReactions() {
        return reactionsInput.get();
    }
}
