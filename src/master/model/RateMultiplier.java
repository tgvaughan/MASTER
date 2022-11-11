/*
 * Copyright (C) 2015 Tim Vaughan <tgvaughan@gmail.com>
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
package master.model;

import beast.base.core.BEASTObject;
import beast.base.core.Input;
import beast.base.core.Input.Validate;
import java.util.List;
import java.util.Map;

import master.model.parsers.MASTERGrammarLexer;
import master.model.parsers.MASTERGrammarParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class RateMultiplier extends BEASTObject {

    public Input<String> expInput = new Input<>( "value",
            "The result of evaluating this expression involving "
                    + "population location variables is multiplied with"
                    + "the base reaction rate to produce a location-specific"
                    + "rate.", Validate.REQUIRED);

    private ExpressionEvaluator visitor;

    @Override
    public void initAndValidate() { }

    /**
     * Evaluate rate multiplier expression for the given variable values.
     * 
     * @param scalarVarNames  Names of scalar variables in expression
     * @param scalarVarVals   Values of scalar variables in expression
     * @param vectorVarNames  Names of vector variables in expression
     * @param vectorVarVals   Values of vector variables in expression
     * @param functions
     * @return result of evaluating the expression
     */
    public double evaluate(List<String> scalarVarNames, int[] scalarVarVals,
            List<String> vectorVarNames, List<Double[]> vectorVarVals,
            Map<String, Function> functions) {
        if (visitor == null) {
            // Parse predicate expression
            ANTLRInputStream input = new ANTLRInputStream(expInput.get());
            MASTERGrammarLexer lexer = new MASTERGrammarLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MASTERGrammarParser parser = new MASTERGrammarParser(tokens);
            ParseTree parseTree = parser.expression();
            visitor = new ExpressionEvaluator(parseTree, scalarVarNames, functions);
        }

        for (int i=0; i<vectorVarNames.size(); i++)
            visitor.setVectorVar(vectorVarNames.get(i), vectorVarVals.get(i));

        Double[] res =  visitor.evaluate(scalarVarVals);
        if (res.length != 1) {
            throw new IllegalArgumentException(
                    "Reaction rate multiplier must be scalar!");
        }

        return res[0];
    }
    
}
