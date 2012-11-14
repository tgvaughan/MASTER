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
package hamlet.beast;

import beast.core.Description;
import beast.core.Input;
import beast.core.Plugin;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Individual reaction in a birth-death model.")
public class Reaction extends Plugin {
    
    public Input<String> nameInput = new Input<String>("reactionName",
            "Name of reaction. (Not used for grouped reactions.)");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Individual reaction rate. (Only used if group rate unset.)");
    
    public Input<List<Population>> reactantsInput = new Input<List<Population>>(
            "reactant",
            "Individual reactant populations.",
            new ArrayList<Population>());
    
    public Input<List<Population>> productsInput = new Input<List<Population>>(
            "product",
            "Individual product populations.",
            new ArrayList<Population>());
    
    // Reactant and product schemata
    hamlet.Population [] reactants, products;
    double rate;
    String name;
    
    public Reaction() { };
    
    @Override
    public void initAndValidate() {
        int nReactants = reactantsInput.get().size();
        reactants = new hamlet.Population[nReactants];
        for (int i=0; i<nReactants; i++)
            reactants[i] = reactantsInput.get().get(i).pop;
            
        int nProducts = productsInput.get().size();
        products = new hamlet.Population[nProducts];
        for (int i=0; i<nProducts; i++)
            products[i] = productsInput.get().get(i).pop;       
            
        if (rateInput.get() != null)
            rate = rateInput.get();
        else
            rate = -1;

        if (nameInput.get() != null)
            name = nameInput.get();
        else
            name = null;
    }
    
}
