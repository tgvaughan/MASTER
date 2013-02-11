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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import master.inheritance.InheritanceEnsemble;
import master.inheritance.InheritanceEnsembleSpec;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Class containing static methods for writing tracjectory, ensemble
 * and ensemble summary results to a given PrintStream using a versatile
 * JSON format.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class JsonOutput {
    
    /**
     * Express a given trajectory as a JSON-formatted string and send the
     * result to a PrintStream.
     * 
     * @param trajectory Trajectory to dump.
     * @param pstream PrintStream where output is sent.
     */
    public static void write(Trajectory trajectory, PrintStream pstream) {
        
        if (trajectory.getSpec().getVerbosity()>0)
            System.out.println("Writing JSON output...");
        
        HashMap<String, Object> outputData = Maps.newHashMap();
        
        TrajectorySpec spec = trajectory.getSpec();
        List<PopulationState> sampledStates = trajectory.sampledStates;
        
        for (PopulationType type : spec.model.getPopulationTypes()) {
            int[] loc = new int[type.getDims().length];
            for (int d=0; d<loc.length; d++)
                loc[d] = 0;
            outputData.put(type.getName(), iterateOverLocs(sampledStates, type, loc, 0));
        }
        
        // Add list of sampling times to output object:
        outputData.put("t", trajectory.sampledTimes);
        
        // Record spec parameters to object output:
        outputData.put("sim", spec);

        ObjectMapper mapper = new ObjectMapper();
        try {
            pstream.println(mapper.writeValueAsString(outputData));
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    private static Object iterateOverLocs (List<PopulationState> sampledStates, PopulationType type, int[] loc, int depth) {
        List<Object> nestedData = Lists.newArrayList();
        for (int i=0; i<type.getDims()[depth]; i++) {
            loc[depth] = i;
            if (depth<type.getDims().length-1)
                nestedData.add(iterateOverLocs(sampledStates, type, loc, depth+1));
            else {
                List<Object> stateList = Lists.newArrayList();
                for (PopulationState state : sampledStates)
                    stateList.add(state.get(new Population(type, loc)));
                nestedData.add(stateList);
            }
        }
        return nestedData;
    }
    
    /**
     * Express a given trajectory ensemble as a JSON-formatted string and send
     * the result to a PrintStream.
     * 
     * @param ensemble Trajectory ensemble to dump.
     * @param pstream PrintStream where output is sent.
     */
    public static void write(Ensemble ensemble, PrintStream pstream) {
        
        if (ensemble.getSpec().getVerbosity()>0)
            System.out.println("Writing JSON output...");
        
        HashMap<String, Object> outputData = Maps.newHashMap();
        
        EnsembleSpec spec = ensemble.spec;
        
        List<Object> trajData = Lists.newArrayList();
        for (Trajectory trajectory : ensemble.trajectories) {
            HashMap<String, Object> thisTrajData = Maps.newHashMap();
            List<PopulationState> sampledStates = trajectory.sampledStates;
        
            for (PopulationType type : spec.model.getPopulationTypes()) {
                int[] loc = new int[type.getDims().length];
                for (int d=0; d<loc.length; d++)
                    loc[d] = 0;
                thisTrajData.put(type.getName(), iterateOverLocs(sampledStates, type, loc, 0));
            }
        
            // Add list of sampling times to output object:
            thisTrajData.put("t", trajectory.sampledTimes);
            trajData.add(thisTrajData);
        }
        outputData.put("trajectories", trajData);
        
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
     * Express a given ensemble summary as a JSON-formatted string and send
     * the result to a PrintStream.
     *
     * @param ensembleSummary Ensemble summary to dump.
     * @param pstream PrintStream where output is sent.
     */
    public static void write(EnsembleSummary ensembleSummary, PrintStream pstream) {
        
        if (ensembleSummary.getSpec().getVerbosity()>0)
            System.out.println("Writing JSON output...");

        HashMap<String, Object> outputData = Maps.newHashMap();
        
        EnsembleSummarySpec spec = ensembleSummary.spec;
        StateSummary[] stateSummaries = ensembleSummary.stateSummaries;

        // Construct an object containing the summarized
        // data.  Heirarchy is moment->[mean/std]->schema->estimate.

        for (MomentGroup moment : spec.momentGroups) {
            HashMap<String, Object> momentData = Maps.newHashMap();

            ArrayList<Object> meanData = new ArrayList<Object>();
            for (int schema = 0; schema<stateSummaries[0].mean.get(moment).length; schema++) {
                ArrayList<Double> schemaData = Lists.newArrayList();
                for (int sidx = 0; sidx<stateSummaries.length; sidx++)
                    schemaData.add(stateSummaries[sidx].mean.get(moment)[schema]);
                meanData.add(schemaData);
            }
            momentData.put("mean", meanData);

            ArrayList<Object> stdData = Lists.newArrayList();
            for (int schema = 0; schema<stateSummaries[0].std.get(moment).length; schema++) {
                ArrayList<Double> schemaData = Lists.newArrayList();
                for (int sidx = 0; sidx<stateSummaries.length; sidx++)
                    schemaData.add(stateSummaries[sidx].std.get(moment)[schema]);
                stdData.add(schemaData);
            }
            momentData.put("std", stdData);

            outputData.put(moment.name, momentData);
        }

        // Add list of sampling times to output object:
        ArrayList<Double> tData = Lists.newArrayList();
        double dT = spec.getSampleDt();
        for (int sidx = 0; sidx<stateSummaries.length; sidx++)
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
     * Express a given trajectory ensemble as a JSON-formatted string and send
     * the result to a PrintStream.
     * 
     * @param ensemble Trajectory ensemble to dump.
     * @param pstream PrintStream where output is sent.
     */
    public static void write(InheritanceEnsemble iensemble, PrintStream pstream) {
        HashMap<String, Object> outputData = Maps.newHashMap();
        
        if (iensemble.getSpec().getVerbosity()>0)
            System.out.println("Writing JSON output...");
        
        InheritanceEnsembleSpec spec = iensemble.getSpec();
        
        List<Object> trajData = Lists.newArrayList();
        for (Trajectory trajectory : iensemble.getTrajectories()) {
            HashMap<String, Object> thisTrajData = Maps.newHashMap();
            List<PopulationState> sampledStates = trajectory.sampledStates;
        
            for (PopulationType type : spec.getModel().getPopulationTypes()) {
                int[] loc = new int[type.getDims().length];
                for (int d=0; d<loc.length; d++)
                    loc[d] = 0;
                thisTrajData.put(type.getName(), iterateOverLocs(sampledStates, type, loc, 0));
            }
        
            // Add list of sampling times to output object:
            thisTrajData.put("t", trajectory.sampledTimes);
            trajData.add(thisTrajData);
        }
        outputData.put("trajectories", trajData);
        
        // Record spec parameters to object output:
        outputData.put("sim", spec);

        ObjectMapper mapper = new ObjectMapper();
        try {
            pstream.println(mapper.writeValueAsString(outputData));
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
