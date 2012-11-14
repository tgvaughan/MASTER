package hamlet.beast;

import java.util.*;
import beast.core.*;
import beast.core.parameter.*;

/**
 * Beast 2 plugin representing a specific sub-population.
 *
 * @author Tim Vaughan
 *
 */
@Description("Specific population of a chosen type.")
public class Population extends Plugin {

    public Input<PopulationType> typeInput = new Input<PopulationType>(
            "type",
            "Type to which this population belongs.", Input.Validate.REQUIRED);
    public Input<IntegerParameter> locationInput = new Input<IntegerParameter>(
            "location",
            "Vector specifying location of specific population.");
    
    // Population object:
    hamlet.Population pop;

    public Population() { };

    @Override
    public void initAndValidate() throws Exception {
        
        if (locationInput.get() != null) {
            int [] location = new int[locationInput.get().getDimension()];

            for (int i = 0; i<locationInput.get().getDimension(); i++)
                location[i] = locationInput.get().getValue(i);
            
            pop = new hamlet.Population(typeInput.get().popType, location);
        } else
            pop = new hamlet.Population(typeInput.get().popType);
    }
}
