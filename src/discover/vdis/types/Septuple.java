package discover.vdis.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tony Pinkston
 */
public class Septuple {

    protected static final Logger logger = LoggerFactory.getLogger(Septuple.class);

    public final String string; // (e.g. "1.1.225.1.1.0.0")

    public final long value;

    public final int kind;
    public final int domain;
    public final int country;
    public final int category;
    public final int subcategory;
    public final int specific;
    public final int extension;

    public Septuple(long value) {

        this(parse(value));
    }

    Septuple(int septuple[]) {

        if (septuple.length != 7) {

            throw new IllegalArgumentException("Array length invalid!");
        }

        kind = septuple[0];
        domain = septuple[1];
        country = septuple[2];
        category = septuple[3];
        subcategory = septuple[4];
        specific = septuple[5];
        extension = septuple[6];

        string = toString(septuple);
        value = toLong(septuple);
    }

    Septuple(String string) {

        final int septuple[] = parse(string);

        this.string = string;

        kind = septuple[0];
        domain = septuple[1];
        country = septuple[2];
        category = septuple[3];
        subcategory = septuple[4];
        specific = septuple[5];
        extension = septuple[6];

        value = toLong(septuple);
    }

    public long toLong() {

        return value;
    }

    public static long toLong(int septuple[]) {

        if (septuple.length != 7) {

            throw new IllegalArgumentException("Array length invalid!");
        }

        return toLong(
            septuple[0],
            septuple[1],
            septuple[2],
            septuple[3],
            septuple[4],
            septuple[5],
            septuple[6]);
    }

    public static long toLong(
            int kind,
            int domain,
            int country,
            int category,
            int subcategory,
            int specific,
            int extension) {

        long value = 0;

        value = (kind & 0xFF);
        value <<= 8;
        value |= (domain & 0xFF);
        value <<= 16;
        value |= (country & 0xFFFF);
        value <<= 8;
        value |= (category & 0xFF);
        value <<= 8;
        value |= (subcategory & 0xFF);
        value <<= 8;
        value |= (specific & 0xFF);
        value <<= 8;
        value |= (extension & 0xFF);

        return value;
    }

    public static String toString(int septuple[]) {

        if (septuple.length != 7) {

            throw new IllegalArgumentException("Array length invalid!");
        }

        return toString(
            septuple[0],
            septuple[1],
            septuple[2],
            septuple[3],
            septuple[4],
            septuple[5],
            septuple[6]);
    }

    public static String toString(
            int kind,
            int domain,
            int country,
            int category,
            int subcategory,
            int specific,
            int extension) {

        StringBuilder builder = new StringBuilder();

        builder.append(kind).append(".");
        builder.append(domain).append(".");
        builder.append(country).append(".");
        builder.append(category).append(".");
        builder.append(subcategory).append(".");
        builder.append(specific).append(".");
        builder.append(extension);

        return builder.toString();
    }

    public static int[] parse(String string) {

        String tokens[] = ((string != null) ? string.split("\\.") : null);
        int septuple[] = new int[] { 0, 0, 0, 0, 0, 0, 0 };

        if ((tokens == null) || (tokens.length != 7)) {

            logger.warn("Invalid input string '{}'", string);
        }
        else try {

            for(int i = 0; i < tokens.length; ++i) {

                septuple[i] = Integer.parseInt(tokens[i]);
            }
        }
        catch(NumberFormatException exception) {

            logger.error("Invalid input string '" + string + "'", exception);
        }

        return septuple;
    }

    public static int[] parse(long value) {

        int septuple[] = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        long bits = value;

        septuple[6] = (int)(bits & 0xFF);
        bits >>= 8;

        septuple[5] = (int)(bits & 0xFF);
        bits >>= 8;

        septuple[4] = (int)(bits & 0xFF);
        bits >>= 8;

        septuple[3] = (int)(bits & 0xFF);
        bits >>= 8;

        septuple[2] = (int)(bits & 0xFFFF);
        bits >>= 16;

        septuple[1] = (int)(bits & 0xFF);
        bits >>= 8;

        septuple[0] = (int)(bits & 0xFF);

        return septuple;
    }
}
