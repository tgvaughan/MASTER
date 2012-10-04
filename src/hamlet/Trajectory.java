package hamlet;

/**
 * Class of objects representing trajectories through the
 * state space of the birth-death model.
 * 
 * @author Tim Vaughan
 *
 */
public class Trajectory {

	State currentState;

	// Sampled states:
	State[] sampledStates;

	// Simulation specification:
	Spec spec;

	/**
	 * Generate trajectory of birth-death process.
	 * 
	 * @param spec Simulation specification.
	 */
	public Trajectory(Spec spec) {

		// Keep copy of simulation parameters with trajectory:
		this.spec = spec;

		// Initialise state list:
		sampledStates = new State[spec.nSamples];

		// Initialise system state:
		currentState = new State(spec.initState);

		// Derived simulation parameters:
		int stepsPerSample = (spec.nTimeSteps-1)/(spec.nSamples-1);

		// Integration loop:
		int sidx = 0;
		for (int tidx=0; tidx<spec.nTimeSteps; tidx++) {

			// Sample state if necessary:
			if (tidx % stepsPerSample == 0)
				sampledStates[sidx++] = new State(currentState);

			// Perform single time step:
			spec.integrator.step(currentState, spec);
		}

	}

	/**
	 * Dump trajectory data to stdout. (Mostly for debugging.)
	 */
	public void dump() {

		double dt = spec.getSampleDt();

		System.out.print("t");
		sampledStates[0].dumpNames();
		int sidx = 0;
		for (State s : sampledStates) {
			System.out.print(dt*(sidx++));
			s.dump();
		}
	}

}
