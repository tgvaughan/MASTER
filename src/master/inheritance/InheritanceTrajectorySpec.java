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

import com.google.common.collect.Lists;
import master.Model;
import master.Stepper;
import master.TrajectorySpec;
import java.util.List;

/**
 * Specification of a simulation which will result in generation of an
 * inheritance graph between individual members of the populations involved.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceTrajectorySpec extends TrajectorySpec {
    
    // Birth-death model including inheritance relationships.
    InheritanceModel inheritanceModel;

    // Nodes used to seed the inheritance graph.
    List<Node> initNodes;
    
    // Lineage end conditions:
    List<LineageEndCondition> lineageEndConditions;

    // Record population size dynamics.
    boolean samplePopSizes;
    
    // When nSamples is <2, this determines whether the state is
    // sampled following every reaction or only those reactions that
    // result in the generation of a node on the inheritance graph.
    boolean sampleStateAtNodes;
    
    // Stepper used in trajectory generation.
    private InheritanceTrajectoryStepper stepper;

    
    /**
     * Constructor.
     */
    public InheritanceTrajectorySpec() {
        super();
        
        lineageEndConditions = Lists.newArrayList();
        
        // For inheritance graphs there is a sensible default
        // maximum simulation time.
        super.setSimulationTime(Double.POSITIVE_INFINITY);
        
        // Do not record population sizes by default:
        samplePopSizes = false;
        
        // Initialise stepper:
        stepper = new InheritanceTrajectoryStepper();
    }
    
    /**
     * Specify inheritance model to use.  Note that the model field
     * is also set by this method, so that the non-inheritance graph
     * methods can still interact with those aspects of the model.
     * 
     * @param inheritanceModel 
     */
    public void setModel(InheritanceModel inheritanceModel) {
        this.inheritanceModel = inheritanceModel;
        super.setModel(inheritanceModel);
    }
    
    /**
     * Prevent use of non-inheritances model.  Messy.  Class structure is
     * getting out of hand.
     * 
     * @param model
     */
    @Override
    public void setModel(Model model) {
       throw new UnsupportedOperationException("Inheritance trajectory "
               + "specification requires inheritance model."); 
    }
    
    /**
     * Retrieve inheritance model.
     * 
     * @return inheritance model.
     */
    @Override
    public InheritanceModel getModel() {
        return inheritanceModel;
    }
    
    /**
     * Specify initial nodes whose lineages the simulation will follow.
     * 
     * @param initNodes List of starting nodes.
     */
    public void setInitNodes(List<Node> initNodes) {
        this.initNodes = initNodes;
    }
    
    /**
     * Specify end condition besides exceeding simulation time.
     * 
     * @param endCondition 
     */
    public void addLineageEndCondition(LineageEndCondition endCondition) {
        this.lineageEndConditions.add(endCondition);
    }
    
    @Override
    public void setEvenSampling(int nSamples) {
        super.setEvenSampling(nSamples);
        this.samplePopSizes = true;
    }
            
    /**
     * Sample the population sizes only when reactions occur.  If
     * sampleStateAtNodes is true, only sample when reactions cause a
     * creation of a new node in the graph.
     * 
     * @param sampleStateAtNodes Sample only when node are created.
     */
    public void setUnevenSampling(boolean sampleStateAtNodes) {
        super.setUnevenSampling();
        this.samplePopSizes = true;
        this.sampleStateAtNodes = sampleStateAtNodes;
    }
    
    @Override
    public void setUnevenSampling() {
        super.setUnevenSampling();
        this.samplePopSizes = true;
        this.sampleStateAtNodes = false;
    }
    
    /**
     * Do not record population sizes at all.
     */
    public void setNoSampling() {
        this.samplePopSizes = false;
    }
    
    @Override
    public void setStepper(Stepper stepper) {
        throw new IllegalArgumentException("State stepper cannot be set for"
                + " inheritance graphs.");
    }
    
    @Override
    public InheritanceTrajectoryStepper getStepper() {
        return stepper;
    }
    
    /**
     * Retrieve list of lineage end conditions currently in use.
     * 
     * @return list of lineage end conditions.
     */
    public List<LineageEndCondition> getLineageEndConditions() {
        return lineageEndConditions;
    }
}
