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
 * SIR epidemic model.
 *
 * @author Tim Vaughan
 *
 */
public class SIR {

    public static void main(String[] argv) throws IOException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:

        Population S = new Population("S");
        Population I = new Population("I");
        Population R = new Population("R");
        model.addPopulations(S, I, R);
        
        // Define reactions:

        // S + I -> 2I
        Reaction infection = new Reaction("Infection");
        infection.setReactantSchema(S, I);
        infection.setProductSchema(I, I);
        infection.setRate(0.001);
        model.addReaction(infection);

        // I -> R
        Reaction recovery = new Reaction("Recovery");
        recovery.setReactantSchema(I);
        recovery.setProductSchema(R);
        recovery.setRate(0.5);
        model.addReaction(recovery);

        // Define moments:

        Moment mS = new Moment("S", S);
        Moment mI = new Moment("I", I);
        Moment mR = new Moment("R", R);

        /*
         * Set initial state:
         */

        State initState = new State();
        initState.set(S, 999.0);
        initState.set(I, 1.0);
        initState.set(R, 0.0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(50.0);
        spec.setnSamples(1001);
        spec.setnTraj(1000);
        spec.setSeed(53);
        spec.setInitState(initState);
        spec.addMoment(mS);
        spec.addMoment(mI);
        spec.addMoment(mR);

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
