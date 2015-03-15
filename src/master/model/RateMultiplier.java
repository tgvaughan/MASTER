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

import beast.core.BEASTObject;
import beast.core.Input;
import beast.core.Input.Validate;
import java.util.List;
import java.util.Map;
import master.model.parsers.ExpressionLexer;
import master.model.parsers.ExpressionParser;
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
    public void initAndValidate() throws Exception { }

    /**
     * Evaluate rate multiplier expression for the given variable values.
     * 
     * @param varNames  Names of variables in expression
     * @param varVals   Values of variables in expression
     * @param functionExpressions
     * @return result of evaluating the expression
     */
    public double evaluate(List<String> varNames, int[] varVals,
            Map<String, ExpressionEvaluator> functionExpressions) {
        if (visitor == null) {
            // Parse predicate expression
            ANTLRInputStream input = new ANTLRInputStream(expInput.get());
            ExpressionLexer lexer = new ExpressionLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ExpressionParser parser = new ExpressionParser(tokens);
            ParseTree parseTree = parser.expression();
            visitor = new ExpressionEvaluator(parseTree, varNames, functionExpressions);
        }

        Double[] res =  visitor.evaluate(varVals);
        if (res.length != 1) {
            throw new IllegalArgumentException(
                    "Reaction rate multiplier must be scalar!");
        }

        return res[0];
    }
    
}
