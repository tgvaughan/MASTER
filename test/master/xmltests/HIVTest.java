package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class HIVTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/HIV.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("HIV_output_cond.json"), "7fe7879d7fd09553ff59a6c9e3f3fd55");
        return map;
    }
}
