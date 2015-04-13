package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class diffusion1D_periodicTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/Diffusion1D_periodic.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("Diffusion1D_periodic_output.json"), "2fca54d35f3505bb99e1340e8a96c1c2");
        return map;
    }
}
