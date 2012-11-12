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
    Map<SubPopulation, Double> popSizes;

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
        for (SubPopulation sub : oldState.popSizes.keySet()) {
            popSizes.put(sub, oldState.popSizes.get(sub));
        }
    }

    /**
     * Get size of a particular sub-population.
     *
     * @param sub Specific sub-population.
     * @return Size of sub-population.
     */
    public double get(SubPopulation sub) {
        if (popSizes.containsKey(sub))
            return popSizes.get(sub);
        else
            return 0.0;
    }

    /**
     * Set size of a particular sub-population.
     *
     * @param sub SubPopulation to modify.
     * @param value Desired size.
     */
    public void set(SubPopulation sub, double value) {
        popSizes.put(sub, value);
    }

    /**
     * Set size of structureless population.
     *
     * @param p	Population to modify.
     * @param value Desired size.
     */
    public void set(Population p, double value) {
        popSizes.put(new SubPopulation(p), value);
    }

    /**
     * Add value to size of particular sub-population.
     *
     * @param sub
     * @param increment
     */
    public void add(SubPopulation sub, double increment) {
        popSizes.put(sub, popSizes.get(sub) + increment);
    }

    /**
     * Add value to size of particular sub-population specified using a
     * pre-calculated offset, truncating at zero if result is negative.
     *
     * @param sub
     * @param increment
     */
    public void addNoNeg(SubPopulation sub, double increment) {
        double newPopSize = get(sub)+increment;
        if (newPopSize<0.0)
            popSizes.put(sub, 0.0);
        else
            popSizes.put(sub, newPopSize);
    }

    /**
     * Alter state according to one ore more occurrences of a particular
     * reaction and subReaction.
     * 
     * @param reaction
     * @param subReaction
     * @param q Number of times for reaction to fire.
     */
    public void implementReaction(Reaction reaction, int subReaction, double q) {
        for (SubPopulation sub : reaction.deltaCounts.get(subReaction).keySet())
            addNoNeg(sub, q*reaction.deltaCounts.get(subReaction).get(sub));
    }

    /**
     * Obtain a very raw string representation of state.
     * 
     * @return string
     */
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();

        for (Population pop : model.pops) {
            for (SubPopulation sub : pop)
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

        for (Population pop : model.pops)
            for (SubPopulation sub : pop)
                sb.append(pop.name).append(String.valueOf(sub.offset));

        return sb.toString();
    }
}
