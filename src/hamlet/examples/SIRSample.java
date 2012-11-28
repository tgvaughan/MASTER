package hamlet.examples;

import hamlet.EnsembleSummary;
import hamlet.EnsembleSummarySpec;
import hamlet.GillespieStepper;
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
 * SIR epidemic model with additional sampling process (without replacement).
 *
 * @author Tim Vaughan
 *
 */
public class SIRSample {

    public static void main(String[] argv) throws IOException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:

        Population S = new Population("S");
        Population I = new Population("I");
        Population R = new Population("R");
        Population I_sample= new Population("I_sample");
        model.addPopulations(S, I, R, I_sample);

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
        recovery.setRate(0.25);
        model.addReaction(recovery);
        
        // I -> I_sample
        Reaction sampling = new Reaction("Sampling");
        sampling.setReactantSchema(I);
        sampling.setProductSchema(I_sample);
        sampling.setRate(0.25);
        model.addReaction(sampling);

        // Define moments:

        Moment mS = new Moment("S", S);
        Moment mI = new Moment("I", I);
        Moment mR = new Moment("R", R);
        Moment mIsamp = new Moment("Isamp", I_sample);

        /*
         * Set initial state:
         */

        PopulationState initState = new PopulationState();
        initState.set(S, 999.0);
        initState.set(I, 1.0);
        initState.set(R, 0.0);
        initState.set(I_sample, 0.0);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(50.0);
        spec.setEvenSampling(1001);
        spec.setnTraj(1000);
        spec.setSeed(53);
        spec.setInitPopulationState(initState);
        spec.addMoment(mS);
        spec.addMoment(mI);
        spec.addMoment(mR);
        spec.addMoment(mIsamp);

        //spec.setStepper(new TauLeapingStepper(0.01));
        spec.setStepper(new GillespieStepper());

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
