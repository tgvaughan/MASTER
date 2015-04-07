package master;

import beast.util.Randomizer;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public abstract class XMLTestCase {

    public long getSeed() {
        return 1;
    }

    public abstract String getXMLFileName();

    public abstract Map<File, String> getOutputFileHashes();

    static class ExitException extends SecurityException { }

    @Test
    public void test() throws IOException {

        System.setSecurityManager(new SecurityManager() {

            @Override
            public void checkPermission(Permission perm) { }

            @Override
            public void checkPermission(Permission perm, Object context) { }

            @Override
            public void checkExit(int status) {
                super.checkExit(status);

                throw new ExitException();
            }
        });

        try {
            Randomizer.setSeed(getSeed());
            beast.app.beastapp.BeastMain.main(new String[]{getXMLFileName()});
        } catch (ExitException e) {

        }

        // Compute MD5 hash of generated file
        for (File outFile : getOutputFileHashes().keySet()) {
            HashCode hc = Files.hash(outFile, Hashing.md5());
            assertEquals(hc.toString(), getOutputFileHashes().get(outFile));
            outFile.delete();
        }

    }

}
