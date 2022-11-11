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

import beast.base.core.BEASTObject;
import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.core.Input.Validate;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Specifies multiple individuals having identical characteristics.")
public class MultipleIndividuals extends BEASTObject {
    
    public Input<Population> populationInput = new Input<Population>(
            "population",
            "Population to which this individual belongs.",
            Validate.REQUIRED);    
    
    public Input<Double> timeInput = new Input<Double>("time",
            "Time at which individual comes into existance. (optional)",
            0.0);

    public Input<Integer> nCopiesInput = new Input<Integer>("copies",
            "Number of individuals to specify.",
            Validate.REQUIRED);
    
    List<Node> nodes;
    
    public MultipleIndividuals() { }
    
    @Override
    public void initAndValidate() {
        nodes = Lists.newArrayList();
        for (int i=0; i<nCopiesInput.get(); i++)
            nodes.add(new master.model.Node(populationInput.get(), timeInput.get()));
    }

    /**
     * @return List of nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }
}
