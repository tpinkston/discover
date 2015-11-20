package discover.vdis.enums;

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
public abstract class Value implements Comparable<Value> {

    protected static final Logger logger = LoggerFactory.getLogger(Value.class);

    private static final Map<Class<?>, Values<?>> caches = new HashMap<>();

    /** Simple name for class. */
    public final String type;

    /** Value name. */
    public final String name;

    /** Value description. */
    public final String description;

    /** Numeric value. */
    public final int value;

    /** True if value is data-driven or hard coded. */
    public final boolean known;

    /** Value returned for {@link #toString()} */
    public final String string;

    /**
     * @param value - see {@link #value}
     * @param name - see {@link #name}
     * @param description - see {@link #description}
     * @param known - see {@link #known}
     */
    protected Value(
            int value,
            String name,
            String description,
            boolean known) {

        this.type = getClass().getSimpleName();
        this.name = name;
        this.description = description;
        this.value = value;
        this.known = known;
        this.string = (name + "[" + Integer.toString(value) + "]");
    }

    @Override
    public final int compareTo(Value other) {

        return Integer.compare(hashCode(), other.hashCode());
    }

    @Override
    public final int hashCode() {

        return (type.hashCode() + value);
    }

    @Override
    public final boolean equals(Object other) {

        boolean equals = super.equals(other);

        if (!equals && (other instanceof Value)) {

            equals = (hashCode() == ((Value)other).hashCode());
        }

        return equals;
    }

    @Override
    public final String toString() {

        return string;
    }

    /**
     * @return Array of all values for specified type or empty list if
     * type not found.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Value> List<E> values(Class<E> type) {

        Values<E> cache = (Values<E>)caches.get(type);

        if (cache != null) {

            return cache.all();
        }

        return Collections.emptyList();
    }

    /**
     * @return Array of known or unknown values for specified type or
     * empty list if type not found.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Value> List<E> values(
            Class<E> type,
            boolean known) {

        Values<E> cache = (Values<E>)caches.get(type);

        if (cache != null) {

            return (known ? cache.known() : cache.unknown());
        }

        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public static <E extends Value> E get(int value, Class<E> type) {

        Values<E> cache = (Values<E>)caches.get(type);
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
    protected static <E extends Value> void cache(
            E element,
            Class<E> type) {

        // Cache all instances that get constructed.
        //
        Values<E> cache = (Values<E>)caches.get(type);

        if (cache == null) {

            cache = new Values<E>();
            caches.put(type, cache);
        }

        cache.add(element);
    }

    private static class Values<E extends Value> {

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
