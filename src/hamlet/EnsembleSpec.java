package hamlet;

/**
 * Specification for generating an ensemble of birth-death trajectories.
 *
 * @author Tim Vaughan
 */
public class EnsembleSpec extends TrajectorySpec {

	// Number of stochastic trajectories to generate:
	int nTraj;

	public EnsembleSpec() {
		super();
	}

	public int getnTraj() {
		return nTraj;
	}

	public void setnTraj(int nTraj) {
		this.nTraj = nTraj;
	}
	
}
