package viralPopGen.debug;

import java.util.*;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		double[] t = new double[101];
		double[] x = new double[101];

		double dt = 10*Math.PI/100;

		HashMap<String,Object> data = new HashMap<String,Object>();
		ArrayList<Double> tArray = new ArrayList<Double>();
		ArrayList<Double> xArray = new ArrayList<Double>();

		for (int i=0; i<t.length; i++) {
			t[i] = i*dt;
			if (i != 0)
				x[i] = Math.sin(t[i])/t[i];
			else
				x[i] = 1.0;

			tArray.add(t[i]);
			xArray.add(x[i]);
		}

		data.put("t", tArray);
		data.put("x", xArray);

		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(data));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
