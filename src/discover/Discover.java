/**
 * @author Tony Pinkston
 */
package discover;

import geotransform.ellipsoids.Ellipsoid;
import geotransform.ellipsoids.WE_Ellipsoid;
import geotransform.transforms.Gcc_To_Gdc_Converter;
import geotransform.transforms.Gdc_To_Gcc_Converter;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import discover.gui.frames.DiscoverFrame;
import discover.headless.Headless;
import discover.system.Network;
import discover.test.Test;
import discover.vdis.enums.EnumPrinter;
import discover.vdis.marking.army.ArmyTracking;
import discover.vdis.types.EntityTypes;
import discover.vdis.types.ObjectTypes;

public class Discover {

    private static final String DISCOVER_LOGGER = "discover";

    private static final boolean timestampedLogFile = false;

    private static final String[] EXTERNAL_CLASSES = {

        "geotransform.coords.Gcc_Coord_3d",
        "geotransform.coords.Gdc_Coord_3d",
        "geotransform.ellipsoids.WE_Ellipsoid",
        "geotransform.transforms.Gcc_To_Gdc_Converter",
        "geotransform.transforms.Gdc_To_Gcc_Converter"
    };

    private static Logger logger = null;

    public static Logger getLogger() {

        if (logger == null) {

            initializeLogger(Level.WARNING.toString());
        }

        return logger;
    }

