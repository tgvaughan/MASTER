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
package master.beast;

import beast.core.Description;
import beast.core.Input;
import beast.core.Plugin;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Individual reaction in an inheritance-tracking birth-death model.")
public class InheritanceReaction extends Plugin {
            
    public Input<String> nameInput = new Input<String>("reactionName",
            "Name of reaction. (Not used for grouped reactions.)");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Individual reaction rate. (Only used if group rate unset.)");
    
    public Input<List<Individual>> reactantsInput = new Input<List<Individual>>(
            "reactant",
            "Reactant individuals. (Times are ignored.)",
            new ArrayList<Individual>());
    
    public Input<List<Individual>> productsInput = new Input<List<Individual>>(
            "product",
            "Product individuals. (Times are ignored.)",
            new ArrayList<Individual>());
    
    public Input<String> reactionStringInput = new Input<String>(
            "value",
            "Alternative string description of reaction. (Overrides reactant and product elements.)");
    
    private master.inheritance.Node [] reactants, products;
    private double rate;
    private String name;

    public InheritanceReaction() { }
    
    @Override
    public void initAndValidate() {
                    
        if (rateInput.get() != null)
            rate = rateInput.get();
        else
            rate = -1;

        if (nameInput.get() != null)
            name = nameInput.get();
        else
            name = null;
        
        if (reactionStringInput.get() == null) {
            int nReactants = reactantsInput.get().size();
            reactants = new master.inheritance.Node[nReactants];
            for (int i=0; i<nReactants; i++)
                reactants[i] = reactantsInput.get().get(i).node;
            
            int nProducts = productsInput.get().size();
            products = new master.inheritance.Node[nProducts];
            for (int i=0; i<nProducts; i++)
                products[i] = productsInput.get().get(i).node;
        }
    }
    
    public void parseStrings(List<master.PopulationType> popTypes) {
        if (reactionStringInput.get() != null) {
            try {
                InheritanceReactionStringParser parser =
                        new InheritanceReactionStringParser(reactionStringInput.get(), popTypes);
                reactants = parser.getReactants();
                products = parser.getProducts();
            } catch (ParseException ex) {
                Logger.getLogger(InheritanceReaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public master.inheritance.Node[] getReactants() {
        return reactants;
    }
    
    public master.inheritance.Node[] getProducts() {
        return products;
    }
        
    public double getRate() {
        return rate;
    }
    
    public String getName() {
        return name;
    }
}
