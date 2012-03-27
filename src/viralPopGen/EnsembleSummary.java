package viralPopGen;

import java.io.*;
import java.util.*;

import beast.util.Randomizer;
import com.google.common.collect.*;

// JSON classes:
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

	// Simulation specification:
	Simulation simulation;

	// Birth death model to simulate:
	Model model;

	// Moments to record:
	List<Moment> moments;

	// Ensemble-averaged state summaries:
	StateSummary[] stateSummaries;

	/**
	 * Constructor.  Assigns simulation parameters and moment list
	 * to non-static fields, performs the simulation, recording the
	 * required summary statistics.
	 * 
	 * @param simulation Simulation specification.
	 */
	public EnsembleSummary(Simulation simulation) {

		this.model = simulation.model;
		this.simulation = simulation;

		// Set seed if defined:
		if (simulation.seed < 0)
			Randomizer.setSeed(simulation.seed);

		// Derived simulation parameters:
		double dt = simulation.getDt();
		int stepsPerSample = simulation.getStepsPerSample();

		// Initialise state summaries:
		stateSummaries = new StateSummary[simulation.nSamples];
		for (int sidx=0; sidx<simulation.nSamples; sidx++)
			stateSummaries[sidx] = new StateSummary(model.moments);

		// Loop over trajectories:
		for (int traj=0; traj<simulation.nTraj; traj++) {

			// Emit verbose reportage to stderr:
			if (simulation.verbose) {
				System.err.println("Integrating trajectory " +
						String.valueOf(traj+1) + " of " +
						String.valueOf(simulation.nTraj));
			}

			// Initialise system state:
			State currentState = new State(simulation.initState);

			// Integration loop:
			int sidx = 0;
			for (int tidx=0; tidx<simulation.nTimeSteps; tidx++) {

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
		double dT = simulation.getSampleDt();
		for (int sidx=0; sidx<stateSummaries.length; sidx++)
			tData.add(dT*sidx);
		outputData.put("t", tData);

		// Record simulation parameters to object output:
		outputData.put("sim", simulation);

		ObjectMapper mapper = new ObjectMapper();
		try {
			pstream.println(mapper.writeValueAsString(outputData));
		} catch (IOException ex) {
			System.err.println(ex);
		}

	}

	/**
	 * Dump ensemble summary to stdout using JSON.
	 */
	public void dump() {
		dump(System.out);
	}
}