package hamlet.examples;

import hamlet.EnsembleSummary;
import hamlet.EnsembleSummarySpec;
import hamlet.Model;
import hamlet.Moment;
import hamlet.Population;
import hamlet.Reaction;
import hamlet.State;
import hamlet.SubPopulation;
import hamlet.TauLeapingIntegrator;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Implements a basic stochastic migration model to demonstrate the
 * handling of structured populations.
 *
 * @author Tim Vaughan
 *
 */
public class StochasticMigration {

    public static void main(String[] argv) throws FileNotFoundException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:
        Population X = new Population("X", 2);
        model.addPopulation(X);

        // Define migration reaction:
        Reaction migrate = new Reaction();
        migrate.setReactantSchema(X);
        migrate.setProductSchema(X);

        // Set up vectors to refer to sub-populations A and B:
        SubPopulation subA = new SubPopulation(X, 0);
        SubPopulation subB = new SubPopulation(X, 1);

        // subA -> subB
        migrate.addReactantSubSchema(subA);
        migrate.addProductSubSchema(subB);
        migrate.addSubRate(0.1);

        // subB -> subA
        migrate.addReactantSubSchema(subB);
        migrate.addProductSubSchema(subA);
        migrate.addSubRate(0.2);

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
        initState.set(subA, 100);
        initState.set(subB, 0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(20.0);
        spec.setIntegrator(new TauLeapingIntegrator(0.01));
        spec.setnSamples(1001);
        spec.setnTraj(100);
        spec.setSeed(42);
        spec.setInitState(initState);
        spec.addMoment(momentX);
        spec.addMoment(momentN);

        // Report on ensemble calculation progress:
        spec.setVerbosity(1);
        
        /*
         * Generate ensemble:
         */

        EnsembleSummary ensemble = new EnsembleSummary(spec);

        /*
         * Dump results to stdout:
         */

        ensemble.dump(new PrintStream("out.json"));

    }
}
