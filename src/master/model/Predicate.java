/*
 * Copyright (C) 2015 Tim Vaughan (tgvaughan@gmail.com)
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
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Class of objects representing predicate functions which are
 * applied to population location variables to determine which
 * reactions to include in a model.
 *
 * @author Tim Vaughan (tgvaughan@gmail.com)
 */
public class Predicate extends BEASTObject {

    public Input<String> expInput = new Input<>("value", "Boolean-valued"
        + " expression used to determine whether reaction should be"
        + " included in model.", Validate.REQUIRED);

    private ExpressionEvaluator visitor;

    @Override
    public void initAndValidate() throws Exception { }

    /**
     * Determine whether the predicate equation holds for a particular set
     * of variable values.
     * 
     * @param scalarVarNames
     * @param scalarVarVals
     * @param vectorVarNames
     * @param vectorVarVals
     * @param functionMap
     * @return true if the predicate holds, false otherwise.
     */
    public boolean isTrue(List<String> scalarVarNames, int[] scalarVarVals,
            List<String> vectorVarNames, List<Double[]> vectorVarVals,
            Map<String, Function> functionMap) {

        if (visitor == null) {
            // Parse predicate expression
            ANTLRInputStream input = new ANTLRInputStream(expInput.get());

            // Custom parse/lexer error listener
            BaseErrorListener errorListener = new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    throw new RuntimeException("Error parsing character "
                            + charPositionInLine + " on line " +
                            + line + " of MASTER predicate expression: " + msg);
                }
            };

            ExpressionLexer lexer = new ExpressionLexer(input);
            lexer.removeErrorListeners();
            lexer.addErrorListener(errorListener);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            ExpressionParser parser = new ExpressionParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);

            ParseTree parseTree = parser.expression();
            visitor = new ExpressionEvaluator(parseTree,
                scalarVarNames, functionMap);
        }
        for (int i=0; i<vectorVarNames.size(); i++)
            visitor.setVectorVar(vectorVarNames.get(i), vectorVarVals.get(i));

        for (Double el : visitor.evaluate(scalarVarVals)) {
            if (el<1.0)
                return false;
        }

        return true;
    }

}
