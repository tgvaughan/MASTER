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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import master.model.parsers.ExpressionBaseVisitor;
import master.model.parsers.ExpressionParser;

/**
 * Parse tree visitor used to process rate multiplier expressions.  This is
 * almost identical to the inner class Predicate.PredicateEquationVisitor:
 * there's got to be some neat way to remove this code duplication!
 *
 * @author Tim Vaughan (tgvaughan@gmail.com)
 */
public class RateMultiplierExpressionVisitor extends ExpressionBaseVisitor<Double[]>{
    private final List<String> varNames;
    private int[] varVals;
    
    public RateMultiplierExpressionVisitor(List<String> varNames) {
        this.varNames = varNames;
    }
    
    
    /**
     * Set the values of the variables used when evaluating the expression.
     *
     * @param varVals
     */
    public void setVarVals(int[] varVals) {
        this.varVals = varVals;
    }
    
    @Override
    public Double[] visitNumber(ExpressionParser.NumberContext ctx) {
        return new Double[] {Double.valueOf(ctx.val.getText())};
    }
    
    @Override
    public Double[] visitVariable(ExpressionParser.VariableContext ctx) {
        String varName = ctx.VARNAME().getText();
        if (!varNames.contains(varName))
            throw new IllegalArgumentException("Variable " + varName
                + " in predicate expression was not found in reaction string.");
        
        return new Double[] {(double)varVals[varNames.indexOf(varName)]};
    }
    
    @Override
    public Double[] visitMulDiv(ExpressionParser.MulDivContext ctx) {
        Double [] left = visit(ctx.factor());
        Double [] right = visit(ctx.molecule());
        
        Double [] res = new Double[Math.max(left.length, right.length)];
        for (int i=0; i<res.length; i++) {
            if (ctx.op.getType() == ExpressionParser.MUL)
                res[i] = left[i%left.length]*right[i%right.length];
            else
                res[i] = left[i%left.length]/right[i%right.length];
        }
        
        return res;
    }
    
    @Override
    public Double[] visitAddSub(ExpressionParser.AddSubContext ctx) {
        Double [] left = visit(ctx.expression());
        Double [] right = visit(ctx.factor());
        
        Double [] res = new Double[Math.max(left.length, right.length)];
        for (int i=0; i<res.length; i++) {
            if (ctx.op.getType() == ExpressionParser.ADD)
                res[i] = left[i%left.length]+right[i%right.length];
            else
                res[i] = left[i%left.length]-right[i%right.length];
        }
        
        return res;
    }
    
    @Override
    public Double[] visitBracketed(ExpressionParser.BracketedContext ctx) {
        return visit(ctx.expression());
    }
    
    @Override
    public Double[] visitUnaryOp(ExpressionParser.UnaryOpContext ctx) {
        Double [] arg = visit(ctx.expression());
        Double [] res = null;
        
        switch(ctx.op.getType()) {
            case ExpressionParser.EXP:
                res = new Double[arg.length];
                for (int i=0; i<arg.length; i++)
                    res[i] = Math.exp(arg[i]);
                break;
                
            case ExpressionParser.LOG:
                res = new Double[arg.length];
                for (int i=0; i<arg.length; i++)
                    res[i] = Math.log(arg[i]);
                break;
                
            case ExpressionParser.SQRT:
                res = new Double[arg.length];
                for (int i=0; i<arg.length; i++)
                    res[i] = Math.sqrt(arg[i]);
                break;
                
            case ExpressionParser.SUM:
                res = new Double[1];
                res[0] = 0.0;
                for (Double el : arg)
                    res[0] += el;
                break;
                
            case ExpressionParser.THETA:
                res = new Double[arg.length];
                for (int i=0; i<arg.length; i++)
                    res[i] = arg[i] < 0.0 ? 0.0 : 1.0;
                break;
                
            case ExpressionParser.ABS:
                res = new Double[arg.length];
                for (int i=0; i<arg.length; i++)
                    res[i] = Math.abs(arg[i]);
                break;
        }
        
        return res;
    }
    
    @Override
    public Double[] visitNegation(ExpressionParser.NegationContext ctx) {
        Double[] arg = visit(ctx.molecule());
        
        Double[] res = new Double[arg.length];
        for (int i=0; i<arg.length; i++)
            res[i] = -arg[i];
        
        return res;
    }
    
    @Override
    public Double[] visitExponentiation(ExpressionParser.ExponentiationContext ctx) {
        Double [] base = visit(ctx.atom());
        Double [] power = visit(ctx.molecule());
        
        Double [] res = new Double[Math.max(base.length, power.length)];
        for (int i=0; i<res.length; i++) {
            res[i] = Math.pow(base[i%base.length], power[i%power.length]);
        }
        
        return res;
    }
    
    @Override
    public Double[] visitArray(ExpressionParser.ArrayContext ctx) {
        List<Double> resList = new ArrayList<>();
        
        for (ExpressionParser.ExpressionContext ectx : ctx.expression())
            resList.addAll(Arrays.asList(visit(ectx)));
        
        return resList.toArray(new Double[0]);
    }
}
