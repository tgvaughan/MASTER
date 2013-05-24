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
import beast.core.Input.Validate;
import beast.core.Plugin;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Inheritance trajectory and ensemble post-processor which filters"
        + " out lineages that do NOT terminate in a reaction belonging to"
        + " the named reaction group.")
public class LineageFilter extends Plugin implements
        InheritanceTrajectoryPostProcessor, InheritanceEnsemblePostProcessor {
    
    public Input<String> reactNameInput = new Input<String>("reactionName",
            "Name of reaction used to filter lineages.");
    
    public Input<String> populationNameInput = new Input<String>("populationName",
            "Name of population used to filter lineages.");
    
    public Input<Boolean> discardInput = new Input<Boolean>("discard",
            "Discard (instead of keep) lineages that match the name.", false);
    
    public Input<Boolean> leavesOnlyInput = new Input<Boolean>("leavesOnly",
            "Only alter leaves.", false);
    
    public Input<String> markAnnotationInput = new Input<String>("markAnnotation",
            "Mark using this annotation rather than pruning.");
    
    public Input<Boolean> noCleanInput = new Input<Boolean>("noClean",
            "Do not remove no-state-change nodes.", false);
    
    public Input<Boolean> reverseTimeInput = new Input<Boolean>("reverseTime",
            "Process inheritance graph in reverse time.  Default false.", false);

    private String string;
    private master.inheritance.FilterLineages.Rule rule;
    
    @Override
    public void initAndValidate() {
        
        if (reactNameInput.get() != null) {
            string = reactNameInput.get();
            if (discardInput.get())
                rule = master.inheritance.FilterLineages.Rule.BY_REACTNAME_DISCARD;
            else
                rule = master.inheritance.FilterLineages.Rule.BY_REACTNAME;
        }
        
        if (populationNameInput.get() != null) {
            string = populationNameInput.get();
            if (discardInput.get())
                rule = master.inheritance.FilterLineages.Rule.BY_POPTYPENAME_DISCARD;
            else
                rule = master.inheritance.FilterLineages.Rule.BY_POPTYPENAME;
        }
        
        if (string == null)
            throw new IllegalArgumentException("Either reaction or population "
                    + "name input to LineageFilter must be specified.");
    }

    @Override
    public void process(master.inheritance.InheritanceTrajectory itraj) {
        master.inheritance.FilterLineages.filter(itraj, rule, string,
                markAnnotationInput.get(), leavesOnlyInput.get(),
                noCleanInput.get(), reverseTimeInput.get());
    }

    @Override
    public void process(master.inheritance.InheritanceEnsemble iensemble) {
        for (master.inheritance.InheritanceTrajectory itraj : iensemble.getTrajectories())
            master.inheritance.FilterLineages.filter(itraj, rule, string,
                    markAnnotationInput.get(), leavesOnlyInput.get(),
                    noCleanInput.get(), reverseTimeInput.get());
    }
    
}
