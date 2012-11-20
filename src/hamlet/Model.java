package hamlet;

import java.util.*;

/**
 * Class describing a birth-death viral population genetics model. Basically a
 * container class including a sets of population and reaction objects.
 *
 * @author Tim Vaughan
 *
 */
public class Model {

    // Population types in model:
    List<PopulationType> types;
    
    // Reaction groups:
    List<ReactionGroup> reactionGroups;

    /**
     * Model constructor.
     */
    public Model() {
        types = new ArrayList<PopulationType>();
        reactionGroups = new ArrayList<ReactionGroup>();
    }

    /**
     * Add population type to model.
     *
     * @param popType Population type to add.
     */
    public void addPopulationType(PopulationType popType) {
        types.add(popType);
    }
    
    /**
     * Add a number of population types to model.
     * 
     * @param popTypes vararg array of population type objects
     */
    public void addPopulationTypes(PopulationType ... popTypes) {
        for (PopulationType popType : popTypes)
            addPopulationType(popType);
    }
    
    /**
     * Add a type corresponding to given population to model.
     * 
     * @param pop 
     */
    public void addPopulation(Population pop) {
        types.add(pop.type);
    }
    
    /**
     * Add a types corresponding to given populations to model.
     * 
     * @param pops 
     */
    public void addPopulations(Population... pops) {
        for (Population pop : pops)
            addPopulation(pop);
    }

    /**
     * Add reaction group to model.
     *
     * @param reactGroup Reaction to add.
     */
    public void addReactionGroup(ReactionGroup reactGroup) {
        reactGroup.postSpecInit();
        reactionGroups.add(reactGroup);
    }
    
    /**
     * Add reaction to model.
     * 
     * @param react 
     */
    public void addReaction(Reaction react) {
        react.postSpecInit();
        reactionGroups.add(react);
    }

    /*
     * Getters:
     */
    public List<PopulationType> getPopulationTypes() {
        return types;
    }

    public List<ReactionGroup> getReactionGroups() {
        return reactionGroups;
    }
}