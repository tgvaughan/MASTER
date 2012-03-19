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

		double simulationTime = 10.0;
		int nTimeSteps = 1001;
		int nSamples = 1001;
		int nTraj = 1;
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
		int[][] locs = new int[2][1];
		locs[0][0] = 0;
		locs[1][0] = 1;
		migrate.addReactant(X, locs);
		locs[0][0] = 1;
		locs[0][0] = 0;
		migrate.addProduct(X, locs);
		
		// Fix migration rates:
		double[] rates = new double[2];
		rates[0] = 0.1;
		rates[1] = 0.1;
		migrate.setRate(rates);
		
		// Add migration reaction to model:
		model.addReaction(migrate);

		// Define moments to record:
		Moment momentX = new Moment("X", X);
		int[] loc = new int[1];
		loc[0] = 0;
		momentX.addLocSchema(loc);
		loc[0] = 1;
		momentX.addLocSchema(loc);
		model.addMoment(momentX);

		// Set initial state:
		State initState = new State(model);
		loc[0] = 0;
		initState.set(X, loc, 100);
		loc[0] = 1;
		initState.set(X, loc, 0);
		
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
