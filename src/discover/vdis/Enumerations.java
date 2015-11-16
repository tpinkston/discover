package discover.vdis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Hexadecimal;
import discover.vdis.enums.AIR_SMOKE;
import discover.vdis.enums.APP_CTRL_APPLICATION_TYPE;
import discover.vdis.enums.APP_CTRL_CONTROL_TYPE;
import discover.vdis.enums.ARTICULATED_PARTS_METRIC;
import discover.vdis.enums.ENT_DOMAIN;
import discover.vdis.enums.ENT_KIND;
import discover.vdis.enums.HOIST_STATUS;
import discover.vdis.enums.IED_PRESENCE;
import discover.vdis.enums.LAND_SMOKE;
import discover.vdis.enums.LF_AGE_GROUP;
import discover.vdis.enums.LF_CAMOUFLAGE_TYPE;
import discover.vdis.enums.LF_CLOTH_TYPE;
import discover.vdis.enums.LF_COMPLIANCE;
import discover.vdis.enums.LF_EQ_LASER;
import discover.vdis.enums.LF_ETHNICITY;
import discover.vdis.enums.LF_GENDER;
import discover.vdis.enums.LF_HEALTH;
import discover.vdis.enums.LF_POSTURE;
import discover.vdis.enums.LF_WEAPON_STATE;
import discover.vdis.enums.ON_OFF;
import discover.vdis.enums.PDU_STATUS_IAI;
import discover.vdis.enums.PRESENCE;
import discover.vdis.enums.SEVERITY;
import discover.vdis.enums.SLING_DAMAGE;
import discover.vdis.enums.YES_NO;

/**
 * @author Tony Pinkston
 */
public class Enumerations {

    private static final Logger logger = LoggerFactory.getLogger(Enumerations.class);

    private static final String INDEX = "enums/index.txt";

    private static final Map<String, Class<? extends EnumInterface>> classes = new HashMap<>();

    private static final Map<Class<? extends EnumInterface>, List<EnumInterface>> known = new HashMap<>();

    private static final Map<Class<? extends EnumInterface>, List<EnumInterface>> unknown = new HashMap<>();

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

    public static EnumInterface getKnownValue(
            int value,
            Class<? extends EnumInterface> c) {

        load();

        if (!classes.containsKey(c.getSimpleName())) {

            logger.warn("Not an enum class: " + c.getName());
        }
        else {

            List<EnumInterface> values = getKnownValues(c);

            if (values != null) {

                for(EnumInterface element : values) {

                    if (element.getValue() == value) {

                        return element;
                    }
                }
            }
        }

        return null;
    }

    public static EnumInterface getUnknownValue(
        final int value,
        Class<? extends EnumInterface> c) {

        load();

        if (!classes.containsKey(c.getSimpleName())) {

            logger.warn("Not an enum class: " + c.getName());
        }
        else {

            List<EnumInterface> values = getUnknownValues(c);

            for(EnumInterface element : values) {

                if (element.getValue() == value) {

                    return element;
                }
            }

            final String name = (c.getSimpleName() + "_" + value);

            // Generate an "unknown" object.
            //
            EnumInterface element = new EnumInterface() {

                @Override
                public int getValue() {

                    return value;
                }

                @Override
                public String getName() {

                    return name;
                }

                @Override
                public String getDescription() {

                    return name;
                }
            };

            values.add(element);

            return element;
        }

        return null;
    }

    public static String getDescription(
            int value,
            Class<? extends EnumInterface> c) {

        EnumInterface enumeration = getKnownValue(value, c);

        if (enumeration == null) {

            enumeration = getUnknownValue(value, c);
        }

        return enumeration.getDescription();
    }

    @SuppressWarnings("unchecked")
    public static void load() {

        Class<? extends EnumInterface> c = null;

        if (!loaded) try {

            final long start = System.currentTimeMillis();

            // These are not auto-generated (not in the index):
            //
            addClass(AIR_SMOKE.class);
            addClass(APP_CTRL_APPLICATION_TYPE.class);
            addClass(APP_CTRL_CONTROL_TYPE.class);
            addClass(ARTICULATED_PARTS_METRIC.class);
            addClass(ENT_DOMAIN.class);
            addClass(ENT_KIND.class);
            addClass(HOIST_STATUS.class);
            addClass(IED_PRESENCE.class);
            addClass(LAND_SMOKE.class);
            addClass(LF_AGE_GROUP.class);
            addClass(LF_CAMOUFLAGE_TYPE.class);
            addClass(LF_CLOTH_TYPE.class);
            addClass(LF_COMPLIANCE.class);
            addClass(LF_EQ_LASER.class);
            addClass(LF_ETHNICITY.class);
            addClass(LF_GENDER.class);
            addClass(LF_HEALTH.class);
            addClass(LF_POSTURE.class);
            addClass(LF_WEAPON_STATE.class);
            addClass(ON_OFF.class);
            addClass(PDU_STATUS_IAI.class);
            addClass(PRESENCE.class);
            addClass(SEVERITY.class);
            addClass(SLING_DAMAGE.class);
            addClass(YES_NO.class);

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

                addClass(c);

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

    private static List<EnumInterface> getKnownValues(
            Class<? extends EnumInterface> c) {

        List<EnumInterface> values = known.get(c);

        if (values == null) {

            EnumInterface array[] = c.getEnumConstants();

            if (array == null) {

                logger.warn("No enum constants for " + c.getName());
            }
            else {

                values = Collections.unmodifiableList(Arrays.asList(array));

                known.put(c, values);
            }
        }

        return values;
    }

    private static List<EnumInterface> getUnknownValues(
            Class<? extends EnumInterface> c) {

        List<EnumInterface> values = unknown.get(c);

        if (values == null) {

            values = new ArrayList<>();

            unknown.put(c, values);
        }

        return values;
    }

    private static void addClass(Class<? extends EnumInterface> type) {

        classes.put(type.getSimpleName(), type);
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
