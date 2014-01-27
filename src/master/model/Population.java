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
package master.model;

import beast.core.BEASTObject;
import beast.core.Input;
import beast.core.parameter.IntegerParameter;

/**
 * Specifies a particular population/deme of a given type.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class Population extends BEASTObject {

    public Input<PopulationType> typeInput = new Input<PopulationType>(
            "type",
            "Type to which this population belongs.");

    public Input<String> popNameInput = new Input<String>(
            "populationName",
            "Name of population.  Needed if no type given.");

    public Input<IntegerParameter> locationInput = new Input<IntegerParameter>(
            "location",
            "Vector specifying location of specific population.");

    PopulationType type;
    int offset;

    private int hash;
    
    /**
     * Default constructor.
     */
    public Population() { };

    @Override
    public void initAndValidate() {
        // Ensure that one of either the population type or its name
        // is specified:
        if (typeInput.get() == null && popNameInput.get() == null)
            throw new RuntimeException("Either the population type or a name"
                    + "must be specified.");
        
        // Either read population type from input or create new type
        // with chosen name:
        if (typeInput.get() != null)
            type = typeInput.get();
        else
            type = new PopulationType(popNameInput.get());
        
        // Use location if provided, else assume scalar:
        if (locationInput.get() != null) {
            int [] location = new int[locationInput.get().getDimension()];

            for (int i = 0; i<locationInput.get().getDimension(); i++)
                location[i] = locationInput.get().getValue(i);
            
            this.offset = type.locToOffset(location);
        } else
            this.offset = 0;
        
        precalculateHash();
    }
    
    /**
     * Create a new population specifier.
     *
     * @param populationType Type of this population.
     * @param location Location/identifier of population.
     */
    public Population(PopulationType populationType, int... location) {
        this.type = populationType;

        if (populationType.nPops == 1) {
            this.offset = 0;
        } else {
            this.offset = populationType.locToOffset(location);
        }

        precalculateHash();
    }

    /**
     * Create new scalar population with its own type.
     *
     * @param populationName
     */
    public Population(String populationName) {
        this.type = new PopulationType(populationName);
        this.offset = 0;
        
        precalculateHash();
    }
    
    /**
     * Pre-calculate hash code.
     */
    private void precalculateHash() {
        hash = 7;
        hash = 89 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 89 * hash + this.offset;
    }

    /**
     * Obtain type of this population.
     *
     * @return population type
     */
    public PopulationType getType() {
        return this.type;
    }

    /**
     * Retrieve location vector for this population.
     *
     * @return Location of this population.
     */
    public int[] getLocation() {
        return this.type.offsetToLoc(offset);
    }

    /**
     * @return true if population is the sole member of its type.
     */
    public boolean isScalar() {
        return this.type.dims.length == 0;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;

        if (other instanceof Population) {
            Population otherPop = (Population) other;
            //result = ((type == otherPop.type) && (offset == otherPop.offset));
            result = (hash == otherPop.hashCode());
        }

        return result;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(type.name);

        if (!isScalar()) {
            sb.append("[");
            int[] loc = type.offsetToLoc(offset);
            for (int i = 0; i < loc.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(loc[i]);
            }
            sb.append("]");
        }

        return sb.toString();
    }
}
