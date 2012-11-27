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

import hamlet.JsonOutput;
import hamlet.Population;
import hamlet.State;
import hamlet.inheritance.ConditionLineagesExtinct;
import hamlet.inheritance.InheritanceTrajectory;
import hamlet.inheritance.InheritanceTrajectorySpec;
import hamlet.inheritance.InheritanceModel;
import hamlet.inheritance.InheritanceReactionGroup;
import hamlet.inheritance.NexusOutput;
import hamlet.inheritance.Node;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates an inheritance tree of a particular individual chosen
 * from within a population evolving under the stochastic logistic model.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class BirthDeathTree {
    
    public static void main(String[] argv) throws FileNotFoundException {
        /*
         * Assemble model:
         */
    
        InheritanceModel model = new InheritanceModel();
    
        // Define populations:
        Population X = new Population("X");
        model.addPopulation(X);
        
        // Define reactions:
        
        // X -> 2X
        InheritanceReactionGroup birth = new InheritanceReactionGroup("Birth");
        Node Xparent = new Node(X);
        Node Xchild1 = new Node(X);
        Node Xchild2 = new Node(X);
        Xparent.addChild(Xchild1);
        Xparent.addChild(Xchild2);
        birth.addInheritanceReactantSchema(Xparent);
        birth.addInheritanceProductSchema(Xchild1, Xchild2);
        birth.addRate(1.0);
        model.addInheritanceReactionGroup(birth);

        // X2 -> X
        InheritanceReactionGroup death = new InheritanceReactionGroup("Death");
        Node Xcompetitor1 = new Node(X);
        Node Xcompetitor2 = new Node(X);
        Node Xsurvivor = new Node(X);
        Xcompetitor1.addChild(Xsurvivor);
        death.addInheritanceReactantSchema(Xcompetitor1, Xcompetitor2);
        death.addInheritanceProductSchema(Xsurvivor);
        death.addRate(0.01);
        model.addInheritanceReactionGroup(death);
        
        /*
         * Set initial state:
         */
        
        State initState = new State();
        initState.set(X, 100.0);
        List<Node> initNodes = new ArrayList<Node>();
        initNodes.add(new Node(X));
        
        /*
         * Define simulation:
         */
        
        InheritanceTrajectorySpec spec = new InheritanceTrajectorySpec();

        spec.setModel(model);
        spec.setInitState(initState);
        spec.setInitNodes(initNodes);
        spec.setUnevenSampling();
        spec.addLineageEndCondition(new ConditionLineagesExtinct());
        
        /*
         * Generate inheritance trajectory:
         */
        
        InheritanceTrajectory traj = new InheritanceTrajectory(spec);
        
        /*
         * Write results as JSON file and a newick tree:
         */
        
        NexusOutput.write(traj, false, new PrintStream("out.tree"));
        JsonOutput.write(traj, new PrintStream("out.json"));
    }
    
}
