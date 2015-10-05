/**
 * @author Tony Pinkston
 */
package discover.vdis.types;

import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

public class EntityType implements Comparable<EntityType>, Bufferable, Writable {
    
    public static final int LENGTH = 8;

    public long value = 0;
    public final Septuple septuple;
    public final String name;
    public final String description;
    public final String alternate;
    
    private String cdtName = null;
    
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
        
        this.septuple = new Septuple(
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
    
        return VDIS.getHandle(VDIS.ENT_KIND).getDescription(this.septuple.kind);
    }
    
    /**
     * @return {@link String}
     */
    public String getDomain() {
    
        return VDIS.getHandle(VDIS.DOMAIN).getDescription(this.septuple.domain);
    }
    
    /**
     * @return {@link String}
     */
    public String getCountry() {
    
        return VDIS.getHandle(VDIS.ENT_CNTRY).getDescription(this.septuple.country);
    }
    
    public String getCDTName() {
        
        return this.cdtName;
    }
    
    public void setCDTName(String name) {
        
        this.cdtName = name;
    }

    @Override
    public int compareTo(EntityType type) {

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
        
        if (object instanceof EntityType) {
        
            return (this.value == ((EntityType)object).value);
        }
        else {
            
            return false;
        }
    }
    
    @Override
    public String toString() {
        
        return this.septuple.string;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addText(this.name);
        buffer.addBreak();
        
        if (this.cdtName != null) {
        
            buffer.addText("CDT Model Name \"" + this.cdtName + "\"");
            buffer.addBreak();
        }
        
        buffer.addText(this.septuple.string);
        buffer.addBreak();
        buffer.addText("\"" + this.description + "\"");
        buffer.addBreak();
    }
    
    @Override
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeByte(this.septuple.kind);
        stream.writeByte(this.septuple.domain);
        stream.writeShort(this.septuple.country);
        stream.writeByte(this.septuple.category);
        stream.writeByte(this.septuple.subcategory);
        stream.writeByte(this.septuple.specific);
        stream.writeByte(this.septuple.extension);
    }
}
