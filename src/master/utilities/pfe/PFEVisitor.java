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

import master.Trajectory;
import master.model.Population;
import master.model.PopulationType;
import master.utilities.pfe.PFExpressionBaseVisitor;
import master.utilities.pfe.PFExpressionParser;

/**
 * AST node visitor which calculates the population function expression
 * for each state in a trajectory.
 * 
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PFEVisitor extends PFExpressionBaseVisitor<Double[]> {

    Trajectory traj;
    int n;
    
    public PFEVisitor(Trajectory traj) {
        this.traj = traj;
        this.n = traj.getSampledTimes().size();
    }
    
    /**
     * Obtain population specified by string given in PFE.
     * 
     * @param popTypeString
     * @param locString
     * @return population object
     */
    Population getPop(String popTypeString, String locString) {
        int[] loc;
        if ("".equals(locString)) {
            loc = new int[1];
        } else {
            String[] locSplit = locString.substring(1, locString.length()-1).split(",");
            loc = new int[locSplit.length];
            for (int i=0; i<locSplit.length; i++)
                loc[i] = Integer.valueOf(locSplit[i]);
        }
        
        PopulationType popType = null;
        for (PopulationType thisPopType : traj.getSpec().getModel().getPopulationTypes()) {
            if (thisPopType.getName().equals(popTypeString)) {
                popType = thisPopType;
                break;
            }
        }
        
        if (popType != null)
            return new Population(popType, loc);
        else
            throw new IllegalStateException("Uknown population type "
                    + popTypeString + ".");
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
        
        String popTypeStr = ctx.population().POPTYPE().getText();
        String locStr = ctx.population().LOC().getText();
        Population pop = getPop(popTypeStr, locStr);
        
        for (int i=0; i<n; i++)
            vec[i] = traj.getSampledStates().get(i).get(pop);
        
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
