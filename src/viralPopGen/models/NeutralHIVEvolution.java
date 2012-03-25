package viralPopGen.models;

import viralPopGen.*;
import beast.math.GammaFunction;

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
		int hTrunc = 20;
		int[] dims = new int[hTrunc];

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

		// X + V -> Y
		Reaction infection = new Reaction();
		infection.setReactantSchema(X,V);
		infection.setProductSchema(Y);

		int[] Vsub = new int[1];
		int[] Ysub = new int[1];
		for (int h=0; h<=hTrunc; h++) {

			Vsub[0] = h;

			int hpmin = h>1 ? h-1 : 0;
			int hpmax = h<hTrunc ? h+1 : hTrunc;

			for (int hp=hpmin; hp<=hpmax; hp++) {

				Ysub[0] = hp;

				infection.addReactantSubSchema(null, Vsub);
				infection.addProductSubSchema(Ysub);
			}
		}

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

	/**
	 * Return the number of sequences s of length L
	 * satisfying d(s,0)=h.
	 * 
	 * @param h Hamming distance.
	 * @param L Length of sequences to count.
	 * @return  The number of sequences of length L with d(s,0)=h.
	 */
	static double g(int h, int L) {

		double logResult =
			h*Math.log(3.0)
			+ GammaFunction.lnGamma(L+1)
			- GammaFunction.lnGamma(h+1)
			- GammaFunction.lnGamma(L-h+1);

		return Math.exp(logResult);

	}

	/**
	 * Return the number of sequences s2 satisfying d(s2,0)=h2
	 * and d(s2,s1)=1 where s1 is a particular sequence satisfying
	 * d(s1,0)=h1.
	 * 
	 * @param h1
	 * @param h2
	 * @param L
	 * @return 
	 */
	static int gcond(int h1, int h2, int L) {

		int result;

		switch (h2-h1) {
			case 1:
				result =  3*(L-h1);
				break;
			case 0:
				result =  2*h1;
				break;
			case -1:
				result =  h1;
				break;
			default:
				result = 0;
		}

		return result;
	}
	
}
