package discover.vdis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Hexadecimal;
import discover.vdis.enums.ENT_DOMAIN;
import discover.vdis.enums.ENT_KIND;
import discover.vdis.enums.ON_OFF;
import discover.vdis.enums.PRESENCE;
import discover.vdis.enums.YES_NO;

/**
 * @author Tony Pinkston
 */
public class Enumerations {

    private static final Logger logger = LoggerFactory.getLogger(Enumerations.class);

    private static final String INDEX = "enums/index.txt";

    private static final Map<String, Class<? extends EnumInterface>> classes = new HashMap<>();

    private static final Map<String, EnumInterface[]> cache = new HashMap<>();

    private static boolean loaded = false;

    /**
     * @return Class object for an enum that implements the
     * {@link EnumInterface} interface for a VDIS enumeration or
     * null if not found.
     */
    public static Class<? extends EnumInterface> getEnumClass(String name) {

        load();

        return classes.get(name);
    }

    public static EnumInterface valueOf(
            int value,
            Class<? extends EnumInterface> c) {

        load();

        if (!classes.containsKey(c.getSimpleName())) {

            logger.warn("Not an enum class: " + c.getName());
        }
        else {

            EnumInterface values[] = getCachedValues(c);

            for(int i = 0; (values != null) && (i < values.length); ++i) {

                if (values[i].getValue() == value) {

                    return values[i];
                }
            }
        }

        return null;
    }

    public static String getDescription(
            int value,
            Class<? extends EnumInterface> c) {

        EnumInterface enumeration = valueOf(value, c);

        String description = null;

        if (enumeration != null) {

            description = enumeration.getDescription();
        }
        else {

            description = c.getSimpleName();
            description += ("_" + Integer.toString(value));
        }

        return description;
    }

    @SuppressWarnings("unchecked")
    public static void load() {

        Class<? extends EnumInterface> c = null;

        if (!loaded) try {

            final long start = System.currentTimeMillis();

            // These are not auto-generated and probably aren't in the index:
            //
            classes.put(ENT_DOMAIN.class.getSimpleName(), ENT_DOMAIN.class);
            classes.put(ENT_KIND.class.getSimpleName(), ENT_KIND.class);
            classes.put(ON_OFF.class.getSimpleName(), ON_OFF.class);
            classes.put(PRESENCE.class.getSimpleName(), PRESENCE.class);
            classes.put(YES_NO.class.getSimpleName(), YES_NO.class);

            // Index file is a text file with class names for all enum types
            // that implement EnumInterface, most are auto-generated.
            //
            InputStream stream = Enumerations.class.getResourceAsStream(INDEX);

            if (stream == null) {

                throw new RuntimeException("Resource not found: " + INDEX);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String string = reader.readLine();
            int total = 0;

            while(string != null) {

                c = (Class<? extends EnumInterface>)Class.forName(string);

                classes.put(c.getSimpleName(), c);

                string = reader.readLine();
                ++total;
            }

            long duration = (System.currentTimeMillis() - start);

            System.out.print("Loaded " + total + " enums in ");
            System.out.println(duration + " milliseconds");

            loaded = true;
        }
        catch(Exception exception) {

            exception.printStackTrace();

            System.exit(1);
        }
    }

    public static void print(String parameter) {

        if (parameter.equalsIgnoreCase("?") ||
            parameter.equalsIgnoreCase("all") ||
            parameter.equalsIgnoreCase("list")) {

            for(String name : classes.keySet()) {

                System.out.println(name);
            }
        }
        else for(String name : classes.keySet()) {

            if (parameter.equalsIgnoreCase(name)) {

                printEnum(name);
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {

        load();
    }

    private static EnumInterface[] getCachedValues(
            Class<? extends EnumInterface> c) {

        EnumInterface values[] = cache.get(c);

        if (values == null) {

            values = c.getEnumConstants();

            if (values != null) {

                cache.put(c.getSimpleName(), values);
            }
            else {

                logger.warn("No enum constants for " + c.getName());
            }
        }

        return values;
    }

    private static void printEnum(String name) {

        Class<? extends EnumInterface> c = classes.get(name);

        if (c == null) {

            System.out.println("No such enumeration: " + name);
        }
        else {

            Object values[] = c.getEnumConstants();

            for(int i = 0; (values != null) && (i < values.length); ++i) {

                EnumInterface value = (EnumInterface)values[i];

                System.out.print(value.getValue());
                System.out.print(" (0x");
                System.out.print(Hexadecimal.toString32(value.getValue()));
                System.out.print("): ");
                System.out.print(value.getName());
                System.out.print(" ('" + value.getDescription() + "')");
                System.out.println();
            }
        }
    }
}
