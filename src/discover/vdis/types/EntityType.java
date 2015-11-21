package discover.vdis.types;

import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.COUNTRY;
import discover.vdis.enums.DOMAIN;
import discover.vdis.enums.ENT_KIND;

/**
 * @author Tony Pinkston
 */
public class EntityType implements Comparable<EntityType>, Bufferable, Writable {

    public static final int LENGTH = 8;

    public final String name;
    public final String description;
    public final Septuple septuple;

    EntityType(String name, String description, long value) {

        this(name, description, new Septuple(value));
    }

    EntityType(String name, String description, int septuple[]) {

        this(name, description, new Septuple(septuple));
    }

    EntityType(String name, String description, String septuple) {

        this(name, description, new Septuple(septuple));
    }

    EntityType(String name, String description, Septuple septuple) {

        if ((name == null) || name.isEmpty()) {

            throw new IllegalArgumentException("Invalid name: " + name);
        }

        if ((description == null) || description.isEmpty()) {

            throw new IllegalArgumentException("Invalid name: " + name);
        }

        if (septuple == null) {

            throw new NullPointerException("Septuple cannot be null!");
        }

        this.septuple = septuple;
        this.name = name;
        this.description = description;
    }

    public long getValue() {

        return septuple.value;
    }

    /**
     * @return {@link String}
     */
    public String getKind() {

        return ENT_KIND.get(septuple.kind).description;
    }

    /**
     * @return {@link String}
     */
    public String getDomain() {

        return DOMAIN.get(septuple.domain).description;
    }

    /**
     * @return {@link String}
     */
    public String getCountry() {

        return COUNTRY.get(septuple.country).description;
    }

    @Override
    public int compareTo(EntityType type) {

        if (type == null) {

            return 1;
        }

        return Long.compare(septuple.value, type.septuple.value);
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof EntityType) {

            return (septuple.value == ((EntityType)object).septuple.value);
        }
        else {

            return false;
        }
    }

    @Override
    public int hashCode() {

        return Long.valueOf(septuple.value).hashCode();
    }

    @Override
    public String toString() {

        return septuple.string;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addText(name);
        buffer.addBreak();
        buffer.addText(septuple.string);
        buffer.addBreak();
        buffer.addText("\"" + description + "\"");
        buffer.addBreak();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(septuple.kind);
        stream.writeByte(septuple.domain);
        stream.writeShort(septuple.country);
        stream.writeByte(septuple.category);
        stream.writeByte(septuple.subcategory);
        stream.writeByte(septuple.specific);
        stream.writeByte(septuple.extension);
    }
}
