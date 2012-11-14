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
 * Iterator to iterate over populations of a chosen population type.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationIterator implements Iterator<Population> {
    
    PopulationType pop;
    int [] loc;
    
    boolean finished;
    
    public PopulationIterator(PopulationType pop) {
        this.pop = pop;
        loc = new int[pop.dims.length];
        for (int i=0; i<loc.length; i++)
            loc[i] = 0;
        
        finished = false;
    }

    @Override
    public boolean hasNext() {
        return !finished;
    }

    @Override
    public Population next() {
        
        // Record population to return
        Population sub = new Population(pop, loc);
        
        // Iterate location vector
        boolean fellThrough = true;
        for (int i=0; i<loc.length; i++) {
            if (loc[i]<pop.dims[i]-1) {
                loc[i] += 1;
                fellThrough = false;
                break;
            } else
                loc[i] = 0;
        }
        if (fellThrough)
            finished = true;
        
        return sub;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}
