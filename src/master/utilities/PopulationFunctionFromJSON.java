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
import beast.evolution.tree.coalescent.PopulationFunction;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Construct a population function from a JSON output file.")
public class PopulationFunctionFromJSON extends PopulationFunction.Abstract {
    
    public Input<String> fileNameInput = new Input<String>("fileName",
            "Name of JSON output file to use.", Validate.REQUIRED);
    
    public Input<String> popSizeExpressionInput = new Input<String>("popSizeExpression",
            "Either the name of a population or a simple mathematical expression"
            + "involving such names. e.g. I/(2*S) if S and I are population names.");
    
    public Input<Integer> trajNumInput = new Input<Integer>("trajNum",
            "The index of the trajectory to use if the JSON file contains an"
            + " ensemble of trajectories, but ignored otherwise.  Default 0.", 0);

    @Override
    public void initAndValidate() throws Exception {
        
    }

    @Override
    public List<String> getParameterIds() {
        return new ArrayList<String>();
    }

    @Override
    public double getPopSize(double t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getIntensity(double t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getInverseIntensity(double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Main method for debugging.
     * 
     * @param args 
     */
    public static void main(String [] args) {
       
        String testExp = "I/(2*S)";
        
        ANTLRInputStream input = new ANTLRInputStream(testExp);
        
        PFExpressionLexer lexer = new PFExpressionLexer(input);
        
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        PFExpressionParser parser = new PFExpressionParser(tokens);
        
        ParseTree tree = parser.start();
        System.out.println(tree.toStringTree(parser));
        
    }
}
