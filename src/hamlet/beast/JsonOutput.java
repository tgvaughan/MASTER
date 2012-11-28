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
import beast.core.Input.Validate;
import beast.core.Plugin;
import hamlet.Ensemble;
import hamlet.EnsembleSummary;
import hamlet.Trajectory;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Hamlet output writer capable of writing trajectories to "
        + "disk as a JSON-formatted file.")
public class JsonOutput extends Plugin implements TrajectoryOutput, EnsembleOutput, EnsembleSummaryOutput {
    
    public Input<String> fileNameInput = new Input<String>("fileName",
            "Name of file to write to.", Validate.REQUIRED);
    
    public JsonOutput() { }
    
    @Override
    public void initAndValidate() { };

    @Override
    public void write(Trajectory traj) {
        try {
            hamlet.JsonOutput.write(traj, new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void write(Ensemble ensemble) {
        try {
            hamlet.JsonOutput.write(ensemble, new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void write(EnsembleSummary ensemblesum) {
        try {
            hamlet.JsonOutput.write(ensemblesum, new PrintStream(fileNameInput.get()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
