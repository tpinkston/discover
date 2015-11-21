package discover.vdis.enums;

import java.util.List;

/**
 *
 * @author Tony Pinkston
 *
 */
public abstract class Value implements Comparable<Value> {

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

    /** @see Values#cache(Value, Class) */
    protected <E extends Value> void cache(E element, Class<E> type) {

        Values.cache(element, type);
    }

    /** @see Values#values(Class) */
    protected static <E extends Value> List<E> values(Class<E> type) {

        return Values.values(type);
    }

    /** @see Values#values(Class, boolean) */
    protected static <E extends Value> List<E> values(Class<E> type, boolean known) {

        return Values.values(type, known);
    }

    /** @see Values#get(int, Class) */
    protected static <E extends Value> E get(int value, Class<E> type) {

        return Values.get(value, type);
    }
}
