package discover.vdis.types;

import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.DOMAIN;
import discover.vdis.enums.OBJECT_GEOMETRY;
import discover.vdis.enums.OBJECT_KIND;

/**
 * @author Tony Pinkston
 */
public class ObjectType implements Comparable<ObjectType>, Bufferable {

    public final OBJECT_GEOMETRY geometry;
    public final String name;
    public final String description;
    public final String tuple;
    public final int domain;
    public final int kind;
    public final int category;
    public final int subcategory;
    public final int value;

    public ObjectType(
            String name,
            String description,
            OBJECT_GEOMETRY geometry,
            int domain,
            int kind,
            int category,
            int subcategory) {

        this.geometry = geometry;
        this.name = name;
        this.description = description;
        this.domain = domain;
        this.kind = kind;
        this.category = category;
        this.subcategory = subcategory;

        tuple = ObjectTypes.toString(kind, domain, category, subcategory);
        value = ObjectTypes.toInteger(domain, kind, category, subcategory);
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

        return DOMAIN.get(domain).description;
    }

    /**
     * @return {@link String}
     */
    public String getKind() {

        return OBJECT_KIND.get(kind).description;
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
