package master.examples;

import master.EnsembleSummary;
import master.EnsembleSummarySpec;
import master.outputs.JsonOutput;
import master.Model;
import master.Moment;
import master.MomentGroup;
import master.PopulationType;
import master.ReactionGroup;
import master.PopulationState;
import master.Population;
import master.Reaction;
import master.TauLeapingStepper;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Simple model of HIV infection with neutral RT error-driven mutation.
 *
 * @author Tim Vaughan
 */
public class NeutralHIVEvolution {

    public static void main(String[] argv) throws FileNotFoundException {

        // Sequence length:
        int L = 1000;
        
        // Maximum number of mutations to consider:
        int hTrunc = 20;

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:

        // Uninfected cell:
        Population X = new Population("X");
        model.addPopulation(X);

        // Infected cell:
        PopulationType Ytype = new PopulationType("Y", hTrunc+1);
        model.addPopulationType(Ytype);

        // Virion:
        PopulationType Vtype = new PopulationType("V", hTrunc+1);
        model.addPopulationType(Vtype);

        // Define reactions:

        // 0 -> X
        Reaction cellBirth = new Reaction();
        cellBirth.setReactantSchema();
        cellBirth.setProductSchema(X);
        cellBirth.setRate(2.5e8);
        model.addReaction(cellBirth);

        // X + V -> Y (with mutation)
        ReactionGroup infection = new ReactionGroup();

        double mu = 2e-5*L; // Mutation probabability per infection event.
        double beta = 5e-13; // Total infection rate.

        for (int h = 0; h<=hTrunc; h++) {

            Population V = new Population(Vtype, h);

            int hpmin = h>1 ? h-1 : 0;
            int hpmax = h<hTrunc ? h+1 : hTrunc;

            for (int hp = hpmin; hp<=hpmax; hp++) {

                Population Y = new Population(Ytype, hp);

                // Transition rate to hp from a given sequence in h:
                double rate = mu*gcond(h, hp, L)/(3.0*L);

                // Mutation-free contribution:
                if (h==hp)
                    rate += (1-mu);

                // Incorporate base infection rate:
                rate *= beta;

                infection.addReactantSchema(X, V);
                infection.addProductSchema(Y);
                infection.addRate(rate);
            }
        }

        model.addReactionGroup(infection);

        // Y -> Y + V
        ReactionGroup budding = new ReactionGroup();
        for (int h = 0; h<=hTrunc; h++) {
            Population Y = new Population(Ytype, h);
            Population V = new Population(Vtype, h);
            budding.addReactantSchema(Y);
            budding.addProductSchema(Y, V);
        }
        budding.setGroupRate(1e3);
        model.addReactionGroup(budding);

        // X -> 0
        Reaction cellDeath = new Reaction();
        cellDeath.setReactantSchema(X);
        cellDeath.setProductSchema();
        cellDeath.setRate(1e-3);
        model.addReaction(cellDeath);

        // Y -> 0
        ReactionGroup infectedDeath = new ReactionGroup();

        for (int h = 0; h<=hTrunc; h++) {
            Population Y = new Population(Ytype, h);

            infectedDeath.addReactantSchema(Y);
            infectedDeath.addProductSchema();
        }
        infectedDeath.setGroupRate(1.0);
        model.addReactionGroup(infectedDeath);

        // V -> 0
        ReactionGroup virionDeath = new ReactionGroup();

        for (int h = 0; h<=hTrunc; h++) {
            Population V = new Population(Vtype, h);

            virionDeath.addReactantSchema(V);
            virionDeath.addProductSchema();
        }
        virionDeath.setGroupRate(3.0);
        model.addReactionGroup(virionDeath);

        /*
         * Define moments:
         */

        Moment mX = new Moment("X", X);
        MomentGroup mY = new MomentGroup("Y");
        MomentGroup mV = new MomentGroup("V");

        for (int h = 0; h<=hTrunc; h++) {
            Population Y = new Population(Ytype, h);
            mY.addSchema(Y);

            Population V = new Population(Vtype, h);
            mV.addSchema(V);
        }

        /*
         * Set initial state:
         */

        PopulationState initState = new PopulationState();
        initState.set(X, 6.1e9);
        initState.set(new Population(Ytype, 0), 2.5e8);
        initState.set(new Population(Vtype, 0), 8.2e10);

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(365.0);
        spec.setStepper(new TauLeapingStepper(365.0/1e4));
        spec.setEvenSampling(1001);
        spec.setnTraj(10);
        spec.setSeed(53);
        spec.setInitPopulationState(initState);
        spec.addMoment(mX);
        spec.addMomentGroup(mY);
        spec.addMomentGroup(mV);

        // Report on ensemble progress:
        spec.setVerbosity(1);

        /*
         * Generate ensemble:
         */

        EnsembleSummary ensemble = new EnsembleSummary(spec);

        /*
         * Dump results to file (JSON):
         */

        (new JsonOutput("out.json")).write(ensemble);
    }

    /**
     * Return the number of sequences s2 satisfying d(s2,0)=h2 and d(s2,s1)=1
     * where s1 is a particular sequence satisfying d(s1,0)=h1.
     *
     * @param h1
     * @param h2
     * @param L
     * @return
     */
    static int gcond(int h1, int h2, int L) {

        int result;

        switch (h2-h1) {
            case 1:
                result = 3*(L-h1);
                break;
            case 0:
                result = 2*h1;
                break;
            case -1:
                result = h1;
                break;
            default:
                result = 0;
        }

        return result;
    }
}
