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
 * Class representing an ensemble of states summarised in terms of moment
 * estimates.
 *
 * @author Tim Vaughan
 *
 */
public class StateSummary {

    Map<Moment, Double> mean, std, summaries;
    Map<MomentGroup, double[]> groupMean, groupStd, groupSummaries;
    int sampleNum;

    /**
     * Create new state summary using a given list of moment groups.
     *
     * @param moments List of moments to use to summarize states.
     * @param momentGroups List of moment groups to use to summarize states.
     */
    public StateSummary(List<Moment> moments, List<MomentGroup> momentGroups) {

        mean = Maps.newHashMap();
        std = Maps.newHashMap();
        summaries = Maps.newHashMap();
        
        groupMean = Maps.newHashMap();
        groupStd = Maps.newHashMap();
        groupSummaries = Maps.newHashMap();
        
        for (Moment moment : moments) {
            mean.put(moment, 0.0);
            std.put(moment, 0.0);
        }
            
        
        for (MomentGroup momentGroup : momentGroups) {
            int nElements;
            if (momentGroup.isSum())
                nElements = 1;
            else
                nElements = momentGroup.getMoments().size();
            
            groupMean.put(momentGroup, new double[nElements]);
            groupStd.put(momentGroup, new double[nElements]);
            groupSummaries.put(momentGroup, new double[nElements]);
        }

        sampleNum = 0;
    }

    /**
     * Add a new state to the summary.
     *
     * @param state
     */
    public void record(PopulationState state) {
        for (Moment moment : mean.keySet())
            summaries.put(moment, moment.getSummary(state));
        
        for (MomentGroup momentGroup : groupMean.keySet())
            momentGroup.getSummary(state, groupSummaries.get(momentGroup));
    }

    /**
     * Incorporate latest summaries into mean and variance estimates.
     */
    public void accept() {
        for (Moment moment : mean.keySet()) {
            double summary = summaries.get(moment);
            mean.put(moment, mean.get(moment) + summary);
            std.put(moment, std.get(moment) + summary*summary);
        }
        
        for (MomentGroup momentGroup : groupMean.keySet()) {
            for (int i = 0; i < groupMean.get(momentGroup).length; i++) {
                double summary = groupSummaries.get(momentGroup)[i];
                groupMean.get(momentGroup)[i] += summary;
                groupStd.get(momentGroup)[i] += summary * summary;
            }
        }
        sampleNum += 1;
    }

    /**
     * Normalize the summary.
     */
    public void normalise() {
        for (Moment moment : mean.keySet()) {
            double thisMean = mean.get(moment)/sampleNum;
            double thisStd = Math.sqrt(std.get(moment)/sampleNum - thisMean*thisMean);
            
            mean.put(moment, thisMean);
            std.put(moment, thisStd);
        }
        
        for (MomentGroup momentGroup : groupMean.keySet()) {
            double[] thisMean = groupMean.get(momentGroup);
            double[] thisStd = groupStd.get(momentGroup);
            for (int i = 0; i < thisMean.length; i++) {
                thisMean[i] /= sampleNum;
                thisStd[i] /= sampleNum;

                double thisMean2 = thisMean[i] * thisMean[i];
                if (thisMean2 > thisStd[i]) {
                    // This can happen due to rounding errors when very large
                    // populations are involved.
                    thisStd[i] = 0.0;
                } else {
                    thisStd[i] = Math.sqrt(thisStd[i] - thisMean2);
                }
            }
        }
    }

    /**
     * @return means for each moment group
     */
    public Map<MomentGroup, double[]> getGroupMeans() {
        return groupMean;
    }
    
    /**
     * @return means for each moment
     */
    public Map<Moment, Double> getMeans() {
        return mean;
    }

    /**
     * @return Standard deviations for each moment group
     */
    public Map<MomentGroup, double[]> getGroupStds() {
        return groupStd;
    }
    
    /**
     * @return Standard deviations for each moment
     */
    public Map<Moment, Double> getStds() {
        return std;
    }
}
