package hamlet.beast;

import java.util.*;
import beast.core.*;

/**
 * Beast 2 plugin representing initial system state.
 *
 * @author Tim Vaughan
 *
 */
@Description("Specification of initial system state.")
public class InitState extends Plugin {

    public Input<List<PopulationSize>> popSizesInput = new Input<List<PopulationSize>>(
            "populationSize",
            "Initial population size.",
            new ArrayList<PopulationSize>());

    public InitState() { };

    @Override
    public void initAndValidate() {
        
    }
}
