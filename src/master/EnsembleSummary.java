/*
 * Copyright (C) 2012 Tim Vaughan <tgvaughan@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package master;

import beast.util.Randomizer;
import java.util.Date;

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
        if (spec.seed>=0 && !spec.seedUsed) {
            Randomizer.setSeed(spec.seed);
            spec.seedUsed = true;
        }
        
        // Record time at start of calculation:
        double startTime = (new Date()).getTime();

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
            PopulationState currentState = new PopulationState(spec.initPopulationState);
            
            // Record initial sample
            stateSummaries[0].record(currentState);

            // Integration loop:
            for (int sidx = 1; sidx<spec.nSamples; sidx++) {
                
                // Check for end conditions:
                boolean endConditionMet = false;
                for (PopulationEndCondition endCondition : spec.populationEndConditions) {
                    if (endCondition.isMet(currentState)) {
                        
                        // Can immediately reject, as only rejection condtions
                        // allowed for ensemble summaries.
                        currentState = new PopulationState(spec.initPopulationState);
                        sidx = 0;
                        endConditionMet = true;
                        
                        // Report if necessary:
                        if (spec.verbosity>0)
                            System.err.println("Rejection end condition met"
                                    + " at time " + sampleDt);
                        
                        break;
                    }
                }
                if (endConditionMet)
                    continue;

                // Report trajectory progress at all times:
                if (spec.verbosity>1)
                    System.err.println("Recording sample time point "
                            +String.valueOf(sidx+1)+" of "
                            +String.valueOf(spec.nSamples));

                // Integrate to next sample time:
                double t = 0;
                while (t<sampleDt)
                    t += spec.stepper.step(currentState, spec.model, sampleDt-t);
                
                // Record sample:
                stateSummaries[sidx].record(currentState);
            }
            
            for (StateSummary summary : stateSummaries)
                summary.accept();
        }

        // Normalise state summaries:
        for (StateSummary summary : stateSummaries)
            summary.normalise();
        
        // Record total time of calculation:
        spec.setWallTime(Double.valueOf((new Date()).getTime() - startTime)/1e3);
    }

    /**
     * Retrieve ensemble simulation specification.
     * 
     * @return EnsembleSummarySpec object.
     */
    public EnsembleSummarySpec getSpec() {
        return spec;
    }

    /**
     * Return array of state summaries.
     * 
     * @return StateSummary array.
     */
    public StateSummary[] getStateSummaries() {
        return stateSummaries;
    }
}