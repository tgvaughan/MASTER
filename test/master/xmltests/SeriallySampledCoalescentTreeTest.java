package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class SeriallySampledCoalescentTreeTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/SeriallySampledCoalescentTree.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("SeriallySampledCoalescentTree_output.nexus"), "75ffdd7912c251f6d7c80c604183116b");
        return map;
    }
}
