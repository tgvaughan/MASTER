package master.model;

import beast.core.BEASTObject;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.parameter.IntegerParameter;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Class of objects describing distinct populations within the model. These
 * populations may be scalar or may involve genetically distinct
 * sub-populations.
 *
 * @author Tim Vaughan
 *
 */
public class PopulationType extends BEASTObject implements Iterable<Population> {
    
    public Input<String> nameInput = new Input<String>("typeName",
            "Name of population", Validate.REQUIRED);
    public Input<IntegerParameter> dimsInput = new Input<IntegerParameter>("dim",
            "Vector containing the dimensions of an n-D array of individual populations of this type.");
    
    String name; // Population type name
    int[] dims; // Bounds of vectors specifying individual populations
    int nPops; // Total number of populations posessing this type
    
    /**
     * Default constructor.
     */
    public PopulationType() { }
    
    @Override
    public void initAndValidate() {
        
        name = nameInput.get();
        
        if (dimsInput.get()==null)
            dims = new int[0];
        else {
            dims = new int[dimsInput.get().getDimension()];
            for (int i = 0; i<dims.length; i++)
                dims[i] = dimsInput.get().getValue(i);
        }
        
        nPops = 1;
        for (int i=0; i<dims.length; i++)
            nPops *= dims[i];
    }
    
    /**
     * Define a population type.
     *
     * @param name Population name.
     * @param dims Sub-population structure.
     */
    public PopulationType(String name, int... dims) {
        this.name = name;
        this.dims = dims;
        
        nPops = 1;
        for (int i=0; i<dims.length; i++)
            nPops *= dims[i];
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
        
        int[] multipliers = new int[dims.length];
        int m=1;
        for (int i=0; i<multipliers.length; i++) {
            multipliers[i] = m;
            m *= dims[i];
        }
        
        int remainder = offset;
        for (int i=dims.length-1; i>=0; i--) {
            location[i] = remainder/multipliers[i];
            remainder = remainder%multipliers[i];
        }
        
        return location;
    }
    
    /**
     * Returns true iff location is within bounds of population type.
     * 
     * @param location
     * @return true if location is valid
     */
    public boolean containsLocation(int[] location) {
        
        if (location.length != dims.length)
            return false;
        
        for (int i=0; i<dims.length; i++) {
            if (location[i] >= dims[i])
                return false;
        }
        
        return true;
    }

    public String getName() {
        return name;
    }

    public int[] getDims() {
        return dims;
    }
    
    /**
     * @return true if only one deme has this population type.
     */
    public boolean isScalar() {
        return dims.length == 0;
    }

    @Override
    public Iterator<Population> iterator() {
        return new PopulationIterator(this);
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Construct representation of specification to use in assembling
     * summary in JSON output file.
     * 
     * @return Map from strings to other objects which have a JSON rep
     */
    @JsonValue
    public Map<String, Object> getJsonValue() {
        
        Map<String, Object> jsonObject = Maps.newHashMap();
        
        jsonObject.put("name", getName());
        jsonObject.put("dimensions", getDims());
        
        return jsonObject;
    }
}