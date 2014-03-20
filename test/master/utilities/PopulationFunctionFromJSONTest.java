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

package master.utilities;

import beast.core.parameter.RealParameter;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationFunctionFromJSONTest {
    
    @Test
    public void test() throws Exception {
    
        PrintStream testInput = new PrintStream("testInput.json");
        testInput.append("{"
                + "\"t\":[0.0, 5.0, 10.0, 20.0],"
                + "\"S\":[999,800,600,600],"
                + "\"I\":[1,200,400,400],"
                + "\"R\":[0,0,0,0] }\n");
        testInput.close();
        
        PopulationFunctionFromJSON instance = new PopulationFunctionFromJSON();
        instance.initByName(
                "fileName", "testInput.json",
                "popSizeExpression", "I/(2*0.00075*S)",
                "origin", new RealParameter("20.0"),
                "trajNum", 1,
                "popSizeStart", 0.0,
                "popSizeEnd", 0.0);

        // Write pop sizes and intensities out
        double i0 = instance.getIntensity(0.0);
        double i10 = instance.getIntensity(10.0);
        double i15 = instance.getIntensity(15.0);
        double i20 = instance.getIntensity(20.0);
        double i10rel = i10 - i0;
        double i15rel = i15 - i0;
        double i20rel = i20 - i0;
        
        double N1 = 400.0/(2*0.00075*600);
        double N2 = 200.0/(2*0.00075*800);
        double N3 = 1.0/(2*0.00075*999);
        
        double i10relTruth = 10.0/N1;
        double i15relTruth = i10relTruth + 5.0/N2;
        double i20relTruth = i15relTruth + 5.0/N3;

        double tol = 1e-15;
        System.out.format("i(10)-i(0)=%g (Truth %g)\n", i10rel, i10relTruth);
        assertTrue(equalWithinTol(i10rel, i10relTruth, tol));
        System.out.format("i(15)-i(0)=%g (Truth %g)\n", i15rel, i15relTruth);
        assertTrue(equalWithinTol(i15rel, i15relTruth, tol));
        System.out.format("i(20)-i(0)=%g (Truth %g)\n", i20rel, i20relTruth);
        assertTrue(equalWithinTol(i20rel, i20relTruth, tol));
                
        System.out.format("\ni(-1)-i(0)=%g (Truth -Infinity)\n", instance.getIntensity(-1.0));
        assertTrue(instance.getIntensity(-1.0)==Double.NEGATIVE_INFINITY);
        System.out.format("i(21)-i(0)=%g (Truth Infinity)\n", instance.getIntensity(21.0));
        assertTrue(instance.getIntensity(21.0)==Double.POSITIVE_INFINITY);
        
        System.out.format("\ninverseIntensity(i0)=%g (Truth 0.0)\n", instance.getInverseIntensity(i0));
        assertTrue(equalWithinTol(instance.getInverseIntensity(i0), 0.0, tol));
        System.out.format("inverseIntensity(i1)=%g (Truth 10.0)\n", instance.getInverseIntensity(i10));
        assertTrue(equalWithinTol(instance.getInverseIntensity(i10), 10.0, tol));
        System.out.format("inverseIntensity(i2)=%g (Truth 15.0)\n", instance.getInverseIntensity(i15));
        assertTrue(equalWithinTol(instance.getInverseIntensity(i15), 15.0, tol));
        System.out.format("inverseIntensity(i3)=%g (Truth 20.0)\n", instance.getInverseIntensity(i20));
        assertTrue(equalWithinTol(instance.getInverseIntensity(i20), 20.0, tol));
    }
    
    boolean equalWithinTol(double x, double y, double tol) {
        return Math.abs(x-y)<tol;
    }
}
