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
        spec.setIntegrator(new TauLeapingStepper(100.0/1e4));
        spec.setnSamples(1001);
        spec.setSeed(42);
        spec.setModel(model);
        spec.setInitState(initState);

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