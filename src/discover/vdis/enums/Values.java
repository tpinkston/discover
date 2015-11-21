package discover.vdis.enums;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tony Pinkston
 *
 */
public class Values {

    private static final Logger logger = LoggerFactory.getLogger(Values.class);

    private static final String INDEX = "index.txt";
    
    private static final Map<Class<?>, Cache<?>> caches = new HashMap<>();
    
    private static boolean loaded = false;

    /**
     * Loads classes that extend {@link Value} class.
     */
    public static void load() {

        if (!loaded) try {

            final long start = System.currentTimeMillis();

            // Index file is a text file with class names for all types
            // that implement Value, most are auto-generated.
            //
            InputStream stream = Value.class.getResourceAsStream(INDEX);

            if (stream == null) {

                throw new RuntimeException("Resource not found: " + INDEX);
            }

            InputStreamReader input = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(input);
            String string = reader.readLine();
            int total = 0;

            while(string != null) {

                // Forces instantiation of static instances.
                //
                Class.forName(string);

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

    /**
     * @return Array of all values for specified type (E) or empty list if
     * type not found.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Value> List<E> values(Class<E> type) {

        Cache<E> cache = (Cache<E>)caches.get(type);

        if (cache != null) {

            return cache.all();
        }

        return Collections.emptyList();
    }

    /**
     * @return Array of known or unknown values for specified type (E) or
     * empty list if type not found.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Value> List<E> values(
            Class<E> type,
            boolean known) {

        Cache<E> cache = (Cache<E>)caches.get(type);

        if (cache != null) {

            return (known ? cache.known() : cache.unknown());
        }

        return Collections.emptyList();
    }

    /**
     * @return Value object of specified type (E) with specified numeric value.
     * If the value is not known then an <i>unknown</i> value gets created.
     * Return null only when exception occurs constructing an <i>unknown</i>.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Value> E get(int value, Class<E> type) {

        Cache<E> cache = (Cache<E>)caches.get(type);
        E result = null;

        if (cache != null) {

            result = cache.find(value);
        }

        if (result == null) {

            try {

                Constructor<E> constructor = (Constructor<E>)type.getDeclaredConstructors()[0];

                constructor.setAccessible(true);

                result = constructor.newInstance(
                    value,
                    ("UNKNOWN_" + value),
                    ("Unknown [" + value + "]"),
                    false);

                cache(result, type);
            }
            catch(Exception exception) {

                logger.error(
                    "Failed to construct new " + type.getName(),
                    exception);
            }
        }

        return result;
    }
    
    @SuppressWarnings("unchecked")
    protected static <E extends Value> void cache(E element, Class<E> type) {

        // Cache all instances that get constructed.
        //
        Cache<E> cache = (Cache<E>)caches.get(type);

        if (cache == null) {

            cache = new Cache<E>();
            caches.put(type, cache);
        }

        cache.add(element);
    }

    private static class Cache<E extends Value> {

        /**
         * List of values known via the VDIS specification or defined via
         * extension of {@link Value} classes.
         */
        final List<E> known = new ArrayList<>();

        /**
         * List of values that are unknown, these are values that appear on
         * PDUs coming in off the wire and do not match any known values.
         */
        final List<E> unknown = new ArrayList<>();

        /**
         * Does not create unknown instance if not found.
         *
         * @return Cached instance either known or unknown or null if not found.
         */
        E find(int value) {

            for(E element : known) {

                if (element.value == value) {

                    return element;
                }
            }

            for(E element : unknown) {

                if (element.value == value) {

                    return element;
                }
            }

            return null;
        }

        /**
         * Adds element to either known or unknown list.
         */
        void add(E element) {

            if (element.known) {

                if (!known.contains(element)) {

                    known.add(element);
                }
            }
            else {

                if (!unknown.contains(element)) {

                    unknown.add(element);
                }
            }
        }

        List<E> known() {

            return Collections.unmodifiableList(known);
        }

        List<E> unknown() {

            return Collections.unmodifiableList(unknown);
        }

        List<E> all() {

            List<E> list = new ArrayList<>();

            list.addAll(known);
            list.addAll(unknown);

            Collections.sort(list);

            return list;
        }
    }
}
