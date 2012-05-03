package hamlet;

import java.util.*;

/**
 *
 * @author Tim Vaughan
 */
public class TreeSpec extends Spec {

	// Populations to exclude from tree:
	List<Population> treePops;

	public TreeSpec() {
		super();

	}

	public List<Population> getExcludedPops() {
		return treePops;
	}

	public void setExcludedPops(Population ... excludedPopsArray) {
		treePops = new ArrayList<Population>();
		treePops.addAll(Arrays.asList(excludedPopsArray));
	}
}