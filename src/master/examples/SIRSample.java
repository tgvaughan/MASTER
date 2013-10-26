package master.examples;

import master.EnsembleSummary;
import master.EnsembleSummarySpec;
import master.GillespieStepper;
import master.outputs.JsonOutput;
import master.Model;
import master.Moment;
import master.Population;
import master.PopulationState;
import master.Reaction;
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

        (new JsonOutput("out.json")).write(ensemble);
    }
}
