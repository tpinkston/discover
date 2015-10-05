/**
 * @author Tony Pinkston
 */
package discover.vdis.types;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import discover.Discover;

public class EntityTypes {

    private static final Logger logger = Discover.getLogger();

    private static final String UNKNOWN = "Unknown Type";

    private static final String CDT = "data/cdtCompositionMappings.xml";

    private static final String FILES[] = {

        "data/PLATFORMS.CSV",
        "data/MUNITIONS.CSV",
        "data/LIFEFORMS.CSV",
        "data/ENVIRONMENTALS.CSV",
        "data/CULTURAL_FEATURES.CSV",
        "data/SUPPLIES.CSV",
        "data/RADIOS.CSV",
        "data/EXPENDABLES.CSV",
        "data/SENSOR_EMITTERS.CSV"
    };

    /** Maps 64-bit entity type value to EntityType object. */
    private static final TreeMap<Long, EntityType> mapping;

    /** Maps 64-bit entity type value to EntityType object. */
    private static final TreeMap<Long, EntityType> unknowns;

    /** List of all EntityType objects read from data file. */
    private static final ArrayList<EntityType> listing;

    private static boolean includingCDT = false;

    static {

        mapping = new TreeMap<Long, EntityType>();
        unknowns = new TreeMap<Long, EntityType>();
        listing = new ArrayList<EntityType>();
    }

    /**
     * @return All known entity types.
     */
    public static List<EntityType> getValues() {

        return Collections.unmodifiableList(listing);
    }

    /**
     * @param value - 64-bit entity type value
     *
     * @return {@link EntityType}
     */
    public static EntityType getEntityType(long value) {

        EntityType type = mapping.get(value);

        if (type == null) {

            type = unknowns.get(value);

            if (type == null) {

                long bits = value;

                int extension = (int)(bits & 0xFF);
                bits >>= 8;
                int specific = (int)(bits & 0xFF);
                bits >>= 8;
                int subcategory = (int)(bits & 0xFF);
                bits >>= 8;
                int category = (int)(bits & 0xFF);
                bits >>= 8;
                int country = (int)(bits & 0xFFFF);
                bits >>= 16;
                int domain = (int)(bits & 0xFF);
                bits >>= 8;
                int kind = (int)(bits & 0xFF);

                String string = Septuple.toString(
                    kind,
                    domain,
                    country,
                    category,
                    subcategory,
                    specific,
                    extension);

                type = new EntityType(
                    kind,
                    domain,
                    country,
                    category,
                    subcategory,
                    specific,
                    extension,
                    value,
                    "UNKNOWN",
                    "Unknown",
                    null,
                    string);

                unknowns.put(value, type);
            }
        }

        return type;
    }

    /**
     *
     * @param stream - {@link DataInputStream}
     *
     * @return {@link EntityType} object from the next 8 bytes in the stream.
     *
     * @throws IOException
     */
    public static EntityType read(DataInputStream stream) throws IOException {

        return getEntityType(stream.readLong());
    }

    /**
     * Loads known enumerations from XML files.
     */
    public static void load(boolean cdt) {

        long start = System.currentTimeMillis();
        int total = 0;

        includingCDT = cdt;

        for(String file : FILES) {

            total += load(file);
        }

        listing.addAll(mapping.values());

        long duration = (System.currentTimeMillis() - start);

        System.out.print("Loaded " + total + " entity types in ");
        System.out.println(duration + " milliseconds");

        if (includingCDT) {

            start = System.currentTimeMillis();
            total = loadCDT();
            duration = (System.currentTimeMillis() - start);

            System.out.print("Loaded " + total + " CDT models in ");
            System.out.println(duration + " milliseconds");
        }
    }

