package hamlet.examples;

import hamlet.EnsembleSummary;
import hamlet.EnsembleSummarySpec;
import hamlet.JsonOutput;
import hamlet.Model;
import hamlet.MomentGroup;
import hamlet.PopulationType;
import hamlet.ReactionGroup;
import hamlet.State;
import hamlet.Population;
import hamlet.TauLeapingStepper;
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
        PopulationType X = new PopulationType("X", 2);
        model.addPopulationType(X);

        // Define migration reaction:
        ReactionGroup migrate = new ReactionGroup();

        // Set up vectors to refer to sub-populations A and B:
        Population subA = new Population(X, 0);
        Population subB = new Population(X, 1);

        // subA -> subB
        migrate.addReactantSchema(subA);
        migrate.addProductSchema(subB);
        migrate.addRate(0.1);

        // subB -> subA
        migrate.addReactantSchema(subB);
        migrate.addProductSchema(subA);
        migrate.addRate(0.2);

        // Add migration reaction to model:
        model.addReactionGroup(migrate);

        /*
         * Define moments:
         */

        // <X>
        MomentGroup momentX = new MomentGroup("X");
        momentX.addSchema(subA);
        momentX.addSchema(subB);

        // <Xa + Xb>
        MomentGroup momentN = new MomentGroup("N");
        momentN.newSum();
        momentN.addSubSchemaToSum(subA);
        momentN.addSubSchemaToSum(subB);

        /*
         * Define initial state:
         */

        State initState = new State();
        initState.set(subA, 100);
        initState.set(subB, 0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(20.0);
        spec.setIntegrator(new TauLeapingStepper(0.01));
        spec.setnSamples(1001);
        spec.setnTraj(100);
        spec.setSeed(42);
        spec.setInitState(initState);
        spec.addMomentGroup(momentX);
        spec.addMomentGroup(momentN);

        // Report on ensemble calculation progress:
        spec.setVerbosity(1);
        
        /*
         * Generate ensemble:
         */

        EnsembleSummary ensemble = new EnsembleSummary(spec);

        /*
         * Dump results to JSON-formatted output file:
         */

        JsonOutput.write(ensemble, new PrintStream("out.json"));
    }
}
