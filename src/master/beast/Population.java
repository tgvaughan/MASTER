package master.beast;

import beast.core.*;
import beast.core.parameter.*;

/**
 * Beast 2 plugin representing a specific sub-population.
 *
 * @author Tim Vaughan
 *
 */
@Description("Specific population of a chosen type.")
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
    
    // Hamlet population object:
    master.Population pop;

    public Population() { };

    @Override
    public void initAndValidate() throws Exception {
        
        // Ensure that one of either the population type or its name
        // is specified:
        if (typeInput.get() == null && popNameInput.get() == null)
            throw new RuntimeException("Either the population type or a name"
                    + "must be specified.");
        
        // Either read population type from input or create new type
        // with chosen name:
        master.PopulationType popType;
        if (typeInput.get() != null)
            popType = typeInput.get().popType;
        else
            popType = new master.PopulationType(popNameInput.get());
        
        // Use location if provided, else assume scalar:
        if (locationInput.get() != null) {
            int [] location = new int[locationInput.get().getDimension()];

            for (int i = 0; i<locationInput.get().getDimension(); i++)
                location[i] = locationInput.get().getValue(i);
            
            pop = new master.Population(popType, location);
        } else
            pop = new master.Population(popType);
    }
}
