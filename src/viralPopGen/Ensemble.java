package viralPopGen;

import cern.jet.random.Poisson;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.MersenneTwister;


public class Ensemble {
	
	RandomEngine engine;
	Poisson poissonian;
	
	State initState;
	
	double T;
	double Nt;
	
	Model model;

}
