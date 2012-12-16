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
package master.inheritance;

import beast.util.Randomizer;
import java.util.ArrayList;

/**
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceEnsemble {
    
    // The ensemble is a large number of trajectories
    ArrayList<InheritanceTrajectory> itrajectories;
    
    // Record of simulation spec:
    InheritanceEnsembleSpec spec;
    
    public InheritanceEnsemble(InheritanceEnsembleSpec spec) {
        
        this.spec = spec;
        
        // Set seed if defined:
        if (spec.getSeed()>=0 && !spec.isSeedUsed()) {
            Randomizer.setSeed(spec.getSeed());
            spec.setSeedUsed();
        }
        
        // Initialise inheritance trajectory list
        itrajectories = new ArrayList<InheritanceTrajectory>();
        
        // Generate trajectories:
        for (int traj=0; traj<spec.nTraj; traj++) {
            
            // Report ensemble progress if verbosity is high enough:
            if (spec.getVerbosity()>0)
                System.err.println("Generating inheritance trajectoy "
                        + String.valueOf(traj+1) + " of "
                        + String.valueOf(spec.nTraj));
            
            InheritanceTrajectory thisTraj = new InheritanceTrajectory(spec);
            itrajectories.add(thisTraj);
        }
    }
    
}
