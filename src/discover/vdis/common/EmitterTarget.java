/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;

public class EmitterTarget implements Bufferable, Readable {

    private EntityId entity = new EntityId();
    private int emitter = 0;
    private int beam = 0;

    public EmitterTarget(DataInputStream stream) throws IOException {

        this.read(stream);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("TARGET");
        buffer.addAttribute("Entity", entity.toString());
        buffer.addAttribute("Emitter", this.emitter);
        buffer.addAttribute("Beam", this.beam);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.entity.read(stream); // 6 bytes
        this.emitter = stream.readUnsignedByte(); // 1 byte
        this.beam = stream.readUnsignedByte(); // 1 byte
    }
}