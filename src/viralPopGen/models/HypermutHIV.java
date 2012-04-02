package viralPopGen.models;

import viralPopGen.*;
import beast.math.Binomial;

/**
 * Model of within-host HIV evolution, including APOBEC3*-driven
 * hyper-mutation.
 *
 * @author Tim Vaughan
 */
public class HypermutHIV {

	public static void main (String[] argv) {

		/*
		 * Assemble model:
		 */

		Model model = new Model();
		
		// Sequence space parameters:

		// Sequence length excluding sites belonging to hypermutable motifs:
		int L = 1000;

		// Total number of hypermutable motifs:
		int La3 = 20;

		// Truncation Hamming distance:
		int hTrunc = 20;

		// Reduced sequence space dimension:
		int[] dims = {hTrunc+1, La3+1};

		// Define populations:

		// Uninfected cell:
		Population X = new Population("X");
		model.addPopulation(X);

		// Infected cell:
		Population Y = new Population("Y", dims);
		model.addPopulation(Y);
		int[] Ysub = new int[2]; // Used to specify individual Y sub-populations

		// Virion:
		Population V = new Population("V", dims);
		model.addPopulation(V);
		int[] Vsub = new int[2]; // Used to specify individual V sub-populations

		// Define reactions:

		// 0 -> X
		Reaction cellBirth = new Reaction();
		cellBirth.setReactantSchema();
		cellBirth.setProductSchema(X);
		cellBirth.setRate(2.5e8);
		model.addReaction(cellBirth);

		// X + V -> Y (with RT mutation)
		Reaction infection = new Reaction();
		infection.setReactantSchema(X,V);
		infection.setProductSchema(Y);

		double mu = 2e-5*L; // Mutation probabability per infection event.
		double beta = 5e-13; // Total infection rate.

		for (int ha=0; ha<=La3; ha++) {

			Vsub[1] = ha;
			Ysub[1] = ha;

			for (int h=0; h<=hTrunc; h++) {

				Vsub[0] = h;

				int hpmin = h>1 ? h-1 : 0;
				int hpmax = h<hTrunc ? h+1 : hTrunc;

				for (int hp=hpmin; hp<=hpmax; hp++) {

					Ysub[0] = hp;

					// Transition rate to hp from a given sequence in h:
					double rate = mu*gcond(h,hp,L)/(3.0*L);

					// Mutation-free contribution:
					if (h == hp)
						rate += (1-mu);

					// Incorporate base infection rate:
					rate *= beta;

					infection.addReactantSubSchema(null, Vsub);
					infection.addProductSubSchema(Ysub);
					infection.addSubRate(rate);
				}
			}
		}

		model.addReaction(infection);

		// X + V -> Y (with hypermutation)
		Reaction infectionHyper = new Reaction();
		infectionHyper.setReactantSchema(X,V);
		infectionHyper.setProductSchema(Y);

		// A3G incorporation probability:
		double pIncorp = 1e-7;

		// A3G hypermutable site mutation probability:
		double pHypermutate = 0.5;

		for (int h=0; h<=hTrunc; h++) {

			Vsub[0] = h;
			Ysub[0] = h;

			for (int ha=0; ha<=La3; ha++) {

				Vsub[1] = ha;
				/* 
				 * Once APOBEC attaches to a sequence with ha remaining
				 * hypermutable sites, it has a finite probability of editing
				 * each one of these sites. Thus the total number of edited
				 * sites delta following any attachment of APOBEC is
				 * binomially distributed.
				 */

				for (int delta=0; delta<=La3-ha; delta++) {

					Ysub[1] = ha+delta;

					double rate = beta*pIncorp
							*Math.pow(Binomial.choose(La3-ha, delta), 2.0)
							*Math.pow(pHypermutate, delta)
							*Math.pow(1.0-pHypermutate, La3-ha-delta);

					infectionHyper.addReactantSubSchema(null, Vsub);
					infectionHyper.addProductSubSchema(Ysub);
					infectionHyper.addSubRate(rate);

				}

			}
		}

		model.addReaction(infectionHyper);

		// Y -> Y + V
		Reaction budding = new Reaction();
		budding.setReactantSchema(Y);
		budding.setProductSchema(Y,V);
		for (int h=0; h<=hTrunc; h++) {

			Ysub[0] = h;
			Vsub[0] = h;

			for (int ha=0; ha<=La3; ha++) {

				Ysub[1] = ha;
				Vsub[1] = ha;

				budding.addReactantSubSchema(Ysub);
				budding.addProductSubSchema(Ysub,Vsub);
			}
		}
		budding.setRate(1e3);
		model.addReaction(budding);

		// X -> 0
		Reaction cellDeath = new Reaction();
		cellDeath.setReactantSchema(X);
		cellDeath.setProductSchema();
		cellDeath.setRate(1e-3);
		model.addReaction(cellDeath);

		// Y -> 0
		Reaction infectedDeath = new Reaction();
		infectedDeath.setReactantSchema(Y);
		infectedDeath.setProductSchema();

		for (int h=0; h<=hTrunc; h++) {
			Ysub[0] = h;
			for (int ha=0; ha<=La3; ha++) {
				Ysub[1] = ha;

				infectedDeath.addReactantSubSchema(Ysub);
				infectedDeath.addProductSubSchema();
			}
		}
		infectedDeath.setRate(1.0);
		model.addReaction(infectedDeath);

		// V -> 0
		Reaction virionDeath = new Reaction();
		virionDeath.setReactantSchema(V);
		virionDeath.setProductSchema();

		for (int h=0; h<=hTrunc; h++) {

			Vsub[0] = h;

			for (int ha=0; ha<=La3; ha++) {

				Vsub[1] = ha;

				virionDeath.addReactantSubSchema(Vsub);
				virionDeath.addProductSubSchema();
			}
		}
		virionDeath.setRate(3.0);
		model.addReaction(virionDeath);

		/*
		 * Define moments:
		 */

		Moment mX = new Moment("X", X);
		model.addMoment(mX);

		Moment mY = new Moment("Y", Y);
		Moment mV = new Moment("V", V);

		for (int totMut=0; totMut<=hTrunc+La3; totMut++) {
			mY.newSum();
			mV.newSum();

			for (int h=0; h<=hTrunc; h++) {

				int ha = totMut-h;

				if (ha>=0 && ha<=La3) {

					Ysub[0] = h;
					Ysub[1] = ha;
					mY.addSubSchemaToSum(Ysub);

					Vsub[0] = h;
					Vsub[1] = ha;
					mV.addSubSchemaToSum(Vsub);
				}
			}
		}

		model.addMoment(mY);
		model.addMoment(mV);

		/*
		 * Set initial state:
		 */

		State initState = new State(model);

		initState.set(X, 6.006e9); // Deterministic steady state values

		Ysub[0] = 0;
		Ysub[1] = 0;
		initState.set(Y, 2.44e8);

		Vsub[0] = 0;
		Vsub[1] = 0;
		initState.set(V, 8.125e10);

		// Note: unspecified population sizes default to zero.

		/*
		 * Define simulation:
		 */

		Simulation simulation = new Simulation();

		simulation.setModel(model);
		simulation.setSimulationTime(365);
		simulation.setnTimeSteps(10001);
		simulation.setnSamples(1001);
		simulation.setnTraj(1);
		simulation.setSeed(53);
		simulation.setInitState(initState);

		// Turn on verbose reportage:
		simulation.setVerbosity(1);

		/*
		 * Generate ensemble:
		 */

		EnsembleSummary ensemble = new EnsembleSummary(simulation);

		/*
		 * Dump results to stdout (JSON):
		 */

		ensemble.dump();

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

	/**
	 * Returns the number of hypermutable motif mutations
	 * available for a configuration having undergone
	 * ha hypermutations already.
	 * 
	 * @param ha Number of hypermutatable motifs already fired.
	 * @param La3 Total number of hypermutable motifs.
	 * @return 
	 */
	static int gcondA3(int ha, int La3) {
			return La3-ha;
	}
	
}
