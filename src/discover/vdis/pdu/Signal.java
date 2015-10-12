package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.Hexadecimal;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class Signal extends AbstractPDU {

    private EntityId entityId = new EntityId();
    private int encodingType = 0;
    private int encodingClass = 0;
    private int tdlType = 0;
    private int radioId = 0;
    private int tdlMessages = 0;
    private int sampleRate = 0;
    private int dataLength = 0;
    private int samples = 0;
    private byte data[] = null;

    public Signal() {

    }

    @Override
    public void clear() {

        entityId.clear();
        encodingType = 0;
        encodingClass = 0;
        tdlType = 0;
        tdlMessages = 0;
        radioId = 0;
        sampleRate = 0;
        dataLength = 0;
        samples = 0;
        data = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Entity", entityId.toString());
        buffer.addAttribute("Radio", radioId);
        buffer.addBreak();

        buffer.addTitle("ENCODING");
        buffer.addAttribute(
            "Encoding Class",
            VDIS.getDescription(VDIS.ENCODING_CLASS, encodingClass));
        buffer.addAttribute(
            "Encoding Type",
            VDIS.getDescription(VDIS.ENCODING_TYPE, encodingType));
        buffer.addAttribute(
            "TDL Type",
            VDIS.getDescription(VDIS.TDL_TYPE, tdlType));
        buffer.addAttribute("TDL Message Count", tdlMessages);
        buffer.addAttribute("Sample Rate", sampleRate);
        buffer.addAttribute("Samples", samples);
        buffer.addBreak();

        buffer.addTitle("DATA");
        buffer.addAttribute("Data Length", (dataLength + " Bits"));

        if (data == null) {

            buffer.addItalic("Empty");
            buffer.addBreak();
        }
        else {

            Hexadecimal.toBuffer(
                buffer,
                "     ",
                4,
                false,
                data);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        entityId.read(stream);
        radioId = stream.readUnsignedShort();

        // Encoding Scheme
        int eScheme = stream.readUnsignedShort();

        // Encoding class is the bits 14-15 of the encoding scheme.
        encodingClass = Binary.get2Bits(15, eScheme);

        if (encodingClass == 0) {

            // ENCODED_AUDIO, bits 0-13 is the encoding type.
            encodingType = (eScheme & 0x3FFF);
        }
        else {

            // Bits 0-13 is the number of Tactical Data Link (TDL) messages.
            tdlMessages = (eScheme & 0x3FFF);
            encodingType = 0;
        }

        tdlType = stream.readUnsignedShort();
        sampleRate = stream.readInt();
        dataLength = stream.readUnsignedShort();
        samples = stream.readUnsignedShort();

        if (dataLength > 0) {

            // Data length is the number of bits!
            final int length = (dataLength / 8);

            data = new byte[length];

            for(int i = 0; i < length; ++i) {

                data[i] = stream.readByte();
            }
        }

        // Variable Padding
        while(stream.available() > 0) {

            stream.skipBytes(1);
        }
    }
}
