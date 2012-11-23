package hamlet.examples;

import hamlet.EnsembleSummary;
import hamlet.EnsembleSummarySpec;
import hamlet.JsonOutput;
import hamlet.Model;
import hamlet.Moment;
import hamlet.Population;
import hamlet.Reaction;
import hamlet.State;
import hamlet.TauLeapingStepper;
import java.io.IOException;
import java.io.PrintStream;

/**
 * A stochastic logistic model of population dynamics. Uses Moment objects to
 * summarise an ensemble in terms of means and variances.
 *
 * @author Tim Vaughan
 *
 */
public class StochasticLogisticSummary {

    public static void main(String[] argv) throws IOException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:

        Population X = new Population("X");
        model.addPopulation(X);

        // Define reactions:

        // X -> 2X
        Reaction birth = new Reaction("Birth");
        birth.setReactantSchema(X);
        birth.setProductSchema(X, X);
        birth.setRate(1.0);
        model.addReaction(birth);

        // 2X -> X
        Reaction death = new Reaction("Death");
        death.setReactantSchema(X, X);
        death.setProductSchema(X);
        death.setRate(0.01);
        model.addReaction(death);

        // Define moments:

        Moment mX = new Moment("X", X);

        /*
         * Set initial state:
         */

        State initState = new State();
        initState.set(X, 1.0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(20.0);
        spec.setEvenSampling(1001);
        spec.setnTraj(1000);
        spec.setSeed(53);
        spec.setInitState(initState);
        spec.addMoment(mX);

        spec.setStepper(new TauLeapingStepper(0.01));
        //spec.setIntegrator(new GillespieIntegrator());

        // Report on ensemble calculation progress:
        spec.setVerbosity(1);

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
