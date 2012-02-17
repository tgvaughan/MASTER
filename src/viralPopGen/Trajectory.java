package viralPopGen;

import cern.jet.random.Poisson;
import cern.jet.random.engine.RandomEngine;

/**
 * Class of objects representing trajectories through the
 * state space of the birth-death model. Will include
 * methods to generate these trajectories using a variety
 * of stochastic integration algorithms.
 * 
 * @author Tim Vaughan
 *
 */
public class Trajectory {

	State currentState;

	// Sampled states:
	State[] sampledStates;

	// Simulation parameters:
	double T;
	int Nt, Nsamples;
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
		
		// Keep copy of simulation parameters with trajectory:
		this.model = model;
		this.T = T;
		this.Nt = Nt;
		this.Nsamples = Nsamples;
		
		// Initialise Poissonian RNG:
		poissonian = new Poisson(1, engine);
		
		// Initialise state list:
		sampledStates = new State[Nsamples];
		
		// Initialise system state:
		currentState = initState;
		
		// Derived simulation parameters:
		double dt = T/(Nt-1);
		int stepsPerSample = (Nt-1)/(Nsamples-1);

		// Integration loop:
		int sidx = 0;
		for (int tidx=0; tidx<Nt; tidx++) {

			// Sample state if necessary:
			if (tidx % stepsPerSample == 0)
				sampledStates[sidx++] = currentState;
		
			// Perform single time step:
			step(dt, poissonian);

		}

	}

	/**
	 * Generate single time step.
	 * 
	 * @param dt			Time step size.
	 * @param poissonian	Poissonian RNG.
	 */
	private void step(double dt, Poisson poissonian) {

		// Calculate transition rates:

		for (int r=0; r<model.reactions.size(); r++) {

			//model.reactions.get(r).calcPropensities();

		}

	}

	/**
	 * Dump trajectory data to stdout. (Mostly for debugging.)
	 */
	public void dump() {

	}
}
