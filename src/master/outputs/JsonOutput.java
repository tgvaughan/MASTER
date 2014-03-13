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
package master.outputs;

import beast.core.BEASTObject;
import beast.core.Input;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import master.Ensemble;
import master.EnsembleSpec;
import master.EnsembleSummary;
import master.EnsembleSummarySpec;
import master.model.Moment;
import master.model.MomentGroup;
import master.model.Population;
import master.model.PopulationState;
import master.model.PopulationType;
import master.model.StateSummary;
import master.Trajectory;
import master.TrajectorySpec;
import master.InheritanceEnsemble;
import master.InheritanceEnsembleSpec;
import master.InheritanceTrajectory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class containing static methods for writing tracjectory, ensemble
 * and ensemble summary results to a given PrintStream using a versatile
 * JSON format.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class JsonOutput extends BEASTObject implements
        TrajectoryOutput, 
        EnsembleOutput,
        EnsembleSummaryOutput,
        InheritanceTrajectoryOutput,
        InheritanceEnsembleOutput {
    
    public Input<String> fileNameInput = new Input<String>("fileName",
            "Name of file to write to.", Input.Validate.REQUIRED);
    
    PrintStream pstream;
    
    /**
     * Default constructor.
     */
    public JsonOutput() { }
    
    /**
     * Constructor for non-beast usage.
     * @param fileName
     * @throws FileNotFoundException 
     */
    public JsonOutput(String fileName) throws FileNotFoundException {
        pstream = new PrintStream(fileName);
    }
    
    @Override
    public void initAndValidate() throws FileNotFoundException {
        pstream = new PrintStream(fileNameInput.get());
    }
    
    /**
     * Express a given trajectory as a JSON-formatted string and send the
     * result to a PrintStream.
     * 
     * @param trajectory Trajectory to dump.
     */
    @Override
    public void write(Trajectory trajectory) {
        
        if (trajectory.getSpec().getVerbosity()>0)
            System.out.println("Writing JSON output...");
        
        HashMap<String, Object> outputData = Maps.newHashMap();
        
        TrajectorySpec spec = trajectory.getSpec();
        List<PopulationState> sampledStates = trajectory.getSampledStates();
        
        for (PopulationType type : spec.getModel().getPopulationTypes()) {
            int[] loc = new int[type.getDims().length];
            for (int d=0; d<loc.length; d++)
                loc[d] = 0;
            outputData.put(type.getName(), iterateOverLocs(sampledStates, type, loc, 0));
        }
        
        // Add list of sampling times to output object:
        outputData.put("t", trajectory.getSampledTimes());
        
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
     * Private method for iteration over locations.
     * 
     * @param sampledStates
     * @param type
     * @param loc
     * @param depth
     * @return 
     */
    private Object iterateOverLocs (List<PopulationState> sampledStates, PopulationType type, int[] loc, int depth) {

        if (depth<type.getDims().length) {
            List<Object> nestedData = Lists.newArrayList();
            
            for (int i=0; i<type.getDims()[depth]; i++) {
                loc[depth] = i;
                nestedData.add(iterateOverLocs(sampledStates, type, loc, depth+1));
            }
            
            return nestedData;
            
        } else {
            
            List<Object> stateList = Lists.newArrayList();
            for (PopulationState state : sampledStates)
                stateList.add(state.get(new Population(type, loc)));

            return stateList;
            
        }
    }
    
    /**
     * Express a given trajectory ensemble as a JSON-formatted string and send
     * the result to a PrintStream.
     * 
     * @param ensemble Trajectory ensemble to dump.
     */
    @Override
    public void write(Ensemble ensemble) {
        
        if (ensemble.getSpec().getVerbosity()>0)
            System.out.println("Writing JSON output...");
        
        HashMap<String, Object> outputData = Maps.newHashMap();
        
        EnsembleSpec spec = ensemble.getSpec();
        
        List<Object> trajData = Lists.newArrayList();
        for (Trajectory trajectory : ensemble.getTrajectories()) {
            HashMap<String, Object> thisTrajData = Maps.newHashMap();
            List<PopulationState> sampledStates = trajectory.getSampledStates();
        
            for (PopulationType type : spec.getModel().getPopulationTypes()) {
                int[] loc = new int[type.getDims().length];
                for (int d=0; d<loc.length; d++)
                    loc[d] = 0;
                thisTrajData.put(type.getName(), iterateOverLocs(sampledStates, type, loc, 0));
            }
        
            // Add list of sampling times to output object:
            thisTrajData.put("t", trajectory.getSampledTimes());
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
     */
    @Override
    public void write(EnsembleSummary ensembleSummary) {
        
        if (ensembleSummary.getSpec().getVerbosity()>0)
            System.out.println("Writing JSON output...");

        HashMap<String, Object> outputData = Maps.newHashMap();
        
        EnsembleSummarySpec spec = ensembleSummary.getSpec();
        StateSummary[] stateSummaries = ensembleSummary.getStateSummaries();

        // Construct an object containing the summarized
        // data.  Heirarchy is moment->[mean/std]->schema->estimate.

        for (MomentGroup momentGroup : spec.getMomentGroups()) {
            Map<String, Object> momentGroupData = Maps.newHashMap();

            ArrayList<Object> meanData = Lists.newArrayList();
            for (int schema = 0; schema<stateSummaries[0].getGroupMeans().get(momentGroup).length; schema++) {
                List<Double> schemaData = Lists.newArrayList();
                for (StateSummary stateSummary : stateSummaries)
                    schemaData.add(stateSummary.getGroupMeans().get(momentGroup)[schema]);
                meanData.add(schemaData);
            }
            momentGroupData.put("mean", meanData);

            List<Object> stdData = Lists.newArrayList();
            for (int schema = 0; schema<stateSummaries[0].getGroupStds().get(momentGroup).length; schema++) {
                List<Double> schemaData = Lists.newArrayList();
                for (StateSummary stateSummary : stateSummaries)
                    schemaData.add(stateSummary.getGroupStds().get(momentGroup)[schema]);
                stdData.add(schemaData);
            }
            momentGroupData.put("std", stdData);

            outputData.put(momentGroup.getName(), momentGroupData);
        }
        
        for (Moment moment : spec.getMoments()) {
            Map<String, Object> momentData = Maps.newHashMap();
            
            List<Object> meanData = Lists.newArrayList();
            List<Double> schemaData = Lists.newArrayList();
            for (StateSummary stateSummary : stateSummaries)
                schemaData.add(stateSummary.getMeans().get(moment));
            meanData.add(schemaData);
            momentData.put("mean", meanData);
            
            List<Object> stdData = Lists.newArrayList();
            schemaData = Lists.newArrayList();
            for (StateSummary stateSummary : stateSummaries)
                schemaData.add(stateSummary.getStds().get(moment));
            stdData.add(schemaData);
            momentData.put("std", stdData);
            
            outputData.put(moment.getName(), momentData);
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
     * @param iensemble Trajectory ensemble to dump.
     */
    @Override
    public void write(InheritanceEnsemble iensemble) {
        HashMap<String, Object> outputData = Maps.newHashMap();
        
        if (iensemble.getSpec().getVerbosity()>0)
            System.out.println("Writing JSON output...");
        
        InheritanceEnsembleSpec spec = iensemble.getSpec();
        
        List<Object> trajData = Lists.newArrayList();
        for (Trajectory trajectory : iensemble.getTrajectories()) {
            HashMap<String, Object> thisTrajData = Maps.newHashMap();
            List<PopulationState> sampledStates = trajectory.getSampledStates();
        
            for (PopulationType type : spec.getModel().getPopulationTypes()) {
                int[] loc = new int[type.getDims().length];
                for (int d=0; d<loc.length; d++)
                    loc[d] = 0;
                thisTrajData.put(type.getName(), iterateOverLocs(sampledStates, type, loc, 0));
            }
        
            // Add list of sampling times to output object:
            thisTrajData.put("t", trajectory.getSampledTimes());
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

    @Override
    public void write(InheritanceTrajectory inheritanceTrajectory) {
        write((Trajectory)inheritanceTrajectory);
    }
}
