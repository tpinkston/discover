package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
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

        read(stream);
    }

    public int getNumber() { return number; }
    public long getTime() { return time; }

    public void setTime(long time) {

        this.time = time;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("EMITTER SYSTEM");
        buffer.addAttribute("Name", name, VDIS.EMITTER_NAME);
        buffer.addAttribute("Number", number);
        buffer.addAttribute("Function", function, VDIS.EMITTER_FUNCTION);
        buffer.addAttribute("Location", location.toString());
        buffer.addAttribute("Data Length", dataLength);
        buffer.addAttribute("Beam Count", beamCount);

        for(int i = 0, size = beams.size(); i < size; ++i) {

            buffer.addBuffer(beams.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 1 byte
        dataLength = stream.readUnsignedByte();

        // 1 byte
        beamCount = stream.readUnsignedByte();

        // 2 bytes padding
        stream.skipBytes(2);

        // Emitter System
        name = stream.readUnsignedShort(); // 2 bytes
        function = stream.readUnsignedByte(); // 1 byte
        number = stream.readUnsignedByte(); // 1 byte

        // 12 bytes
        location.read(stream);

        for(int i = 0; i < beamCount; ++i) {

            beams.add(new EmitterBeamData(stream));
        }
    }
}
