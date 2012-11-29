package hamlet.beast;

import beast.core.*;
import java.util.*;

/**
 * Beast 2 plugin representing a general birth-death model.
 *
 * @author Tim Vaughan
 *
 */
@Description("Specification of a birth-death model.")
public class Model extends Plugin {

    public Input<List<PopulationType>> populationTypesInput = new Input<List<PopulationType>>(
            "populationType",
            "Population type involved in the birth-death process.",
            new ArrayList<PopulationType>());
    
    public Input<List<Population>> populationsInput = new Input<List<Population>>(
            "population",
            "Population involved in the birth-death process.",
            new ArrayList<Population>());
    
    public Input<List<ReactionGroup>> reactionGroupsInput = new Input<List<ReactionGroup>>(
            "reactionGroup",
            "Group of reactions involved in the birth-death process.",
            new ArrayList<ReactionGroup>());
    
    public Input<List<Reaction>> reactionsInput = new Input<List<Reaction>>(
            "reaction",
            "Individual reactions involved in the birth-death process.",
            new ArrayList<Reaction>());
    
    // True model object:
    hamlet.Model model;

    public Model() { };

    @Override
    public void initAndValidate() throws Exception {

        model = new hamlet.Model();

        // Add population types to model:
        for (PopulationType popType : populationTypesInput.get())
            model.addPopulationType(popType.popType);
        
        // Add population types corresponding to individual populations to model:
        for (Population pop : populationsInput.get())
            model.addPopulation(pop.pop);

        // Add reaction groups to model:
        for (ReactionGroup reactGroup : reactionGroupsInput.get())
            model.addReactionGroup(reactGroup.reactionGroup);

        // Add individual reactions to model:
        for (Reaction react : reactionsInput.get()) {

            hamlet.Reaction reaction;
            if (react.name!=null)
                reaction = new hamlet.Reaction(react.nameInput.get());
            else
                reaction = new hamlet.Reaction();

            reaction.setReactantSchema(react.reactants);
            reaction.setProductSchema(react.products);

            if (react.rate>=0)
                reaction.addRate(react.rate);
            else
                throw new RuntimeException("Reaction does not specify reaction rate.");
            
            model.addReaction(reaction);
        }
    }
}
