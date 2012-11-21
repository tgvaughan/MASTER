package hamlet;

import java.util.Iterator;

/**
 * Class of objects describing distinct populations within the model. These
 * populations may be scalar or may involve genetically distinct
 * sub-populations.
 *
 * @author Tim Vaughan
 *
 */
public class PopulationType implements Iterable<Population> {

    String name; // Population type name
    int[] dims; // Bounds of vectors specifying individual populations
    int nPops; // Total number of populations posessing this type
    
    /**
     * Define a population type.
     *
     * @param name Population name.
     * @param dims Sub-population structure.
     */
    public PopulationType(String name, int... dims) {
        this.name = name;

        if (dims.length==0) {
            this.dims = new int[1];
            this.dims[0] = 1;
            nPops = 1;
        } else {
            this.dims = dims;
            nPops = 1;
            for (int i = 0; i<dims.length; i++)
                nPops *= dims[i];
        }
    }

    /**
     * Get offset into population sizes vector.
     *
     * @param location Location of sub-population.
     * @return Offset.
     */
    public int locToOffset(int[] location) {
        int offset = 0;

        int m = 1;
        for (int i = 0; i<location.length; i++) {
            if (location[i]>=dims[i])
                throw new IndexOutOfBoundsException(
                        "Population location out of bounds.");
            offset += m*location[i];
            m *= dims[i];
        }

        return offset;
    }
    
    /**
     * Reverse translation of offset into population sizes vector back to the
     * original location array.  Only used by a couple of toString() methods
     * and potentially when writing output to disk.
     * 
     * @param offset
     * @return Location array.
     */
    public int[] offsetToLoc(int offset) {
        int[] location = new int[dims.length];
        
        int m=1;
        for (int i=0; i<dims.length; i++) {
            m *= dims[i];
            location[i] = offset%m;
        }
        
        return location;
    }

    /*
     * Getters for JSON object mapper
     */
    public String getName() {
        return name;
    }

    public int[] getDims() {
        return dims;
    }

    @Override
    public Iterator<Population> iterator() {
        return new PopulationIterator(this);
    }
}