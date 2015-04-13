package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class Diffusion2DTest extends XMLTestCase {

    @Override
    public String getXMLFileName() {
        return "examples/Diffusion2D.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("Diffusion2D_output.json"), "a91b07891c5da73936899cb408c23444");
        return map;
    }
}
