package hamlet;

import beast.util.Randomizer;
import com.google.common.collect.*;
import java.io.*;
import java.util.*;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A class representing a collection of results obtained by estimating moments
 * from an ensemble of trajectories of a birth-death process. Use this class as
 * an alternative to Ensemble to avoid having to keep a record of every
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
        if (spec.seed>=0)
            Randomizer.setSeed(spec.seed);

        // Derived spec parameters:
        double sampleDt = spec.getSampleDt();

        // Initialise state summaries:
        stateSummaries = new StateSummary[spec.nSamples];
        for (int sidx = 0; sidx<spec.nSamples; sidx++)
            stateSummaries[sidx] = new StateSummary(spec.momentGroups);

        // Loop over trajectories:
        for (int traj = 0; traj<spec.nTraj; traj++) {

            // Report ensemble progress if verbosity high enough:
            if (spec.verbosity>0)
                System.err.println("Integrating trajectory "
                        +String.valueOf(traj+1)+" of "
                        +String.valueOf(spec.nTraj));

            // Initialise system state:
            State currentState = new State(spec.initState);

            // Integration loop:
            for (int sidx = 0; sidx<spec.nSamples; sidx++) {

                // Report trajectory progress at all times:
                if (spec.verbosity==2)
                    System.err.println("Recording sample time point "
                            +String.valueOf(sidx+1)+" of "
                            +String.valueOf(spec.nSamples));

                // Record sample:
                stateSummaries[sidx].record(currentState);

                // Integrate to next sample time:
                double t = 0;
                while (t<sampleDt)
                    t += spec.stepper.step(currentState, spec.model, sampleDt-t);
            }
        }

        // Normalise state summaries:
        for (StateSummary summary : stateSummaries)
            summary.normalise();
    }

}