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
import java.io.IOException;
import java.io.PrintStream;

/**
 * SIS epidemic model.
 *
 * @author Tim Vaughan
 *
 */
public class SIS {

    public static void main(String[] argv) throws IOException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:

        Population S = new Population("S");
        Population I = new Population("I");
        model.addPopulations(S, I);

        // Define reactions:

        // S + I -> 2I
        Reaction infection = new Reaction("Infection");
        infection.setReactantSchema(S, I);
        infection.setProductSchema(I, I);
        infection.setRate(0.005);
        model.addReaction(infection);

        // I -> S
        Reaction recovery = new Reaction("Recovery");
        recovery.setReactantSchema(I);
        recovery.setProductSchema(S);
        recovery.setRate(0.5);
        model.addReaction(recovery);

        // Define moments:

        Moment mS = new Moment("S", S);
        Moment mI = new Moment("I", I);

        /*
         * Set initial state:
         */

        PopulationState initState = new PopulationState();
        initState.set(S, 999.0);
        initState.set(I, 1.0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(5.0);
        spec.setEvenSampling(1001);
        spec.setnTraj(1000);
        spec.setSeed(53);
        spec.setInitPopulationState(initState);
        spec.addMoment(mS);
        spec.addMoment(mI);

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
