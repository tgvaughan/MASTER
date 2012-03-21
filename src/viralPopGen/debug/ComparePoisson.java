package viralPopGen.debug;

import viralPopGen.math.Poisson;

/**
 * Compare output of Poissonian algorithm found in NR with
 * the corresponding method in colt.
 * 
 * Result: Both give equivalently distributed sequences for
 * lambda<1e9, but colt sequences "saturate" at higher lambda
 * while NR continues to give sensible numbers.
 * 
 * Verdict: Use NR method.
 * 
 * @author Tim Vaughan
 *
 */
public class ComparePoisson {
	
	public static void main (String[] argv) {
		
		// Seed for colt only:
		int seed = 42;
		
		// Mean of poissonian:
		double lambda = 5e9;
		
		// Length of sequences to generate:
		int N=100000;
		
		// Initialise colt RNG:
		cern.jet.random.engine.RandomEngine engine =
				new cern.jet.random.engine.MersenneTwister(seed);
		cern.jet.random.Poisson poissonian = new cern.jet.random.Poisson(1, engine);
		
		// Header for R:
		System.out.println("NR colt");
		
		// Generate sequences:
		for (int i=0; i<N; i++) {
			
			// NR pick:
			System.out.print(Poisson.nextDouble(lambda));
			System.out.print(" ");
			
			// colt pick:
			poissonian.setMean(lambda);
			System.out.println((double)poissonian.nextInt());
		}
		
	}

}