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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Specification for birth-death trajectory (as opposed to tree) simulations.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TrajectorySpec extends Spec {
    
    // Length of time to propagate for:
    double simulationTime;

    // Integrator to use:
    Stepper stepper;
    
    // Number of evenly spaced samples times.  If this is < 2 then
    // The number of samples is set by the state stepping algorithm.
    int nSamples;
    

    /*
     * Setters:
     */
    
    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    public void setIntegrator(Stepper integrator) {
        this.stepper = integrator;
    }

    /**
     * Set the samples to record.  If this number is >= 2, that number
     * of evenly spaced sample times will be chosen between the start
     * and end of the simulation interval.  (The actual number of samples
     * recorded may be different if state-dependent end conditions are
     * used.)  If the number is less than 2, the number of samples recorded
     * will depend on the state stepper used.  If the stepper is a finite
     * time step stochastic integrator, a sample will be taken following
     * each integration interval.  If the stepper is a Gillespie simulator,
     * a sample will be taken following each reaction.
     * 
     * @param nSamples 
     */
    public void setnSamples(int nSamples) {
        this.nSamples = nSamples;
    }
    
    /*
     * Getters:
     */
    
    public double getSimulationTime() {
        return simulationTime;
    }
   
    public Stepper getIntegrator() {
        return stepper;
    }

    public int getnSamples() {
        return nSamples;
    }

    /**
     * Get time between samples.
     *
     * @return Sampling period.
     */
    @JsonIgnore
    public double getSampleDt() {
        return simulationTime/(nSamples-1);
    }
}
