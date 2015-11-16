package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.IFF_SYSTEM_MODE;
import discover.vdis.enums.IFF_SYSTEM_NAME;
import discover.vdis.enums.IFF_SYSTEM_TYPE;

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

        buffer.addAttribute("Type", type, IFF_SYSTEM_TYPE.class);
        buffer.addAttribute("Name", name, IFF_SYSTEM_NAME.class);
        buffer.addAttribute("Mode", mode, IFF_SYSTEM_MODE.class);
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
