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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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
    
    public Input<String> reactionStringInput = new Input<String>(
            "value",
            "Alternative string description of reaction. (Overrides reactant and product elements.)");
    
    // Reactant and product schemata
    private hamlet.Population [] reactants, products;
    private double rate;
    private String name;
    
    public Reaction() { };
    
    @Override
    public void initAndValidate() throws ParseException {
 
                    
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
            reactants = new hamlet.Population[nReactants];
            for (int i=0; i<nReactants; i++)
                reactants[i] = reactantsInput.get().get(i).pop;
            
            int nProducts = productsInput.get().size();
            products = new hamlet.Population[nProducts];
            for (int i=0; i<nProducts; i++)
                products[i] = productsInput.get().get(i).pop;
        }

    }
    
    public void parseStrings(List<hamlet.PopulationType> popTypes) {
        if (reactionStringInput.get() != null) {
            try {
                ReactionStringParser parser = new ReactionStringParser(
                        reactionStringInput.get(), popTypes);
                reactants = parser.getReactants();
                products = parser.getProducts();
            } catch (ParseException ex) {
                Logger.getLogger(Reaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public hamlet.Population[] getReactants() {
        return reactants;
    };
    public hamlet.Population[] getProducts() {
        return products;
    }
    
    public double getRate() {
        return rate;
    }
    
    public String getName() {
        return name;
    }
    
}
