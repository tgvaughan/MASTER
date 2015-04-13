package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class CoalescentForestTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/CoalescentForest.xml";
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("CoalescentForest_output.nexus"), "616acf418fe1f49bf4f53fcbc248248c");
        return map;
    }
}
