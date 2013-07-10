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
package master.beast;

import beast.core.Description;
import beast.core.Input;
import beast.core.Plugin;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Leaf count end condition for an inheritance trajectory.")
public class LeafCountEndCondition extends Plugin {
    
    public Input<Integer> nLeavesInput = new Input<Integer>(
            "nLeaves",
            "Leaf count threshold for end condition.",
            Input.Validate.REQUIRED);
    
    public Input<Boolean> includeExtantInput = new Input<Boolean>(
            "includeExtant",
            "Whether to include extant lineages in terminal node count. (Default false.)",
            false);
    
    public Input<Boolean> isRejectionInput = new Input<Boolean>(
            "isRejection",
            "Whether this end condition should cause a rejection. (Default false.)",
            false);
    
    public master.inheritance.LeafCountEndCondition endConditionObject;
    
    public LeafCountEndCondition() { }
    
    @Override
    public void initAndValidate() {
        
        endConditionObject = new master.inheritance.LeafCountEndCondition(
                nLeavesInput.get(),
                includeExtantInput.get(),
                isRejectionInput.get());
    }
}
