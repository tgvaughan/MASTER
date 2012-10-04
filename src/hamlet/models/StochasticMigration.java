package hamlet.models;

import hamlet.State;
import hamlet.Population;
import hamlet.Reaction;
import hamlet.Model;
import hamlet.Moment;
import hamlet.EnsembleSummary;
import hamlet.EnsembleSummarySpec;
import hamlet.TauLeapingIntegrator;

/**
 * Implements a basic stochastic migration model to test the ability of
 * viralPopGen to handle structured populations.
 *
 * @author Tim Vaughan
 *
 */
public class StochasticMigration {

    public static void main(String[] argv) {

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
        subA[0] = 0;
        subB[0] = 1;

        // subA -> subB
        migrate.addReactantSubSchema(subA);
        migrate.addProductSubSchema(subB);
        migrate.addSubRate(0.1);

        // subB -> subA
        migrate.addReactantSubSchema(subB);
        migrate.addProductSubSchema(subA);
        migrate.addSubRate(0.1);

        // Add migration reaction to model:
        model.addReaction(migrate);

        /*
         * Define moments:
         */

        // <X>
        Moment momentX = new Moment("X", X);
        momentX.addSubSchema(subA);
        momentX.addSubSchema(subB);

        // <Xa + Xb>
        Moment momentN = new Moment("N", X);
        momentN.newSum();
        momentN.addSubSchemaToSum(subA);
        momentN.addSubSchemaToSum(subB);

        /*
         * Define initial state:
         */

        State initState = new State(model);
        initState.set(X, subA, 100);
        initState.set(X, subB, 0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(20.0);
        spec.setIntegrator(new TauLeapingIntegrator(20.0/1e4));
        spec.setnSamples(1001);
        spec.setnTraj(100);
        spec.setSeed(42);
        spec.setInitState(initState);
        spec.addMoment(momentX);
        spec.addMoment(momentN);

        /*
         * Generate ensemble:
         */

        EnsembleSummary ensemble = new EnsembleSummary(spec);

        /*
         * Dump results to stdout:
         */

        ensemble.dump();

    }
}
