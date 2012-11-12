package hamlet.examples;

import beast.math.Binomial;
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
 * Model of within-host HIV evolution, including APOBEC3*-driven hyper-mutation.
 *
 * @author Tim Vaughan
 */
public class HypermutHIV {

    public static void main(String[] argv) throws FileNotFoundException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Sequence space parameters:

        // Sequence length excluding sites belonging to hypermutable motifs:
        int L = 1000;

        // Total number of hypermutable motifs:
        int La3 = 20;

        // Truncation Hamming distance:
        int hTrunc = 20;

        // Reduced sequence space dimension:
        int[] dims = {hTrunc+1, La3+1};

        // Define populations:

        // Uninfected cell:
        Population X = new Population("X");
        model.addPopulation(X);

        // Infected cell:
        Population Y = new Population("Y", dims);
        model.addPopulation(Y);

        // Virion:
        Population V = new Population("V", dims);
        model.addPopulation(V);

        // Define reactions:

        // 0 -> X
        Reaction cellBirth = new Reaction();
        cellBirth.setReactantSchema();
        cellBirth.setProductSchema(X);
        cellBirth.setRate(2.5e8);
        model.addReaction(cellBirth);

        // X + V -> Y (with RT mutation)
        Reaction infection = new Reaction();
        infection.setReactantSchema(X, V);
        infection.setProductSchema(Y);

        double mu = 2e-5*L; // Mutation probabability per infection event.
        double beta = 5e-13; // Total infection rate.

        for (int ha = 0; ha<=La3; ha++) {

            for (int h = 0; h<=hTrunc; h++) {

                SubPopulation Vsub = new SubPopulation(V, h, ha);

                int hpmin = h>1 ? h-1 : 0;
                int hpmax = h<hTrunc ? h+1 : hTrunc;

                for (int hp = hpmin; hp<=hpmax; hp++) {

                    SubPopulation Ysub = new SubPopulation(Y, hp, ha);

                    // Transition rate to hp from a given sequence in h:
                    double rate = mu*gcond(h, hp, L)/(3.0*L);

                    // Mutation-free contribution:
                    if (h==hp)
                        rate += (1-mu);

                    // Incorporate base infection rate:
                    rate *= beta;

                    infection.addReactantSubSchema(null, Vsub);
                    infection.addProductSubSchema(Ysub);
                    infection.addSubRate(rate);
                }
            }
        }

        model.addReaction(infection);

        // X + V -> Y (with hypermutation)
        Reaction infectionHyper = new Reaction();
        infectionHyper.setReactantSchema(X, V);
        infectionHyper.setProductSchema(Y);

        // A3G incorporation probability:
        double pIncorp = 1e-7;

        // A3G hypermutable site mutation probability:
        double pHypermutate = 0.5;

        for (int h = 0; h<=hTrunc; h++) {

            for (int ha = 0; ha<=La3; ha++) {

                SubPopulation Vsub = new SubPopulation(V, h, ha);
                /* 
                 * Once APOBEC attaches to a sequence with ha remaining
                 * hypermutable sites, it has a finite probability of editing
                 * each one of these sites. Thus the total number of edited
                 * sites delta following any attachment of APOBEC is
                 * binomially distributed.
                 */

                for (int delta = 0; delta<=La3-ha; delta++) {

                    SubPopulation Ysub = new SubPopulation(Y, h, ha+delta);

                    double rate = beta*pIncorp
                            *Math.pow(Binomial.choose(La3-ha, delta), 2.0)
                            *Math.pow(pHypermutate, delta)
                            *Math.pow(1.0-pHypermutate, La3-ha-delta);

                    infectionHyper.addReactantSubSchema(null, Vsub);
                    infectionHyper.addProductSubSchema(Ysub);
                    infectionHyper.addSubRate(rate);

                }

            }
        }

        model.addReaction(infectionHyper);

        // Y -> Y + V
        Reaction budding = new Reaction();
        budding.setReactantSchema(Y);
        budding.setProductSchema(Y, V);
        for (int h = 0; h<=hTrunc; h++) {
            for (int ha = 0; ha<=La3; ha++) {

                SubPopulation Ysub = new SubPopulation(Y, h, ha);
                SubPopulation Vsub = new SubPopulation(V, h, ha);

                budding.addReactantSubSchema(Ysub);
                budding.addProductSubSchema(Ysub, Vsub);
            }
        }
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

        for (int h = 0; h<=hTrunc; h++) {
            for (int ha = 0; ha<=La3; ha++) {
                SubPopulation Ysub = new SubPopulation(Y, h, ha);

                infectedDeath.addReactantSubSchema(Ysub);
                infectedDeath.addProductSubSchema();
            }
        }
        infectedDeath.setRate(1.0);
        model.addReaction(infectedDeath);

        // V -> 0
        Reaction virionDeath = new Reaction();
        virionDeath.setReactantSchema(V);
        virionDeath.setProductSchema();

        for (int h = 0; h<=hTrunc; h++) {
            for (int ha = 0; ha<=La3; ha++) {
                SubPopulation Vsub = new SubPopulation (V, h, ha);

                virionDeath.addReactantSubSchema(Vsub);
                virionDeath.addProductSubSchema();
            }
        }
        virionDeath.setRate(3.0);
        model.addReaction(virionDeath);

        /*
         * Define moments:
         */

        Moment mX = new Moment("X", X);
        Moment mY = new Moment("Y", Y);
        Moment mV = new Moment("V", V);

        for (int totMut = 0; totMut<=hTrunc+La3; totMut++) {
            mY.newSum();
            mV.newSum();

            for (int h = 0; h<=hTrunc; h++) {

                int ha = totMut-h;

                if (ha>=0 && ha<=La3) {

                    SubPopulation Ysub = new SubPopulation(Y, h, ha);
                    mY.addSubSchemaToSum(Ysub);

                    SubPopulation Vsub = new SubPopulation(V, h, ha);
                    mV.addSubSchemaToSum(Vsub);
                }
            }
        }

        /*
         * Set initial state:
         */

        State initState = new State(model);

        initState.set(X, 6.006e9); // Deterministic steady state values
        initState.set(new SubPopulation(Y, 0, 0), 2.44e8);
        initState.set(new SubPopulation(V, 0, 0), 8.125e10);

        // Note: unspecified population sizes default to zero.

        /*
         * Define simulation:
         */

        EnsembleSummarySpec spec = new EnsembleSummarySpec();

        spec.setModel(model);
        spec.setSimulationTime(365);
        spec.setIntegrator(new TauLeapingIntegrator(365.0/10000.0));
        spec.setnSamples(1001);
        spec.setnTraj(1);
        spec.setSeed(53);
        spec.setInitState(initState);
        spec.addMoment(mY);
        spec.addMoment(mV);
        spec.addMoment(mX);

        // Turn on verbose reportage:
        spec.setVerbosity(2);

        /*
         * Generate ensemble:
         */

        EnsembleSummary ensemble = new EnsembleSummary(spec);

        /*
         * Dump results to file (JSON):
         */

        ensemble.dump(new PrintStream("out.json"));

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