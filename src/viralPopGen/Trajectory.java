package viralPopGen;

import cern.jet.random.Poisson;
import cern.jet.random.engine.RandomEngine;

public class Trajectory {
	
	// Initial state:
	State initState;
	
	// Integration time:
	double T;
	
	// Step size:
	int Nt;
	
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

}
