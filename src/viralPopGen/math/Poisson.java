package viralPopGen.math;

import beast.util.Randomizer;

/**
 * Class with a static method useful for generating sequences
 * of numbers which appear to be chosen from Poissonian distributions.
 * 
 * Uses BEAST's RNG.
 * 
 * @author Tim Vaughan
 *
 */
public class Poisson {

	private static double PI = 3.141592653589793238;

	// If you believe NR, returns the value ln[Gamma(xx)] for xx>0. Pure black magick.
	private static double gammln(double xx)
	{
		int j;
		double x,y,tmp,ser;
		double [] cof = {
				76.18009172947146,
				-86.50532032941677,
				24.01409824083091,
				-1.231739572450155,
				0.1208650973866179e-2,
				-0.5395239384953e-5
				};

		y = x = xx;

		tmp = x + 5.5;
		tmp -= (x + 0.5)*Math.log(tmp);

		ser=1.000000000190015;

		for (j=0; j<6; j++)
			ser += cof[j]/++y;

		return -tmp + Math.log(2.5066282746310005*ser/x);
	}

	// Rejection method from NR, apparently good for lambda>=12
	private static double poissonian_reject(double lambda)
	{
		double sq = Math.sqrt(2.0*lambda);
		double alxm = Math.log(lambda);
		double g = lambda*alxm - gammln(lambda+1.0);
		double em, t, y;

		do {
			do {
				y = Math.tan(PI*Randomizer.nextDouble());
				em = sq*y + lambda;
			} while (em < 0.0);

			em = Math.floor(em);
			t = 0.9*(1.0 + y*y)*Math.exp(em*alxm - gammln(em + 1.0)-g);

		} while (Randomizer.nextDouble() > t);

		return em;
	}

	// Direct method due to Knuth. Only efficient for small lambda. Bad Knuth.
	private static double poissonian_knuth(double lambda)
	{
		double L = Math.exp(-lambda);
		double p;
		int k;

		for (k=0, p=1; p >= L; k++)
			p = p*Randomizer.nextDouble();

		return k-1;
	}

	// Meta-function selects direct or rejection method according to lambda.
	public static double nextDouble(double lambda)
	{
		if (lambda < 12.0)
			return poissonian_knuth(lambda);

		return poissonian_reject(lambda);
	}

}
