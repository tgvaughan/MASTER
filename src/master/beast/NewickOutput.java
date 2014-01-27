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

import master.outputs.InheritanceEnsembleOutput;
import master.outputs.InheritanceTrajectoryOutput;
import beast.core.BEASTObject;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import master.InheritanceEnsemble;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Hamlet output writer capable of writing inheritance graph to"
        + " disk in extended Newick format of Cardona et al, BMC Bioinf. (2008).")
public class NewickOutput extends BEASTObject implements InheritanceTrajectoryOutput, InheritanceEnsembleOutput {
    
    public Input<String> fileNameInput = new Input<String>("fileName",
            "Name of file to write to.", Validate.REQUIRED);
    
    public Input<Boolean> reverseTimeInput = new Input<Boolean>("reverseTime",
            "Read graph in reverse time - useful for building coalescent trees.  (Default false.)",
            false);
    
    public Input<Boolean> collapseSingleChildNodesInput = new Input<Boolean>(
            "collapseSingleChildNodes",
            "Prune nodes having a single child from output. (Default false.)",
            false);

    public NewickOutput() { }
    
    @Override
    public void initAndValidate() { }

    @Override
    public void write(master.inheritance.InheritanceTrajectory itraj) {
        try {
            master.inheritance.NewickOutput.write(itraj,
                    reverseTimeInput.get(),
                    collapseSingleChildNodesInput.get(),
                    new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewickOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void write(InheritanceEnsemble iensemble) {
        try {
            master.inheritance.NewickOutput.write(iensemble,
                    reverseTimeInput.get(),
                    collapseSingleChildNodesInput.get(),
                    new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewickOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
