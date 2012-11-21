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
 * Static methods for generating Newick representations of tree-like
 * inheritance graphs.  Contains methods for interpreting graphs as
 * trees in either forward or reverse time directions.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class NewickOutput {
    
    /**
     *  Construct a Newick-formatted string representing a tree-like graph.
     * Graph must be tree-like in the forward time direction.
     * 
     * @param graph Graph to render as a Newick string.
     * @param includeRootBranch False causes the branch connecting the starting
     * node to its child to be excluded.
     * @param pstream PrintStream object to write output to.
     */
    public static void writeOut(InheritanceGraph graph,
            boolean includeRootBranch,
            PrintStream pstream) {
        
        StringBuilder sb = new StringBuilder();
        
        if (includeRootBranch)
            subTreeToNewick(graph.startNodes.get(0), sb, true);            
        else
            subTreeToNewick(graph.startNodes.get(0).getChildren().get(0), sb, true);

        pstream.println(sb.append(";"));
    }
    
    private static void subTreeToNewick(Node node, StringBuilder sb, boolean start) {
        
        double branchLength;
        if (start)
            branchLength=0;
        else
            branchLength = node.getTime() - node.getParents().get(0).getTime();
        
        if (node.getChildren().size()>0) {
            sb.append("(");
            boolean first=true;
            for (Node child : node.getChildren()) {
                if (!first)
                    sb.append(",");
                else
                    first = false;
                subTreeToNewick(child, sb, false);
            }
            sb.append(")");
        }
        sb.append(node.hashCode());
        sb.append(":").append(branchLength);
    }
    
    /**
     *  Construct a Newick-formatted string representing a tree-like graph.
     * Graph must be tree-like in the <b>reverse</b> time direction.
     * 
     * @param graph Graph to render as a Newick string.
     * @param includeRootBranch False causes the root branch to be excluded.
     * @param pstream PrintStream object to write output to.
     */
    public static void writeOutReverse(InheritanceGraph graph,
            boolean includeRootBranch,
            PrintStream pstream) {
        
        StringBuilder sb = new StringBuilder();
        Node reverseRoot = findReverseRoot(graph.startNodes.get(0));
        if (includeRootBranch)
            subTreeToNewickReverse(reverseRoot, sb, true);
        else
            subTreeToNewickReverse(reverseRoot.getParents().get(0), sb, true);
        
        pstream.println(sb.append(";"));
    }
    
    private static Node findReverseRoot(Node node) {
        if (node.getChildren().isEmpty())
            return node;
        else
            return findReverseRoot(node.getChildren().get(0));
    }
    
    private static void subTreeToNewickReverse(Node node, StringBuilder sb, boolean start) {
        
        double branchLength;
        if (start)
            branchLength=0;
        else
            branchLength = node.getChildren().get(0).getTime() - node.getTime();
        
        if (node.getParents().size()>0) {
            sb.append("(");
            boolean first=true;
            for (Node parent : node.getParents()) {
                if (!first)
                    sb.append(",");
                else
                    first = false;
                subTreeToNewickReverse(parent, sb, false);
            }
            sb.append(")");
        }
        sb.append(node.hashCode());
        sb.append(":").append(branchLength);
    }
    
}
