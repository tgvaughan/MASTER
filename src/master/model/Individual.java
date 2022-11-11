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
package master.model;

import master.model.Node;
import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.core.Input.Validate;
import beast.base.core.BEASTObject;
import java.util.ArrayList;
import java.util.List;
import master.model.Population;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Plugin representing an individual member of a population.  Used"
        + "for specifying inheritance in reactions.")
public class Individual extends BEASTObject {
    
    public Input<Population> populationInput = new Input<Population>(
            "population",
            "Population to which this individual belongs.",
            Validate.REQUIRED);    
    
    public Input<Double> timeInput = new Input<Double>("time",
            "Time at which individual comes into existance. (Default 0.0)",
            0.0);
    
    public Input<String> labelInput = new Input<String>("label",
            "Optional unique node label for use in generating "
            + "Newick/NEXUS/BEAST output.");
    
    public Input<List<Individual>> childrenInput = new Input<List<Individual>>(
            "child",
            "An individual which is a child of this one.",
            new ArrayList<Individual>());
    
    Node node;
    
    public Individual() { }
    
    @Override
    public void initAndValidate() {
        
        node = new master.model.Node(populationInput.get(), timeInput.get());
        if (labelInput.get() != null)
            node.setName(labelInput.get());
        
        for (Individual child : childrenInput.get())
            node.addChild(child.node, child.node.getPopulation());
    }

    /**
     * @return Node object
     */
    public Node getNode() {
        return node;
    }
}
