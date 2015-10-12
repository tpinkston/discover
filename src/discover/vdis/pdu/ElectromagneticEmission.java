package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EmitterSystemData;
import discover.vdis.common.EntityId;

/**
 * @author Tony Pinkston
 */
public class ElectromagneticEmission extends AbstractPDU {

    private EntityId emitter = new EntityId();
    private EntityId event = new EntityId();
    private List<EmitterSystemData> systems = new ArrayList<EmitterSystemData>();
    private int update = 0;
    private int systemCount = 0;

    public ElectromagneticEmission() {

    }

    public List<EmitterSystemData> getSystems() {

        return systems;
    }

    @Override
    public void clear() {

        emitter.clear();
        event.clear();
        systems.clear();
        update = 0;
        systemCount = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Emitting Entity", emitter.toString());
        buffer.addAttribute("Event", event.toString());
        buffer.addAttribute("State Update Indicator", update);
        buffer.addAttribute("Systems", systemCount);

        for(int i = 0, size = systems.size(); i < size; ++i) {

            buffer.addBreak();
            buffer.addSeparator();
            buffer.addBuffer(systems.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 12 bytes (header)
        super.read(stream);

        // 6 bytes
        emitter.read(stream);

        // 6 bytes
        event.read(stream);

        // 1 byte
        update = stream.readUnsignedByte();

        // 1 byte
        systemCount = stream.readUnsignedByte();

        // 2 bytes padding
        stream.skipBytes(2);

        for(int i = 0; i < systemCount; ++i) {

            systems.add(new EmitterSystemData(stream));
        }
    }
}
