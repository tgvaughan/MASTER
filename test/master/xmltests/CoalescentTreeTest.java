package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class CoalescentTreeTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/CoalescentTree.xml";
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("CoalescentTree_output.nexus"), "e3439be33ab827e65482e45248881fac");
        return map;
    }
}
