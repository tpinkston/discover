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

public class IFFSystemIdentifier implements Bufferable, Readable {
    
    private int type = 0;
    private int name = 0;
    private int mode = 0;
    private IFFChangeOptions options = new IFFChangeOptions();

    public int getType() { return this.type; }
    public int getName() { return this.name; }
    public int getMode() { return this.mode; }
    public IFFChangeOptions getOptions() { return this.options; }

    public void clear() {
        
        this.type = 0;
        this.name = 0;
        this.mode = 0;
        this.options.set((byte)0x00);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Type", this.type, VDIS.IFF_SYSTEM_TYPE);
        buffer.addAttribute("Name", this.name, VDIS.IFF_SYSTEM_NAME);
        buffer.addAttribute("Mode", this.mode, VDIS.IFF_SYSTEM_MODE);
        buffer.addTitle("CHANGE/OPTIONS");
        buffer.addBuffer(this.options);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.type = stream.readUnsignedShort();
        this.name = stream.readUnsignedShort();
        this.mode = stream.readUnsignedByte();
        this.options.read(stream);
    }
}
