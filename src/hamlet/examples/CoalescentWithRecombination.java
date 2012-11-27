/*
 * Copyright (C) 2012 Tim Vaughan <tgvaughan@gmail.com>
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
package hamlet.examples;

import hamlet.Population;
import hamlet.PopulationState;
import hamlet.inheritance.ConditionMRCA;
import hamlet.inheritance.InheritanceModel;
import hamlet.inheritance.InheritanceReactionGroup;
import hamlet.inheritance.InheritanceTrajectory;
import hamlet.inheritance.InheritanceTrajectorySpec;
import hamlet.inheritance.NewickOutput;
import hamlet.inheritance.Node;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulates an ancestral recombination graph for a set of N samples
 * under a recombinant coalescent model.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class CoalescentWithRecombination {
    
    public static void main (String[] args) throws FileNotFoundException {
        
        /*
         * Assemble model
         */
        
        InheritanceModel model = new InheritanceModel();
        
        // Define populations:
        Population X = new Population("X");
        model.addPopulation(X);
        
        // Define inheritance relationships in a coalescence event
        Node Xparent1 = new Node(X);
        Node Xparent2 = new Node(X);
        Node Xchild = new Node(X);
        Xparent1.addChild(Xchild);
        Xparent2.addChild(Xchild);
        
        // Define coalescence reaction:
        InheritanceReactionGroup coalescence = new InheritanceReactionGroup("Coalescence");
        coalescence.addInheritanceReactantSchema(Xparent1, Xparent2);
        coalescence.addInheritanceProductSchema(Xchild);
        coalescence.addRate(1.0);
        model.addInheritanceReactionGroup(coalescence);
        
        // Define inheritance relationships in a recombination event
        Node Xparent = new Node(X);
        Node Xchild1 = new Node(X);
        Node Xchild2 = new Node(X);
        Xparent.addChild(Xchild1);
        Xparent.addChild(Xchild2);
        
        // Define recombination reaction:
        InheritanceReactionGroup recombination = new InheritanceReactionGroup("Recombination");
        recombination.addInheritanceReactantSchema(Xparent);
        recombination.addInheritanceProductSchema(Xchild1, Xchild2);
        recombination.addRate(2.0);
        model.addInheritanceReactionGroup(recombination);
        
        /*
         * Set initial state:
         */

        List<Node> initNodes = new ArrayList<Node>();
        for (int i=0; i<50; i++)
            initNodes.add(new Node(X));
                
        /*
         * Define simulation:
         */
        
        InheritanceTrajectorySpec spec = new InheritanceTrajectorySpec();
        spec.setModel(model);
        spec.setSimulationTime(Double.POSITIVE_INFINITY);
        spec.setInitNodes(initNodes);
        spec.addLineageEndCondition(new ConditionMRCA());
                
        /*
         * Generate coalescent tree:
         */
        
        InheritanceTrajectory traj = new InheritanceTrajectory(spec);
        
        /*
         * Write output in extended Newick format:
         */
        
        NewickOutput.write(traj, true, new PrintStream("out.tree"));
    }
}
