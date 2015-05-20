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
        map.put(new File("HIV_output_cond.json"), "f0ca42e8e50694e798a446f7b583469e");
        return map;
    }
}
