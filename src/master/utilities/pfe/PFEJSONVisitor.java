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

package master.utilities.pfe;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import master.utilities.pfe.PFExpressionBaseVisitor;
import master.utilities.pfe.PFExpressionParser;

/**
 * AST node visitor which calculates the population function expression
 * for each state in the JSON file.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PFEJSONVisitor extends PFExpressionBaseVisitor<Double[]> {

    JsonNode rootNode;
    int n;
    
    public PFEJSONVisitor(JsonNode rootNode) {
        this.rootNode = rootNode;
        this.n = rootNode.get("t").size();
    }

    @Override
    public Double[] visitNumber(PFExpressionParser.NumberContext ctx) {
        Double [] vec = new Double[n];
        
        double num = Double.valueOf(ctx.NUM().getText());
        for (int i=0; i<n; i++)
            vec[i] = num;
        
        return vec;
    }

    @Override
    public Double[] visitPop(PFExpressionParser.PopContext ctx) {
        Double [] vec = new Double[n];
        
        String popType = ctx.population().POPTYPE().getText();
        for (int i=0; i<n; i++)
            vec[i] = rootNode.get(popType).get(i).asDouble();
        
        return vec;
    }

    @Override
    public Double[] visitAdd(PFExpressionParser.AddContext ctx) {
        Double [] vec = new Double[n];
        
        Double [] left = visit(ctx.expression());
        Double [] right = visit(ctx.term());
        for (int i=0; i<n; i++)
            vec[i] = left[i]+right[i];

        return vec;
    }

    @Override
    public Double[] visitSub(PFExpressionParser.SubContext ctx) {
        Double [] vec = new Double[n];
        
        Double [] left = visit(ctx.expression());
        Double [] right = visit(ctx.term());
        for (int i=0; i<n; i++)
            vec[i] = left[i]-right[i];

        return vec;
    }

    @Override
    public Double[] visitMul(PFExpressionParser.MulContext ctx) {
        Double [] vec = new Double[n];
        
        Double [] left = visit(ctx.term());
        Double [] right = visit(ctx.factor());
        for (int i=0; i<n; i++)
            vec[i] = left[i]*right[i];

        return vec;
    }

    @Override
    public Double[] visitDiv(PFExpressionParser.DivContext ctx) {
        Double [] vec = new Double[n];
        
        Double [] left = visit(ctx.term());
        Double [] right = visit(ctx.factor());
        for (int i=0; i<n; i++)
            vec[i] = left[i]/right[i];

        return vec;
    }

    @Override
    public Double[] visitBracketed(PFExpressionParser.BracketedContext ctx) {
        return visit(ctx.expression());
    }
}
