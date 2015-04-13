package master.xmltests;

import beagle.BeagleFlag;
import beast.app.BEASTVersion;
import beast.app.BeastMCMC;
import beast.app.beastapp.BeastMain;
import beast.app.util.Arguments;
import beast.core.util.Log;
import beast.util.Randomizer;
import beast.util.XMLParserException;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public abstract class XMLTestCase {

    public long getSeed() {
        return 1;
    }

    public abstract String getXMLFileName();

    public abstract Map<File, String> getOutputFileHashes();

    @Test
    public void test() throws IOException {

        //Randomizer.setSeed(getSeed());

        beastMain(new String[]{"-seed", Long.toString(getSeed()),
                "-overwrite",
                getXMLFileName()});

        // Compute MD5 hash of generated file
        for (File outFile : getOutputFileHashes().keySet()) {
            if (getOutputFileHashes().get(outFile) != null) {
                File filteredOutFile = truncateFloats(outFile);

                String expectedHC = getOutputFileHashes().get(outFile);
                String actualHC = Files.hash(filteredOutFile, Hashing.md5()).toString();

                System.out.println("Expected: " + expectedHC + " Actual: " + actualHC);
                assertEquals(expectedHC, actualHC);

                if (!filteredOutFile.delete())
                    throw new RuntimeException("Error deleting expected output file.");
            }

            if (!outFile.delete())
                throw new RuntimeException("Error deleting expected output file.");
        }
    }

    /**
     * Reduce precision of all floats in file to 14 digits in an attempt
     * to avoid checksum mismatches due to platform-dependent rounding
     * errors.
     *
     * @param oldFile file to filter
     * @return filtered file
     * @throws FileNotFoundException
     */
    public static File truncateFloats(File oldFile) throws FileNotFoundException {
        File newFile = new File(oldFile.getName() + "_filtered");

        BufferedReader reader = new BufferedReader(new FileReader(oldFile));
        PrintStream pstream = new PrintStream(newFile);

        Pattern p = Pattern.compile("[0-9]+(\\.[0-9]*)?([eE]-?[0-9]+)?");

        reader.lines().forEach(s -> {
            int lastMatchEnd = 0;
            Matcher m = p.matcher(s);
            while (m.find()) {
                if (m.start()>lastMatchEnd)
                    pstream.print(s.substring(lastMatchEnd, m.start()));

                double oldDouble = Double.parseDouble(m.group());
                BigDecimal bd = new BigDecimal(oldDouble);
                bd = bd.round(new MathContext(10));
                pstream.print(bd.doubleValue());

                lastMatchEnd = m.end();
            }

            pstream.println(s.substring(lastMatchEnd, s.length()));

        });

        return newFile;
    }

    /**
     * Ugly wholesale copy of BeastMain.main() with the System.exit()s replaced
     * by simple returns.
     *
     * @param args BEAST command line arguments
     * @return exit status of original main method
     * @throws java.io.IOException
     */
    private int beastMain(final String[] args) throws java.io.IOException {

        final List<String> MCMCargs = new ArrayList<>();
//    	Utils.loadUIManager();

        final Arguments arguments = new Arguments(
                new Arguments.Option[]{

//                        new Arguments.Option("verbose", "Give verbose XML parsing messages"),
//                        new Arguments.Option("warnings", "Show warning messages about BEAST XML file"),
//                        new Arguments.Option("strict", "Fail on non-conforming BEAST XML file"),
                        new Arguments.Option("working", "Change working directory to input file's directory"),
                        new Arguments.LongOption("seed", "Specify a random number generator seed"),
                        new Arguments.StringOption("prefix", "PREFIX", "Specify a prefix for all output log filenames"),
                        new Arguments.StringOption("statefile", "STATEFILE", "Specify the filename for storing/restoring the state"),
                        new Arguments.Option("overwrite", "Allow overwriting of log files"),
                        new Arguments.Option("resume", "Allow appending of log files"),
                        // RRB: not sure what effect this option has
                        new Arguments.IntegerOption("errors", "Specify maximum number of numerical errors before stopping"),
                        new Arguments.IntegerOption("threads", "The number of computational threads to use (default auto)"),
                        new Arguments.Option("java", "Use Java only, no native implementations"),
                        new Arguments.Option("noerr", "Suppress all output to standard error"),
                        new Arguments.StringOption("loglevel", "LEVEL", "error,warning,info"),
                        new Arguments.Option("beagle", "Use beagle library if available"),
                        new Arguments.Option("beagle_info", "BEAGLE: show information on available resources"),
                        new Arguments.StringOption("beagle_order", "order", "BEAGLE: set order of resource use"),
                        new Arguments.IntegerOption("beagle_instances", "BEAGLE: divide site patterns amongst instances"),
                        new Arguments.Option("beagle_CPU", "BEAGLE: use CPU instance"),
                        new Arguments.Option("beagle_GPU", "BEAGLE: use GPU instance if available"),
                        new Arguments.Option("beagle_SSE", "BEAGLE: use SSE extensions if available"),
                        new Arguments.Option("beagle_single", "BEAGLE: use single precision if available"),
                        new Arguments.Option("beagle_double", "BEAGLE: use double precision if available"),
                        new Arguments.StringOption("beagle_scaling", new String[]{"default", "none", "dynamic", "always"},
                                false, "BEAGLE: specify scaling scheme to use"),
                });

        try {
            arguments.parseArguments(args);
        } catch (Arguments.ArgumentException ae) {
            System.out.println();
            System.out.println(ae.getMessage());
            System.out.println();
            BeastMain.printUsage(arguments);
            return 1;
        }

        if (arguments.hasOption("help")) {
            BeastMain.printUsage(arguments);
            return 0;
        }

        final boolean working = arguments.hasOption("working");
        String fileNamePrefix = null;
        String stateFileName = null;

        long seed = Randomizer.getSeed();
        boolean useJava = false;

        int threadCount = 0;

        if (arguments.hasOption("java")) {
            useJava = true;
        }

        if (arguments.hasOption("prefix")) {
            fileNamePrefix = arguments.getStringOption("prefix");
        }

        if (arguments.hasOption("statefile")) {
            stateFileName = arguments.getStringOption("statefile");
        }

        long beagleFlags = 0;

        if (arguments.hasOption("beagle_scaling")) {
            System.setProperty("beagle.scaling", arguments.getStringOption("beagle_scaling"));
        }

        if (arguments.hasOption("beagle_CPU")) {
            beagleFlags |= BeagleFlag.PROCESSOR_CPU.getMask();
        }
        if (arguments.hasOption("beagle_GPU")) {
            beagleFlags |= BeagleFlag.PROCESSOR_GPU.getMask();
        }
        if (arguments.hasOption("beagle_SSE")) {
            beagleFlags |= BeagleFlag.PROCESSOR_CPU.getMask();
            beagleFlags |= BeagleFlag.VECTOR_SSE.getMask();
        }
        if (arguments.hasOption("beagle_double")) {
            beagleFlags |= BeagleFlag.PRECISION_DOUBLE.getMask();
        }
        if (arguments.hasOption("beagle_single")) {
            beagleFlags |= BeagleFlag.PRECISION_SINGLE.getMask();
        }

        if (arguments.hasOption("noerr")) {
            System.setErr(new PrintStream(new OutputStream() {
                public void write(int b) {
                }
            }));
        }

        if (arguments.hasOption("beagle_order")) {
            System.setProperty("beagle.resource.order", arguments.getStringOption("beagle_order"));
        }

        if (arguments.hasOption("beagle_instances")) {
            System.setProperty("beagle.instance.count", Integer.toString(arguments.getIntegerOption("beagle_instances")));
        }

        if (arguments.hasOption("beagle_scaling")) {
            System.setProperty("beagle.scaling", arguments.getStringOption("beagle_scaling"));
        }

        if (arguments.hasOption("threads")) {
            threadCount = arguments.getIntegerOption("threads");
            if (threadCount < 0) {
                BeastMain.printTitle();
                System.err.println("The number of threads should be >= 0");
                return 1;
            }
        }

        if (arguments.hasOption("seed")) {
            seed = arguments.getLongOption("seed");
            if (seed <= 0) {
                BeastMain.printTitle();
                System.err.println("The random number seed should be > 0");
                return 1;
            }
        }

        int maxErrorCount = 0;
        if (arguments.hasOption("errors")) {
            maxErrorCount = arguments.getIntegerOption("errors");
            if (maxErrorCount < 0) {
                maxErrorCount = 0;
            }
        }

        BeastMain.printTitle();

        File inputFile = null;

        if (arguments.hasOption("overwrite")) {
            MCMCargs.add("-overwrite");
        }

        if (arguments.hasOption("resume")) {
            MCMCargs.add("-resume");
        }

        if (inputFile == null) {

            final String[] args2 = arguments.getLeftoverArguments();

            if (args2.length > 1) {
                System.err.println("Unknown option: " + args2[1]);
                System.err.println();
                BeastMain.printUsage(arguments);
                return 1;
            }

            String inputFileName = null;

            if (args2.length > 0) {
                inputFileName = args2[0];
                inputFile = new File(inputFileName);
            }
        }

        if (inputFile != null && inputFile.getParent() != null && working) {
            System.setProperty("file.name.prefix", inputFile.getParentFile().getAbsolutePath());
        }

        if (useJava) {
            System.setProperty("java.only", "true");
        }

        if (arguments.hasOption("loglevel")) {
            String l = arguments.getStringOption("loglevel");
            switch (l) {
                case "error":
                    Log.setLevel(Log.Level.error);
                    break;
                case "warning":
                    Log.setLevel(Log.Level.warning);
                    break;
                case "info":
                    Log.setLevel(Log.Level.info);
                    break;
            }
        }

        if (fileNamePrefix != null && fileNamePrefix.trim().length() > 0) {
            System.setProperty("file.name.prefix", fileNamePrefix.trim());
        }

        if (stateFileName!= null && stateFileName.trim().length() > 0) {
            System.setProperty("state.file.name", stateFileName.trim());
            System.out.println("Writing state to file " + stateFileName);
        }

        if (beagleFlags != 0) {
            System.setProperty("beagle.preferred.flags", Long.toString(beagleFlags));

        }

        if (threadCount > 0) {
            System.setProperty("thread.count", String.valueOf(threadCount));
            MCMCargs.add("-threads");
            MCMCargs.add(threadCount + "");
        }

        MCMCargs.add("-seed");
        MCMCargs.add(seed + "");
        Randomizer.setSeed(seed);

        System.out.println("Random number seed: " + seed);
        System.out.println();

        // Construct the beast object
        final BeastMCMC beastMCMC = new BeastMCMC();

        try {
            // set all the settings...
            MCMCargs.add(inputFile.getAbsolutePath());
            beastMCMC.parseArgs(MCMCargs.toArray(new String[MCMCargs.size()]));


            new BeastMain(beastMCMC, null, maxErrorCount);
        } catch (RuntimeException rte) {
            // logger.severe will throw a RTE but we want to keep the console visible
        } catch (XMLParserException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

}
