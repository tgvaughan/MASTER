package viralPopGen;

import java.util.*;

// COLT RNG classes:
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.Poisson;

/**
 * A class representing a collection of results obtained by
 * estimating moments from an ensemble of trajectories of
 * a birth-death process.  Use this class as an alternative
 * to Ensemble to avoid having to keep a record of every
 * trajectory generated during the calculation.
 * 
 * @author Tim Vaughan
 *
 */
public class EnsembleSummary {

	// Record of simulation parameters:
	Model model;
	State initState;
	double T;
	int nTimeSteps, nSamples, nTraj;
	int seed;

	// Moments to record:
	ArrayList<Moment> moments;
	
	// Ensemble-averaged state summaries:
	StateSummary[] stateSummaries;

	/**
	 * Constructor.  Assigns simulation parameters and moment list
	 * to non-static fields, performs the simulation, recording the
	 * required summary statistics.
	 * 
	 * @param model
	 * @param initState
	 * @param t
	 * @param nTimeSteps
	 * @param nSamples
	 * @param nTraj
	 * @param seed
	 */
	public EnsembleSummary(Model model, State initState, double T, int nTimeSteps,
			int nSamples, int nTraj, int seed, ArrayList<Moment> moments) {

		this.model = model;
		this.initState = initState;
		this.T = T;
		this.nTimeSteps = nTimeSteps;
		this.nSamples = nSamples;
		this.nTraj = nTraj;
		this.seed = seed;
		
		// Derived simulation parameters:
		double dt = T/(nTimeSteps-1);
		int stepsPerSample = (nTimeSteps-1)/(nSamples-1);

		// Initialise RNG:
		RandomEngine engine = new MersenneTwister(seed);
		Poisson poissonian = new Poisson(1, engine);
		
		// Initialise state summaries:
		stateSummaries = new StateSummary[nSamples];
		for (int sidx=0; sidx<nSamples; sidx++)
			stateSummaries[sidx] = new StateSummary(moments);
		
		// Loop over trajectories:
		for (int traj=0; traj<nTraj; traj++) {
			
			// Initialise system state:
			State currentState = new State(initState);
			
			// Integration loop:
			int sidx = 0;
			for (int tidx=0; tidx<nTimeSteps; tidx++) {
				
				if (tidx % stepsPerSample == 0) {
					stateSummaries[sidx].record(currentState);
					sidx++;
				}
				
				// Calculate transition rates:
				for (Reaction reaction : model.reactions) {
					reaction.calcPropensities(currentState);
				}
				
				// Update state with required changes:
				for (Reaction reaction : model.reactions) {
					reaction.leap(currentState, dt, poissonian);
				}
				
			}
		}
		
		// Normalise state summaries:
		for (StateSummary summary : stateSummaries)
			summary.normalise();

	}

	/**
	 * For debugging only.
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) {

	}

}