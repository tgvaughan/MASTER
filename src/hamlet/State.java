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
    Map<Population, Double[]> popSizes;

    /**
     * Constructor
     *
     * @param model Model defining the state space.
     */
    public State(Model model) {

        this.model = model;

        // Initialise sub-population sizes:
        popSizes = Maps.newHashMap();
        for (Population p : model.pops) {

            // Allocate sub-population size array:
            Double[] subPopSizes = new Double[p.nSubPops];

            // Initialise elements to zero:
            for (int i = 0; i<subPopSizes.length; i++)
                subPopSizes[i] = 0.0;

            // Assign to popSizes map:
            popSizes.put(p, subPopSizes);
        }

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
        for (Population p : model.pops) {
            popSizes.put(p, new Double[p.nSubPops]);
            for (int i = 0; i<p.nSubPops; i++)
                popSizes.get(p)[i] = oldState.popSizes.get(p)[i];
        }

    }

    /**
     * Get size of a particular sub-population.
     *
     * @param p	Population to interrogate.
     * @param sub	Specific sub-population location.
     * @return Size of sub-population.
     */
    public double get(Population p, int[] sub) {
        return popSizes.get(p)[p.subToOffset(sub)];
    }

    /**
     * Get size of a population using pre-calculated offset.
     *
     * @param p	Population to interrogate.
     * @param offset	Offset into sub-population size array.
     * @return Size of sub-population.
     */
    public double get(Population p, int offset) {
        return popSizes.get(p)[offset];
    }

    /**
     * Get size of structureless population.
     *
     * @param p	Population to interrogate.
     * @return Size of sub-population.
     */
    public double get(Population p) {
        return popSizes.get(p)[0];
    }

    /**
     * Set size of a particular sub-population.
     *
     * @param p	Population to modify.
     * @param loc	Specific sub-population location.
     * @param value	Desired size.
     */
    public void set(Population p, int[] loc, double value) {
        popSizes.get(p)[p.subToOffset(loc)] = value;
    }

    /**
     * Set size of a population using pre-calculated offset.
     *
     * @param p	Population whose size to set.
     * @param offset	Offset into sub-population size array.
     * @param value	Desired size.
     */
    public void set(Population p, int offset, double value) {
        popSizes.get(p)[offset] = value;
    }

    /**
     * Set size of structureless population.
     *
     * @param p	Population to modify.
     * @param value Desired size.
     */
    public void set(Population p, double value) {
        popSizes.get(p)[0] = value;
    }

    /**
     * Add value to size of particular sub-population specified using a
     * pre-calculated offset.
     *
     * @param p
     * @param offset
     * @param increment
     */
    public void add(Population p, int offset, double increment) {
        popSizes.get(p)[offset] += increment;
    }

    /**
     * Add value to size of particular sub-population specified using a
     * pre-calculated offset, truncating at zero if result is negative.
     *
     * @param p
     * @param offset
     * @param increment
     */
    public void addNoNeg(Population p, int offset, double increment) {
        double newPopSize = popSizes.get(p)[offset]+increment;
        if (newPopSize<0.0)
            popSizes.get(p)[offset] = 0.0;
        else
            popSizes.get(p)[offset] = newPopSize;
    }

    /**
     * Calculate difference between population sizes in this state and those in
     * another state. i.e. result = this-other.
     *
     * @param otherState
     * @return
     */
    public State difference(State otherState) {
        State diff = new State(this);

        for (Population p : popSizes.keySet())
            for (int i = 0; i<popSizes.get(p).length; i++)
                diff.popSizes.get(p)[i] -= otherState.popSizes.get(p)[i];

        return diff;
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
        for (Population pop : reaction.deltas.get(subReaction).getPopulationKeySet())
            for (int offset : reaction.deltas.get(subReaction).getOffsetKeySet(pop))
                addNoNeg(pop, offset,
                        q*reaction.deltas.get(subReaction).get(pop, offset));
    }

    /**
     * Obtain a string representation of state.
     * 
     * @return string
     */
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();

        for (Population p : popSizes.keySet())
            for (int i = 0; i<popSizes.get(p).length; i++)
                sb.append(" ").append(String.valueOf(popSizes.get(p)[i]));

        return sb.toString();
    }

    /**
     * Retrieve names of constituent populations.
     * 
     * @return string
     */
    public String getNames() {
        
        StringBuilder sb = new StringBuilder();

        for (Population p : popSizes.keySet())
            for (int i = 0; i<popSizes.get(p).length; i++)
                sb.append(p.name).append(String.valueOf(i));

        return sb.toString();
    }
}
