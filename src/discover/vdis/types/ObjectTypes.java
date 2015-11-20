package discover.vdis.types;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.vdis.enums.OBJECT_GEOMETRY;

/**
 * @author Tony Pinkston
 */
public class ObjectTypes {

    private static final String FILE = "data/OBJECT_STATE.CSV";

    private static final Logger logger = LoggerFactory.getLogger(ObjectTypes.class);

    /** Maps 32-bit entity type value to ObjectType object. */
    private static final TreeMap<OBJECT_GEOMETRY, TreeMap<Integer, ObjectType>> mapping;

    /** Maps 32-bit entity type value to ObjectType object. */
    private static final TreeMap<OBJECT_GEOMETRY, TreeMap<Integer, ObjectType>> unknowns;

    /** List of all ObjectType objects read from data file. */
    private static final ArrayList<ObjectType> listing;

    static {

        mapping = new TreeMap<OBJECT_GEOMETRY, TreeMap<Integer, ObjectType>>();
        unknowns = new TreeMap<OBJECT_GEOMETRY, TreeMap<Integer, ObjectType>>();
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
     * @param geometry - Point, linear, areal...
     * @param value - 32-bit entity type value
     *
     * @return {@link ObjectType}
     */
    public static ObjectType getObjectType(int geometry, int value) {

        TreeMap<Integer, ObjectType> submapping = mapping.get(geometry);
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

                String tuple = toString(
                    domain,
                    kind,
                    category,
                    subcategory);

                type = new ObjectType(
                    domain,
                    kind,
                    category,
                    subcategory,
                    value,
                    geometry,
                    "UNKNOWN",
                    "Unknown",
                    tuple);

                submapping.put(value, type);
            }
        }

        return type;
    }

    /**
     * Loads known enumerations from XML files.
     */
    public static void load() {

        BufferedReader reader = getReader();
        long start = System.currentTimeMillis();
        int total = 0;

        if (reader == null) {

            logger.error("File not found {}", FILE);
        }
        else try {

            TreeMap<Integer, ObjectType> submapping = null;
            String string = reader.readLine();
            int values[] = new int[4];
            int line = 1;

            while(string != null) {

                String tokens[] = string.split(",");

                if ((tokens == null) || (tokens.length != 10)) {

                    logger.error(
                        "Parse error:\n" + FILE +
                        ": Expecting 13 tokens on line " + line +
                        "\n" + Arrays.toString(tokens));
                }
                else try {

                    for(int i = 0; i < 4; ++i) {

                        values[i] = Integer.parseInt(tokens[i + 1]);
                    }

                    int value = toInteger(
                        values[0],
                        values[1],
                        values[2],
                        values[3]);

                    String tuple = toString(
                        values[0],
                        values[1],
                        values[2],
                        values[3]);

                    OBJECT_GEOMETRY geometry = OBJECT_GEOMETRY.get(Integer.parseInt(tokens[0]));

                    ObjectType type = new ObjectType(
                        values[0],
                        values[1],
                        values[2],
                        values[3],
                        value,
                        geometry.value,
                        tokens[5],
                        tokens[6],
                        tuple);

                    ++total;

                    submapping = mapping.get(geometry);

                    if (submapping == null) {

                        submapping = new TreeMap<Integer, ObjectType>();

                        mapping.put(geometry, submapping);
                    }

                    if (submapping.containsKey(value)) {

                        logger.error("Duplicate object type: {}", tuple);
                    }
                    else {

                        submapping.put(value, type);
                    }
                }
                catch(NumberFormatException exception) {

                    if (line > 1) {

                        logger.error("Caught exception!", exception);
                    }
                }

                string = reader.readLine();
                ++line;
            }

            listing.addAll(mapping.get(OBJECT_GEOMETRY.POINT).values());
            listing.addAll(mapping.get(OBJECT_GEOMETRY.LINEAR).values());
            listing.addAll(mapping.get(OBJECT_GEOMETRY.AREAL).values());

            reader.close();
        }
        catch(Exception exception) {

            logger.error("Caught exception!", exception);
        }

        long duration = (System.currentTimeMillis() - start);

        System.out.print("Loaded " + total + " object types in ");
        System.out.println(duration + " milliseconds");
    }

    private static int toInteger(
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

    private static String toString(
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

    private static BufferedReader getReader() {

        InputStream stream = ObjectTypes.class.getResourceAsStream(FILE);
        BufferedReader reader = null;

        if (stream != null) {

            reader = new BufferedReader(new InputStreamReader(stream));
        }

        return reader;
    }
}
