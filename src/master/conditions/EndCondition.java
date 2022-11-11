/*
 * Copyright (C) 2014 Tim Vaughan <tgvaughan@gmail.com>
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

package master.conditions;

import beast.base.core.BEASTObject;
import beast.base.core.Description;
import beast.base.core.Input;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;
import master.model.Population;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Abstract end condition class.")
public abstract class EndCondition extends BEASTObject {
    
    public Input<List<Population>> populationInput = new Input<>(
            "population",
            "Specific population to which end condition applies (Optional).",
            new ArrayList<>());
        
    public Input<Boolean> rejectionInput = new Input<>(
            "isRejection",
            "Whether condition causes trajectory rejection. (Default false.)",
            false);

    public EndCondition() { }
    
    /**
     * @return true if this end condition is a rejection.
     */
    public boolean isRejection() {
        return this.rejectionInput.get();
    }
    
    /**
     * @return String description of condition.
     */
    @JsonValue
    public abstract String getConditionDescription();
    
    @Override
    public String toString() {
        return getConditionDescription();
    }
}
