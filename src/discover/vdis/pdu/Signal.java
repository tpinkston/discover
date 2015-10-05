/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.Hexadecimal;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

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

        this.entityId.clear();
        this.encodingType = 0;
        this.encodingClass = 0;
        this.tdlType = 0;
        this.tdlMessages = 0;
        this.radioId = 0;
        this.sampleRate = 0;
        this.dataLength = 0;
        this.samples = 0;
        this.data = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Entity", this.entityId.toString());
        buffer.addAttribute("Radio", this.radioId);
        buffer.addBreak();

        buffer.addTitle("ENCODING");
        buffer.addAttribute(
            "Encoding Class",
            VDIS.getDescription(VDIS.ENCODING_CLASS, this.encodingClass));
        buffer.addAttribute(
            "Encoding Type",
            VDIS.getDescription(VDIS.ENCODING_TYPE, this.encodingType));
        buffer.addAttribute(
            "TDL Type",
            VDIS.getDescription(VDIS.TDL_TYPE, this.tdlType));
        buffer.addAttribute("TDL Message Count", this.tdlMessages);
        buffer.addAttribute("Sample Rate", this.sampleRate);
        buffer.addAttribute("Samples", this.samples);
        buffer.addBreak();

        buffer.addTitle("DATA");
        buffer.addAttribute("Data Length", (this.dataLength + " Bits"));

        if (this.data == null) {

            buffer.addItalic("Empty");
            buffer.addBreak();
        }
        else {

            Hexadecimal.toBuffer(
                buffer,
                "     ",
                4,
                false,
                this.data);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        this.entityId.read(stream);
        this.radioId = stream.readUnsignedShort();

        // Encoding Scheme
        int eScheme = stream.readUnsignedShort();

        // Encoding class is the bits 14-15 of the encoding scheme.
        this.encodingClass = Binary.get2Bits(15, eScheme);

        if (this.encodingClass == 0) {

            // ENCODED_AUDIO, bits 0-13 is the encoding type.
            this.encodingType = (eScheme & 0x3FFF);
        }
        else {

            // Bits 0-13 is the number of Tactical Data Link (TDL) messages.
            this.tdlMessages = (eScheme & 0x3FFF);
            this.encodingType = 0;
        }

        this.tdlType = stream.readUnsignedShort();
        this.sampleRate = stream.readInt();
        this.dataLength = stream.readUnsignedShort();
        this.samples = stream.readUnsignedShort();

        if (this.dataLength > 0) {

            // Data length is the number of bits!
            final int length = (this.dataLength / 8);

            this.data = new byte[length];

            for(int i = 0; i < length; ++i) {

                this.data[i] = stream.readByte();
            }
        }

        // Variable Padding
        while(stream.available() > 0) {

            stream.skipBytes(1);
        }
    }
}
