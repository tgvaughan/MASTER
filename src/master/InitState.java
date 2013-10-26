package master;

import beast.core.*;
import java.util.*;
import master.beast.Individual;
import master.beast.MultipleIndividuals;

/**
 * Beast 2 object representing initial system state.
 *
 * @author Tim Vaughan
 *
 */
@Description("Specification of initial system state.")
public class InitState extends BEASTObject {

    public Input<List<PopulationSize>> popSizesInput = new Input<List<PopulationSize>>(
            "populationSize",
            "Initial population size.",
            new ArrayList<PopulationSize>());
    
    public Input<List<Individual>> lineageSeedsInput = new Input<List<Individual>>(
            "lineageSeed",
            "Individual parent of a lineage to follow in an inheritance trajectory.",
            new ArrayList<Individual>());
    
    public Input<List<MultipleIndividuals>> multipleLineageSeedsInput = new Input<List<MultipleIndividuals>>(
            "lineageSeedMultiple",
            "Specify multiple identical lineage seeds.",
            new ArrayList<MultipleIndividuals>());

    List<master.inheritance.Node> initNodes;
    
    public InitState() { };

    @Override
    public void initAndValidate() {
        initNodes = new ArrayList<master.inheritance.Node>();
        for (Individual individual : lineageSeedsInput.get())
            initNodes.add(individual.node);
        
        for (MultipleIndividuals multi : multipleLineageSeedsInput.get())
            initNodes.addAll(multi.nodes);            
    }
}
