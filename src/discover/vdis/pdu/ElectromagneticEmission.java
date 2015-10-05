/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EmitterSystemData;
import discover.vdis.common.EntityId;

public class ElectromagneticEmission extends AbstractPDU {

    private EntityId emitter = new EntityId();
    private EntityId event = new EntityId();
    private List<EmitterSystemData> systems = new ArrayList<EmitterSystemData>();
    private int update = 0;
    private int systemCount = 0;

    public ElectromagneticEmission() {

    }

    public List<EmitterSystemData> getSystems() {

        return this.systems;
    }

    @Override
    public void clear() {

        this.emitter.clear();
        this.event.clear();
        this.systems.clear();
        this.update = 0;
        this.systemCount = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Emitting Entity", this.emitter.toString());
        buffer.addAttribute("Event", this.event.toString());
        buffer.addAttribute("State Update Indicator", this.update);
        buffer.addAttribute("Systems", this.systemCount);

        for(int i = 0, size = this.systems.size(); i < size; ++i) {

            buffer.addBreak();
            buffer.addSeparator();
            buffer.addBuffer(this.systems.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 12 bytes (header)
        super.read(stream);

        // 6 bytes
        this.emitter.read(stream);

        // 6 bytes
        this.event.read(stream);

        // 1 byte
        this.update = stream.readUnsignedByte();

        // 1 byte
        this.systemCount = stream.readUnsignedByte();

        // 2 bytes padding
        stream.skipBytes(2);

        for(int i = 0; i < this.systemCount; ++i) {

            this.systems.add(new EmitterSystemData(stream));
        }
    }
}
