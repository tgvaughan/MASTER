package viralPopGen;

import java.io.*;
import java.util.*;

import beast.util.Randomizer;

import com.google.common.collect.*;

// JSON classes:
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A class representing a collection of results obtained by
 * estimating moments from an ensemble of trajectories of
 * a birth-death process.  Use this class as an alternative
 * to Ensemble to avoid having to keep a record of every
 * trajectory generated during the calculation.
 * 
 * @author Tim Vaughan
 *
 */
public class EnsembleSummary {

	// Record of simulation parameters:
	Model model;
	State initState;
	double simulationTime;
	int nTimeSteps, nSamples, nTraj;
	long seed;

	// Moments to record:
	List<Moment> moments;

	// Ensemble-averaged state summaries:
	StateSummary[] stateSummaries;

	/**
	 * Constructor.  Assigns simulation parameters and moment list
	 * to non-static fields, performs the simulation, recording the
	 * required summary statistics.
	 * 
	 * @param model				Model to simulate.
	 * @param initState			Initial state of system.
	 * @param simulationTime	Length of time to simulate for.
	 * @param nTimeSteps		Number of integration time steps to use.
	 * @param nSamples			Number of samples to record.
	 * @param nTraj				Number of trajectories to generate.
	 * @param seed				Seed for RNG. (<0 means use default seed.)
	 */
	public EnsembleSummary(Model model, State initState, double simulationTime, int nTimeSteps,
			int nSamples, int nTraj, long seed) {

		this.model = model;
		this.initState = initState;
		this.simulationTime = simulationTime;
		this.nTimeSteps = nTimeSteps;
		this.nSamples = nSamples;
		this.nTraj = nTraj;
		this.seed = seed;

		// Set seed if defined:
		if (seed < 0)
			Randomizer.setSeed(seed);

		// Derived simulation parameters:
		double dt = simulationTime/(nTimeSteps-1);
		int stepsPerSample = (nTimeSteps-1)/(nSamples-1);

		// Initialise state summaries:
		stateSummaries = new StateSummary[nSamples];
		for (int sidx=0; sidx<nSamples; sidx++)
			stateSummaries[sidx] = new StateSummary(model.moments);

		// Loop over trajectories:
		for (int traj=0; traj<nTraj; traj++) {

			// Initialise system state:
			State currentState = new State(initState);

			// Integration loop:
			int sidx = 0;
			for (int tidx=0; tidx<nTimeSteps; tidx++) {

				// Sample if necessary:
				if (tidx % stepsPerSample == 0)
					stateSummaries[sidx++].record(currentState);

				// Calculate transition rates:
				for (Reaction reaction : model.reactions)
					reaction.calcPropensities(currentState);

				// Update state with required changes:
				for (Reaction reaction : model.reactions)
					reaction.leap(currentState, dt);
			}
		}

		// Normalise state summaries:
		for (StateSummary summary : stateSummaries)
			summary.normalise();
	}

	/**
	 * Alternate constructor which uses default seed value.
	 *
	 * @param model				Model to simulate.
	 * @param initState			Initial state of system.
	 * @param simulationTime	Length of time to simulate for.
	 * @param nTimeSteps		Number of integration time steps to use.
	 * @param nSamples			Number of samples to record.
	 * @param nTraj				Number of trajectories to generate.
	 */
	public EnsembleSummary(Model model, State initState, double simulationTime,
			int nTimeSteps, int nSamples, int nTraj) {

		// Call main constructor with seed=-1: instructs constructor
		// not to use seed.
		this(model, initState, simulationTime, nTimeSteps, nSamples, nTraj, -1);
	}

	/**
	 *  Dump ensemble summary to a given PrintStream object out using JSON.
	 *  
	 * @param pstream PrintStream where output is sent.
	 */
	public void dump(PrintStream pstream) {

		HashMap<String, Object> outputData = Maps.newHashMap();

		// Construct an object containing the summarized
		// data.  Heirarchy is moment->[mean/std]->schema->estimate.

		for (Moment moment : model.moments) {
			HashMap<String,Object> momentData = Maps.newHashMap();

			ArrayList<Object> meanData = new ArrayList<Object>();
			for (int schema=0; schema<stateSummaries[0].mean.get(moment).length; schema++) {
				ArrayList<Double> schemaData = Lists.newArrayList();
				for (int sidx=0; sidx<stateSummaries.length; sidx++)
					schemaData.add(stateSummaries[sidx].mean.get(moment)[schema]);
				meanData.add(schemaData);
			}
			momentData.put("mean", meanData);

			ArrayList<Object> stdData = Lists.newArrayList();
			for (int schema=0; schema<stateSummaries[0].std.get(moment).length; schema++) {
				ArrayList<Double> schemaData = Lists.newArrayList();
				for (int sidx=0; sidx<stateSummaries.length; sidx++)
					schemaData.add(stateSummaries[sidx].std.get(moment)[schema]);
				stdData.add(schemaData);
			}
			momentData.put("std", stdData);

			outputData.put(moment.name, momentData);
		}

		// Add list of sampling times to output object:
		ArrayList<Double> tData = Lists.newArrayList();
		double dT = simulationTime/(nSamples-1);
		for (int sidx=0; sidx<stateSummaries.length; sidx++)
			tData.add(dT*sidx);
		outputData.put("t", tData);

		ObjectMapper mapper = new ObjectMapper();
		try {
			pstream.println(mapper.writeValueAsString(outputData));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Dump ensemble summary to stdout using JSON.
	 */
	public void dump() {
		dump(System.out);
	}
}