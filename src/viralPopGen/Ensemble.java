package viralPopGen;

import java.util.*;

// COLT RNG classes:
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.MersenneTwister;

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
		
		/**
		 *  Simulation parameters:
		 */
		
		double T = 1.0;
		int Nt = 1001;
		int Nsamples = 1001;
		int Ntraj = 1;
		
		/**
		 * Assemble model:
		 */
		
		Model model = new Model();
		
		// Define populations:
		
		int[] seqDims = {1};
		int[] otherDims = {1};
		Population X = new Population("X", seqDims, otherDims);
		model.addPopulation(X);
		
		// Define reactions:
		
		// X -> 2X
		Reaction birth = new Reaction();
		int[] loc = {0,0};
		birth.addReactant(X, loc);
		birth.addProduct(X, loc);
		birth.addProduct(X, loc);
		double[] birthRate = {1.0};
		birth.setRate(birthRate);
		model.addReaction(birth);
		
		// X -> 0
		Reaction death = new Reaction();
		death.addReactant(X, loc);
		death.addProduct(X, loc);
		double[] deathRate = {0.01};
		death.setRate(deathRate);
		model.addReaction(death);
		
		/**
		 * Set initial state:
		 */
		State initState = new State(model);
		initState.setSize("X", loc, 10.0);
		
		/**
		 * Generate ensemble
		 */
		Ensemble ensemble = new Ensemble(model, initState,
				T, Nt, Nsamples, Ntraj, 4253);
		
		/**
		 * Dump first trajectory to stdout:
		 */
		//ensemble.trajectories.get(0).dump();
		
	}
}