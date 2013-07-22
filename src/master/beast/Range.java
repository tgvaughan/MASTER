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
import beast.core.Input.Validate;
import beast.core.Plugin;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("A range of values an element of a location vector may take.")
public class Range extends Plugin {
    
    public Input<String> variableNameInput = new Input<String>("variableName",
            "Name of variable.", Validate.REQUIRED);
    public Input<String> fromInput = new Input<String>("from",
            "Start value of range.", Validate.REQUIRED);
    public Input<String> toInput = new Input<String>("to",
            "Stop value of range.", Validate.REQUIRED);
    
    private String variableName, from, to;
    
    public Range() { };
    
    @Override
    public void initAndValidate() {
        variableName = variableNameInput.get();
        from = fromInput.get();
        to = toInput.get();
    }
    
    public String getVariableName() {
        return variableName;
    }
    
    public String getFrom() {
        return from;
    }
    
    public String getTo() {
        return to;
    }
}
