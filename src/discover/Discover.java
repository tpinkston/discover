package discover;

import java.awt.HeadlessException;
import java.net.NetworkInterface;
import java.net.SocketException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.gui.frames.DiscoverFrame;
import discover.headless.Headless;
import discover.system.Network;
import discover.vdis.enums.EnumPrinter;
import discover.vdis.marking.army.ArmyTracking;
import discover.vdis.types.EntityTypes;
import discover.vdis.types.ObjectTypes;
import geotransform.ellipsoids.Ellipsoid;
import geotransform.ellipsoids.WE_Ellipsoid;
import geotransform.transforms.Gcc_To_Gdc_Converter;
import geotransform.transforms.Gdc_To_Gcc_Converter;

/**
 * @author Tony Pinkston
 */
public class Discover {

    private static final Logger logger = LoggerFactory.getLogger(Discover.class);

    private static final String[] EXTERNAL_CLASSES = {

        "geotransform.coords.Gcc_Coord_3d",
        "geotransform.coords.Gdc_Coord_3d",
        "geotransform.ellipsoids.WE_Ellipsoid",
        "geotransform.transforms.Gcc_To_Gdc_Converter",
        "geotransform.transforms.Gdc_To_Gcc_Converter"
    };

    public static void main(String[] args) {

        boolean unbundled = Boolean.getBoolean("discover.unbundled");
        boolean multicast = Boolean.getBoolean("discover.multicast");
        String headless = getPropertyValue("discover.headless", null);
        String iface = getPropertyValue("discover.iface", "eth0");
        String playback = getPropertyValue("discover.playback", null);
        String enumeration = getPropertyValue("discover.enum", null);
        String laf = getPropertyValue("discover.laf", null);

        for(String name : EXTERNAL_CLASSES) {

            forName(name);
        }

        Ellipsoid ellipsoid = new WE_Ellipsoid();

        Gcc_To_Gdc_Converter.Init(ellipsoid);
        Gdc_To_Gcc_Converter.Init(ellipsoid);

        ArmyTracking.load();
        EntityTypes.load();
        ObjectTypes.load();

        if (enumeration != null) {

            EnumPrinter.print(enumeration);
        }
        else {

            initializeNetwork(iface, playback, unbundled, multicast);

            if (headless != null) {

                Headless.run(headless);
            }
            else {

                if (!"none".equalsIgnoreCase(laf)) {

                    setLookAndFeel(laf);
                }

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
    }

    private static String getPropertyValue(String name, String value) {

        String property = System.getProperty(name);

        if (property == null) {

            property = value;
        }

        return property;
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

            Class.forName(name);
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
                System.exit(1);
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
                System.exit(1);
            }
            else if (!networkInterface.isUp()) {

                System.err.print("ERROR: Network interface: ");
                System.err.print(networkInterface.getDisplayName());
                System.err.println(" is not up and running!");
                System.exit(2);
            }
            else if (!Network.initialize(
                         networkInterface,
                         playback,
                         unbundled,
                         multicast)) {

                System.err.print("ERROR: Network interface: ");
                System.err.print(networkInterface.getDisplayName());
                System.err.println(" could not be initialized!");
                System.exit(3);
            }
        }
        catch(SocketException exception) {

            exception.printStackTrace();
            System.exit(4);
        }
    }

    private static void forName(String name) {

        try {

            Class.forName(name);
        }
        catch(Exception exception) {

            exception.printStackTrace();
            System.exit(5);
        }
    }
}
