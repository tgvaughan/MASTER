package hamlet.math;

import beast.math.GammaFunction;
import beast.util.Randomizer;

/**
 * Class with a static method useful for generating sequences of numbers which
 * appear to be chosen from Poissonian distributions.
 *
 * Uses BEAST's uniform RNG and logGamma.
 *
 * Note that the nextDouble(lambda) method implemented by this class produces
 * sensible results even when lambda exceeds Integer.MAXINT.
 *
 * @author Tim Vaughan
 *
 */
public class Poisson {

    // If mean is above this, use rejection sampling method.
    private static double REJECT = 12;

    // Rejection method from NR, apparently good for lambda>=12
    private static double poissonian_reject(double lambda) {
        double sq = Math.sqrt(2.0*lambda);
        double alxm = Math.log(lambda);
        double g = lambda*alxm-GammaFunction.lnGamma(lambda+1.0);
        double em, t, y;

        do {
            do {
                y = Math.tan(Math.PI*Randomizer.nextDouble());
                em = sq*y+lambda;
            } while (em<0.0);

            em = Math.floor(em);
            t = 0.9*(1.0+y*y)*Math.exp(em*alxm
                    -GammaFunction.lnGamma(em+1.0)-g);

        } while (Randomizer.nextDouble()>t);

        return em;
    }

    // Direct method due to Knuth. Only efficient for small lambda.
    private static double poissonian_knuth(double lambda) {
        double L = Math.exp(-lambda);
        double p;
        int k;

        for (k = 0, p = 1; p>=L; k++)
            p = p*Randomizer.nextDouble();

        return k-1;
    }

    // Meta-function selects direct or rejection method according to lambda.
    public static double nextDouble(double lambda) {
        if (lambda<REJECT)
            return poissonian_knuth(lambda);

        return poissonian_reject(lambda);
    }

    /**
     * Speed test.
     *
     * @param argv
     */
    public static void main(String[] argv) {

        double res = 0;
        for (int i = 0; i<100000; i++)
            for (int j = 0; j<100; j++)
                res = nextDouble(1e10);

        System.out.println(res);

    }
}