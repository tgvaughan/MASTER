package hamlet;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Specification for tree generation based on stochastic birth-death models.
 *
 * @author Tim Vaughan
 */
public class TreeSpec extends Spec {

    Population rootPop;
    int [] rootSubPopLoc;
    int rootSubPopOffset;
    
    TreeIntegrator integrator;
    
    double maxHeight;

    public TreeSpec() {
        super();
    }
    
    /*
     * Setters:
     */
    
    /**
     * Identify the specific population and sub-population from which to choose
     * the root lineage of the tree.
     * 
     * @param rootPop Population containing root lineage.
     * @param subPopLoc Subpopulation containing root lineage.
     */
    public void setRootPop(Population rootPop, int[] subPopLoc) {
        this.rootPop = rootPop;
        this.rootSubPopLoc = subPopLoc;
        this.rootSubPopOffset = rootPop.subToOffset(subPopLoc);
    }
    
    /**
     * Select integration algorithm to use in tree generation.
     * 
     * @param integrator Tree-specific integrator.
     */
    public void setIntegrator(TreeIntegrator integrator) {
        this.integrator = integrator;
    }
    
    /**
     * Set maximum tree height.
     * 
     * @param maxHeight 
     */
    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }
    
    /*
     * Getters:
     */
    
    /**
     * Obtain population to which root lineage belongs.
     * 
     * @return root population
     */
    public Population getRootPop() {
        return rootPop;
    }
    
    /**
     * Obtain sub-population to which root lineage belongs.
     * 
     * @return sub population specifier.
     */
    public int[] getRootSubPop() {
        return rootSubPopLoc;
    }
    
    /**
     * Obtain offset of sub-population to which root lineage belongs.
     * 
     * @return sub population offset.
     */
    @JsonIgnore
    public int getRootSubPopOffset() {
        return rootSubPopOffset;
    }
    
    /**
     * Obtain integrator to be used in constructing tree.
     * 
     * @return Tree integrator
     */
    public TreeIntegrator getIntegrator() {
        return integrator;
    }
    
    /**
     * Obtain chosen maximum tree height.
     * 
     * @return maximum tree height.
     */
    public double getMaxHeight() {
        return maxHeight;
    }
}