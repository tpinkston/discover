package discover.vdis.types;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import discover.vdis.enums.OBJECT_GEOMETRY;

/**
 * @author Tony Pinkston
 */
public class ObjectTypes {

    private static final String FILE = "data/objects.xml";

    private static final Logger logger = LoggerFactory.getLogger(ObjectTypes.class);

    /** Maps 32-bit entity type value to ObjectType object. */
    private static final Map<OBJECT_GEOMETRY, Map<Integer, ObjectType>> mapping;

    /** Maps 32-bit entity type value to ObjectType object. */
    private static final Map<OBJECT_GEOMETRY, Map<Integer, ObjectType>> unknowns;

    /** List of all ObjectType objects read from data file. */
    private static final ArrayList<ObjectType> listing;

    static {
        
        mapping = new TreeMap<OBJECT_GEOMETRY, Map<Integer, ObjectType>>();
        unknowns = new TreeMap<OBJECT_GEOMETRY, Map<Integer, ObjectType>>();
        listing = new ArrayList<ObjectType>();

        unknowns.put(OBJECT_GEOMETRY.AREAL, new TreeMap<Integer, ObjectType>());
        unknowns.put(OBJECT_GEOMETRY.LINEAR, new TreeMap<Integer, ObjectType>());
        unknowns.put(OBJECT_GEOMETRY.POINT, new TreeMap<Integer, ObjectType>());
        unknowns.put(OBJECT_GEOMETRY.UNKNOWN, new TreeMap<Integer, ObjectType>());
    }

    /**
     * @return All known object types.
     */
    public static List<ObjectType> getValues() {

        return Collections.unmodifiableList(listing);
    }

    /**
     * @param geometry - {@link OBJECT_GEOMETRY}
     * @param value - 32-bit entity type value
     *
     * @return {@link ObjectType}
     */
    public static ObjectType getObjectType(OBJECT_GEOMETRY geometry, int value) {

        Map<Integer, ObjectType> submapping = mapping.get(geometry);
        ObjectType type = submapping.get(value);

        if (type == null) {

            submapping = unknowns.get(geometry);
            type = submapping.get(value);

            if (type == null) {

                int bits = value;

                int subcategory = (bits & 0xFF);
                bits >>= 8;
                int category = (bits & 0xFF);
                bits >>= 8;
                int kind = (bits & 0xFF);
                bits >>= 8;
                int domain = (bits & 0xFF);

                type = new ObjectType(
                    ("UNKNOWN_" + geometry.name + "_" + value),
                    ("Unknown " + geometry.description + "_" + value),
                    geometry,
                    domain,
                    kind,
                    category,
                    subcategory);

                submapping.put(value, type);
            }
        }

        return type;
    }

    /**
     * Loads known enumerations from XML files.
     */
    public static void load() {

        final long start = System.currentTimeMillis();
        int total = 0;

        InputStream resource = ObjectTypes.class.getResourceAsStream(FILE);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(resource));

            document.getDocumentElement().normalize();

            NodeList types = document.getElementsByTagName("type");

            for(int i = 0; i < types.getLength(); ++i) {

                Element element = (Element)types.item(i);

                String geometry = element.getAttribute("geometry");
                String description = element.getAttribute("description");
                String value = element.getAttribute("value");

                String tokens[] = value.split("\\.");

                if ((tokens == null) || (tokens.length != 4)) {

                    logger.error("Invalid object type: {}", value);
                }
                else try {

                    int values[] = new int[tokens.length];
                    
                    for(int j = 0; j < tokens.length; ++j) {

                        values[j] = Integer.parseInt(tokens[j]);
                    }

                    OBJECT_GEOMETRY objectGeometry = getGeometry(geometry);

                    String name = description.toUpperCase();
                    
                    name = name.replaceAll(" ", "_");
                    name = name.replaceAll(",", "");
                    name = name.replaceAll("\\-", "_");
                    name = name.replaceAll("_\\([A-Z]+\\)", "");
                    
                    ObjectType type = new ObjectType(
                        name,
                        description,
                        objectGeometry,
                        values[0],
                        values[1],
                        values[2],
                        values[3]);

                    ++total;

                    Map<Integer, ObjectType> submapping = mapping.get(objectGeometry);

                    if (submapping == null) {

                        submapping = new TreeMap<Integer, ObjectType>();

                        mapping.put(objectGeometry, submapping);
                    }

                    if (submapping.containsKey(type.value)) {

                        logger.error("Duplicate object type: {}", value);
                    }
                    else {

                        listing.add(type);
                        submapping.put(type.value, type);
                    }
                }
                catch(NumberFormatException exception) {

                    logger.error("Caught exception!", exception);
                }
            }
        }
        catch(Exception exception) {

            logger.error("Failed to load object types!", exception);
        }

        long duration = (System.currentTimeMillis() - start);

        System.out.print("Loaded " + total + " object types in ");
        System.out.println(duration + " milliseconds");
    }

    /**
     * Converts object type tuple (4 unsigned 9-bit numbers) to 32-bit integer.
     * 
     * @param domain
     * @param kind
     * @param category
     * @param subcategory
     * 
     * @return Integer value.
     */
    public static int toInteger(
            int domain,
            int kind,
            int category,
            int subcategory) {

        int value = 0;

        value |= (domain & 0xFF);
        value <<= 8;
        value |= (kind & 0xFF);
        value <<= 8;
        value |= (category & 0xFF);
        value <<= 8;
        value |= (subcategory & 0xFF);

        return value;
    }

    /**
     * @param kind
     * @param domain
     * @param category
     * @param subcategory
     * 
     * @return Tuple as string (e.g. "1.3.0.2")
     */
    public static String toString(
            int kind,
            int domain,
            int category,
            int subcategory) {

        StringBuilder builder = new StringBuilder();

        builder.append(kind);
        builder.append(".");
        builder.append(domain);
        builder.append(".");
        builder.append(category);
        builder.append(".");
        builder.append(subcategory);

        return builder.toString();
    }
    
    private static OBJECT_GEOMETRY getGeometry(String value) {
        
        List<OBJECT_GEOMETRY> list = OBJECT_GEOMETRY.values(true);
        
        for(OBJECT_GEOMETRY geometry : list) {
            
            if (geometry.name.equalsIgnoreCase(value)) {
                
                return geometry;
            }
        }
        
        logger.error("Invalid object geometry: {}", value);
        
        return null;
    }
}
