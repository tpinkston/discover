package discover.vdis.types;

import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;
import discover.vdis.enums.VDISNames;

/**
 * @author Tony Pinkston
 */
public class ObjectType implements Comparable<ObjectType>, Bufferable {

    /**
     * Must match the OBJECT_GEOMETRY enumeration
     *
     * @see VDISNames.OBJECT_GEOMETRY
     */
    public static enum Geometry {

        UNKNOWN,
        POINT,
        LINEAR,
        AREAL
    }

    public final int domain;
    public final int kind;
    public final int category;
    public final int subcategory;
    public final int value;
    public final Geometry geometry;
    public final String name;
    public final String description;
    public final String tuple;

    public ObjectType(
        int domain,
        int kind,
        int category,
        int subcategory,
        int value,
        Geometry geometry,
        String name,
        String description,
        String tuple) {

        this.domain = domain;
        this.kind = kind;
        this.category = category;
        this.subcategory = subcategory;

        this.value = value;
        this.geometry = geometry;

        this.name = name;
        this.description = description;
        this.tuple = tuple;
    }

    @Override
    public int compareTo(ObjectType type) {

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

        if (object instanceof ObjectType) {

            return (value == ((ObjectType)object).value);
        }
        else {

            return false;
        }
    }

    @Override
    public int hashCode() {

        return value;
    }

    /**
     * @return {@link String}
     */
    public String getDomain() {

        return VDIS.getHandle(VDIS.DOMAIN).getDescription(domain);
    }

    /**
     * @return {@link String}
     */
    public String getKind() {

        return VDIS.getHandle(VDIS.OBJECT_KIND).getDescription(kind);
    }

    /**
     * @return {@link String}
     */
    public String getGeometry() {

        return VDIS.getHandle(VDIS.OBJECT_GEOMETRY).getDescription(geometry.ordinal());
    }

    @Override
    public String toString() {

        return tuple;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addText(name);
        buffer.addBreak();
        buffer.addText(tuple);
        buffer.addBreak();
    }
}
