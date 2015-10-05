/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

public class EmitterSystemData implements Bufferable, Readable {

    private Location12 location = new Location12();
    private List<EmitterBeamData> beams = new ArrayList<EmitterBeamData>();
    private int name = 0;
    private int function = 0;
    private int number = 0;
    private int beamCount = 0;
    private int dataLength = 0;
    private long time = 0;

    public EmitterSystemData(DataInputStream stream) throws IOException {

        this.read(stream);
    }

    public int getNumber() { return this.number; }
    public long getTime() { return this.time; }

    public void setTime(long time) {

        this.time = time;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("EMITTER SYSTEM");
        buffer.addAttribute("Name", this.name, VDIS.EMITTER_NAME);
        buffer.addAttribute("Number", this.number);
        buffer.addAttribute("Function", this.function, VDIS.EMITTER_FUNCTION);
        buffer.addAttribute("Location", this.location.toString());
        buffer.addAttribute("Data Length", this.dataLength);
        buffer.addAttribute("Beam Count", this.beamCount);

        for(int i = 0, size = this.beams.size(); i < size; ++i) {

            buffer.addBuffer(this.beams.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 1 byte
        this.dataLength = stream.readUnsignedByte();

        // 1 byte
        this.beamCount = stream.readUnsignedByte();

        // 2 bytes padding
        stream.skipBytes(2);

        // Emitter System
        this.name = stream.readUnsignedShort(); // 2 bytes
        this.function = stream.readUnsignedByte(); // 1 byte
        this.number = stream.readUnsignedByte(); // 1 byte

        // 12 bytes
        this.location.read(stream);

        for(int i = 0; i < this.beamCount; ++i) {

            this.beams.add(new EmitterBeamData(stream));
        }
    }
}
