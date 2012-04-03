package popgen;

/**
 * Class of objects representing trajectories through the
 * state space of the birth-death model. Will include
 * methods to generate these trajectories using a variety
 * of stochastic integration algorithms.
 * 
 * @author Tim Vaughan
 *
 */
public class Trajectory {

	State currentState;

	// Sampled states:
	State[] sampledStates;

	// Simulation parameters:
	double T;
	int nTimeSteps, nSamples;
	Model model;

	/**
	 * Generate trajectory of birth-death process.
	 * 
	 * @param model			Model to implement.
	 * @param initState		Initial system state.
	 * @param T				Length of simulation.
	 * @param nTimeSteps	Number of time steps to evaluate.
	 * @param nSamples		Number of samples to record.
	 * @param engine		RNG engine to use.
	 */
	public Trajectory(Model model, State initState,
			double T, int nTimeSteps, int nSamples) {

		// Keep copy of simulation parameters with trajectory:
		this.model = model;
		this.T = T;
		this.nTimeSteps = nTimeSteps;
		this.nSamples = nSamples;

		// Initialise state list:
		sampledStates = new State[nSamples];

		// Initialise system state:
		currentState = new State(initState);

		// Derived simulation parameters:
		double dt = T/(nTimeSteps-1);
		int stepsPerSample = (nTimeSteps-1)/(nSamples-1);

		// Integration loop:
		int sidx = 0;
		for (int tidx=0; tidx<nTimeSteps; tidx++) {

			// Sample state if necessary:
			if (tidx % stepsPerSample == 0)
				sampledStates[sidx++] = new State(currentState);

			// Perform single time step:
			step(dt);

		}

	}

	/**
	 * Generate single time step.
	 * 
	 * @param dt			Time step size.
	 * @param poissonian	Poissonian RNG.
	 */
	private void step(double dt) {

		// Calculate transition rates:
		for (int r=0; r<model.reactions.size(); r++)
			model.reactions.get(r).calcPropensities(currentState);

		// Update state with required changes:
		for (int r=0; r<model.reactions.size(); r++)
			model.reactions.get(r).leap(currentState, dt);
	}

	/**
	 * Dump trajectory data to stdout. (Mostly for debugging.)
	 */
	public void dump() {

		double dt = T/(nSamples-1);

		System.out.print("t");
		sampledStates[0].dumpNames();
		int sidx = 0;
		for (State s : sampledStates) {
			System.out.print(dt*(sidx++));
			s.dump();
		}
	}

}
