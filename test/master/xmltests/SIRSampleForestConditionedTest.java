package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class SIRSampleForestConditionedTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/SIRSampleForestConditioned.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("SIRSampleForestConditioned_output.json"), "3ab6a967074d1fb464b27849ed303a88");
        map.put(new File("SIRSampleForestConditioned_output.nexus"), "8ac5690dfbdf503cd16094739f728b9b");
        return map;
    }
}
