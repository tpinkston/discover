/**
 * @author Tony Pinkston
 */
package discover.vdis.types;

import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

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

        if (this.value < type.value) {
            
            return -1;
        }
        else if (this.value == type.value) {
            
            return 0;
        }
        else {
            
            return 1;
        }
    }

    @Override
    public boolean equals(Object object) {
        
        if (object instanceof ObjectType) {
            
            return (this.value == ((ObjectType)object).value);
        }
        else {
            
            return false;
        }
    }
    
    /**
     * @return {@link String}
     */
    public String getDomain() {
    
        return VDIS.getHandle(VDIS.DOMAIN).getDescription(this.domain);
    }

    /**
     * @return {@link String}
     */
    public String getKind() {
    
        return VDIS.getHandle(VDIS.OBJECT_KIND).getDescription(this.kind);
    }
    
    /**
     * @return {@link String}
     */
    public String getGeometry() {
    
        return VDIS.getHandle(VDIS.OBJECT_GEOMETRY).getDescription(this.geometry.ordinal());
    }

    @Override
    public String toString() {

        return this.tuple;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addText(this.name);
        buffer.addBreak();
        buffer.addText(this.tuple);
        buffer.addBreak();
    }
}
