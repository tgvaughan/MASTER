package viralPopGen;

import cern.jet.random.Poisson;
import cern.jet.random.engine.RandomEngine;

public class Trajectory {
	
	// Initial state:
	State initState;
	
	// Sampled states:
	State[] states;
	
	// Integration time:
	double T;
	
	// Number of time steps:
	int Nt;
	
	// Number of samples:
	int Nsamples;
	
	// Model:
	Model model;
	
	// Poissonian RNG:
	Poisson poissonian;
	
	/**
	 * Generate trajectory of birth-death process.
	 * 
	 * @param model		Model to implement.
	 * @param initState	Initial system state.
	 * @param T			Length of simulation.
	 * @param Nt		Number of time steps to evaluate.
	 * @param Nsamples	Number of samples to record.
	 * @param engine	RNG engine to use.
	 */
	public Trajectory(Model model, State initState,
			double T, int Nt, int Nsamples, RandomEngine engine) {
		
		// Initialise Poissonian RNG:
		poissonian = new Poisson(1, engine);
		
	}

	/**
	 * Dump trajectory data to stdout. (Mostly for debugging.)
	 */
	public void dump() {
		
		for (int s=0; s<states.length; s++) {
			double t = s*T/(Nsamples-1);
			System.out.print(t);
			for (int p=0; p<initState.populations.size(); p++) {
				for (int i=0; i<initState.popSizes[p].length; i++) {
					System.out.print(" "+states[s].popSizes[p][i]);
				}
			}
			System.out.println();
		}
		
	}
}
