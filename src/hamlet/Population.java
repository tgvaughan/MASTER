package hamlet;

/**
 * Class of objects describing distinct populations within the model. These
 * populations may be scalar or may involve genetically distinct
 * sub-populations.
 *
 * @author Tim Vaughan
 *
 */
public class Population {

    String name; // Population name
    int[] dims; // Structural space dimensions
    int nSubPops; // Total number of sub-populations
    
    /**
     * Define a population.
     *
     * @param name Population name.
     * @param dims Sub-population structure.
     */
    public Population(String name, int... dims) {
        this.name = name;

        if (dims.length==0) {
            this.dims = new int[1];
            dims[0] = 1;
            nSubPops = 1;
        } else {
            this.dims = dims;
            nSubPops = 1;
            for (int i = 0; i<dims.length; i++)
                nSubPops *= dims[i];
        }
    }

    /**
     * Get offset into sub-population sizes vector.
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
                        "Subpopulation location out of bounds.");
            offset += m*location[i];
            m *= dims[i];
        }

        return offset;
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
}