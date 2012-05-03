package hamlet;

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
	EnsembleSummarySpec spec;

	// Ensemble-averaged state summaries:
	StateSummary[] stateSummaries;

	/**
	 * Assign simulation parameters and moment list to non-static fields,
	 * performs the spec, recording the required summary statistics.
	 *
	 * @param spec Simulation specification.
	 */
	public EnsembleSummary(EnsembleSummarySpec spec) {

		this.spec = spec;

		// Set seed if defined:
		if (spec.seed < 0)
			Randomizer.setSeed(spec.seed);

		// Derived spec parameters:
		double dt = spec.getDt();
		int stepsPerSample = spec.getStepsPerSample();

		// Initialise state summaries:
		stateSummaries = new StateSummary[spec.nSamples];
		for (int sidx=0; sidx<spec.nSamples; sidx++)
			stateSummaries[sidx] = new StateSummary(spec.moments);

		// Loop over trajectories:
		for (int traj=0; traj<spec.nTraj; traj++) {

			// Report ensemble progress if verbosity high enough:
			if (spec.verbosity>0) {
				System.err.println("Integrating trajectory " +
						String.valueOf(traj+1) + " of " +
						String.valueOf(spec.nTraj));
			}

			// Initialise system state:
			State currentState = new State(spec.initState);

			// Integration loop:
			int sidx = 0;
			for (int tidx=0; tidx<spec.nTimeSteps; tidx++) {

				// Report trajectory progress at all times:
				if (spec.verbosity==3) {
					System.err.println("Computing time step " +
							String.valueOf(tidx+1) + " of " +
							String.valueOf(spec.nTimeSteps));
				}

				// Sample if necessary:
				if (tidx % stepsPerSample == 0) {
					stateSummaries[sidx++].record(currentState);

					// Report trajectory progress at sampling times only:
					if (spec.verbosity==2) {
						System.err.println("Computing time step " +
								String.valueOf(tidx+1) + " of " +
								String.valueOf(spec.nTimeSteps));
					}
				}

				// Calculate transition rates:
				for (Reaction reaction : spec.model.reactions)
					reaction.calcPropensities(currentState);

				// Update state with required changes:
				for (Reaction reaction : spec.model.reactions)
					reaction.leap(currentState, spec);
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

		for (Moment moment : spec.moments) {
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
		double dT = spec.getSampleDt();
		for (int sidx=0; sidx<stateSummaries.length; sidx++)
			tData.add(dT*sidx);
		outputData.put("t", tData);

		// Record spec parameters to object output:
		outputData.put("sim", spec);

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