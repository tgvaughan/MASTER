package hamlet;

import com.google.common.collect.*;
import java.util.*;

/**
 * Class of objects describing distinct states of the simulated system.
 *
 * @author Tim Vaughan
 *
 */
public class State {

    Model model;
    Map<Population, Double> popSizes;

    /**
     * Constructor
     *
     * @param model Model defining the state space.
     */
    public State(Model model) {
        this.model = model;

        // Initialise sub-population sizes:
        popSizes = Maps.newHashMap();
    }

    /**
     * Copy constructor
     *
     * @param oldState State to copy.
     */
    public State(State oldState) {
        this.model = oldState.model;

        // Copy sub-population sizes:
        this.popSizes = Maps.newHashMap();
        for (Population sub : oldState.popSizes.keySet()) {
            popSizes.put(sub, oldState.popSizes.get(sub));
        }
    }

    /**
     * Get size of a particular sub-population.
     *
     * @param sub Specific sub-population.
     * @return Size of sub-population.
     */
    public double get(Population sub) {
        if (popSizes.containsKey(sub))
            return popSizes.get(sub);
        else
            return 0.0;
    }

    /**
     * Set size of a particular population.
     *
     * @param pop Population to modify.
     * @param value Desired size.
     */
    public void set(Population pop, double value) {
        popSizes.put(pop, value);
    }

    /**
     * Add value to size of particular sub-population.
     *
     * @param pop
     * @param increment
     */
    public void add(Population pop, double increment) {
        popSizes.put(pop, popSizes.get(pop) + increment);
    }

    /**
     * Add value to size of particular sub-population specified using a
     * pre-calculated offset, truncating at zero if result is negative.
     *
     * @param pop
     * @param increment
     */
    public void addNoNeg(Population pop, double increment) {
        double newPopSize = get(pop)+increment;
        if (newPopSize<0.0)
            popSizes.put(pop, 0.0);
        else
            popSizes.put(pop, newPopSize);
    }

    /**
     * Alter state according to one ore more occurrences of a particular
     * reaction and subReaction.
     * 
     * @param reactionGroup
     * @param reactionIndex
     * @param q Number of times for reaction to fire.
     */
    public void implementReaction(ReactionGroup reactionGroup, int reactionIndex, double q) {
        for (Population pop : reactionGroup.deltaCounts.get(reactionIndex).keySet())
            addNoNeg(pop, q*reactionGroup.deltaCounts.get(reactionIndex).get(pop));
    }

    /**
     * Obtain a very raw string representation of state.
     * 
     * @return string
     */
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();

        for (PopulationType pop : model.types) {
            for (Population sub : pop)
                sb.append(" ").append(String.valueOf(get(sub)));
        }

        return sb.toString();
    }

    /**
     * Retrieve names of constituent populations.
     * 
     * @return string
     */
    public String getNames() {
        
        StringBuilder sb = new StringBuilder();

        for (PopulationType popType : model.types)
            for (Population pop : popType)
                sb.append(popType.name).append(String.valueOf(pop.offset));

        return sb.toString();
    }
}
