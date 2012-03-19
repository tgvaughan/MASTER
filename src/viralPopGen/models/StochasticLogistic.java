package viralPopGen.models;

import viralPopGen.*;

/**
 * Implements a stochastic logistic model of population dynamics.
 * 
 * @author Tim Vaughan
 *
 */
public class StochasticLogistic {
	
	public static void main(String[] argv) {
		
		/*
		 *  Simulation parameters:
		 */
		
		double T = 100.0;
		int Nt = 10001;
		int Nsamples = 1001;
		int Ntraj = 1;
		
		/*
		 * Assemble model:
		 */

		Model model = new Model();

		// Define populations:

		Population X = new Population("X");
		model.addPopulation(X);

		// Define reactions:

		// X -> 2X
		Reaction birth = new Reaction();
		birth.setReactantSchema(X);
		birth.setProductSchema(X,X);
		birth.setRate(1.0);
		model.addReaction(birth);

		// 2X -> X
		Reaction death = new Reaction();
		death.setReactantSchema(X,X);
		death.setProductSchema(X);
		death.setRate(0.01);
		model.addReaction(death);

		/*
		 * Set initial state:
		 */

		State initState = new State(model);
		initState.set(X, 1.0);

		/*
		 * Generate ensemble
		 */

		Ensemble ensemble = new Ensemble(model, initState, T, Nt, Nsamples, Ntraj, 53);

		/*
		 * Dump first trajectory to stdout:
		 */

		ensemble.dump();
		
	}

}
