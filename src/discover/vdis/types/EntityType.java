package discover.vdis.types;

import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class EntityType implements Comparable<EntityType>, Bufferable, Writable {

    public static final int LENGTH = 8;

    public long value = 0;
    public final Septuple septuple;
    public final String name;
    public final String description;
    public final String alternate;

    EntityType(
        int kind,
        int domain,
        int country,
        int category,
        int subcategory,
        int specific,
        int extension,
        long value,
        String name,
        String description,
        String alternate,
        String string) {

        septuple = new Septuple(
            string,
            kind,
            domain,
            country,
            category,
            subcategory,
            specific,
            extension);

        this.value = value;
        this.name = name;
        this.description = description;
        this.alternate = alternate;
    }

    /**
     * @return {@link String}
     */
    public String getKind() {

        return VDIS.getHandle(VDIS.ENT_KIND).getDescription(septuple.kind);
    }

    /**
     * @return {@link String}
     */
    public String getDomain() {

        return VDIS.getHandle(VDIS.DOMAIN).getDescription(septuple.domain);
    }

    /**
     * @return {@link String}
     */
    public String getCountry() {

        return VDIS.getHandle(VDIS.ENT_CNTRY).getDescription(septuple.country);
    }

    @Override
    public int compareTo(EntityType type) {

        if (value < type.value) {

            return -1;
        }
        else if (value == type.value) {

            return 0;
        }
        else {

            return 1;
        }
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof EntityType) {

            return (value == ((EntityType)object).value);
        }
        else {

            return false;
        }
    }

    @Override
    public int hashCode() {

        return Long.hashCode(value);
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
