package viralPopGen;

/**
 * Class of objects describing simulations.
 *
 * @author Tim Vaughan
 */
public class Simulation {

	// Birth-death model to simulate:
	Model model;

	// Length of time to propagate for:
	double simulationTime;

	// Number of integration steps:
	int nTimeSteps;

	// Number of evenly spaced samples times
	// (relevant to EnsembleSummary only):
	int nSamples;

	// Number of stochastic trajectories to generate:
	int nTraj;

	// Seed for RNG (negative number means use default seed):
	long seed;

	// Initial state of system:
	State initState;

	// Verbose reportage on progress of simulation:
	boolean verbose;

	/*
	 * Constructor:
	 */
	public Simulation () {
		this.verbose = false;
	}

	/*
	 * Setters:
	 */

	public void setModel(Model model) {
		this.model = model;
	}

	public void setnSamples(int nSamples) {
		this.nSamples = nSamples;
	}

	public void setnTimeSteps(int nTimeSteps) {
		this.nTimeSteps = nTimeSteps;
	}

	public void setnTraj(int nTraj) {
		this.nTraj = nTraj;
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

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/*
	 * Getters:
	 * (Really only provided for the benifit of the JSON object mapper.)
	 */

	public int getnSamples() {
		return nSamples;
	}

	public int getnTimeSteps() {
		return nTimeSteps;
	}

	public int getnTraj() {
		return nTraj;
	}

	public long getSeed() {
		return seed;
	}

	public double getSimulationTime() {
		return simulationTime;
	}

	/**
	 * Get integration time step size.
	 * 
	 * @return Time step size.
	 */
	public double getDt() {
		return simulationTime/(nTimeSteps-1);
	}

	/**
	 * Get time between samples.
	 * 
	 * @return Sampling period.
	 */
	public double getSampleDt() {
		return simulationTime/(nSamples-1);
	}

	/**
	 * Get the number of time increments between samples.
	 * 
	 * @return Integration steps per sample.
	 */
	public int getStepsPerSample() {
		return (nTimeSteps-1)/(nSamples-1);
	}
	
}
