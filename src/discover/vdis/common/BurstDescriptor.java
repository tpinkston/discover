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
 *
 * @author Tony Pinkston
 */
public class BurstDescriptor implements Bufferable, Readable {

    private EntityType munition = null;
    private int warhead = 0;
    private int fuse = 0;
    private int quantity = 0;
    private int rate = 0;

    public EntityType getMunition() { return munition; }
    public int getWarhead() { return warhead; }
    public int getFuse() { return fuse; }
    public int getQuantity() { return quantity; }
    public int getRate() { return rate; }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("MUNITION");
        munition.toBuffer(buffer);
        buffer.addBreak();
        buffer.addTitle("BURST DESCRIPTION");
        buffer.addAttribute(
            "Warhead",
            warhead,
            VDIS.WARHEAD_BURST_DESC);
        buffer.addAttribute(
            "Fuse",
            fuse,
            VDIS.FUSE_BURST_DESC);
        buffer.addAttribute("Quantity", quantity);
        buffer.addAttribute("Rate", rate);
        buffer.addBreak();
    }

    public void clear() {

        munition = null;
        warhead = 0;
        fuse = 0;
        quantity = 0;
        rate = 0;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 8 bytes
        munition = EntityTypes.read(stream);

        // 2 bytes
        warhead = stream.readUnsignedShort();

        // 2 bytes
        fuse = stream.readUnsignedShort();

        // 2 bytes
        quantity = stream.readUnsignedShort();

        // 2 bytes
        rate = stream.readUnsignedShort();
    }
}
