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
        map.put(new File("Mandelbrot_output.json"), "89a7be250ac8ec962e5bcf1675259ad4");
        return map;
    }
}
