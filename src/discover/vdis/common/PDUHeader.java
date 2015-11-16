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

    private int type = 0; // TODO: use PDU_TYPE
    private int family = 0; // TODO: use PDU_FAMILY
    private int protocol = 0; // TODO: use PDU_PROTOCOL_FAMILY
    private int exercise = -1;
    private int length = -1;
    private int padding = 0;
    private Timestamp timestamp = new Timestamp();
    private PDUStatus status = new PDUStatus();

    public int getType() { return type; }
    public int getFamily() { return family; }
    public int getProtocol() { return protocol; }
    public int getExercise() { return exercise; }
    public int getLength() { return length; }
    public int getPadding() { return padding; }
    public Timestamp getTimestamp() { return timestamp; }
    public PDUStatus getStatus() { return status; }

    public void setType(int type) {

        this.type = type;
    }

    public void setFamily(int family) {

        this.family = family;
    }

    public void setProtocol(int protocol) {

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

        protocol = stream.readUnsignedByte();
        exercise = stream.readUnsignedByte();
        type = stream.readUnsignedByte();
        family = stream.readUnsignedByte();
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

        stream.writeByte(protocol);
        stream.writeByte(exercise);
        stream.writeByte(type);
        stream.writeByte(family);
        stream.writeInt(timestamp.getValue());
        stream.writeShort(length);
        stream.writeByte(status.getValue()); // status
        stream.writeByte(padding);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("HEADER");
        buffer.addAttribute("Protocol", protocol, PROTOCOL_VERSION.class);
        buffer.addAttribute("Exercise", exercise);
        buffer.addAttribute("Type", type, PDU_TYPE.class);
        buffer.addAttribute("Family", family, PDU_FAMILY.class);
        buffer.addAttribute("Length", length);
        buffer.addAttribute("Timestamp", timestamp.toString());
        buffer.addBuffer(status);

        if (padding != 0) {

            buffer.addAttribute("Padding", padding);
        }

        buffer.addBreak();
    }
}
