package hamlet;

/**
 * Class of objects representing trajectories through the
 * state space of the birth-death model.
 * 
 * @author Tim Vaughan
 *
 */
public class Trajectory {

	// Sampled states:
	State[] sampledStates;

	// Simulation specification:
	TrajectorySpec spec;

	/**
	 * Generate trajectory of birth-death process.
	 * 
	 * @param spec Simulation specification.
	 */
	public Trajectory(TrajectorySpec spec) {

		// Keep copy of simulation parameters with trajectory:
		this.spec = spec;

		// Initialise state list:
		sampledStates = new State[spec.nSamples];

		// Initialise system state:
		State currentState = new State(spec.initState);

		// Derived simulation parameters:
                double sampleDt = spec.getSampleDt();

		// Integration loop:
		for (int sidx=0; sidx<spec.nSamples; sidx++) {

                    // Sample state if necessary:
                    sampledStates[sidx] = new State(currentState);

                    // Perform single time step:
                    spec.integrator.step(currentState, spec.model, sampleDt);
		}

	}

	/**
	 * Dump trajectory data to stdout. (Mostly for debugging.)
	 */
	public void dump() {

		double dt = spec.getSampleDt();

		System.out.print("t ");
		System.out.println(sampledStates[0].getNames());
		int sidx = 0;
		for (State s : sampledStates) {
			System.out.print(dt*(sidx++) + " ");
			System.out.println(s);
		}
	}

}
