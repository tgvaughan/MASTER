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

    // Populations in model:
    List<Population> pops;
    
    // Reactions to model:
    List<Reaction> reactions;

    /**
     * Model constructor.
     */
    public Model() {
        pops = new ArrayList<Population>();
        reactions = new ArrayList<Reaction>();
    }

    /**
     * Add population to model.
     *
     * @param pop Population to add.
     */
    public void addPopulation(Population pop) {
        pops.add(pop);
    }

    /**
     * Add reaction to model.
     *
     * @param react Reaction to add.
     */
    public void addReaction(Reaction react) {
        react.postSpecInit();
        reactions.add(react);
    }

    /*
     * Getters for JSON object mapper
     */
    public List<Population> getPopulations() {
        //return pops.toArray(new Population[pops.size()]);
        return pops;
    }

    public List<Reaction> getReactions() {
        //return reactions.toArray(new Reaction[reactions.size()]);
        return reactions;
    }
}