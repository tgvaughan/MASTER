package hamlet;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * Class of objects representing trajectories through the
 * state space of the birth-death model.
 * 
 * @author Tim Vaughan
 *
 */
public class Trajectory {

	// List of sampled states:
	List<State> sampledStates;

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
		sampledStates = Lists.newArrayList();

		// Initialise system state:
		State currentState = new State(spec.initState);

		// Derived simulation parameters:
                double sampleDt = spec.getSampleDt();

		// Integration loop:
		for (int sidx=0; sidx<spec.nSamples; sidx++) {

                    // Sample state if necessary:
                    sampledStates.add(new State(currentState));

                    // Integrate to next sample time:
                    double t=0;
                    while (t<sampleDt)
                        t+=spec.integrator.step(currentState, spec.model, sampleDt-t);
                    
		}
	}
}
