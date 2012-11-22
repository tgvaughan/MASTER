package hamlet.examples;

import hamlet.EnsembleSummary;
import hamlet.EnsembleSummarySpec;
import hamlet.GillespieStepper;
import hamlet.JsonOutput;
import hamlet.Model;
import hamlet.Moment;
import hamlet.MomentGroup;
import hamlet.Population;
import hamlet.PopulationType;
import hamlet.Reaction;
import hamlet.ReactionGroup;
import hamlet.State;
import hamlet.TauLeapingStepper;
import java.io.IOException;
import java.io.PrintStream;

/**
 * IR epidemic model with a) replication of infecteds via birth process and
 * b) a sampling process (without replacement).
 *
 * @author Tim Vaughan
 *
 */
public class IRSample {

    public static void main(String[] argv) throws IOException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:

        Population I = new Population("I");
        Population R = new Population("R");
        Population I_sample= new Population("I_sample");
        model.addPopulations(I, R, I_sample);

        // Define reactions:

        // I -> 2I
        Reaction infection = new Reaction("Infection");
        infection.setReactantSchema(I);
        infection.setProductSchema(I, I);
        infection.setRate(1);
        model.addReaction(infection);

        // I -> R
        Reaction recovery = new Reaction("Recovery");
        recovery.setReactantSchema(I);
        recovery.setProductSchema(R);
        recovery.setRate(0.25);
        model.addReaction(recovery);
        
        // I -> I_sample
        Reaction sampling = new Reaction("Sampling");
        sampling.setReactantSchema(I);
        sampling.setProductSchema(I_sample);
        sampling.setRate(0.25);
        model.addReaction(sampling);

        // Define moments:

        Moment mI = new Moment("I", I);
        Moment mR = new Moment("R", R);
        Moment mIsamp = new Moment("Isamp", I_sample);

        /*
         * Set initial state:
         */

        State initState = new State();
        initState.set(I, 1.0);
        initState.set(R, 0.0);
        initState.set(I_sample, 0.0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(100.0);
        spec.setnSamples(1001);
        spec.setnTraj(1000);
        spec.setSeed(53);
        spec.setInitState(initState);
        spec.addMoment(mI);
        spec.addMoment(mR);
        spec.addMoment(mIsamp);

        spec.setIntegrator(new TauLeapingStepper(0.01));
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
