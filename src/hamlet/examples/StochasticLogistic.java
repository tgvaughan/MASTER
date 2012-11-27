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
package hamlet.examples;

import hamlet.JsonOutput;
import hamlet.Model;
import hamlet.Population;
import hamlet.Reaction;
import hamlet.State;
import hamlet.TauLeapingStepper;
import hamlet.Trajectory;
import hamlet.TrajectorySpec;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Implements a stochastic logistic model of population dynamics.
 *
 * @author Tim Vaughan
 *
 */
public class StochasticLogistic {

    public static void main(String[] argv) throws FileNotFoundException {

        /*
         * Assemble model:
         */

        Model model = new Model();

        // Define populations:

        Population X = new Population("X");
        model.addPopulation(X);

        // Define reactions:

        // X -> 2X
        Reaction birth = new Reaction();
        birth.setReactantSchema(X);
        birth.setProductSchema(X, X);
        birth.setRate(1.0);
        model.addReaction(birth);

        // 2X -> X
        Reaction death = new Reaction();
        death.setReactantSchema(X, X);
        death.setProductSchema(X);
        death.setRate(0.01);
        model.addReaction(death);

        /*
         * Set initial state:
         */

        State initState = new State();
        initState.set(X, 1.0);

        /*
         * Assemble simulation spec:
         */

        TrajectorySpec spec = new TrajectorySpec();

        spec.setSimulationTime(100.0);
        spec.setStepper(new TauLeapingStepper(100.0/1e4));
        //spec.setStepper(new GillespieStepper());
        spec.setEvenSampling(1001);
        //spec.setUnevenSampling();
        //spec.setSeed(42);
        spec.setModel(model);
        spec.setInitState(initState);
        spec.setVerbosity(2);

        /*
         * Generate trajectory
         */

        Trajectory trajectory = new Trajectory(spec);
        
        /*
         * Write result to JSON-formatted output file:
         */
        
        JsonOutput.write(trajectory, new PrintStream("out.json"));
        
    }
}