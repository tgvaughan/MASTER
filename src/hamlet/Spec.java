package hamlet;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * A basic birth-death simulation specification.
 *
 * @author Tim Vaughan
 */
public abstract class Spec {

    // Birth-death model to simulate:
    Model model;
    
    // Length of time to propagate for:
    double simulationTime;
    
    // Integrator to use:
    Integrator integrator;
    
    // Number of evenly spaced samples times
    int nSamples;
    
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

    public void setIntegrator(Integrator integrator) {
        this.integrator = integrator;
    }

    public void setnSamples(int nSamples) {
        this.nSamples = nSamples;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    public void setInitState(State initState) {
        this.initState = initState;
    }

    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    /*
     * Getters:
     */
    public Model getModel() {
        return model;
    }
    
    public Integrator getIntegrator() {
        return integrator;
    }

    public int getnSamples() {
        return nSamples;
    }

    public long getSeed() {
        return seed;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    /**
     * Get time between samples.
     *
     * @return Sampling period.
     */
    @JsonIgnore
    public double getSampleDt() {
        return simulationTime/(nSamples-1);
    }

}
