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
	 * Create new state summary using a given list of moments.
	 * 
	 * @param moments List of moments to use to summarise states.
	 */
	public StateSummary (List<MomentGroup> moments) {

		mean = Maps.newHashMap();
		std = Maps.newHashMap();
                summaries = Maps.newHashMap();

		for (MomentGroup moment : moments) {
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

		for (MomentGroup moment : mean.keySet())
			moment.getSummary(state, summaries.get(moment));

		sampleNum += 1;

	}
      
        /**
         * Incorporate latest full trajectory summaries into mean
         * and variance estimates.
         */
        public void accept() {
            for (MomentGroup moment : mean.keySet()) {
                for (int i=0; i<mean.get(moment).length; i++) {
                    double summary = summaries.get(moment)[i];
                    mean.get(moment)[i] += summary;
                    std.get(moment)[i] += summary*summary;
                }
            }
        }

	/**
	 * Normalise the summary.
	 */
	public void normalise() {
		for (MomentGroup moment : mean.keySet()) {
			double[] thisMean = mean.get(moment);
			double[] thisStd = std.get(moment);
			for (int i=0; i<thisMean.length; i++) {
				thisMean[i] /= sampleNum;
				thisStd[i] /= sampleNum;
				thisStd[i] = Math.sqrt(thisStd[i] - thisMean[i]*thisMean[i]);
			}
		}
	}
}
