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
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class NewickOutput {
    
    public static void writeOut(InheritanceGraph graph, PrintStream pstream) {
        
        StringBuilder sb = new StringBuilder();
        subTreeToNewick(graph.startNodes.get(0), sb);
        pstream.println(sb.append(";"));
    }
    
    public static void subTreeToNewick(Node node, StringBuilder sb) {
        
        double branchLength;
        if (node.getParents().isEmpty())
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
                subTreeToNewick(child, sb);
            }
            sb.append(")");
        }
        sb.append(node.hashCode());
        sb.append(":").append(branchLength);
    }
    
    public static void writeOutReverse(InheritanceGraph graph, PrintStream pstream) {
        
        StringBuilder sb = new StringBuilder();
        subTreeToNewickReverse(graph.startNodes.get(0), sb);
        pstream.println(sb.append(";"));
    }
    
    public static Node findReverseRoot(Node node) {
        if (node.getParents().isEmpty())
            return node;
        else
            return findReverseRoot(node.getParents().get(0));
    }
    
    public static void subTreeToNewickReverse(Node node, StringBuilder sb) {
        
        double branchLength;
        if (node.getChildren().isEmpty())
            branchLength=0;
        else
            branchLength = node.getParents().get(0).getTime() - node.getTime();
        
        if (node.getParents().size()>0) {
            sb.append("(");
            boolean first=true;
            for (Node parent : node.getParents()) {
                if (!first)
                    sb.append(",");
                else
                    first = false;
                subTreeToNewick(parent, sb);
            }
            sb.append(")");
        }
        sb.append(node.hashCode());
        sb.append(":").append(branchLength);
        }
    
}
