package viralPopGen;

import java.util.*;

// COLT RNG classes:
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.MersenneTwister;

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
	
	// Record of simulation parameters:
	Model model;
	State initState;
	double T;
	int Nt, Nsamples, Ntraj;
	int seed;
	
	/**
	 * Generate trajectory ensemble.
	 * 
	 * @param model		Model to use.
	 * @param initState	Initial state of trajectories.
	 * @param T			Integration period.
	 * @param Nt		Number of time steps for each trajectory.
	 * @param Nsamples	Number of time points to record.
	 * @param Ntraj		Number of trajectories to evaluate.
	 */
	public Ensemble (Model model, State initState, double T, int Nt,
			int Nsamples, int Ntraj, int seed) {
		
		// Keep copy of simulation parameters with ensemble:
		this.model = model;
		this.initState = initState;
		this.T = T;
		this.Nt = Nt;
		this.Nsamples = Nsamples;
		this.Ntraj = Ntraj;
		this.seed = seed;
		
		// Initialise RNG:
		RandomEngine engine = new MersenneTwister(seed);
		
		// Initialise trajectory list:
		trajectories = new ArrayList<Trajectory>(Ntraj);
		
		// Generate trajectories:
		for (int traj=0; traj<Ntraj; traj++) {
			
			Trajectory thisTraj = new Trajectory(model,
					initState, T, Nt, Nsamples, engine);
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