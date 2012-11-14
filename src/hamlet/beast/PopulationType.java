package hamlet.beast;

import java.util.*;
import beast.core.*;
import beast.core.parameter.*;

/**
 * Beast 2 plugin representing a generic population.
 *
 * @author Tim Vaughan
 *
 */
@Description("Population type involved in a birth-death model.")
public class PopulationType extends Plugin {

    public Input<String> nameInput = new Input<String>("name", "Name of population");
    public Input<IntegerParameter> dimsInput = new Input<IntegerParameter>("dim",
            "Vector containing the dimensions of an n-D array of individual populations of this type.");

    // True population type object:
    hamlet.PopulationType popType;

    public PopulationType() { };

    @Override
    public void initAndValidate() throws Exception {

        if (dimsInput.get()==null)
            popType = new hamlet.PopulationType(nameInput.get());
        else {
            int[] dims = new int[dimsInput.get().getDimension()];
            for (int i = 0; i<dims.length; i++)
                dims[i] = dimsInput.get().getValue(i);

            popType = new hamlet.PopulationType(nameInput.get(), dims);
        }

    }
}
