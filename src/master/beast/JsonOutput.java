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
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Hamlet output writer capable of writing population size samples"
        + " from all kinds of trajectories and ensembles to disk as a"
        + " JSON-formatted file.")
public class JsonOutput extends Plugin implements
        TrajectoryOutput,
        EnsembleOutput,
        EnsembleSummaryOutput,
        InheritanceTrajectoryOutput {
    
    public Input<String> fileNameInput = new Input<String>("fileName",
            "Name of file to write to.", Validate.REQUIRED);
    
    public JsonOutput() { }
    
    @Override
    public void initAndValidate() { };

    @Override
    public void write(master.Trajectory traj) {
        try {
            master.JsonOutput.write(traj, new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void write(master.Ensemble ensemble) {
        try {
            master.JsonOutput.write(ensemble, new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void write(master.EnsembleSummary ensemblesum) {
        try {
            master.JsonOutput.write(ensemblesum, new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void write(master.inheritance.InheritanceTrajectory itraj) {
        try {
            master.JsonOutput.write(itraj, new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
