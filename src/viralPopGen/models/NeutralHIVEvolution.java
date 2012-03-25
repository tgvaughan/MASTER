package viralPopGen.models;

import viralPopGen.*;

/**
 * Simple model of HIV infection with neutral
 * RT error-driven mutation.
 *
 * @author Tim Vaughan
 */
public class NeutralHIVEvolution {

	public static void main (String[] argv) {

		/*
		 * Simulation parameters:
		 */

		double simulationTime = 100;
		int nTimeSteps = 1001;
		int nSamples = 1001;
		int nTraj = 1;
		int seed = 53;

		// Sequence length:
		int L = 105;
		int Ltrunc = 20;
		int[] dims = new int[Ltrunc];

		/*
		 * Assemble model:
		 */

		Model model = new Model();

		// Define populations:

		// Uninfected cell:
		Population X = new Population("X");
		model.addPopulation(X);

		// Infected cell:
		Population Y = new Population("Y", dims);
		model.addPopulation(Y);

		// Virion:
		Population V = new Population("V", dims);
		model.addPopulation(V);

		// Define reactions:

		// 0 -> X
		Reaction cellBirth = new Reaction();
		cellBirth.setReactantSchema();
		cellBirth.setProductSchema(X);
		cellBirth.setRate(2.5e8);
		model.addReaction(cellBirth);

		/*
		 * Set initial state:
		 */

		State initState = new State(model);
		initState.set(X, 2.5e11);
		initState.set(Y, 0.0);
		initState.set(V, 100.0);

		/*
		 * Generate ensemble:
		 */

		EnsembleSummary ensemble = new EnsembleSummary(model,
			initState, simulationTime, nTimeSteps, nSamples,
			nTraj, seed);

		/*
		 * Dump results to stdout (JSON):
		 */

		ensemble.dump();
	}
	
}
