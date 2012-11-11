package hamlet.examples;

import hamlet.EnsembleSpec;
import hamlet.Ensemble;
import hamlet.State;
import hamlet.Population;
import hamlet.Reaction;
import hamlet.Model;
import hamlet.TauLeapingIntegrator;

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
        birth.setProductSchema(X, X);
        birth.setRate(1.0);
        model.addReaction(birth);

        // 2X -> X
        Reaction death = new Reaction();
        death.setReactantSchema(X, X);
        death.setProductSchema(X);
        death.setRate(0.01);
        model.addReaction(death);

        /*
         * Set initial state:
         */

        State initState = new State(model);
        initState.set(X, 1.0);

        /*
         * Assemble simulation spec:
         */

        EnsembleSpec spec = new EnsembleSpec();

        spec.setSimulationTime(100.0);
        spec.setIntegrator(new TauLeapingIntegrator(100.0/1e4));
        spec.setnSamples(1001);
        spec.setnTraj(1);
        spec.setSeed(42);
        spec.setModel(model);
        spec.setInitState(initState);

        /*
         * Generate ensemble
         */

        Ensemble ensemble = new Ensemble(spec);

        /*
         * Dump first trajectory to stdout:
         */

        ensemble.dump();

    }
}