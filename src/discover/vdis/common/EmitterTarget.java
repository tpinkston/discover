package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;

/**
 * @author Tony Pinkston
 */
public class EmitterTarget implements Bufferable, Readable {

    private EntityId entity = new EntityId();
    private int emitter = 0;
    private int beam = 0;

    public EmitterTarget(DataInputStream stream) throws IOException {

        read(stream);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("TARGET");
        buffer.addAttribute("Entity", entity.toString());
        buffer.addAttribute("Emitter", emitter);
        buffer.addAttribute("Beam", beam);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        entity.read(stream); // 6 bytes
        emitter = stream.readUnsignedByte(); // 1 byte
        beam = stream.readUnsignedByte(); // 1 byte
    }
}