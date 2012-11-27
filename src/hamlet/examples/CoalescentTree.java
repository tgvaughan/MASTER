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
import hamlet.inheritance.ConditionMRCA;
import hamlet.inheritance.InheritanceModel;
import hamlet.inheritance.InheritanceReaction;
import hamlet.inheritance.InheritanceTrajectory;
import hamlet.inheritance.InheritanceTrajectorySpec;
import hamlet.inheritance.NexusOutput;
import hamlet.inheritance.Node;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a tree in reverse by simulating the coalescent process from
 * serially-sampled tips.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class CoalescentTree {
    
    public static void main (String[] args) throws FileNotFoundException {
        
        /*
         * Assemble model
         */
        
        InheritanceModel model = new InheritanceModel();
        
        // Define populations:
        Population X = new Population("X");
        model.addPopulation(X);
        
        // Define inheritance relationships in a coalescence event:
        Node Xparent1 = new Node(X);
        Node Xparent2 = new Node(X);
        Node Xchild = new Node(X);
        Xparent1.addChild(Xchild);
        Xparent2.addChild(Xchild);
                
        // Define coalescence reaction:
        InheritanceReaction coalescence = new InheritanceReaction("Coalescence");
        coalescence.setInheritanceReactantSchema(Xparent1, Xparent2);
        coalescence.setInheritanceProductSchema(Xchild);
        coalescence.setRate(1.0);
        model.addInheritanceReaction(coalescence);
        
        /*
         * Set initial state:
         */
        
        List<Node> initNodes = new ArrayList<Node>();
        for (int i=0; i<100; i++)
            initNodes.add(new Node(X, 0.1*i));
        
        /*
         * Define simulation:
         */
        
        InheritanceTrajectorySpec spec = new InheritanceTrajectorySpec();
        spec.setModel(model);
        spec.setInitNodes(initNodes);
        spec.addLineageEndCondition(new ConditionMRCA());
        spec.setUnevenSampling();
        
        /*
         * Generate coalescent tree:
         */
        
        InheritanceTrajectory traj = new InheritanceTrajectory(spec);
        
        /*
         * Write result to file:
         */
        
        NexusOutput.write(traj, true, new PrintStream("out.tree"));
    }
    
}