    public static void main(String[] args) {

        List<String> list = Arrays.asList(args);
        boolean info = false;
        boolean cdt = false;
        boolean unbundled = false;
        boolean noLAF = false;
        boolean multicast = false;
        String test = null;
        String level = "WARNING";
        String nic = "eth0";
        String playback = null;
        String headless = null;
        String enumeration = null;
        String laf = null;

        for(String name : EXTERNAL_CLASSES) {

            forName(name);
        }

        Ellipsoid ellipsoid = new WE_Ellipsoid();

        Gcc_To_Gdc_Converter.Init(ellipsoid);
        Gdc_To_Gcc_Converter.Init(ellipsoid);

        // Parse arguments:
        Iterator<String> iterator = list.iterator();

        while(iterator.hasNext()) {

            String argument = iterator.next();

            if (argument.equals("-I") ||
                argument.equals("--info")) {

                info = true;
            }
            else if (argument.equals("-u") ||
                     argument.equals("--unbundled")) {

                unbundled = true;
            }
            else if (argument.equals("-c") ||
                     argument.equals("--cdt")) {

                cdt = true;
            }
            else if (argument.startsWith("--log=") ||
                     argument.startsWith("--level=")) {

                level = getArgumentValue(argument);
            }
            else if (argument.contains("?") ||
                     argument.equals("--help")) {

                usage();
                System.exit(0);
            }
            else if (argument.startsWith("--nic=")) {

                nic = getArgumentValue(argument);
            }
            else if (argument.startsWith("--playback=")) {

                playback = getArgumentValue(argument);
            }
            else if (argument.startsWith("--test=")) {

                test = getArgumentValue(argument);
            }
            else if (argument.equals("--nolaf")) {

                noLAF = true;
            }
            else if (argument.startsWith("--headless=")) {

                headless = getArgumentValue(argument);;
            }
            else if (argument.startsWith("--enum=")) {

                enumeration = getArgumentValue(argument);;
            }
            else if (argument.equals("--multicast")) {

                multicast = true;
            }
            else if (argument.startsWith("--laf=")) {

                laf = getArgumentValue(argument);
            }
            else {

                System.err.println("ERROR: Invalid argument: " + argument);
                usage();
                System.exit(0);
            }
        }

        if (level != null) {

            initializeLogger(level);
        }

        if (info) {

            Map<String, String> netinfo = Network.getNetworkInfo(false);

            for(String key : netinfo.keySet()) {

                String value = netinfo.get(key);

                System.out.println("INTERFACE: " + key);
                System.out.println(value);
            }

            System.exit(0);
        }

        if ((laf != null) && ((headless != null) || (enumeration != null))) {

            System.err.println("WARNING: LAF specified for headless mode!");
        }

        if ((headless == null) && (enumeration == null)) {

            if (!noLAF) {

                setLookAndFeel(laf);
            }

            initializeNetwork(nic, playback, unbundled, multicast);

            ArmyTracking.load();
            EntityTypes.load(cdt);
            ObjectTypes.load();
        }

        if (test != null) {

            Test.test(test);
        }
        else if (enumeration != null) {

            EnumPrinter.print(enumeration);
        }
        else if (headless != null) {

            Headless.run(headless);
        }
        else {

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    try {

                        DiscoverFrame.getInstance();
                    }
                    catch(HeadlessException exception) {

                        System.out.println("ERROR: Can only run headless!");
                    }
                }
            });
        }
    }

    private static String getArgumentValue(String argument) {

        StringTokenizer tokenizer = new StringTokenizer(argument, "=");

        if (tokenizer.countTokens() != 2) {

            // Terminate application.
            System.err.println("Misformed argument: " + argument);
            System.exit(1);
        }

        // Skip first token...
        tokenizer.nextToken();

        return tokenizer.nextToken();
    }

    private static void setLookAndFeel(String name) {

        if (name == null) {

            name = UIManager.getSystemLookAndFeelClassName();
            logger.info("Using system LAF: " + name);
        }
        else try {

            if (name.equalsIgnoreCase("GTK")) {

                name = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            }
            else if (name.equalsIgnoreCase("MOTIF")) {

                name = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }
            else if (name.equalsIgnoreCase("NIMBUS")) {

                name = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
            }
            else if (name.equalsIgnoreCase("WINDOWS")) {

                name = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            }

            Class<?> c = Class.forName(name);

            if (c != null) {

                name = c.getName();
                logger.info("Using user specified LAF: " + name);
            }
            else {

                System.err.println("LAF class not found: " + name);
                name = null;
            }
        }
        catch(Exception exception) {

            exception.printStackTrace();
            name = null;
        }

        if (name != null) {

            try {

                System.out.println("Setting LAF: " + name);
                UIManager.setLookAndFeel(name);
            }
            catch(Exception exception) {

                exception.printStackTrace();
            }
        }
    }

    private static void initializeNetwork(
        String name,
        String playback,
        boolean unbundled,
        boolean multicast) {

        NetworkInterface networkInterface = null;

        try {

            networkInterface = NetworkInterface.getByName(name);

            if (networkInterface == null) {

                System.err.print("ERROR: Network interface: ");
                System.err.print(name);
                System.err.println(" could not be found!");
                System.exit(0);
            }
            else if (!networkInterface.isUp()) {

                System.err.print("ERROR: Network interface: ");
                System.err.print(networkInterface.getDisplayName());
                System.err.println(" is not up and running!");
                System.exit(0);
            }
            else if (!Network.initialize(
                         networkInterface,
                         playback,
                         unbundled,
                         multicast)) {

                System.err.print("ERROR: Network interface: ");
                System.err.print(networkInterface.getDisplayName());
                System.err.println(" could not be initialized!");
                System.exit(0);
            }
        }
        catch(SocketException exception) {

            exception.printStackTrace();
            System.exit(0);
        }
    }

    private static void forName(String name) {

        try {

            Class.forName(name);
        }
        catch(Exception exception) {

            exception.printStackTrace();
            System.exit(0);
        }
    }

    private static void usage() {

        PrintStream out = System.out;

        out.println("Usage: discover [OPTIONS]...");
        out.println();
        out.println("Options:");
        out.println("  ?, --help            Displays this help and exit");
        out.println(" -I, --info            Dump network information");
        out.println(" -u, --unbundled       Unbundle inbound PDU traffic");
        out.println(" -c, --cdt             Include CDT specific entity types");
        out.println("     --nic=NAME        Use NAME as the network interface");
        out.println("     --log=LEVEL       Sets logger level to LEVEL");
        out.println("     --level=LEVEL     Equivalent as --log=LEVEL");
        out.println("     --test=TEST       Run developer test named TEST");
        out.println("     --laf=LAF         Use LAF for GUI look and feel");
        out.println("     --headless=PARAMS Run without GUI");
        out.println("     --playback=IPADDR Use IPADDR as playback address");
        out.println("     --enum=PARAM      Print enumeration information");
        out.println("     --nolaf           Do not use any look and feel");
        out.println("     --multicast       Use any multicast addresses");
        out.println();
        out.println("Default network interface is 'eth0'");
        out.println();
        out.println("Default logger level is WARNING, other possible values:");
        out.println("CONFIG, INFO, FINE, FINER or FINEST");
        out.println();
        out.println("GUI look and feel (LAF) is the java class name (subject");
        out.println("to platform, JRE support) or shortcut (default is GTK):");
        out.println();
        out.println("Shortcut GTK:");
        out.println("  com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        out.println("Shortcut MOTIF:");
        out.println("  com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        out.println("Shortcut NIMBUS:");
        out.println("  com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        out.println("Shortcut WINDOWS:");
        out.println("  com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        Headless.printUsage();
        EnumPrinter.printUsage();
    }

    private static void initializeLogger(String name) {

        Level level = Level.parse(name);

        if (level == null) {

            System.err.println("ERROR: Invalid logger level: " + name);
            level = Level.WARNING;
        }

        if (logger != null) {

            logger.setLevel(level);
        }
        else try {

            String file = getLogFile();
            LogManager manager = LogManager.getLogManager();
            FileHandler handler = null;

            manager.reset();

            logger = Logger.getLogger(DISCOVER_LOGGER);

            handler = new FileHandler(file, false);
            handler.setFormatter(new LogFormatter());

            logger.setLevel(level);
            logger.addHandler(handler);

            System.out.println("Opening logger...");

            Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        }
        catch(IllegalArgumentException exception) {

            exception.printStackTrace();
            System.exit(0);
        }
        catch(SecurityException exception) {

            exception.printStackTrace();
            System.exit(0);
        }
        catch(IOException exception) {

            exception.printStackTrace();
            System.exit(0);
        }
    }

    private static void uninitializeLogger() {

        System.out.println("Closing logger...");

        Logger logger = Logger.getLogger(DISCOVER_LOGGER);

        for(Handler handler : logger.getHandlers()) {

            handler.close();
        }
    }

    private static String getLogFile() {

        StringBuilder builder = new StringBuilder();

        builder.append("%h/.discover");

        if (timestampedLogFile) {

            NumberFormat format = NumberFormat.getInstance();
            Calendar calendar = Calendar.getInstance();

            format.setMinimumIntegerDigits(2);

            builder.append("_");
            builder.append(calendar.get(Calendar.YEAR));
            builder.append(format.format(calendar.get(Calendar.MONTH) + 1));
            builder.append(format.format(calendar.get(Calendar.DATE)));
            builder.append("_");
            builder.append(format.format(calendar.get(Calendar.HOUR_OF_DAY)));
            builder.append(format.format(calendar.get(Calendar.MINUTE)));
            builder.append(format.format(calendar.get(Calendar.SECOND)));
        }

        builder.append(".txt");

        return builder.toString();
    }

    private static class ShutdownHook extends Thread {

        @Override
        public void run() {

            Discover.uninitializeLogger();
            System.out.println("Shutting down...");
        }
    }

    private static class LogFormatter extends Formatter {

        private Map<Level, String> levels = new HashMap<Level, String>();

        public LogFormatter() {

            this.levels.put(Level.SEVERE, "[SEVERE ] ");
            this.levels.put(Level.WARNING, "[WARNING] ");
            this.levels.put(Level.INFO, "[INFO   ] ");
            this.levels.put(Level.CONFIG, "[CONFIG ] ");
            this.levels.put(Level.FINE, "[FINE   ] ");
            this.levels.put(Level.FINER, "[FINER  ] ");
            this.levels.put(Level.FINEST, "[FINEST ] ");
        }

        @Override
        public String format(LogRecord record) {

          StringBuffer buffer = new StringBuffer();
          Date date = new Date(record.getMillis());

          buffer.append(this.levels.get(record.getLevel()));
          buffer.append("[" + date + "] ");
          buffer.append(record.getSourceClassName().toString() + ".");
          buffer.append(record.getSourceMethodName().toString() + "(): ");
          buffer.append("\n+ " + record.getMessage() + "\n");

          Throwable throwable = record.getThrown();

          if (throwable != null) {

              buffer.append("\n" + throwable.getClass().getName());
              buffer.append(": " + throwable.getMessage());

              for(StackTraceElement element : throwable.getStackTrace()) {

                  buffer.append("\n    at ");
                  buffer.append(element.getClassName() + ".");
                  buffer.append(element.getMethodName() + "(");
                  buffer.append(element.getFileName() + ":");
                  buffer.append(element.getLineNumber() + ")");
              }

              buffer.append("\n");

              if ((throwable.getCause() != null) &&
                  (throwable.getCause() != throwable)) {

                  throwable = throwable.getCause();

                  buffer.append("\nCaused by: ");
                  buffer.append(throwable.getClass().getName());
                  buffer.append(": " + throwable.getMessage());

                  for(StackTraceElement element : throwable.getStackTrace()) {

                      buffer.append("\n    at ");
                      buffer.append(element.getClassName() + ".");
                      buffer.append(element.getMethodName() + "(");
                      buffer.append(element.getFileName() + ":");
                      buffer.append(element.getLineNumber() + ")");
                  }

                  buffer.append("\n");
              }
          }

          if (record.getLevel().equals(Level.SEVERE)) {

              System.err.println(buffer.toString());
          }

          return buffer.toString();
        }
    }
}
