package popgen;

import java.util.*;

/**
 *
 * @author Tim Vaughan
 */
public class TreeSpec extends Spec {

	// Populations to exclude from tree:
	List<Population> excludedPops;

	public TreeSpec() {
		super();

	}

	public List<Population> getExcludedPops() {
		return excludedPops;
	}

	public void setExcludedPops(Population ... excludedPopsArray) {
		excludedPops = new ArrayList<Population>();
		excludedPops.addAll(Arrays.asList(excludedPopsArray));
	}
}