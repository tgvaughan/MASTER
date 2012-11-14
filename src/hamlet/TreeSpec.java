package hamlet;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Specification for tree generation based on stochastic birth-death models.
 *
 * @author Tim Vaughan
 */
public class TreeSpec extends Spec {

    Population rootSubPop;

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
    public void setRootSubPop(Population rootSubPop) {
        this.rootSubPop = rootSubPop;
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
    public Population getRootSubPop() {
        return rootSubPop;
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