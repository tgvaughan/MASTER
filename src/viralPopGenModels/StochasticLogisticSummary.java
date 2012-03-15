package viralPopGenModels;

import viralPopGen.*;

/**
 * A stochastic logistic model of population dynamics.  Uses
 * Moment objects to summarise an ensemble in terms of means
 * and variances.
 * 
 * @author Tim Vaughan
 *
 */
public class StochasticLogisticSummary {
	
	public static void main (String[] argv) {
		
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
		birth.addReactant(X);
		birth.addProduct(X);
		birth.addProduct(X);
		birth.setRate(1.0);
		model.addReaction(birth);

		// 2X -> X
		Reaction death = new Reaction();
		death.addReactant(X);
		death.addReactant(X);
		death.addProduct(X);
		death.setRate(0.01);
		model.addReaction(death);
		
		// Define moments:
		
		Moment mX = new Moment("X",X);
		model.addMoment(mX);

		/*
		 * Set initial state:
		 */
		
		State initState = new State(model);
		initState.set(X, 1.0);
		
		/*
		 * Generate summarized ensemble:
		 */
		
		EnsembleSummary ensemble = new EnsembleSummary(model, initState,
				T, Nt, Nsamples, Ntraj, 53);
	}

}
