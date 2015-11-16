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
import discover.vdis.enums.APP_CTRL_APPLICATION_TYPE;
import discover.vdis.enums.APP_CTRL_CONTROL_TYPE;

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

        records = new ArrayList<AbstractDatumRecord>();
    }

    public EntityId getOriginator() { return originator; }
    public EntityId getRecipient() { return recipient; }
    public int getReliabilityService() { return reliabilityService; }
    public int getTimeInterval() { return timeInterval; }
    public int getControlType() { return controlType; }
    public int getPadding() { return padding; }
    public int getOriginatorApplicationType() { return originatorType; }
    public int getRecpientApplicationType() { return recpientType; }
    public int getRequestId() { return requestId; }
    public int getTotalParts() { return totalParts; }
    public int getCurrentPart() { return currentPart; }
    public int getNumberRecords() { return records.size(); }
    public List<AbstractDatumRecord> getRecords() { return records; }

    public void setRequestId(int id) {

        requestId = id;
    }

    public void setReliabilityService(int value) {

        reliabilityService = value;
    }

    public void setTimeInterval(int value) {

        timeInterval = value;
    }

    public void setControlType(int value) {

        controlType = value;
    }

    public void setPadding(int value) {

        padding = value;
    }

    public void setOriginatorApplicationType(int value) {

        originatorType = value;
    }

    public void setRecpientApplicationType(int value) {

        recpientType = value;
    }

    public void setTotalParts(int value) {

        totalParts = value;
    }

    public void setCurrentPart(int value) {

        currentPart = value;
    }

    @Override
    public void clear() {

        originator.clear();
        recipient.clear();
        records.clear();

        reliabilityService = 0;
        timeInterval = 0;
        controlType = 0;
        originatorType = 0;
        recpientType = 0;
        requestId = 0;
        totalParts = 0;
        currentPart = 0;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("APPLICATION CONTROL");

        buffer.addAttribute(
            "Originator",
            originator.toString());
        buffer.addAttribute(
            "Recipient",
            recipient.toString());
        buffer.addAttribute(
            "Request Id",
            requestId);
        buffer.addAttribute(
            "Reliability Service",
            reliabilityService);
        buffer.addAttribute(
            "Time Interval",
            timeInterval);
        buffer.addAttribute(
            "Total Parts",
            totalParts);
        buffer.addAttribute(
            "Current Part",
            currentPart);
        buffer.addAttribute(
            "Control Type",
            controlType,
            APP_CTRL_CONTROL_TYPE.class);
        buffer.addAttribute(
            "Originating Application Type",
            originatorType,
            APP_CTRL_APPLICATION_TYPE.class);
        buffer.addAttribute(
            "Recipient Application Type",
            recpientType,
            APP_CTRL_APPLICATION_TYPE.class);

        buffer.addBreak();
        buffer.addTitle("VARIABLE DATUM RECORDS (" + records.size() + ")");

        for(int i = 0, size = records.size(); i < size; ++i) {

            buffer.addBoldLabel("Record " + (i + 1));
            buffer.addBreak();
            buffer.addBuffer(records.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header) - 12

        originator.read(stream); // 6 bytes - 18
        recipient.read(stream); // 6 bytes - 24
        reliabilityService = stream.readUnsignedByte(); // 1 byte - 25
        timeInterval = stream.readUnsignedByte(); // 1 byte - 26
        controlType = stream.readUnsignedByte(); // 1 byte - 27
        padding = stream.readUnsignedByte(); // 1 byte padding - 28
        originatorType = stream.readUnsignedShort(); // 2 bytes - 30
        recpientType = stream.readUnsignedShort(); // 2 bytes - 32
        requestId = stream.readInt(); // 4 bytes - 36
        totalParts = stream.readUnsignedByte(); // 1 byte - 37
        currentPart = stream.readUnsignedByte(); // 1 byte - 38

        int count = stream.readUnsignedShort(); // 2 bytes - 40

        for(int i = 0; i < count; ++i) {

            AbstractDatumRecord record = null;

            record = DatumRecordFactory.getVariableRecord(stream);

            if (record != null) {

                records.add(record);
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

        getHeader().write(stream);

        originator.write(stream);
        recipient.write(stream);

        stream.writeByte(reliabilityService);
        stream.writeByte(timeInterval);
        stream.writeByte(controlType);
        stream.writeByte(padding);
        stream.writeShort(originatorType);
        stream.writeShort(recpientType);
        stream.writeInt(requestId);
        stream.writeByte(totalParts);
        stream.writeByte(currentPart);
        stream.writeShort(records.size());

        for(AbstractDatumRecord record : records) {

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
