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
        map.put(new File("SIRSampleForestConditioned_output.json"), "28cbb6cf7c5e76e304f9d49ddeb2edf3");
        map.put(new File("SIRSampleForestConditioned_output.nexus"), "74a2591fe4fc418924dd8787152c35fd");
        return map;
    }
}
