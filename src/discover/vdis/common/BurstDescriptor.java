/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;
import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;

/**
 * 16 byte record used in Fire and Detonation PDUs 
 */
public class BurstDescriptor implements Bufferable, Readable {

    private EntityType munition = null;
    private int warhead = 0;
    private int fuse = 0;
    private int quantity = 0;
    private int rate = 0;
    
    public EntityType getMunition() { return this.munition; }
    public int getWarhead() { return this.warhead; }
    public int getFuse() { return this.fuse; }
    public int getQuantity() { return this.quantity; }
    public int getRate() { return this.rate; }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("MUNITION");
        this.munition.toBuffer(buffer);
        buffer.addBreak();
        buffer.addTitle("BURST DESCRIPTION");
        buffer.addAttribute(
            "Warhead",
            this.warhead,
            VDIS.WARHEAD_BURST_DESC);
        buffer.addAttribute(
            "Fuse", 
            this.fuse,
            VDIS.FUSE_BURST_DESC);
        buffer.addAttribute("Quantity", this.quantity);
        buffer.addAttribute("Rate", this.rate);
        buffer.addBreak();
    }
    
    public void clear() {
        
        this.munition = null;
        this.warhead = 0;
        this.fuse = 0;
        this.quantity = 0;
        this.rate = 0;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 8 bytes
        this.munition = EntityTypes.read(stream);
        
        // 2 bytes
        this.warhead = stream.readUnsignedShort();

        // 2 bytes
        this.fuse = stream.readUnsignedShort();

        // 2 bytes
        this.quantity = stream.readUnsignedShort();
        
        // 2 bytes
        this.rate = stream.readUnsignedShort();
    }
}
