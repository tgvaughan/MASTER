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
import beast.core.Input.Validate;
import beast.core.Plugin;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Individual reaction in a birth-death model.")
public class Reaction extends Plugin {
    
    public Input<String> nameInput = new Input<String>("reactionName",
            "Name of reaction. (Not used for grouped reactions.)");
    
    public Input<Double> rateInput = new Input<Double>("rate",
            "Individual reaction rate. (Only used if group rate unset.)");
    
    public Input<List<Range>> rangesInput = new Input<List<Range>>("range",
            "Define multiple reactions for different values of a variable.",
            new ArrayList<Range>());
    
    public Input<String> reactionStringInput = new Input<String>(
            "value",
            "String description of reaction.", Validate.REQUIRED);
    
    // Reactant and product schemata
    private master.Population [] reactants, products;
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

    }
    
    public void parseStrings(List<master.PopulationType> popTypes) {
        if (reactionStringInput.get() != null) {
            try {
                ReactionStringParser parser = new ReactionStringParser(
                        reactionStringInput.get(), popTypes, rangesInput.get());
            } catch (ParseException ex) {
                Logger.getLogger(Reaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public master.Population[] getReactants() {
        return reactants;
    };
    public master.Population[] getProducts() {
        return products;
    }
    
    /**
     * Retrieve reaction rate.
     * @return rate
     */
    public double getRate() {
        return rate;
    }
    
    /**
     * Retrieve reaction name.
     * @return name
     */
    public String getName() {
        return name;
    }
    
}
