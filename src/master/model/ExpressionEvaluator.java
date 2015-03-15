package master.model;

import beast.math.GammaFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import master.model.parsers.ExpressionBaseVisitor;
import master.model.parsers.ExpressionParser;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Evaluates expressions found in rate multipliers and predicates.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class ExpressionEvaluator extends ExpressionBaseVisitor<Double[]>{

    private final List<String> varNames;
    private int[] scalarVarVals;
    private Double[][] vectorVarVals;
    private ParseTree parseTree;
    private Map<String, ExpressionEvaluator> functions;

    public ExpressionEvaluator(ParseTree parseTree, List<String> varNames,
            Map<String, ExpressionEvaluator> functions) {
        this.parseTree = parseTree;
        this.varNames = varNames;
        this.functions = functions;
    }

    public Double[] evaluate(int[] varVals) {
        this.scalarVarVals = varVals;
        return visit(parseTree);
    }

    public Double[] evaluate(Double[][] vectorVarVals) {
        this.vectorVarVals = vectorVarVals;
        return visit(parseTree);
    }

    @Override
    public Double[] visitEquality(ExpressionParser.EqualityContext ctx) {
        Double[] lhs = visit(ctx.expression(0));
        Double[] rhs = visit(ctx.expression(1));

        Double[] res = new Double[Math.max(lhs.length, rhs.length)];
        for (int i=0; i<res.length; i++) {
            int iLeft = i%lhs.length;
            int iRight = i%rhs.length;

            switch (ctx.op.getType()) {
                case ExpressionParser.EQ:
                    res[i] = booleanToDouble(
                            Objects.equals(lhs[iLeft], rhs[iRight]));
                    break;

                case ExpressionParser.NE:
                    res[i] = booleanToDouble(
                            !Objects.equals(lhs[iLeft], rhs[iRight]));
                    break;
                    
                case ExpressionParser.LT:
                    res[i] = booleanToDouble(lhs[iLeft] < rhs[iRight]);
                    break;
                    
                case ExpressionParser.GT:
                    res[i] = booleanToDouble(lhs[iLeft] > rhs[iRight]);
                    break;
                    
                case ExpressionParser.LE:
                    res[i] = booleanToDouble(lhs[iLeft] <= rhs[iRight]);
                    break;
                    
                case ExpressionParser.GE:
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
    public Double[] visitNumber(ExpressionParser.NumberContext ctx) {
        return new Double[] {Double.valueOf(ctx.val.getText())};
    }
    
    @Override
    public Double[] visitVariable(ExpressionParser.VariableContext ctx) {
        String varName = ctx.VARNAME().getText();
        if (!varNames.contains(varName))
            throw new IllegalArgumentException("Variable " + varName
                    + " in predicate expression was not found in reaction string.");

        if (scalarVarVals != null)
            return new Double[] {(double)scalarVarVals[varNames.indexOf(varName)]};
        else
            return vectorVarVals[varNames.indexOf(varName)];
    }
    
    @Override
    public Double[] visitMulDiv(ExpressionParser.MulDivContext ctx) {
        Double [] left = visit(ctx.expression(0));
        Double [] right = visit(ctx.expression(1));
        
        Double [] res = new Double[Math.max(left.length, right.length)];
        switch(ctx.op.getType()) {
            case ExpressionParser.MUL:
                for (int i=0; i<res.length; i++)
                    res[i] = left[i%left.length] * right[i%right.length];
                break;

            case ExpressionParser.DIV:
                for (int i=0; i<res.length; i++)
                    res[i] = left[i%left.length] / right[i%right.length];
                break;

            case ExpressionParser.MOD:
                for (int i=0; i<res.length; i++)
                    res[i] = left[i%left.length] % right[i%right.length];
                break;

            default:
                // Should never get here.
        }
        
        return res;
    }
    
    @Override
    public Double[] visitAddSub(ExpressionParser.AddSubContext ctx) {
        Double [] left = visit(ctx.expression(0));
        Double [] right = visit(ctx.expression(1));
        
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
        Double[] arg = visit(ctx.expression());
        
        Double[] res = new Double[arg.length];
        for (int i=0; i<arg.length; i++)
            res[i] = -arg[i];
        
        return res;
    }
    
    @Override
    public Double[] visitExponentiation(ExpressionParser.ExponentiationContext ctx) {
        Double [] base = visit(ctx.expression(0));
        Double [] power = visit(ctx.expression(1));
        
        Double [] res = new Double[Math.max(base.length, power.length)];
        for (int i=0; i<res.length; i++) {
            res[i] = Math.pow(base[i%base.length], power[i%power.length]);
        }
        
        return res;
    }

    @Override
    public Double[] visitFactorial(ExpressionParser.FactorialContext ctx) {
        Double[] arg = visit(ctx.expression());
        Double[] res = new Double[arg.length];

        for (int i=0; i<arg.length; i++)
            res[i] = Math.exp(GammaFunction.lnGamma(arg[i]+1));

        return res;
    }

    @Override
    public Double[] visitArray(ExpressionParser.ArrayContext ctx) {
        List<Double> resList = new ArrayList<>();
        
        for (ExpressionParser.ExpressionContext ectx : ctx.expression())
            resList.addAll(Arrays.asList(visit(ectx)));
        
        return resList.toArray(new Double[0]);
    }

    @Override
    public Double[] visitBooleanOp(ExpressionParser.BooleanOpContext ctx) {
        Double[] left = visit(ctx.expression(0));
        Double[] right = visit(ctx.expression(1));

        Double [] res = new Double[Math.max(left.length, right.length)];

        switch(ctx.op.getType()) {
            case ExpressionParser.AND:
                for (int i=0; i<res.length; i++)
                    res[i] = (left[i%left.length] != 0.0) && (right[i%right.length] != 0.0) ? 1.0 : 0.0;
                break;
            case ExpressionParser.OR:
                for (int i=0; i<res.length; i++)
                    res[i] = (left[i%left.length] != 0.0) || (right[i%right.length] != 0.0) ? 1.0 : 0.0;
                break;
        }

        return res;
    }

    @Override
    public Double[] visitIfThenElse(ExpressionParser.IfThenElseContext ctx) {
        Double[] cond = visit(ctx.expression(0));

        if (cond.length != 1)
            throw new IllegalArgumentException("Condition expressions in"
                    + "if-then-else must be scalar.");

        return (cond[0] != 0.0) ? visit(ctx.expression(1)) : visit(ctx.expression(2));
    }

    @Override
    public Double[] visitFunction(ExpressionParser.FunctionContext ctx) {
        String funcName = ctx.VARNAME().getText();

        if (functions == null || functions.get(funcName) == null)
            throw new IllegalArgumentException("Reference to undefined"
                    + " function '" + funcName + "' found.");

        ExpressionEvaluator funcEvaluator = functions.get(funcName);

        Double[][] paramVals = new Double[ctx.expression().size()][];

        for (int i=0; i<ctx.expression().size(); i++)
            paramVals[i] = visit(ctx.expression(i));

        return funcEvaluator.evaluate(paramVals);
    }

    
}
