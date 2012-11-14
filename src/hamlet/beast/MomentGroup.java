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
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Group of moments to estimate from the simulated trajectories.")
public class MomentGroup extends Plugin {
    
    public Input<String> nameInput = new Input<String>("momentGroupName",
            "Name of moment group.", Input.Validate.REQUIRED);
    
    public Input<Boolean> factorialInput = new Input<Boolean>("factorial",
            "True causes factorial moments to be used.  Default is false.", false);
    
    public Input<Boolean> sumInput = new Input<Boolean>("sum",
            "True causes individual moments to be summed.  Default is false.", false);
    
    public Input<List<Moment>> momentsInput = new Input<List<Moment>>(
            "moment", "Individual moment in group.",
            new ArrayList<Moment>());
    
    hamlet.MomentGroup momentGroup;
    
    public MomentGroup() { };
    
    @Override
    public void initAndValidate() {

        momentGroup = new hamlet.MomentGroup(nameInput.get(), factorialInput.get());
        
        if (sumInput.get()) {
            momentGroup.newSum();
            for (Moment moment : momentsInput.get())
                momentGroup.addSubSchemaToSum(moment.factors);
        } else {
            for (Moment moment : momentsInput.get())
                momentGroup.addSchema(moment.factors);
        }

    }
}