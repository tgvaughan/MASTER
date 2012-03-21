package viralPopGen;

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
	
	// Record of simulation parameters:
	Model model;
	State initState;
	double simulationTime;
	int nTimeSteps, nSamples, nTraj;
	long seed;
	
	/**
	 * Generate trajectory ensemble.
	 * 
	 * @param model				Model to use.
	 * @param initState			Initial state of trajectories.
	 * @param simulationTime	Integration period.
	 * @param nTimeSteps		Number of time steps for each trajectory.
	 * @param nSamples			Number of time points to record.
	 * @param nTraj				Number of trajectories to evaluate.
	 * @param seed				RNG seed (<0 means do not set).
	 */
	public Ensemble (Model model, State initState, double simulationTime,
			int nTimeSteps, int nSamples, int nTraj, int seed) {
		
		// Keep copy of simulation parameters with ensemble:
		this.model = model;
		this.initState = initState;
		this.simulationTime = simulationTime;
		this.nTimeSteps = nTimeSteps;
		this.nSamples = nSamples;
		this.nTraj = nTraj;
		this.seed = seed;
		
		// Set RNG seed unless seed<0:
		if (seed>=0)
			Randomizer.setSeed(seed);

		// Initialise trajectory list:
		trajectories = new ArrayList<Trajectory>(nTraj);
		
		// Generate trajectories:
		for (int traj=0; traj<nTraj; traj++) {
			
			Trajectory thisTraj = new Trajectory(model,
					initState, simulationTime, nTimeSteps, nSamples);
			trajectories.add(thisTraj);
			
		}
		
	}

	/**
	 * Generate trajectory ensemble using default BEAST RNG seed.
	 * 
	 * @param model		Model to use.
	 * @param initState	Initial state of trajectories.
	 * @param simulationTime			Integration period.
	 * @param nTimeSteps		Number of time steps for each trajectory.
	 * @param nSamples	Number of time points to record.
	 * @param nTraj		Number of trajectories to evaluate.
	 */
	public Ensemble (Model model, State initState, double simulationTime,
			int nTimeSteps, int nSamples, int nTraj) {
		
		// Call main constructor with seed=-1: instructs constructor
		// not to use seed.
		this(model, initState, simulationTime,
				nTimeSteps, nSamples, nTraj, -1);
	}
	
	/**
	 * Raw dump of the ensemble contents.  For debugging.
	 */
	public void dump () {
		for (Trajectory trajectory : trajectories)
			trajectory.dump();
	}
}