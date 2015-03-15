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
import java.util.List;
import master.model.parsers.ExpressionLexer;
import master.model.parsers.ExpressionParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
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

    ExpressionEvaluator evaluator;

    @Override
    public void initAndValidate() throws Exception {
        if (getID() == null || getID().isEmpty())
            throw new IllegalArgumentException("Functions must have an ID"
                    + " specified, as this is used to identify functions"
                    + " in expressions.");

        List<String> paramNames = new ArrayList<>();
        if (paramsInput.get() != null) {
            for (String paramName : paramsInput.get().trim().split(" +"))
                paramNames.add(paramName);
        }

        // Parse function expression
        ANTLRInputStream input = new ANTLRInputStream(valueInput.get());
        ExpressionLexer lexer = new ExpressionLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokens);
        ParseTree parseTree = parser.expression();
        evaluator = new ExpressionEvaluator(parseTree, paramNames, null);
    }

    /**
     * Retrieve expression evaluator corresponding to this function.
     * @return 
     */
    public ExpressionEvaluator getEvaluator() {
        return evaluator;
    }
}
