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
package hamlet.inheritance;

import java.io.PrintStream;

/**
 * Class containing static methods useful for exporting tree-like graphs
 * using the NEXUS format.  An advantage of this format over the vanilla
 * Newick format is that it allows annotations describing the population
 * type and location of each node.
 * 
 * <p>Note that extended newick strings will be used if the graph is not
 * tree-like in the direction it's traversed.</p>
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class NexusOutput extends NewickOutput {

    /**
     * Constructor.
     * 
     * @param graph Graph to represent.
     * @param reverseTime True causes the graph to be read in the direction
     * from the latest nodes to the earliest.  Useful for coalescent trees.
     */
    public NexusOutput(InheritanceTrajectory graph, boolean reverseTime) {
        super(graph, reverseTime);
    }
    
    @Override
    protected void addLabel(Node node, double branchLength) {
        
        if (leafLabels.containsKey(node))
            newickStr.append(leafLabels.get(node));
        
        if (hybridLabels.containsKey(node))
            newickStr.append("#H").append(hybridLabels.get(node));
        
        newickStr.append("[&");
        newickStr.append("type=").append(node.population.getType().getName());
        if (!node.population.isScalar()) {
            newickStr.append(",location={");
            int[] loc = node.population.getLocation();
            for (int i=0; i<loc.length; i++) {
                if (i>0)
                    newickStr.append(",");
                newickStr.append(loc[i]);
            }
            newickStr.append("}");
        }
        newickStr.append("]");
        
        newickStr.append(":").append(branchLength);
    }
    
    @Override
    public String toString() {
        StringBuilder nexusStr = new StringBuilder();
        
        nexusStr.append("#nexus\n\n")
                .append("Begin trees;\n")
                .append("tree TREE = ")
                .append(newickStr)
                .append("\nEnd;");
        
        return nexusStr.toString();
    }
    
    /**
     * Write extended Newick representation of graph to PrintStream pstream
     * with a NEXUS wrapper.  Note that in this representation nodes are
     * annotated with details of the population they belong to.
     * 
     * @param graph Graph to represent.
     * @param reverseTime Whether to traverse tree in backward time.
     * @param pstream PrintStream object to which result is sent.
     */
    public static void write(InheritanceTrajectory graph,
            boolean reverseTime, PrintStream pstream) {
        pstream.println(new NexusOutput(graph, reverseTime));
    }
}
