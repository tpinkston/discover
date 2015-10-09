package discover.vdis.pdu;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.datum.AbstractDatumRecord;
import discover.vdis.datum.DatumRecordFactory;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class ApplicationControlPDU extends AbstractPDU implements Writable {

    private final EntityId originator = new EntityId();
    private final EntityId recipient = new EntityId();
    private final List<AbstractDatumRecord> records;

    private int reliabilityService = 0; // 8-bit enumeration
    private int timeInterval = 0; // 8-bit unsigned integer
    private int controlType = 0; // 8-bit enumeration
    private int padding = 0; // 8-bit unsigned integer
    private int originatorType = 0; // 16-bit enumeration
    private int recpientType = 0; // 16-bit enumeration
    private int requestId = 0; // 32-bit unsigned integer
    private int totalParts = 0; // 8-bit unsigned integer
    private int currentPart = 0; // 8-bit unsigned integer

    public ApplicationControlPDU() {

        this.records = new ArrayList<AbstractDatumRecord>();
    }

    public EntityId getOriginator() { return this.originator; }
    public EntityId getRecipient() { return this.recipient; }
    public int getReliabilityService() { return this.reliabilityService; }
    public int getTimeInterval() { return this.timeInterval; }
    public int getControlType() { return this.controlType; }
    public int getPadding() { return padding; }
    public int getOriginatorApplicationType() { return this.originatorType; }
    public int getRecpientApplicationType() { return recpientType; }
    public int getRequestId() { return this.requestId; }
    public int getTotalParts() { return this.totalParts; }
    public int getCurrentPart() { return this.currentPart; }
    public int getNumberRecords() { return this.records.size(); }
    public List<AbstractDatumRecord> getRecords() { return this.records; }

    public void setRequestId(int id) {

        this.requestId = id;
    }

    public void setReliabilityService(int value) {

        this.reliabilityService = value;
    }

    public void setTimeInterval(int value) {

        this.timeInterval = value;
    }

    public void setControlType(int value) {

        this.controlType = value;
    }

    public void setPadding(int value) {

        this.padding = value;
    }

    public void setOriginatorApplicationType(int value) {

        this.originatorType = value;
    }

    public void setRecpientApplicationType(int value) {

        this.recpientType = value;
    }

    public void setTotalParts(int value) {

        this.totalParts = value;
    }

    public void setCurrentPart(int value) {

        this.currentPart = value;
    }

    @Override
    public void clear() {

        this.originator.clear();
        this.recipient.clear();
        this.records.clear();

        this.reliabilityService = 0;
        this.timeInterval = 0;
        this.controlType = 0;
        this.originatorType = 0;
        this.recpientType = 0;
        this.requestId = 0;
        this.totalParts = 0;
        this.currentPart = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("APPLICATION CONTROL");

        buffer.addAttribute(
            "Originator",
            this.originator.toString());
        buffer.addAttribute(
            "Recipient",
            this.recipient.toString());
        buffer.addAttribute(
            "Request Id",
            this.requestId);
        buffer.addAttribute(
            "Reliability Service",
            this.reliabilityService);
        buffer.addAttribute(
            "Time Interval",
            this.timeInterval);
        buffer.addAttribute(
            "Total Parts",
            this.totalParts);
        buffer.addAttribute(
            "Current Part",
            this.currentPart);
        buffer.addAttribute(
            "Control Type",
            this.controlType,
            VDIS.APP_CTRL_CONTROL_TYPE);
        buffer.addAttribute(
            "Originating Application Type",
            this.originatorType,
            VDIS.APP_CTRL_APPLICATION_TYPE);
        buffer.addAttribute(
            "Recipient Application Type",
            this.recpientType,
            VDIS.APP_CTRL_APPLICATION_TYPE);

        buffer.addBreak();
        buffer.addTitle("VARIABLE DATUM RECORDS (" + this.records.size() + ")");

        for(int i = 0, size = this.records.size(); i < size; ++i) {

            buffer.addBoldLabel("Record " + (i + 1));
            buffer.addBreak();
            buffer.addBuffer(this.records.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header) - 12

        this.originator.read(stream); // 6 bytes - 18
        this.recipient.read(stream); // 6 bytes - 24
        this.reliabilityService = stream.readUnsignedByte(); // 1 byte - 25
        this.timeInterval = stream.readUnsignedByte(); // 1 byte - 26
        this.controlType = stream.readUnsignedByte(); // 1 byte - 27
        this.padding = stream.readUnsignedByte(); // 1 byte padding - 28
        this.originatorType = stream.readUnsignedShort(); // 2 bytes - 30
        this.recpientType = stream.readUnsignedShort(); // 2 bytes - 32
        this.requestId = stream.readInt(); // 4 bytes - 36
        this.totalParts = stream.readUnsignedByte(); // 1 byte - 37
        this.currentPart = stream.readUnsignedByte(); // 1 byte - 38

        int count = stream.readUnsignedShort(); // 2 bytes - 40

        for(int i = 0; i < count; ++i) {

            AbstractDatumRecord record = null;

            record = DatumRecordFactory.getVariableRecord(stream);

            if (record != null) {

                this.records.add(record);
            }
        }
    }

    public byte[] write() throws IOException {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(array);

        this.write(stream);

        byte bytes[] = array.toByteArray();

        stream.close();

        return bytes;
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.getHeader().write(stream);

        this.originator.write(stream);
        this.recipient.write(stream);

        stream.writeByte(this.reliabilityService);
        stream.writeByte(this.timeInterval);
        stream.writeByte(this.controlType);
        stream.writeByte(this.padding);
        stream.writeShort(this.originatorType);
        stream.writeShort(this.recpientType);
        stream.writeInt(this.requestId);
        stream.writeByte(this.totalParts);
        stream.writeByte(this.currentPart);
        stream.writeShort(this.records.size());

        for(AbstractDatumRecord record : this.records) {

            if (record instanceof Writable) {

                Writable writable = (Writable)record;

                writable.write(stream);
            }
            else {

                logger.error(
                    "Variable Datum Record is not writable: {}",
                    record.getClass().getName());
            }
        }
    }
}
