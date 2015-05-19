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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import master.model.parsers.MASTERGrammarLexer;
import master.model.parsers.MASTERGrammarParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Specifies a function to be used inside rate multiplier and
 * predicate expressions.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class Function extends BEASTObject {

    public Input<String> paramsInput = new Input<>("params",
            "Names of one or more function parameters.");

    public Input<String> valueInput = new Input<>("value",
            "Expression used as the body of the function.",
            Validate.REQUIRED);

    List<String> paramNames;
    private ParseTree parseTree;
    private ExpressionEvaluator evaluator;

    @Override
    public void initAndValidate() throws Exception {
        if (getID() == null || getID().isEmpty())
            throw new IllegalArgumentException("Functions must have an ID"
                    + " specified, as this is used to identify functions"
                    + " in expressions.");

        paramNames = new ArrayList<>();
        if (paramsInput.get() != null)
            paramNames.addAll(Arrays.asList(paramsInput.get().trim().split(" +")));

        // Parse function expression
        ANTLRInputStream input = new ANTLRInputStream(valueInput.get());

        // Custom parse/lexer error listener
        BaseErrorListener errorListener = new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                throw new RuntimeException("Error parsing character " +
                        charPositionInLine + " of line " + line +
                        " of MASTER expression function: " + msg);
            }
        };

        MASTERGrammarLexer lexer = new MASTERGrammarLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        MASTERGrammarParser parser = new MASTERGrammarParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        parseTree = parser.expression();
    }

    /**
     * Obtain evaluator for function expression.
     * 
     * @param scalarVarNames
     * @param functionMap
     * @return evaluator
     */
    public ExpressionEvaluator getEvaluator(List<String> scalarVarNames,
            Map<String, Function> functionMap) {
        if (evaluator == null)
            evaluator = new ExpressionEvaluator(parseTree, scalarVarNames, functionMap);

        return evaluator;
    }

    /**
     * @return list of parameter names.
     */
    public List<String> getParamNames() {
        return paramNames;
    }
}
