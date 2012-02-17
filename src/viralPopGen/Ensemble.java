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
		
		// Initialise trajectory arraylist:
		trajectories = new ArrayList<Trajectory>(Ntraj);
		
		// Generate trajectories:
		for (int traj=0; traj<Ntraj; traj++) {
			
			Trajectory thisTraj = new Trajectory(model,
					initState, T, Nt, Nsamples, engine);
			trajectories.add(thisTraj);
			
		}
		
	}

	/**
	 * For debugging only.
	 * 
	 * @param argv
	 */
	public static void main (String[] argv) {
		
		/*
		 *  Simulation parameters:
		 */
		
		double T = 1.0;
		int Nt = 1001;
		int Nsamples = 1001;
		int Ntraj = 1;
		
		/*
		 * Assemble model:
		 */

		int L = 105;
		int[] seqDims = {105};
		Model model = new Model(L, seqDims);

		// Define populations:

		Population X = new Population("X", false);
		model.addPopulation(X);

		// Define reactions:

		// X -> 2X
		Reaction birth = new Reaction();
		birth.addReactant(X);
		birth.addProduct(X, false);
		birth.addProduct(X, false);
		double[] birthRate = {1.0};
		birth.setRate(birthRate);
		model.addReaction(birth);

		// X -> 0
		Reaction death = new Reaction();
		death.addReactant(X);
		death.addProduct(X, false);
		double[] deathRate = {0.01};
		death.setRate(deathRate);
		model.addReaction(death);

		/*
		 * Set initial state:
		 */
		State initState = new State(model);
		initState.setScalar(X, 10.0);

		/*
		 * Generate ensemble
		 */
		Ensemble ensemble = new Ensemble(model, initState,
				T, Nt, Nsamples, Ntraj, 4253);

		/*
		 * Dump first trajectory to stdout:
		 */
		ensemble.trajectories.get(0).dump();

	}
}