/**
 * @author Tony Pinkston
 */
package discover.system;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import discover.Discover;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.HypertextBuffer;
import discover.common.buffer.PlainTextBuffer;

public class Network {

    public static final int DEFAULT_PORT = 3000;

    private static final Logger logger = Discover.getLogger();

    private static final String MULTICAST_ADDRESSES = "multicast_addresses.xml";

    private static final String DEFAULT_ADDRESSES[] = {

        "224.0.0.1",
        "ff02::1:e000:701",
        "ff02::1:e000:901",
        "ff02::1:e000:c001"
    };

    private static final List<String> addresses;

    private static InetAddress captureAddress = null;
    private static InetAddress playbackAddress = null;

    private static boolean bundledPDUs = true;

    static {

        addresses = new CopyOnWriteArrayList<String>();
    }

    /**
     * Determines capture and playback IP addresses.
     *
     * @param nif - {@link NetworkInterface}
     * @param unbundled - True if PDUs are unbundled.
     *
     * @return True if successful.
     */
    public static boolean initialize(
        NetworkInterface nif,
        String playback,
        boolean unbundled,
        boolean multicast) {

        logger.info("Network interface: " + nif.getDisplayName());

        List<InterfaceAddress> addresses = nif.getInterfaceAddresses();
        InputStream stream = null;
        boolean loaded = false;

        for(InterfaceAddress address : addresses) {

            logger.info("Address: " + address);

            if (address.getAddress().isSiteLocalAddress()) {

                System.out.print("Using IP address (capture): ");
                System.out.print(address.getAddress().getHostAddress());
                System.out.println();

                captureAddress = address.getAddress();
            }

            if (playback != null) {

                try {

                    playbackAddress = Inet4Address.getByName(playback);
                }
                catch(UnknownHostException exception) {

                    exception.printStackTrace();
                }
            }
            else if (address.getBroadcast() != null) {

                System.out.print("Using IP address (playback): ");
                System.out.print(address.getBroadcast().getHostAddress());
                System.out.println();

                playbackAddress = address.getBroadcast();

                if (captureAddress == null) {

                	captureAddress = address.getAddress();
                }
            }
        }

        if (unbundled) {

            bundledPDUs = false;
        }

        if (multicast) {

            stream = Network.class.getResourceAsStream(MULTICAST_ADDRESSES);

            if (stream == null) {

                System.err.println("File not found: " + MULTICAST_ADDRESSES);
            }
            else {

                loaded = loadMulticastAddresses(stream);
            }

            if (!loaded) {

                defaultMulticastAddress();
            }
        }

        return (captureAddress != null) && (playbackAddress != null);
    }

    /**
     * @return {@link InetAddress}
     */
    public static InetAddress getCaptureAddress() {

        return captureAddress;
    }

    /**
     * @return {@link InetAddress}
     */
    public static InetAddress getPlaybackAddress() {

        return playbackAddress;
    }

    /**
     * @return True if captured PDUs get unbundled.
     */
    public static boolean getBundledPDUs() {

        return bundledPDUs;
    }

    /**
     * @return Host name via capture IP address.
     */
    public static String getHostname() {

        if (captureAddress != null) {

            return captureAddress.getCanonicalHostName();
        }
        else {

            return null;
        }
    }

    /**
     * @param packet - {@link DatagramPacket}
     *
     * @return IP address representation and machine that sent the packet.
     */
    public static String getHostAddress(DatagramPacket packet) {

        if (packet.getAddress() == null) {

            return null;
        }
        else {

            return packet.getAddress().getHostAddress();
        }
    }

    /**
     * @return Array of multicast addresses in string form.
     */
    public synchronized static String[] getMulticastAddresses() {

        return addresses.toArray(new String[addresses.size()]);
    }

    /**
     * Adds IP address to the list of multicast addresses.
     *
     * @param address - IP address in string form.
     *
     * @return Short error description if address is not added to list.
     */
    public static synchronized String addMulticastAddress(String address) {

        try {

            InetAddress multicast = InetAddress.getByName(address);

            if (multicast == null) {

                return "Could not get address by name.";
            }
            else if (addresses.contains(address)) {

                return "Address is already listed.";
            }
            else {

                logger.info("Adding multicast address: " + address);

                addresses.add(address);

                return null;
            }
        }
        catch(Exception exception) {

            return ("Caught " + exception.getClass().getSimpleName());
        }
    }

