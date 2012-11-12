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
package hamlet;

import java.util.Iterator;

/**
 * Iterator to iterate over subpopulations of a chosen population.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class SubPopulationIterator implements Iterator<SubPopulation> {
    
    Population pop;
    int [] loc;
    
    public SubPopulationIterator(Population pop) {
        loc = new int[pop.dims.length];
        for (int i=0; i<loc.length; i++)
            loc[i] = 0;
    }

    @Override
    public boolean hasNext() {
        for (int i=0; i<loc.length; i++)
            if (loc[i]<pop.dims[i]-1)
                return true;
        
        return false;
    }

    @Override
    public SubPopulation next() {
        
        for (int i=0; i<loc.length; i++) {
            if (loc[i]<pop.dims[i]-1) {
                loc[i] += 1;
                break;
            } else
                loc[i] = 0;
        }
        
        return new SubPopulation(pop, loc);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}
