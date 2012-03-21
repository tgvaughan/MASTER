package viralPopGen.debug;

import cern.jet.random.*;
import cern.jet.random.engine.*;

/**
 * Try to find out why colt's poissonian RNG is failing.
 * 
 * Result: for one thing, colt RNG _saturates_ at ~2e9.. Why!?
 * 
 * @author Tim Vaughan
 *
 */
public class BreakPoisson {
	
	public static void main (String[] argv) {
		
		int seed = 42;
		
		// Initialise RNG:
		RandomEngine engine = new MersenneTwister(seed);
		Poisson poissonian = new Poisson(1, engine);
		
		// Draw number of reactions to fire within time tau:
		double lambda = 1e20;
		poissonian.setMean(lambda);
		double q = (double)poissonian.nextInt();
		
		System.out.println(String.valueOf(lambda)+" "+String.valueOf(q));
		
	}

}