    /**
     * Removes IP address from the list of multicast addresses.
     *
     * @param address - IP address in string form.
     */
    public static synchronized void removeMulticastAddress(String address) {

        if (addresses.contains(address)) {

            logger.info("Removing multicast address: " + address);

            addresses.remove(address);
        }
    }

    /**
     * Clears list of multicast addresses and adds hard coded default addresses.
     */
    public static synchronized void defaultMulticastAddress() {

        logger.info("Defaulting multicast addresses");

        addresses.clear();

        for(String address : DEFAULT_ADDRESSES) {

            String result = addMulticastAddress(address.toLowerCase());

            if (result != null) {

                System.err.println(
                    "Error with address " + address +
                    " - " + result);
            }
        }
    }

    /**
     * @return Map of interface name to description.
     */
    public static Map<String, String> getNetworkInfo(boolean html) {

        Map<String, String> info = new TreeMap<String, String>();

        List<InterfaceAddress> interfaceAddresses;
        Enumeration<NetworkInterface> interfaces = null;

        try {

            interfaces = NetworkInterface.getNetworkInterfaces();

            while((interfaces != null) & interfaces.hasMoreElements()) {

                NetworkInterface nif = interfaces.nextElement();
                AbstractBuffer buffer = null;

                if (html) {

                    buffer = new HypertextBuffer();
                }
                else {

                    buffer = new PlainTextBuffer();
                }

                buffer.addAttribute("MTU", nif.getMTU());
                buffer.addAttribute("Virtual", Boolean.toString(nif.isVirtual()));
                buffer.addAttribute("Multicast", Boolean.toString(nif.supportsMulticast()));
                buffer.addAttribute("Loopback", Boolean.toString(nif.isLoopback()));
                buffer.addAttribute("Up and Running", Boolean.toString(nif.isUp()));
                buffer.addAttribute("Point to Point", Boolean.toString(nif.isPointToPoint()));

                if (html) {

                    buffer.addBreak();
                }

                buffer.addBoldLabel("Addresses");
                buffer.addBreak();

                Enumeration<InetAddress> addresses = nif.getInetAddresses();

                while((addresses != null) & addresses.hasMoreElements()) {

                    InetAddress address = addresses.nextElement();

                    if (!html) {

                        buffer.addText("  ");
                    }

                    buffer.addLabel(address.getClass().getSimpleName());
                    buffer.addItalic(address.getHostAddress());

                    if (address.isMulticastAddress()) {

                        buffer.addItalic(" (multicast)");
                    }

                    if (address.isSiteLocalAddress()) {

                        buffer.addItalic(" (site local)");
                    }

                    if (address.isLinkLocalAddress()) {

                        buffer.addItalic(" (link local)");
                    }

                    if (address.isAnyLocalAddress()) {

                        buffer.addItalic(" (any local)");
                    }

                    buffer.addBreak();
                }

                if (html) {

                    buffer.addBreak();
                }

                buffer.addBoldLabel("Interfaces");
                buffer.addBreak();

                interfaceAddresses = nif.getInterfaceAddresses();

                for(InterfaceAddress address : interfaceAddresses) {

                    if (!html) {

                        buffer.addText("  ");
                    }

                    buffer.addText(address.getAddress().getHostAddress());

                    if (address.getBroadcast() != null) {

                        buffer.addItalic(" (broadcast: ");
                        buffer.addItalic(address.getBroadcast().getHostAddress());
                        buffer.addItalic(")");
                    }

                    buffer.addBreak();
                }

                info.put(nif.getName() + "(" + nif.getDisplayName() + ")", buffer.toString());
            }
        }
        catch(SocketException exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);
        }

        return info;
    }

    private static boolean loadMulticastAddresses(InputStream stream) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        boolean loaded = false;

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            NodeList nodes = document.getDocumentElement().getChildNodes();
            String result = null;

            for(int i = 0, size = nodes.getLength(); i < size; ++i) {

                Node node = nodes.item(i);

                if (node.getNodeName().equals("address")) {

                    String address = node.getTextContent().trim();

                    result = addMulticastAddress(address.toLowerCase());

                    if (result != null) {

                        System.err.println(
                            "Error with address " + address +
                            " - " + result);
                    }
                }
            }

            loaded = true;
        }
        catch(ParserConfigurationException exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);
        }
        catch(SAXException exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);
        }
        catch(IOException exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);
        }

        return loaded;
    }
}
