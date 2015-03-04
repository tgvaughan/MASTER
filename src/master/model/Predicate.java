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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import master.model.parsers.EquationBaseVisitor;
import master.model.parsers.EquationLexer;
import master.model.parsers.EquationParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Class of objects representing predicate functions which are
 * applied to population location variables to determine which
 * reactions to include in a model.
 *
 * @author Tim Vaughan (tgvaughan@gmail.com)
 */
public class Predicate extends BEASTObject {

    public Input<String> expInput = new Input<>("exp", "Boolean-valued"
        + " expression used to determine whether reaction should be"
        + " included in model.", Validate.REQUIRED);

    private ParseTree parseTree;
    private PredicateEquationVisitor visitor;

    @Override
    public void initAndValidate() throws Exception {

        // Parse predicate expression
        ANTLRInputStream input = new ANTLRInputStream(expInput.get());
        EquationLexer lexer = new EquationLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        EquationParser parser = new EquationParser(tokens);
        parseTree = parser.equation();

    }

    /**
     * Determine whether the predicate equation holds for a particular set
     * of variable values.
     * 
     * @param varNames
     * @param varVals
     * @return true if the predicate holds, false otherwise.
     */
    public boolean isTrue(List<String> varNames, int[] varVals) {
        if (visitor == null)
            visitor = new PredicateEquationVisitor(varNames);

        visitor.setVarVals(varVals);

        for (Double el : visitor.visit(parseTree)) {
            if (el<1.0)
                return false;
        }

        return true;
    }

    /**
     * Visitor used to assess the truth value of an equation.
     */
    class PredicateEquationVisitor extends EquationBaseVisitor<Double []> {

        private final List<String> varNames;
        private int[] varVals;

        public PredicateEquationVisitor(List<String> varNames) {
            this.varNames = varNames;
        }


        /**
         * Set the values of the variables used when evaluating the
         * predicate equation.
         * 
         * @param varVals 
         */
        public void setVarVals(int[] varVals) {
            this.varVals = varVals;
        }

        @Override
        public Double[] visitEquation(EquationParser.EquationContext ctx) {
            Double[] lhs = visit(ctx.expression(0));
            Double[] rhs = visit(ctx.expression(1));

            Double[] res = new Double[Math.max(lhs.length, rhs.length)];
            for (int i=0; i<res.length; i++) {
                int iLeft = i%lhs.length;
                int iRight = i%rhs.length;

                switch (ctx.op.getType()) {
                    case EquationParser.EQ:
                        res[i] = booleanToDouble(
                            Objects.equals(lhs[iLeft], rhs[iRight]));
                        break;

                    case EquationParser.NE:
                        res[i] = booleanToDouble(
                            !Objects.equals(lhs[iLeft], rhs[iRight]));
                        break;

                    case EquationParser.LT:
                        res[i] = booleanToDouble(lhs[iLeft] < rhs[iRight]);
                        break;

                    case EquationParser.GT:
                        res[i] = booleanToDouble(lhs[iLeft] > rhs[iRight]);
                        break;
 
                    case EquationParser.LE:
                        res[i] = booleanToDouble(lhs[iLeft] <= rhs[iRight]);
                        break;

                    case EquationParser.GE:
                        res[i] = booleanToDouble(lhs[iLeft] >= rhs[iRight]);
                        break;
               }
            }

            return res;
        }

        /**
         * Encode booleans as doubles.
         * 
         * @param arg
         * @return 1.0 if arg is true, 0.0 otherwise.
         */
        private double booleanToDouble(boolean arg) {
            return arg ? 1.0 : 0.0;
        }

        @Override
        public Double[] visitNumber(EquationParser.NumberContext ctx) {
            return new Double[] {Double.valueOf(ctx.val.getText())};
        }

        @Override
        public Double[] visitVariable(EquationParser.VariableContext ctx) {
            String varName = ctx.VARNAME().getText();
            if (!varNames.contains(varName))
                throw new IllegalArgumentException("Variable " + varName
                + " in predicate expression was not found in reaction string.");

            return new Double[] {(double)varVals[varNames.indexOf(varName)]};
        }

        @Override
        public Double[] visitMulDiv(EquationParser.MulDivContext ctx) {
            Double [] left = visit(ctx.factor());
            Double [] right = visit(ctx.molecule());

            Double [] res = new Double[Math.max(left.length, right.length)];
            for (int i=0; i<res.length; i++) {
                if (ctx.op.getType() == EquationParser.MUL)
                    res[i] = left[i%left.length]*right[i%right.length];
                else
                    res[i] = left[i%left.length]/right[i%right.length];
            }

            return res;
        }

        @Override
        public Double[] visitAddSub(EquationParser.AddSubContext ctx) {
            Double [] left = visit(ctx.expression());
            Double [] right = visit(ctx.factor());
        
            Double [] res = new Double[Math.max(left.length, right.length)];
            for (int i=0; i<res.length; i++) {
                if (ctx.op.getType() == EquationParser.ADD)
                    res[i] = left[i%left.length]+right[i%right.length];
                else
                    res[i] = left[i%left.length]-right[i%right.length];
            }
        
            return res; 
        }

        @Override
        public Double[] visitBracketed(EquationParser.BracketedContext ctx) {
            return visit(ctx.expression());
        }

        @Override
        public Double[] visitUnaryOp(EquationParser.UnaryOpContext ctx) {
            Double [] arg = visit(ctx.expression());
            Double [] res = null;
            
            switch(ctx.op.getType()) {
                case EquationParser.EXP:
                    res = new Double[arg.length];
                    for (int i=0; i<arg.length; i++)
                        res[i] = Math.exp(arg[i]);
                    break;
                    
                case EquationParser.LOG:
                res = new Double[arg.length];
                for (int i=0; i<arg.length; i++)
                    res[i] = Math.log(arg[i]);
                break;
                    
                case EquationParser.SQRT:
                    res = new Double[arg.length];
                    for (int i=0; i<arg.length; i++)
                        res[i] = Math.sqrt(arg[i]);
                    break;
                    
                case EquationParser.SUM:
                    res = new Double[1];
                    res[0] = 0.0;
                    for (Double el : arg)
                        res[0] += el;
                    break;
                    
                case EquationParser.THETA:
                    res = new Double[arg.length];
                    for (int i=0; i<arg.length; i++)
                        res[i] = arg[i] < 0.0 ? 0.0 : 1.0;
                    break;

                case EquationParser.ABS:
                    res = new Double[arg.length];
                    for (int i=0; i<arg.length; i++)
                        res[i] = Math.abs(arg[i]);
                    break;
            }
        
            return res;
        }

        @Override
        public Double[] visitNegation(EquationParser.NegationContext ctx) {
            Double[] arg = visit(ctx.molecule());

            Double[] res = new Double[arg.length];
            for (int i=0; i<arg.length; i++)
                res[i] = -arg[i];

            return res;
        }

        @Override
        public Double[] visitExponentiation(EquationParser.ExponentiationContext ctx) {
            Double [] base = visit(ctx.atom());
            Double [] power = visit(ctx.molecule());

            Double [] res = new Double[Math.max(base.length, power.length)];
            for (int i=0; i<res.length; i++) {
                res[i] = Math.pow(base[i%base.length], power[i%power.length]);
            }

            return res; 
        }

        @Override
        public Double[] visitArray(EquationParser.ArrayContext ctx) {
            List<Double> resList = new ArrayList<>();

            for (EquationParser.ExpressionContext ectx : ctx.expression())
                resList.addAll(Arrays.asList(visit(ectx)));

            return resList.toArray(new Double[0]);
        }
    }
    
}
