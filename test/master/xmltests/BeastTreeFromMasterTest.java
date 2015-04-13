package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class BeastTreeFromMasterTest extends XMLTestCase {
    @Override
    public String getXMLFileName() {
        return "examples/BeastTreeFromMasterExample.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("BeastTreeFromMasterExample_output.log"), null);
        map.put(new File("BeastTreeFromMasterExample_output.trees"), null);
        map.put(new File("BeastTreeFromMasterExample.xml.state"), null);
        map.put(new File("BeastTreeFromMasterExample_start.tree"), "2a3663b14e670e643f1b6ff9deebf638");
        return map;
    }
}
