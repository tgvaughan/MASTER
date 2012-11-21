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
import hamlet.State;
import hamlet.inheritance.InheritanceGraph;
import hamlet.inheritance.InheritanceGraphSpec;
import hamlet.inheritance.InheritanceModel;
import hamlet.inheritance.InheritanceReactionGroup;
import hamlet.inheritance.NewickOutput;
import hamlet.inheritance.Node;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a tree in reverse by simulating the coalescent process.
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
        InheritanceReactionGroup coalescence = new InheritanceReactionGroup("Coalescence");
        coalescence.addInheritanceReactantSchema(Xparent1, Xparent2);
        coalescence.addInheritanceProductSchema(Xchild);
        coalescence.addRate(1.0);
        model.addInheritanceReactionGroup(coalescence);
        
        /*
         * Set initial state:
         */
        
        State initState = new State(model);
        initState.set(X, 100.0);
        
        List<Node> initNodes = new ArrayList<Node>();
        for (int i=0; i<100; i++)
            initNodes.add(new Node(X));
        
        /*
         * Define simulation:
         */
        
        InheritanceGraphSpec spec = new InheritanceGraphSpec();
        spec.setModel(model);
        spec.setSimulationTime(Double.POSITIVE_INFINITY);
        spec.setInitState(initState);
        spec.setInitNodes(initNodes);
        
        /*
         * Generate coalescent tree:
         */
        
        InheritanceGraph graph = new InheritanceGraph(spec);
        
        /*
         * Write result to file:
         */
        
        NewickOutput.writeOutReverse(graph, false, new PrintStream("out.tree"));
    }
    
}
