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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tony Pinkston
 */
public class EntityTypes {

    private static final Logger logger = LoggerFactory.getLogger(EntityTypes.class);

    private static final String UNKNOWN = "Unknown Type";

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

        BufferedReader reader = getReader(filename);
        int count = 0;

        if (reader == null) {

            logger.error("File not found: {}", filename);
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

                    logger.warn(
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

                        logger.error(
                            "Duplicate entity type: " + value +
                            ", " + tuple + ", " + tokens[0]);
                    }
                }
                catch(NumberFormatException exception) {

                    if (line > 1) {

                        logger.warn(
                            "Parse error " + filename + ":" + line,
                            exception);
                    }
                }

                string = reader.readLine();
                ++line;
            }

            reader.close();
        }
        catch(Exception exception) {

            logger.error(
                "Caught exception parsing file " + filename,
                exception);
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
