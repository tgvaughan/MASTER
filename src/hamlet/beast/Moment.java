package hamlet.beast;

import beast.core.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Vaughan
 */
@Description("Specification of a summary statistic to calculate.")
public class Moment extends Plugin {
    
    public Input<String> nameInput = new Input<String>("name",
            "Name of moment. (Overridden by moment group name.)");
    
    public Input<List<Population>> factorInput = new Input<List<Population>>(
            "factor",
            "Population whose size will be factored into moment calculation.",
            new ArrayList<Population>());


    public Moment() { };

    @Override
    public void initAndValidate() throws Exception {

        
        
    }
}
