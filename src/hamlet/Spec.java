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
package hamlet;

/**
 * A basic birth-death simulation specification.
 *
 * @author Tim Vaughan
 */
public class Spec {

    // Birth-death model to simulate:
    Model model;

    // Seed for RNG (negative number means use default seed):
    long seed;
    
    // Initial state of system:
    State initState;
    
    // Verbosity level for reportage on progress of simulation:
    int verbosity;

    /*
     * Constructor:
     */
    public Spec() {

        // Spec progress reportage off by default:
        this.verbosity = 0;

        // Use BEAST RNG seed unless specified:
        this.seed = -1;

    }

    /*
     * Setters:
     */
    public void setModel(Model model) {
        this.model = model;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setInitState(State initState) {
        this.initState = initState;
    }

    public void setVerbosity(int verbosity) {
        if (verbosity<0 || verbosity>3)
            throw new IllegalArgumentException("Verbosity number must be between 0 and 3.");

        this.verbosity = verbosity;
    }

    /*
     * Getters:
     */
    public Model getModel() {
        return model;
    }

    public long getSeed() {
        return seed;
    }
    
    public State getInitState() {
        return initState;
    }

}
