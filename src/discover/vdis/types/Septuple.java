package discover.vdis.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tony Pinkston
 */
public class Septuple {

    protected static final Logger logger = LoggerFactory.getLogger(Septuple.class);

    public final String string; // (e.g. "1.1.225.1.1.0.0")

    public final int kind;
    public final int domain;
    public final int country;
    public final int category;
    public final int subcategory;
    public final int specific;
    public final int extension;

    Septuple(
            String string,
            int kind,
            int domain,
            int country,
            int category,
            int subcategory,
            int specific,
            int extension) {

        this.string = string;
        this.kind = kind;
        this.domain = domain;
        this.country = country;
        this.category = category;
        this.subcategory = subcategory;
        this.specific = specific;
        this.extension = extension;
    }

    public long toLong() {

        return toLong(
            kind,
            domain,
            country,
            category,
            subcategory,
            specific,
            extension);
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

    public static String toString(
            int kind,
            int domain,
            int country,
            int category,
            int subcategory,
            int specific,
            int extension) {

        StringBuilder builder = new StringBuilder();

        builder.append(kind);
        builder.append(".");
        builder.append(domain);
        builder.append(".");
        builder.append(country);
        builder.append(".");
        builder.append(category);
        builder.append(".");
        builder.append(subcategory);
        builder.append(".");
        builder.append(specific);
        builder.append(".");
        builder.append(extension);

        return builder.toString();
    }

    public static Septuple parse(String string) {

        String tokens[] = ((string != null) ? string.split("\\.") : null);
        int kind = 0;
        int domain = 0;
        int country = 0;
        int category = 0;
        int subcategory = 0;
        int specific = 0;
        int extension = 0;

        if ((tokens == null) || (tokens.length != 7)) {

            logger.warn("Invalid input string '{}'", string);
        }
        else try {

            kind = Integer.parseInt(tokens[0]);
            domain = Integer.parseInt(tokens[1]);
            country = Integer.parseInt(tokens[2]);
            category = Integer.parseInt(tokens[3]);
            subcategory = Integer.parseInt(tokens[4]);
            specific = Integer.parseInt(tokens[5]);
            extension = Integer.parseInt(tokens[6]);
        }
        catch(NumberFormatException exception) {

            logger.error("Caught exception!", exception);
        }

        return new Septuple(
            string,
            kind,
            domain,
            country,
            category,
            subcategory,
            specific,
            extension);
    }
}
