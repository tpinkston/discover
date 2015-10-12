package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class IFFSystemIdentifier implements Bufferable, Readable {

    private int type = 0;
    private int name = 0;
    private int mode = 0;
    private IFFChangeOptions options = new IFFChangeOptions();

    public int getType() { return type; }
    public int getName() { return name; }
    public int getMode() { return mode; }
    public IFFChangeOptions getOptions() { return options; }

    public void clear() {

        type = 0;
        name = 0;
        mode = 0;
        options.set((byte)0x00);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Type", type, VDIS.IFF_SYSTEM_TYPE);
        buffer.addAttribute("Name", name, VDIS.IFF_SYSTEM_NAME);
        buffer.addAttribute("Mode", mode, VDIS.IFF_SYSTEM_MODE);
        buffer.addTitle("CHANGE/OPTIONS");
        buffer.addBuffer(options);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        type = stream.readUnsignedShort();
        name = stream.readUnsignedShort();
        mode = stream.readUnsignedByte();
        options.read(stream);
    }
}
