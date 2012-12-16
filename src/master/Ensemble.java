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
package master;

import beast.util.Randomizer;
import java.util.*;

/**
 * A class representing an ensemble of stochastic trajectories through the state
 * space of a population genetics model of viral dynamics.
 *
 * @author Tim Vaughan
 *
 */
public class Ensemble {

    // The ensemble is a large number of trajectories:
    ArrayList<Trajectory> trajectories;
    
    // Local record of simulation spec:
    EnsembleSpec spec;

    /**
     * Generate trajectory ensemble.
     *
     * @param spec Simulation specification.
     */
    public Ensemble(EnsembleSpec spec) {

        this.spec = spec;

        // Set RNG seed unless seed<0:
        if (spec.seed>=0 && !spec.seedUsed) {
            Randomizer.setSeed(spec.seed);
            spec.seedUsed = true;
        }

        // Initialise trajectory list:
        trajectories = new ArrayList<Trajectory>();

        // Generate trajectories:
        for (int traj = 0; traj<spec.nTraj; traj++) {
            
            // Report ensemble progress if verbosity high enough:
            if (spec.verbosity>0)
                System.err.println("Integrating trajectory "
                        +String.valueOf(traj+1)+" of "
                        +String.valueOf(spec.nTraj));

            Trajectory thisTraj = new Trajectory(spec);
            trajectories.add(thisTraj);

        }

    }
}