    private static int load(String filename) {

        BufferedReader reader = getReader(filename);
        int count = 0;

        if (reader == null) {

            logger.severe("File not found: " + filename);
        }
        else try {

            String string = reader.readLine();
            int values[] = new int[7];
            int line = 1;

            while(string != null) {

                if (string.endsWith(",")) {

                    string = string.concat("0");
                }

                String tokens[] = string.split(",");

                if ((tokens == null) || (tokens.length != 13)) {

                    logger.warning(
                        "Parse error:\n" + filename +
                        ": Expecting 13 tokens on line " + line +
                        "\n" + Arrays.toString(tokens));
                }
                else try {

                    String alternate = null;

                    if (tokens[12].equals("0")) {

                        alternate = UNKNOWN;
                    }
                    else {

                        alternate = tokens[12];
                    }

                    for(int i = 0; i < 7; ++i) {

                        values[i] = Integer.parseInt(tokens[i + 1]);
                    }

                    long value = Septuple.toLong(
                        values[0],
                        values[1],
                        values[2],
                        values[3],
                        values[4],
                        values[5],
                        values[6]);

                    String tuple = Septuple.toString(
                        values[0],
                        values[1],
                        values[2],
                        values[3],
                        values[4],
                        values[5],
                        values[6]);

                    EntityType type = new EntityType(
                        values[0],
                        values[1],
                        values[2],
                        values[3],
                        values[4],
                        values[5],
                        values[6],
                        value,
                        tokens[0],
                        tokens[8].replaceAll("~", ","),
                        alternate,
                        tuple);

                    if (!mapping.containsKey(value)) {

                        mapping.put(value, type);
                        ++count;
                    }
                    else if (mapping.get(value).name.endsWith("_DELETED")) {

                        // Overwrite...
                        mapping.put(value, type);
                    }
                    else {

                        logger.severe(
                            "Duplicate entity type: " + value +
                            ", " + tuple + ", " + tokens[0]);
                    }
                }
                catch(NumberFormatException exception) {

                    if (line > 1) {

                        logger.log(
                            Level.WARNING,
                            "Parse error:\n" + filename +
                            ": Number format exception on line " + line,
                            exception);
                    }
                }

                string = reader.readLine();
                ++line;
            }

            reader.close();
        }
        catch(Exception exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);

            logger.severe(
                exception.getClass().getName() +
                " parsing file " + filename);
        }

        return count;
    }

    private static int loadCDT() {

        InputStream stream = EntityTypes.class.getResourceAsStream(CDT);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        int count = 0;

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            NodeList nodes = document.getDocumentElement().getChildNodes();

            for(int i = 0, size = nodes.getLength(); i < size; ++i) {

                Node node = nodes.item(i);
                Septuple septuple = null;
                EntityType entityType = null;

                if (node.getNodeName().equals("entry")) {

                    NamedNodeMap map = node.getAttributes();
                    Node model = map.getNamedItem("model");
                    Node type = map.getNamedItem("type");
                    Node composition = map.getNamedItem("composition");

                    if ((model != null) &&
                        (model.getNodeValue() != null) &&
                        (type != null) &&
                        (type.getNodeValue() != null) &&
                        (composition != null) &&
                        (composition.getNodeValue() != null)) {

                        septuple = Septuple.parse(type.getNodeValue());
                        entityType = getEntityType(septuple.toLong());

                        entityType.setCDTName(model.getNodeValue());

                        ++count;
                    }
                }
            }
        }
        catch(IOException exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);

            logger.severe(
                exception.getClass().getName() +
                " parsing file " + CDT);
        }
        catch(SAXException exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);

            logger.severe(
                exception.getClass().getName() +
                " parsing file " + CDT);
        }
        catch(ParserConfigurationException exception) {

            logger.log(Level.SEVERE, "Caught exception!", exception);

            logger.severe(
                exception.getClass().getName() +
                " parsing file " + CDT);
        }

        return count;
    }

    private static BufferedReader getReader(String file) {

        InputStream stream = EntityTypes.class.getResourceAsStream(file);
        BufferedReader reader = null;

        if (stream != null) {

            reader = new BufferedReader(new InputStreamReader(stream));
        }

        return reader;
    }
}
