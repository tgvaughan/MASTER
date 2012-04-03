package popgen.models;

import popgen.Moment;
import popgen.EnsembleSummary;
import popgen.Population;
import popgen.Reaction;
import popgen.State;
import popgen.Simulation;
import popgen.Model;

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

		// Define moments:

		Moment mX = new Moment("X",X);

		/*
		 * Set initial state:
		 */

		State initState = new State(model);
		initState.set(X, 1.0);

		/*
		 * Define simulation:
		 */

		Simulation simulation = new Simulation();

		simulation.setModel(model);
		simulation.setSimulationTime(100.0);
		simulation.setnTimeSteps(10001);
		simulation.setnSamples(1001);
		simulation.setnTraj(1000);
		simulation.setSeed(53);
		simulation.setInitState(initState);
		simulation.addMoment(mX);

		// Report on ensemble calculation progress:
		simulation.setVerbosity(0);

		/*
		 * Generate summarised ensemble:
		 */

		EnsembleSummary ensemble = new EnsembleSummary(simulation);

		/*
		 * Dump results (JSON):
		 */

		ensemble.dump();
	}
}
