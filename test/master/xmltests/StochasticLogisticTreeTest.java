package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class StochasticLogisticTreeTest extends XMLTestCase {
    @Override
    public String getXMLFileName() {
        return "examples/StochasticLogisticTree.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("StochasticLogisticTree_output.json"), "b0d50b13f9b60b07425e1467018b73ad");
        map.put(new File("StochasticLogisticTree_output.nexus"), "0d11ca147f4ddb7e0269796e046bd359");
        return map;
    }
}
