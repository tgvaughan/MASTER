package viralPopGen.models;

import viralPopGen.*;

/**
 * Implements a basic stochastic migration model to test
 * the ability of viralPopGen to handle structured populations.
 * 
 * @author Tim Vaughan
 *
 */
public class StochasticMigration {
	
	public static void main(String[] argv) {
		
		/*
		 * Simulation parameters:
		 */

		double simulationTime = 20.0;
		int nTimeSteps = 10001;
		int nSamples = 1001;
		int nTraj = 10;
		int seed = 42;
		
		/*
		 * Assemble model:
		 */

		Model model = new Model();

		// Define populations:
		int[] dims = new int[1];
		dims[0] = 2;
		Population X = new Population("X", dims);
		model.addPopulation(X);
		
		// Define migration reaction:
		Reaction migrate = new Reaction();
		migrate.setReactantSchema(X);
		migrate.setProductSchema(X);
		
		// Set up vectors to refer to sub-populations A and B:
		int[] subA = new int[1];
		int[] subB = new int[1];
		subA[0] = 0; subB[0] = 1;
		
		// subA -> subB
		migrate.addReactantSubSchema(subA);
		migrate.addProductSubSchema(subB);
		
		// subB -> subA
		migrate.addReactantSubSchema(subB);
		migrate.addProductSubSchema(subA);
		
		// Fix migration rates:
		migrate.setRate(0.1, 0.1);
		
		// Add migration reaction to model:
		model.addReaction(migrate);

		// Define moments to record:
		Moment momentX = new Moment("X", X);
		momentX.addSubSchema(subA);
		momentX.addSubSchema(subB);
		model.addMoment(momentX);

		// Set initial state:
		State initState = new State(model);
		initState.set(X, subA, 100);
		initState.set(X, subB, 0);
		
		/*
		 * Generate ensemble:
		 */

		EnsembleSummary ensemble = new EnsembleSummary(model, initState,
				simulationTime, nTimeSteps, nSamples, nTraj, seed);
		
		/*
		 * Dump results to stdout:
		 */
		
		ensemble.dump();
		
	}

}
