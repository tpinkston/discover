package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;

/**
 * @author Tony Pinkston
 */
public class IFFFundamentalOperationalData implements Bufferable, Readable {

    private IFFSystemStatus status = new IFFSystemStatus();
    private IFFInformationLayers informationLayers = new IFFInformationLayers();
    private byte dataField1 = 0x00;
    private byte dataField2 = 0x00;
    private short parameter1 = 0x00;
    private short parameter2 = 0x00;
    private short parameter3 = 0x00;
    private short parameter4 = 0x00;
    private short parameter5 = 0x00;
    private short parameter6 = 0x00;

    public void clear() {

        status.set((byte)0x00);
        informationLayers.set((byte)0x00);
        dataField1 = 0x00;
        dataField2 = 0x00;
        parameter1 = 0x00;
        parameter2 = 0x00;
        parameter3 = 0x00;
        parameter4 = 0x00;
        parameter5 = 0x00;
        parameter6 = 0x00;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("System Status");
        buffer.addBuffer(status);
        buffer.addTitle("Information Layers");
        buffer.addBuffer(informationLayers);
        buffer.addTitle("Data Fields");
        buffer.addAttribute("1", Binary.toString8(dataField1));
        buffer.addAttribute("2", Binary.toString8(dataField2));
        buffer.addTitle("Parameters");
        buffer.addAttribute("1", Binary.toString16(parameter1));
        buffer.addAttribute("2", Binary.toString16(parameter2));
        buffer.addAttribute("3", Binary.toString16(parameter3));
        buffer.addAttribute("4", Binary.toString16(parameter4));
        buffer.addAttribute("5", Binary.toString16(parameter5));
        buffer.addAttribute("6", Binary.toString16(parameter6));
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        status.read(stream);
        dataField1 = stream.readByte();
        informationLayers.read(stream);
        dataField2 = stream.readByte();
        parameter1 = readAvailableShort(stream);
        parameter2 = readAvailableShort(stream);
        parameter3 = readAvailableShort(stream);
        parameter4 = readAvailableShort(stream);
        parameter5 = readAvailableShort(stream);

        if (stream.available() > 0) {

            parameter6 = stream.readShort();
        }
        else {

            parameter6 = 0x00;
        }
    }

    private short readAvailableShort(DataInputStream stream) throws IOException {

        if (stream.available() > 1) {

            return stream.readShort();
        }
        else {

            return 0x00;
        }
    }
}
