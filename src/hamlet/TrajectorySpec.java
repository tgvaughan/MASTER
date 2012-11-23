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
    
    // Whether to collect evenly spaced samples or let the state stepper
    // when to sample:
    boolean evenlySpacedSampling;

    // Number of evenly spaced samples times.  Must be >=2.
    int nSamples;
    
    /*
     * Setters:
     */
    
    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    public void setStepper(Stepper integrator) {
        this.stepper = integrator;
    }

    /**
     * Sample population sizes at evenly spaced times.
     * 
     * @param nSamples Number of samples to record. Must be >=2.
     */
    public void setEvenSampling(int nSamples) {
        if (nSamples>=2) {
            this.nSamples = nSamples;
            this.evenlySpacedSampling = true;
        } else
            this.evenlySpacedSampling = false;
    }
    
    /**
     * Sample the state following every simulation step.  Note that this
     * will still give evenly spaced samples when a finite time-step
     * integration algorithm is used as the stepper.
     */
    public void setUnevenSampling() {
        this.evenlySpacedSampling = false;
    }
    
    /*
     * Getters:
     */
    
    public double getSimulationTime() {
        return simulationTime;
    }
   
    public Stepper getStepper() {
        return stepper;
    }
    
    public int getnSamples() {
        return nSamples;
    }
    
    public boolean isSamplingEvenlySpaced() {
        return evenlySpacedSampling;
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
