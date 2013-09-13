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

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Lineage count end condition for an inheritance trajectory.")
public class LineageEndCondition extends BEASTObject {
    
    public Input<Population> populationInput = new Input<Population>(
            "population",
            "Specific population to which lineages belong (optional).");
    
    public Input<Integer> nLineagesInput = new Input<Integer>(
            "nLineages",
            "Linage count threshold for end condition.",
            Validate.REQUIRED);
    
    public Input<Boolean> isRejectionInput = new Input<Boolean>(
            "isRejection",
            "Whether this end condition should cause a rejection. (Default false.)",
            false);

    public master.inheritance.LineageEndCondition endConditionObject;
    
    public LineageEndCondition() { }
    
    @Override
    public void initAndValidate() {
        
        if (populationInput.get() != null) {
            endConditionObject = new master.inheritance.LineageEndCondition(
                    populationInput.get().pop,
                    nLineagesInput.get(),
                    isRejectionInput.get());
        } else {
            endConditionObject = new master.inheritance.LineageEndCondition(
                    nLineagesInput.get(),
                    isRejectionInput.get());
        }
    }
    
}
