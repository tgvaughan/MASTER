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

import com.google.common.collect.*;
import java.util.*;

/**
 * Class representing an ensemble of states summarised in
 * terms of moment estimates.
 * 
 * @author Tim Vaughan
 *
 */
public class StateSummary {

	Map<MomentGroup,double[]> mean, std;
        Map<MomentGroup,double[]> summaries;
	int sampleNum;

	/**
	 * Create new state summary using a given list of moment groups.
	 * 
	 * @param momentGroups List of moments groups to use to summarise states.
	 */
	public StateSummary (List<MomentGroup> momentGroups) {

		mean = Maps.newHashMap();
		std = Maps.newHashMap();
                summaries = Maps.newHashMap();

		for (MomentGroup moment : momentGroups) {
			mean.put(moment, new double[moment.summationGroups.size()]);
			std.put(moment, new double[moment.summationGroups.size()]);
                        summaries.put(moment, new double[moment.summationGroups.size()]);
		}

		sampleNum = 0;
	}

	/**
	 * Add a new state to the summary.
	 * 
	 * @param state
	 */
	public void record(PopulationState state) {
		for (MomentGroup momentGroup : mean.keySet())
			momentGroup.getSummary(state, summaries.get(momentGroup));
	}
      
        /**
         * Incorporate latest summaries into mean
         * and variance estimates.
         */
        public void accept() {
            for (MomentGroup momentGroup : mean.keySet()) {
                for (int i=0; i<mean.get(momentGroup).length; i++) {
                    double summary = summaries.get(momentGroup)[i];
                    mean.get(momentGroup)[i] += summary;
                    std.get(momentGroup)[i] += summary*summary;
                }
            }
            sampleNum += 1;
        }

	/**
	 * Normalise the summary.
	 */
	public void normalise() {
		for (MomentGroup momentGroup : mean.keySet()) {
			double[] thisMean = mean.get(momentGroup);
			double[] thisStd = std.get(momentGroup);
			for (int i=0; i<thisMean.length; i++) {
				thisMean[i] /= sampleNum;
				thisStd[i] /= sampleNum;
				thisStd[i] = Math.sqrt(thisStd[i] - thisMean[i]*thisMean[i]);
			}
		}
	}
}
