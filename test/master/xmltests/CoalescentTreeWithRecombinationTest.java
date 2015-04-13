package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class CoalescentTreeWithRecombinationTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/CoalescentTreeWithRecombination.xml";
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();

        map.put(new File("CoalescentTreeWithRecombination_output.nexus"), "bf0477be7fa5ebaef9a432f2398b29a8");

        return map;
    }
}
