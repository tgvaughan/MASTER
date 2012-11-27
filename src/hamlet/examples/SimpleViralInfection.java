package hamlet.examples;

import hamlet.EnsembleSummary;
import hamlet.EnsembleSummarySpec;
import hamlet.JsonOutput;
import hamlet.Model;
import hamlet.Moment;
import hamlet.Population;
import hamlet.Reaction;
import hamlet.PopulationState;
import hamlet.TauLeapingStepper;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * A simple model of within-host viral infection dynamics.
 *
 * @author Tim Vaughan
 *
 */
public class SimpleViralInfection {

    public static void main(String[] argv) throws FileNotFoundException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:

        // Uninfected cell:
        Population X = new Population("X");
        model.addPopulation(X);

        // Infected cell:
        Population Y = new Population("Y");
        model.addPopulation(Y);

        // Virion:
        Population V = new Population("V");
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
        infection.setReactantSchema(X, V);
        infection.setProductSchema(Y);
        infection.setRate(5e-13);
        model.addReaction(infection);

        // Y -> Y + V
        Reaction budding = new Reaction();
        budding.setReactantSchema(Y);
        budding.setProductSchema(Y, V);
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
        infectedDeath.setRate(1.0);
        model.addReaction(infectedDeath);

        // V -> 0
        Reaction virionDeath = new Reaction();
        virionDeath.setReactantSchema(V);
        virionDeath.setProductSchema();
        virionDeath.setRate(3.0);
        model.addReaction(virionDeath);

        // Define moments:

        // <N_X>
        Moment mX = new Moment("X", X);

        // <N_Y>
        Moment mY = new Moment("Y", Y);

        // <N_V>
        Moment mV = new Moment("V", V);

        /*
         *  Set initial state:
         */

        PopulationState initState = new PopulationState();
        initState.set(X, 2.5e11);
        initState.set(Y, 0);
        initState.set(V, 100.0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(10.0); // days
        spec.setStepper(new TauLeapingStepper(10.0/1e3));
        spec.setEvenSampling(1001);
        spec.setnTraj(1000);
        spec.setSeed(42);
        spec.setInitPopulationState(initState);
        spec.addMoment(mX);
        spec.addMoment(mY);
        spec.addMoment(mV);

        /*
         * Generate summarised ensemble:
         */

        EnsembleSummary ensemble = new EnsembleSummary(spec);

        /*
         * Dump results (JSON):
         */

        JsonOutput.write(ensemble, new PrintStream("out.json"));
    }
}
