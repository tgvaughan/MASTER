package master.beast;

import beast.core.*;
import beast.core.parameter.*;

/**
 * Beast 2 plugin representing a generic population.
 *
 * @author Tim Vaughan
 *
 */
@Description("Population type involved in a birth-death model.")
public class PopulationType extends BEASTObject {

    public Input<String> nameInput = new Input<String>("typeName", "Name of population");
    public Input<IntegerParameter> dimsInput = new Input<IntegerParameter>("dim",
            "Vector containing the dimensions of an n-D array of individual populations of this type.");

    // True population type object:
    master.PopulationType popType;

    public PopulationType() { };

    @Override
    public void initAndValidate() throws Exception {

        if (dimsInput.get()==null)
            popType = new master.PopulationType(nameInput.get());
        else {
            int[] dims = new int[dimsInput.get().getDimension()];
            for (int i = 0; i<dims.length; i++)
                dims[i] = dimsInput.get().getValue(i);

            popType = new master.PopulationType(nameInput.get(), dims);
        }

    }
}
