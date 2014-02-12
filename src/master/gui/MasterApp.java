/*
 * Copyright (C) 2014 Tim Vaughan <tgvaughan@gmail.com>
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

package master.gui;

import javax.swing.table.DefaultTableModel;
import master.Ensemble;
import master.EnsembleSummary;
import master.InheritanceEnsemble;
import master.InheritanceTrajectory;
import master.Trajectory;
import master.model.InitState;
import master.model.Model;
import master.model.PopulationType;
import master.model.Reaction;
import master.outputs.EnsembleOutput;
import master.outputs.EnsembleSummaryOutput;
import master.outputs.InheritanceEnsembleOutput;
import master.outputs.InheritanceTrajectoryOutput;
import master.outputs.JsonOutput;
import master.outputs.NewickOutput;
import master.outputs.NexusOutput;
import master.outputs.TrajectoryOutput;

/**
 * Controller for MASTER gui.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class MasterApp {

    MasterFrame view;
    
    beast.core.Runnable runnable;
    Model model;
    InitState initState;
    JsonOutput jsonOutput;
    NewickOutput newickOutput;
    NexusOutput nexusOutput;
    
    
    /**
     * Constructor creates default trajectory simulation.
     */
    public MasterApp() {
    }
    
    /**
     * Set view object.
     * @param view 
     */
    public void setView(MasterFrame view) {
        this.view = view;
    }
    
    /**
     * Retrieve view object.
     * 
     * @return MasterFrame object.
     */
    public MasterFrame getView() {
        return this.view;
    }
    
    /**
     * Set BEAST representation of a MASTER simulation
     * @param runnable 
     */
    public void setRunnable(beast.core.Runnable runnable) {
        this.runnable = runnable;
    }
    
    /**
     * Updates the view with the current simulation.
     */
    public void updateView() {
        
        model = null;
        jsonOutput = null;
        newickOutput = null;
        nexusOutput = null;
        
        if (runnable instanceof Trajectory) {
            Trajectory r = (Trajectory)runnable;
            model = r.getSpec().getModel();

            for (TrajectoryOutput o : r.outputsInput.get()) {
                if (o instanceof JsonOutput)
                    jsonOutput = (JsonOutput)o;
            }
        }
        
        if (runnable instanceof Ensemble) {
            Ensemble r = (Ensemble)runnable;
            model = r.getSpec().getModel();

            for (EnsembleOutput o : r.outputsInput.get()) {
                if (o instanceof JsonOutput)
                    jsonOutput = (JsonOutput)o;
            }
        }
        
        if (runnable instanceof EnsembleSummary) {
            EnsembleSummary r = (EnsembleSummary)runnable;
            model = r.getSpec().getModel();
            
            for (EnsembleSummaryOutput o : r.outputsInput.get()) {
                if (o instanceof JsonOutput)
                    jsonOutput = (JsonOutput)o;
            }
        }

        if (runnable instanceof InheritanceTrajectory) {
            InheritanceTrajectory r = (InheritanceTrajectory)runnable;
            model = r.getSpec().getModel();
            
            for (InheritanceTrajectoryOutput o : r.outputsInput.get()) {
                if (o instanceof JsonOutput) {
                    jsonOutput = (JsonOutput)o;
                    continue;
                }

                if (o instanceof NexusOutput) {
                    nexusOutput = (NexusOutput)o;
                    continue;
                }
                
                if (o instanceof NewickOutput) {
                    newickOutput = (NewickOutput)o;
                    continue;
                }
            }
        }

        if (runnable instanceof InheritanceEnsemble) {
            InheritanceEnsemble r = (InheritanceEnsemble)runnable;
            model = r.getSpec().getModel();
            
            for (InheritanceEnsembleOutput o : r.outputsInput.get()) {
                if (o instanceof JsonOutput) {
                    jsonOutput = (JsonOutput)o;
                    continue;
                }

                if (o instanceof NexusOutput) {
                    nexusOutput = (NexusOutput)o;
                    continue;
                }
                
                if (o instanceof NewickOutput) {
                    newickOutput = (NewickOutput)o;
                    continue;
                }
            }
        }
        
        view.updateModelTab(model);
        view.updateExecutionTab(runnable, jsonOutput, newickOutput, nexusOutput);
    }
    
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MasterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MasterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MasterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MasterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MasterApp controller = new MasterApp();
                controller.setView(new MasterFrame(controller));
                controller.getView().setVisible(true);
            }
        });
    }
    
}
