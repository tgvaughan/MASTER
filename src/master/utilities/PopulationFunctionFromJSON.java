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
import com.google.common.collect.Lists;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import master.utilities.pfe.PFExpressionLexer;
import master.utilities.pfe.PFExpressionParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BrentSolver;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Construct a population function from a JSON output file.")
public class PopulationFunctionFromJSON extends PopulationFunction.Abstract {
    
    public Input<String> fileNameInput = new Input<String>("fileName",
            "Name of JSON output file to use.", Validate.REQUIRED);
    
    public Input<String> popSizeExpressionInput = new Input<String>("popSizeExpression",
            "Either the name of a population or a simple mathematical expression"
            + "involving such names. e.g. I/(2*S) if S and I are population names.",
            Validate.REQUIRED);
    
    public Input<RealParameter> originInput = new Input<RealParameter>("origin",
            "Maps population time onto time before most recent tree sample. "
                    + "Think of this as specifying the time of the most recent "
                    + "sample in the population size trajectory time scale.",
            Validate.REQUIRED);
    
    public Input<Double> popSizeEndInput = new Input<Double>("popSizeEnd",
            "Population size to use beyond the end of the simulated trajectory.",
            0.0);
    
    public Input<Double> popSizeStartInput = new Input<Double>("popSizeStart",
            "Population size to use before the start of the simulated trajectory.",
            0.0);
    
    /*
    public Input<Integer> trajNumInput = new Input<Integer>("trajNum",
            "The index of the trajectory to use if the JSON file contains an"
            + " ensemble of trajectories, but ignored otherwise.  Default 0.", 0);
    */

    Double [] times, popSizes, intensities, intensitiesRev;
    
    double tIntensityTrajStart, dt;
    
    int stepCount;
    
    @Override
    public void initAndValidate() throws Exception {
        
        // Read in JSON file:
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(
                new FileInputStream(fileNameInput.get()));
       
        // Read in times
        times = new Double[rootNode.get("t").size()];
        for (int i=0; i<times.length; i++)
            times[i] = rootNode.get("t").get(i).asDouble();
        
        // Build AST of population size expression
        ANTLRInputStream input = new ANTLRInputStream(popSizeExpressionInput.get());
        PFExpressionLexer lexer = new PFExpressionLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PFExpressionParser parser = new PFExpressionParser(tokens);
        ParseTree tree = parser.start();
        
        // Calculate population sizes
        PFEVisitor visitor = new PFEVisitor(rootNode);
        popSizes = visitor.visit(tree);
        
        // Numerically integrate to get intensities:
        intensities = new Double[times.length];
        intensities[times.length-1] = 0.0;
        for (int i=times.length-1; i>0; i--) {
            intensities[i-1] = intensities[i]
                    + (times[i]-times[i-1])/popSizes[i-1];
        }
        
        // Copy to reversed intensities array needed for binary search
        intensitiesRev = new Double[times.length];
        for (int i=0; i<times.length/2; i++) {
            int j = times.length-1-i;
            intensitiesRev[i] = intensities[j];
            intensitiesRev[j] = intensities[i];
        }
    }

    @Override
    public List<String> getParameterIds() {
        return new ArrayList<String>();
    }

    @Override
    public double getPopSize(double t) {
        
        double tforward = originInput.get().getValue() - t;
        
        if (tforward>times[times.length-1])
            return popSizeEndInput.get();
        
        if (tforward<0)
            return popSizeStartInput.get();
        
        // Choose which index into integration lattice to use:
        int tidx = Arrays.binarySearch(times, originInput.get().getValue()-t);
        if (tidx<0)
            tidx = -(tidx + 1) - 1;

        return popSizes[tidx];
    }

    @Override
    public double getIntensity(double t) {
        
        double tforward = originInput.get().getValue() - t;
        
        if (tforward>times[times.length-1]) {
            if (popSizeEndInput.get()>0.0) {
                return (times[times.length-1]-tforward)/popSizeEndInput.get();
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
            return ((times[tidx]-tforward)/(popSizes[tidx-1]) + intensities[tidx]);
        } else
            return intensities[tidx]; // Exact match can happen at boundaries.
        
    }

    @Override
    public double getInverseIntensity(double intensity) {

        if (intensity<intensities[times.length-1])
            return convertTime(times[times.length-1]) + popSizeEndInput.get()*intensity;

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
     * @param t
     * @return origin - t
     */
    private double convertTime(double t) {
        return originInput.get().getValue() - t;
    }
    
    /**
     * Main method for debugging.
     * 
     * @param args 
     * @throws java.lang.Exception 
     */
    public static void main(String [] args) throws Exception {

        
        PopulationFunctionFromJSON instance = new PopulationFunctionFromJSON();
        instance.initByName(
                "fileName", "/home/tim/work/code/MASTER/examples/SIR_mod_output.json",
                "popSizeExpression", "I",
                "origin", new RealParameter("50.0"),
                "popSizeStart", 0.1,
                "popSizeEnd", 0.1);

        // Write pop sizes and intensities out
        PrintStream outf = new PrintStream("test.txt");
        outf.println("t N intensity invIntensity");
        double dt = 60.0/1000;
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
