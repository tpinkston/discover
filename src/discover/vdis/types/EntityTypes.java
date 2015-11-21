package discover.vdis.types;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Tony Pinkston
 */
public class EntityTypes {

    private static final Logger logger = LoggerFactory.getLogger(EntityTypes.class);

    private static final String FILES[] = {

        "data/platforms.xml",
        "data/munitions.xml",
        "data/lifeforms.xml",
        "data/environmentals.xml",
        "data/cultural_features.xml",
        "data/supplies.xml",
        "data/radios.xml",
        "data/expendables.xml",
        "data/sensor_emitters.xml"
    };

    /** Maps 64-bit entity type value to EntityType object. */
    private static final TreeMap<Long, EntityType> mapping;

    /** Maps 64-bit entity type value to EntityType object. */
    private static final TreeMap<Long, EntityType> unknowns;

    /** List of all EntityType objects read from data file. */
    private static final ArrayList<EntityType> listing;

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

                type = new EntityType(
                    ("UNKNOWN_" + value),
                    ("Unknown Type [" + value + "]"),
                    value);

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
    public static void load() {

        long start = System.currentTimeMillis();
        int total = 0;

        for(String file : FILES) {

            total += load(file);
        }

        listing.addAll(mapping.values());

        long duration = (System.currentTimeMillis() - start);

        System.out.print("Loaded " + total + " entity types in ");
        System.out.println(duration + " milliseconds");
    }

    private static int load(String filename) {

        InputStream resource = EntityTypes.class.getResourceAsStream(filename);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        int count = 0;

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(resource));

            document.getDocumentElement().normalize();

            NodeList types = document.getElementsByTagName("type");

            for(int i = 0; i < types.getLength(); ++i) {

                Element element = (Element)types.item(i);

                String name = element.getAttribute("name");
                String value = element.getAttribute("value");
                String description = element.getAttribute("description");

                EntityType type = new EntityType(
                    name,
                    description,
                    value);

                if (!mapping.containsKey(type.getValue())) {

                    mapping.put(type.getValue(), type);
                    ++count;
                }
                else if (mapping.get(value).name.endsWith("_DELETED")) {

                    // Overwrite...
                    mapping.put(type.getValue(), type);
                }
                else {

                    logger.error("Duplicate entity type: {}", value);
                }

                ++count;
            }
        }
        catch(Exception exception) {

            logger.error(
                ("Failed to load entity types file: " + filename),
                exception);
        }

        return count;
    }
}
