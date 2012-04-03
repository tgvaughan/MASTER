package popgen.debug;

/**
 * Class containing various static methods for interrogating
 * viralPopGen objects.
 * 
 * @author Tim Vaughan
 *
 */
public class Dump {

	/**
	 * Dump a sub-population specifier to stdout.
	 * 
	 * @param sub Sub-pop specifier.
	 */
	public static void sub (int[] sub) {

		System.out.print("[");
		if (sub.length>0) {
			System.out.print(String.valueOf(sub[0]));
			for (int i=1; i<sub.length; i++)
						System.out.print(","+String.valueOf(sub[i]));
		}
		System.out.println("]");

	}

}