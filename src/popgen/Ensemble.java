package popgen;

import java.util.*;
import beast.util.Randomizer;

/**
 * A class representing an ensemble of stochastic trajectories
 * through the state space of a population genetics model of viral
 * dynamics.
 * 
 * @author Tim Vaughan
 *
 */
public class Ensemble {

	// The ensemble is a large number of trajectories:
	ArrayList<Trajectory> trajectories;

	// Local record of simulation spec:
	EnsembleSpec spec;

	/**
	 * Generate trajectory ensemble.
	 * 
	 * @param spec Simulation specification.
	 */
	public Ensemble (EnsembleSpec spec) {

		// Set RNG seed unless seed<0:
		if (spec.seed>=0)
			Randomizer.setSeed(spec.seed);

		// Initialise trajectory list:
		trajectories = new ArrayList<Trajectory>(spec.nTraj);

		// Generate trajectories:
		for (int traj=0; traj<spec.nTraj; traj++) {

			Trajectory thisTraj = new Trajectory(spec);
			trajectories.add(thisTraj);

		}

	}

	/**
	 * Raw dump of the ensemble contents.  For debugging.
	 */
	public void dump () {
		for (Trajectory trajectory : trajectories)
			trajectory.dump();
	}
}