package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class MandelbrotTest extends XMLTestCase{

    @Override
    public String getXMLFileName() {
        return "examples/Mandelbrot.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("Mandelbrot_output.json"), "d918f84464b13daad8bcceea81d78f9e");
        return map;
    }
}
