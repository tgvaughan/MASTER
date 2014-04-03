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
package master;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Maps;
import java.util.Map;

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
	
    /**
     * Construct representation of specification to use in assembling
     * summary in JSON output file.
     * 
     * @return Map from strings to other objects which have a JSON rep
     */
    @JsonValue
    @Override
    public Map<String, Object> getJsonValue() {
        
        Map<String, Object> jsonObject = super.getJsonValue();
        
        jsonObject.put("nTraj", getnTraj());
        return jsonObject;
    }
}
