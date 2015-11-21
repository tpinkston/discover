package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.PDU_FAMILY;
import discover.vdis.enums.PDU_TYPE;
import discover.vdis.enums.PROTOCOL_VERSION;

/**
 * @author Tony Pinkston
 */
public class PDUHeader implements Bufferable, Readable, Writable {

    public static final int LENGTH = 12;

    private PDU_TYPE type = null;
    private PDU_FAMILY family = null;
    private PROTOCOL_VERSION protocol = null;
    private int exercise = -1;
    private int length = -1;
    private int padding = 0;
    private Timestamp timestamp = new Timestamp();
    private PDUStatus status = new PDUStatus();

    public PDU_TYPE getType() { return type; }
    public PDU_FAMILY getFamily() { return family; }
    public PROTOCOL_VERSION getProtocol() { return protocol; }
    public int getExercise() { return exercise; }
    public int getLength() { return length; }
    public int getPadding() { return padding; }
    public Timestamp getTimestamp() { return timestamp; }
    public PDUStatus getStatus() { return status; }

    public void setType(PDU_TYPE type) {

        if (type == null) {

            throw new NullPointerException("Type is null!");
        }

        this.type = type;
    }

    public void setFamily(PDU_FAMILY family) {

        if (family == null) {

            throw new NullPointerException("Family is null!");
        }

        this.family = family;
    }

    public void setProtocol(PROTOCOL_VERSION protocol) {

        if (protocol == null) {

            throw new NullPointerException("Protocol is null!");
        }

        this.protocol = protocol;
    }

    public void setTimestamp(Timestamp timestamp) {

        this.timestamp = timestamp.clone();
    }

    public void setStatus(PDUStatus status) {

        this.status = status;
    }

    public void setExercise(int exercise) {

        this.exercise = exercise;
    }

    public void setLength(int length) {

        this.length = length;
    }

    public void setPadding(int padding) {

        this.padding = padding;
    }

    /**
     * Reads 12 bytes from stream
     */
    @Override
    public void read(DataInputStream stream) throws IOException {

        protocol = PROTOCOL_VERSION.get(stream.readUnsignedByte());
        exercise = stream.readUnsignedByte();
        type = PDU_TYPE.get(stream.readUnsignedByte());
        family = PDU_FAMILY.get(stream.readUnsignedByte());
        timestamp.read(stream); // 4 bytes
        length = stream.readUnsignedShort();
        status.read(stream);
        padding = stream.readUnsignedByte();

        status.setEnumValues(type);
    }

    /**
     * Writes 12 bytes to stream
     */
    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(protocol.value);
        stream.writeByte(exercise);
        stream.writeByte(type.value);
        stream.writeByte(family.value);
        stream.writeInt(timestamp.getValue());
        stream.writeShort(length);
        stream.writeByte(status.getValue()); // status
        stream.writeByte(padding);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("HEADER");
        buffer.addAttribute("Protocol", protocol.description);
        buffer.addAttribute("Exercise", exercise);
        buffer.addAttribute("Type", type.description);
        buffer.addAttribute("Family", family.description);
        buffer.addAttribute("Length", length);
        buffer.addAttribute("Timestamp", timestamp.toString());
        buffer.addBuffer(status);

        if (padding != 0) {

            buffer.addAttribute("Padding", padding);
        }

        buffer.addBreak();
    }
}
