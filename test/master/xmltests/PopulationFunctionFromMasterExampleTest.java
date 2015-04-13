package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class PopulationFunctionFromMasterExampleTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/PopulationFunctionFromMasterExample.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("PopulationFunctionFromMasterExample.log"), "8d509c028823af0dbbf8cd3eee6364d0");
        map.put(new File("PopulationFunctionFromMasterExample_popTraj.json"), "98b4efe3cdaef76f7c1d3979c04248aa");
        map.put(new File("PopulationFunctionFromMasterExample.trees"), null);
        map.put(new File("PopulationFunctionFromMasterExample.xml.state"), null);
        return map;
    }
}
