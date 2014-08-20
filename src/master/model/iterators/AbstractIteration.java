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

package master.model.iterators;

import beast.core.BEASTObject;
import java.util.List;

/**
 * Abstract class of objects representing iterations over a set of variables.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public abstract class AbstractIteration extends BEASTObject {
    
    /**
     * Get list where each element is an array of integers
     * corresponding to the one complete set of values of the
     * iteration variables can take.
     *
     * @return list of variable value arrays
     */
    public abstract List<int[]> getVariableValuesList();
    
    /**
     * Get array of strings corresponding to the variable names.
     * 
     * @return array of variable names
     */
    public abstract String[] getVariableNames();
    
}
