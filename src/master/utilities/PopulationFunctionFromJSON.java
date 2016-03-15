/*
 * Copyright (C) 2014 Tim Vaughan <tgvaughan@gmail.com>
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

package master.utilities;

import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.parameter.RealParameter;
import beast.evolution.tree.coalescent.PopulationFunction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import master.utilities.pfe.PFEJSONVisitor;
import master.utilities.pfe.PFExpressionLexer;
import master.utilities.pfe.PFExpressionParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Construct a population function from a JSON output file.")
public class PopulationFunctionFromJSON extends PopulationFunction.Abstract {
    
    public Input<String> fileNameInput = new Input<>("fileName",
            "Name of JSON output file to use.", Validate.REQUIRED);
    
    public Input<String> popSizeExpressionInput = new Input<>("popSizeExpression",
            "Either the name of a population or a simple mathematical expression"
                    + "involving such names. e.g. I/(2*S) if S and I are population names.",
            Validate.REQUIRED);
    
    public Input<RealParameter> originInput = new Input<>("origin",
            "Maps population time onto time before most recent tree sample. "
                    + "Think of this as specifying the time of the most recent "
                    + "sample in the population size trajectory time scale.",
            Validate.REQUIRED);
    
    public Input<Double> popSizeEndInput = new Input<>("popSizeEnd",
            "Population size to use beyond the end of the simulated trajectory.",
            0.0);
    
    public Input<Double> popSizeStartInput = new Input<>("popSizeStart",
            "Population size to use before the start of the simulated trajectory.",
            0.0);
    
    public Input<Integer> trajNumInput = new Input<>("trajNum",
            "The index of the trajectory to use if the JSON file contains an"
                    + " ensemble of trajectories, but ignored otherwise.  Default 0.", 0);

    Double [] times, popSizes, intensities, intensitiesRev;
    
    int peakIdx;
    
    @Override
    public void initAndValidate() {
        
        // Read in JSON file:
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;

        try {
            rootNode = mapper.readTree(
                    new FileInputStream(fileNameInput.get()));
        } catch (IOException e) {
            throw new RuntimeException("Error reading population size trajectory file "
                    + fileNameInput.get() + ".");
        }

        JsonNode trajRootNode;
        if (rootNode.has("trajectories"))
            trajRootNode = rootNode.get("trajectories").get(trajNumInput.get());
        else
            trajRootNode = rootNode;
        
        // Read in times
        times = new Double[trajRootNode.get("t").size()];
        for (int i=0; i<times.length; i++)
            times[i] = trajRootNode.get("t").get(i).asDouble();
        
        // Build AST of population size expression
        ANTLRInputStream input = new ANTLRInputStream(popSizeExpressionInput.get());
        PFExpressionLexer lexer = new PFExpressionLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PFExpressionParser parser = new PFExpressionParser(tokens);
        ParseTree tree = parser.start();
        
        // Calculate population sizes
        PFEJSONVisitor visitor = new PFEJSONVisitor(trajRootNode);
        popSizes = visitor.visit(tree);
        
        // Find peak population size
        peakIdx=-1;
        double peakVal = 0.0;
        for (int i=0; i<times.length; i++) {
            if (popSizes[i]>peakVal) {
                peakIdx = i;
                peakVal = popSizes[i];
            }
        }

        // Numerically integrate intensities from peak population size
        intensities = new Double[times.length];
        intensities[peakIdx] = 0.0;
        for (int i=peakIdx; i>0; i--) {
            intensities[i-1] = intensities[i]
                    + (times[i]-times[i-1])/popSizes[i-1];
        }
        for (int i=peakIdx; i<times.length-1; i++) {
            intensities[i+1] = intensities[i]
                    + (times[i]-times[i+1])/popSizes[i];
        }
        
        // Copy to reversed intensities array needed for binary search
        intensitiesRev = new Double[times.length];
        for (int i=0; i<times.length/2; i++) {
            int j = times.length-1-i;
            intensitiesRev[i] = intensities[j];
            intensitiesRev[j] = intensities[i];
        }
        if (times.length%2>0)
            intensitiesRev[times.length/2] = intensities[times.length/2];
    }

    @Override
    public List<String> getParameterIds() {
        return new ArrayList<>();
    }

    @Override
    public double getPopSize(double t) {
        
        double tforward = convertTime(t);
        
        if (tforward>times[times.length-1])
            return popSizeEndInput.get();
        
        if (tforward<0)
            return popSizeStartInput.get();
        
        // Choose which index into integration lattice to use:
        int tidx = Arrays.binarySearch(times, tforward);
        if (tidx<0)
            tidx = -(tidx + 1) - 1;

        return popSizes[tidx];
    }

    @Override
    public double getIntensity(double t) {
        
        double tforward = convertTime(t);
        
        if (tforward>times[times.length-1]) {
            if (popSizeEndInput.get()>0.0) {
                return intensities[times.length-1] + (times[times.length-1]-tforward)/popSizeEndInput.get();
            } else
                return Double.NEGATIVE_INFINITY;
        }
        
        if (tforward<0.0) {
            if (popSizeStartInput.get()>0.0) {
                return intensities[0]
                        + (-tforward)/popSizeStartInput.get();
            } else
                return Double.POSITIVE_INFINITY;
        }
        
        int tidx = Arrays.binarySearch(times, tforward);
        if (tidx<0) {
            tidx = -(tidx + 1);  // index of first element greater than key
            
            // Integrate from different sides depending on location wrt peakIdx
            if (tidx<=peakIdx) {
                return (times[tidx]-tforward)/(popSizes[tidx-1]) + intensities[tidx];
            } else {
                return intensities[tidx-1] - (tforward-times[tidx-1])/popSizes[tidx-1];
            }
        } else
            return intensities[tidx]; // Exact match can happen at boundaries.
        
    }

    @Override
    public double getInverseIntensity(double intensity) {

        if (intensity<intensities[times.length-1])
            return convertTime(times[times.length-1]) + popSizeEndInput.get()*(intensity-intensities[times.length-1]);

        if (intensity>intensities[0])
            return convertTime(times[0]) + popSizeStartInput.get()*(intensity-intensities[0]);
        
        int idx = Arrays.binarySearch(intensitiesRev, intensity);
        if (idx<0) {
            idx = -(idx+1);
            int tidx = times.length - 1 - idx;  // index into forward-time array
            return convertTime(times[tidx]) + (intensity-intensities[tidx])*popSizes[tidx];
        } else
            return convertTime(times[times.length-1-idx]);
    }
    
    /**
     * Convert between simulation time and tree age time.
     * @param t simulation time
     * @return origin - t
     */
    private double convertTime(double t) {
        return originInput.get().getValue() - t;
    }
    
    /**
     * Main method for debugging.
     */
    public static void main(String [] args) throws Exception {

        
        PopulationFunctionFromJSON instance = new PopulationFunctionFromJSON();
        instance.initByName(
                "fileName", "/home/tim/work/articles/volzpaper/SimulatedData/SIR_1000sims.json",
                "popSizeExpression", "(I-1)/(2*0.00075*S)",
                "origin", new RealParameter("66.5499977474"),
                "trajNum", 1,
                "popSizeStart", 0.0,
                "popSizeEnd", 0.0);

        // Write pop sizes and intensities out
        PrintStream outf = new PrintStream("test.txt");
        outf.println("t N intensity invIntensity");
        double dt = 66.5499977474/1000;
        for (int i=0; i<=1000; i++) {
            double t = dt*i;
            double N = instance.getPopSize(t);
            double intensity = instance.getIntensity(t);
            double invIntensity = instance.getInverseIntensity(intensity);
            
            outf.format("%g %g %g %g\n", t, N, intensity, invIntensity);
        }
        outf.println();
        
    }
}
