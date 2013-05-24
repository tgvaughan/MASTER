/*
 * Copyright (C) 2013 Tim Vaughan <tgvaughan@gmail.com>
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

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Inheritance trajectory and ensemble post-processor which filters"
        + " out lineages that do NOT terminate in a reaction belonging to"
        + " the named reaction group.")
public class LineageFilter extends Plugin implements
        InheritanceTrajectoryPostProcessor, InheritanceEnsemblePostProcessor {
    
    public Input<InheritanceReaction> reactionInput = new Input<InheritanceReaction>("reaction",
            "Reaction used to filter lineages.");
    
    public Input<Population> populationInput = new Input<Population>("population",
            "Population used to filter lineages.");
    
    public Input<Boolean> invertInput = new Input<Boolean>("invert",
            "Filter out lineages that DO match the name.", false);
    
    public Input<Boolean> markOnlyInput = new Input<Boolean>("markOnly",
            "If true, unsampled lineages are NOT discarded. Default false.", false);
    
    public Input<Boolean> noCleanInput = new Input<Boolean>("noClean",
            "Do not remove no-state-change nodes.", false);
    
    public Input<Boolean> reverseTimeInput = new Input<Boolean>("reverseTime",
            "Process inheritance graph in reverse time.  Default false.", false);

    private String string;
    private master.inheritance.FilterLineages.Rule rule;
    
    @Override
    public void initAndValidate() {
        
        if (reactionInput.get() != null) {
            string = reactionInput.get().getName();
            if (invertInput.get())
                rule = master.inheritance.FilterLineages.Rule.BY_REACTNAME_INV;
            else
                rule = master.inheritance.FilterLineages.Rule.BY_REACTNAME;
        }
        
        if (populationInput.get() != null) {
            string = populationInput.get().pop.getType().getName();
            if (invertInput.get())
                rule = master.inheritance.FilterLineages.Rule.BY_POPTYPENAME_INV;
            else
                rule = master.inheritance.FilterLineages.Rule.BY_POPTYPENAME;
        }
    
        if (string == null)
            throw new IllegalArgumentException("Either reaction or population "
                    + "input to LineageFilter must be specified.");
    }

    @Override
    public void process(master.inheritance.InheritanceTrajectory itraj) {
        master.inheritance.FilterLineages.filter(itraj, rule, string,
                markOnlyInput.get(), noCleanInput.get(), reverseTimeInput.get());
    }

    @Override
    public void process(master.inheritance.InheritanceEnsemble iensemble) {
        for (master.inheritance.InheritanceTrajectory itraj : iensemble.getTrajectories())
            master.inheritance.FilterLineages.filter(itraj, rule, string,
                    markOnlyInput.get(), noCleanInput.get(), reverseTimeInput.get());
    }
    
}
