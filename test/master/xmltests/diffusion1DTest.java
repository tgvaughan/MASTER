package master.xmltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class diffusion1DTest extends XMLTestCase {
    @Override
    public String getXMLFileName() {
        return "examples/Diffusion1D.xml";
    }

    @Override
    public long getSeed() {
        return 1;
    }

    @Override
    public Map<File, String> getOutputFileHashes() {
        Map<File, String> map = new HashMap<>();
        map.put(new File("Diffusion1D_output.json"), "29ef09b21865b33099a4e960a04cc0dd");
        return map;
    }
}
