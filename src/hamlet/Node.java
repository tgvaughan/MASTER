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
package hamlet;

import com.google.common.collect.Lists;
import java.util.*;

/**
 * Node in inheritance graph generated from birth-death model.
 *
 * @author Tim Vaughan
 */
public class Node {

    // Parents of this node.
    List<Node> parents;
    
    // Children of this node.
    List<Node> children;
    
    // Population to which the individual represented by this node belongs.
    Population population;
    
    // Height of this node.
    double time;
    
    /**
     * Constructor.
     *
     * @param population Population to which node belongs.
     * @param time Time at which to position node.
     */
    public Node(Population population, double time) {

        this.population = population;
        this.time = time;

        parents = new ArrayList<Node>();
        children = new ArrayList<Node>();
    }
    
    /**
     * Constructor for inheritance map node.
     * 
     * @param population Population to which node belongs.
     */
    public Node(Population population) {
        this.population = population;
        this.time = -1;
        
        parents = new ArrayList<Node>();
        children = new ArrayList<Node>();
    }
    
    /**
     * Add a child node to the list of children.  Returns this node to
     * allow method chaining.
     * 
     * @param child Child to add.
     * @return this 
     */
    public Node addChild(Node child) {
        children.add(child);
        child.parents.add(this);
        
        return this;
    }
    
    /**
     * Add a parent node to the list of parents.  Returns this node to
     * allow method chaining.
     * 
     * @param parent Parent to add.
     * @return this
     */
    public Node addParent(Node parent) {
        parents.add(parent);
        parent.children.add(this);
        
        return this;
    }

    /**
     * Internal method for constructing copy of graph.
     * 
     * @param nodesSeen List of nodes already added to graph.
     * @param nodeCopies List of node copies corresponding to original nodes.
     * @return 
     */
    private Node graphCopy(List<Node> nodesSeen, List<Node> nodeCopies) {
        nodesSeen.add(this);
        Node copy = new Node(population, time);
        nodeCopies.add(copy);
        
        for (Node parent : parents) {
            if (!nodesSeen.contains(parent))
                copy.parents.add(parent.graphCopy(nodesSeen, nodeCopies));
            else
                copy.parents.add(nodeCopies.get(nodesSeen.indexOf(parent)));
        }
        
        for (Node child : children) {
            if (!nodesSeen.contains(child))
                copy.children.add(child.graphCopy(nodesSeen, nodeCopies));
            else
                copy.children.add(nodeCopies.get(nodesSeen.indexOf(child)));
        }
        
        return copy;
    }
    
    /**
     * Return a copy of this node and everything attached to that node.
     * 
     * @return Copy of node and attached graph.
     */
    public Node graphCopy() {
        List<Node> nodesSeen = Lists.newArrayList();
        List<Node> nodeCopies = Lists.newArrayList();
        
        return graphCopy(nodesSeen, nodeCopies);
    }
    
    /**
     * Main method for testing.
     * @param args 
     */
    public static void main(String [] args) {
        
        Population X = new Population("X");
        
        Node root1 = new Node(X);
        Node root2 = new Node(X);
        
        Node leaf1 = new Node(X);
        Node leaf2 = new Node(X);
        
        root1.addChild(leaf1);
        root1.addChild(leaf2);
        
        root2.addChild(leaf1);
        root2.addChild(leaf2);
        
        Node rootCopy = root1.graphCopy();
        System.out.println(rootCopy);
    }
}
