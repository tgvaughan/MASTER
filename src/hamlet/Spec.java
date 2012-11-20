package hamlet;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * A basic birth-death simulation specification.
 *
 * @author Tim Vaughan
 */
public class Spec {

    // Birth-death model to simulate:
    Model model;

    // Seed for RNG (negative number means use default seed):
    long seed;
    
    // Initial state of system:
    State initState;
    
    // Verbosity level for reportage on progress of simulation:
    int verbosity;

    /*
     * Constructor:
     */
    public Spec() {

        // Spec progress reportage off by default:
        this.verbosity = 0;

        // Use BEAST RNG seed unless specified:
        this.seed = -1;

    }

    /*
     * Setters:
     */
    public void setModel(Model model) {
        this.model = model;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setInitState(State initState) {
        this.initState = initState;
    }

    public void setVerbosity(int verbosity) {
        if (verbosity<0 || verbosity>3)
            throw new IllegalArgumentException("Verbosity number must be between 0 and 3.");

        this.verbosity = verbosity;
    }

    /*
     * Getters:
     */
    public Model getModel() {
        return model;
    }

    public long getSeed() {
        return seed;
    }
    
    public State getInitState() {
        return initState;
    }

}
