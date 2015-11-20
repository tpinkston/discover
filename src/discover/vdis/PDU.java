package discover.vdis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.ByteArray;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.common.buffer.HypertextBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.Timestamp;
import discover.vdis.enums.PDU_FAMILY;
import discover.vdis.enums.PDU_TYPE;
import discover.vdis.marking.EntityMarking;
import discover.vdis.pdu.AbstractPDU;
import discover.vdis.pdu.Acknowledge;
import discover.vdis.pdu.ActionRequest;
import discover.vdis.pdu.ActionResponse;
import discover.vdis.pdu.ApplicationControlPDU;
import discover.vdis.pdu.CreateEntity;
import discover.vdis.pdu.Data;
import discover.vdis.pdu.DataQuery;
import discover.vdis.pdu.DefaultPDU;
import discover.vdis.pdu.Designator;
import discover.vdis.pdu.Detonation;
import discover.vdis.pdu.ElectromagneticEmission;
import discover.vdis.pdu.EntityState;
import discover.vdis.pdu.Fire;
import discover.vdis.pdu.IdentificationFriendOrFoe;
import discover.vdis.pdu.LinearObjectState;
import discover.vdis.pdu.PointObjectState;
import discover.vdis.pdu.Receiver;
import discover.vdis.pdu.RemoveEntity;
import discover.vdis.pdu.SetData;
import discover.vdis.pdu.Signal;
import discover.vdis.pdu.StartResume;
import discover.vdis.pdu.StopFreeze;
import discover.vdis.pdu.Transmitter;

/**
 * @author Tony Pinkston
 */
public class PDU implements Bufferable {

    protected static final DateFormat format = DateFormat.getDateTimeInstance();
    protected static final Logger logger = LoggerFactory.getLogger(PDU.class);

    private String source = null;
    private String title = null;
    private String initiator = null;
    private String timestamp = null;
    private int port = 0;
    private long time = 0;
    private byte data[] = null;
    private AbstractPDU pdu = null;
    private PDU_TYPE pduType = null;
    private PDU_FAMILY pduFamily = null;

    public PDU() {

    }

    public PDU(byte data[]) {

        this.data = data;

        if (data == null) {

            throw new NullPointerException();
        }
    }

    public AbstractPDU getPDU() {

        return pdu;
    }

    public void setPDU(AbstractPDU pdu) {

        this.pdu = pdu;
    }

    public String getSource() {

        return source;
    }

    public void setSource(String source) {

        this.source = source;
    }

    public String getTitle() {

        if ((title == null) || title.isEmpty()) {

            return getTypeEnum().description;
        }

        return title;
    }

    public String getInitiator() {

        return initiator;
    }

    public String getTimestamp() {

        return timestamp;
    }

    public int getPort() {

        return port;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public long getTime() {

        return time;
    }

    public void setTime(long time) {

        this.time = time;
    }

    public int getProtocol() {

        return ByteArray.get8bits(data, 0);
    }

    public void setProtocol(byte protocol) {

        ByteArray.set8Bits(data, 0, protocol);
    }

    public int getExercise() {

        if (data != null) {

            return ByteArray.get8bits(data, 1);
        }
        else {

            return 0;
        }
    }

    public void setExercise(byte exercise) {

        ByteArray.set8Bits(data, 1, exercise);
    }

    public int getType() {

        return ByteArray.get8bits(data, 2);
    }

    public PDU_TYPE getTypeEnum() {

        if (pduType == null) {

            pduType = PDU_TYPE.get(getType());
        }

        return pduType;
    }

    public void setType(byte type) {

        ByteArray.set8Bits(data, 2, type);
    }

    public void setTypeEnum(PDU_TYPE type) {

        pduType = type;
        setType((byte)type.value);
    }

    public int getFamily() {

        return ByteArray.get8bits(data, 3);
    }

    public PDU_FAMILY getFamilyEnum() {

        if (pduFamily == null) {

            pduFamily = PDU_FAMILY.get(getFamily());
        }

        return pduFamily;
    }

    public void setFamily(byte family) {

        ByteArray.set8Bits(data, 3, family);
    }

    public void setFamilyEnum(PDU_FAMILY family) {

        pduFamily = family;
        setFamily((byte)family.value);
    }

    public int getLength() {

        return ByteArray.get16bits(data, 8);
    }

    public void setLength(int length) {

        ByteArray.set16Bits(data, 8, length);
    }

    public int getStatus() {

        return ByteArray.get8bits(data, 10);
    }

    public void setStatus(byte status) {

        ByteArray.set8Bits(data, 10, status);
    }

    public byte[] getData() {

        return data;
    }

    public void setData(byte[] bytes) {

        data = bytes;
        setTitle();
        setInitiator();
        setTimestamp(true);
    }

    public int getDataLength() {

        return data.length;
    }

    public boolean hasEntityId() {

        if (data == null) {

            return false;
        }

        if ((getTypeEnum() == PDU_TYPE.ACKNOWLEDGE) ||
            (getTypeEnum() == PDU_TYPE.ACTION_REQUEST) ||
            (getTypeEnum() == PDU_TYPE.ACTION_RESPONSE) ||
            (getTypeEnum() == PDU_TYPE.APPLICATION_CTRL) ||
            (getTypeEnum() == PDU_TYPE.CREATE_ENTITY) ||
            (getTypeEnum() == PDU_TYPE.DESIGNATOR) ||
            (getTypeEnum() == PDU_TYPE.DETONATION) ||
            (getTypeEnum() == PDU_TYPE.FIRE) ||
            (getTypeEnum() == PDU_TYPE.EM_EMISSION) ||
            (getTypeEnum() == PDU_TYPE.IFF) ||
            (getTypeEnum() == PDU_TYPE.ENTITY_STATE) ||
            (getTypeEnum() == PDU_TYPE.TRANSMITTER) ||
            (getTypeEnum() == PDU_TYPE.RECEIVER) ||
            (getTypeEnum() == PDU_TYPE.SIGNAL) ||
            (getTypeEnum() == PDU_TYPE.DATA) ||
            (getTypeEnum() == PDU_TYPE.DATA_QUERY) ||
            (getTypeEnum() == PDU_TYPE.SET_DATA) ||
            (getTypeEnum() == PDU_TYPE.START_RESUME) ||
            (getTypeEnum() == PDU_TYPE.STOP_FREEZE) ||
            (getTypeEnum() == PDU_TYPE.POINT_OBJECT_STATE)) {

            return true;
        }

        return false;
    }

    public EntityId getId() {

        if (!hasEntityId()) {

            return null;
        }
        else {

            EntityId id = new EntityId();

            getEntityId(id);

            return id;
        }
    }

    public void getEntityId(EntityId entityId) {

        if (hasEntityId()) {

            entityId.set(getSiteId(), getApplicationId(), getEntityId());
        }
    }

    public int getSiteId() {

        if ((data != null) && hasEntityId()) {

            return ByteArray.get16bits(data, 12);
        }

        return 0xFFFF;
    }

    public int getApplicationId() {

        if ((data != null) && hasEntityId()) {

            return ByteArray.get16bits(data, 14);
        }

        return 0xFFFF;
    }

    public int getEntityId() {

        if ((data != null) && hasEntityId()) {

            return ByteArray.get16bits(data, 16);
        }

        return 0xFFFF;
    }

    public boolean hasRecipient() {

        if ((getTypeEnum() == PDU_TYPE.FIRE) ||
            (getTypeEnum() == PDU_TYPE.DETONATION) ||
            (getTypeEnum() == PDU_TYPE.APPEARANCE)) {

            return true;
        }

        return hasRequestId();
    }

    public EntityId getRecipient() {

        if (!hasRecipient()) {

            return null;
        }
        else {

            EntityId id = new EntityId();

            getRecepient(id);

            return id;
        }
    }

    public void getRecepient(EntityId id) {

        if (hasRecipient()) {

            id.setSite(ByteArray.get16bits(data, 18));
            id.setApplication(ByteArray.get16bits(data, 20));
            id.setEntity(ByteArray.get16bits(data, 22));
        }
    }

    public boolean hasInitiator() {

        return (initiator != null);
    }

    public void setInitiator() {

        if (!hasEntityId()) {

            initiator = null;
        }
        else {

            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append(getSiteId());
            builder.append(", ");
            builder.append(getApplicationId());
            builder.append(", ");
            builder.append(getEntityId());
            builder.append(")");

            initiator = builder.toString();
        }
    }

    public void setTimestamp(boolean force) {

        if (force || (timestamp == null)) {

            Timestamp timestamp = new Timestamp(ByteArray.get32bits(
                data,
                4));

            this.timestamp = timestamp.toString();
        }
    }

    public boolean hasEntityType() {

        if ((getTypeEnum() == PDU_TYPE.FIRE) ||
            (getTypeEnum() == PDU_TYPE.DETONATION) ||
            (getTypeEnum() == PDU_TYPE.ENTITY_STATE) ||
            (getTypeEnum() == PDU_TYPE.TRANSMITTER)) {

            return true;
        }

        return false;
    }

    public long getEntityType() {

        if ((getTypeEnum() == PDU_TYPE.ENTITY_STATE) ||
            (getTypeEnum() == PDU_TYPE.TRANSMITTER)) {

            return ByteArray.get64bits(data, 20);
        }
        else if (getTypeEnum() == PDU_TYPE.FIRE) {

            return ByteArray.get64bits(data, 64);
        }
        else if (getTypeEnum() == PDU_TYPE.DETONATION) {

            return ByteArray.get64bits(data, 72);
        }

        return 0x0;
    }

    public int getEntityKind() {

        if ((getTypeEnum() == PDU_TYPE.ENTITY_STATE) ||
            (getTypeEnum() == PDU_TYPE.TRANSMITTER)) {

            return ByteArray.get8bits(data, 20);
        }
        else if (getTypeEnum() == PDU_TYPE.FIRE) {

            return ByteArray.get8bits(data, 64);
        }
        else if (getTypeEnum() == PDU_TYPE.DETONATION) {

            return ByteArray.get8bits(data, 72);
        }

        return 0x0;
    }

    public int getEntityDomain() {

        if ((getTypeEnum() == PDU_TYPE.ENTITY_STATE) ||
            (getTypeEnum() == PDU_TYPE.TRANSMITTER)) {

            return ByteArray.get8bits(data, 21);
        }
        else if (getTypeEnum() == PDU_TYPE.FIRE) {

            return ByteArray.get8bits(data, 65);
        }
        else if (getTypeEnum() == PDU_TYPE.DETONATION) {

            return ByteArray.get8bits(data, 73);
        }

        return 0x0;
    }

    public boolean hasMarking() {

        return (getTypeEnum() == PDU_TYPE.ENTITY_STATE);
    }

    public String getMarking() {

        if (!hasMarking()) {

            return null;
        }
        else {

            EntityMarking marking = new EntityMarking();

            marking.read(data, 128);

            return marking.getMarking();
        }
    }

    public boolean hasRequestId() {

        if ((getTypeEnum() == PDU_TYPE.ACKNOWLEDGE) ||
            (getTypeEnum() == PDU_TYPE.ACTION_REQUEST) ||
            (getTypeEnum() == PDU_TYPE.ACTION_RESPONSE) ||
            (getTypeEnum() == PDU_TYPE.APPLICATION_CTRL) ||
            (getTypeEnum() == PDU_TYPE.CREATE_ENTITY) ||
            (getTypeEnum() == PDU_TYPE.DATA) ||
            (getTypeEnum() == PDU_TYPE.DATA_QUERY) ||
            (getTypeEnum() == PDU_TYPE.SET_DATA) ||
            (getTypeEnum() == PDU_TYPE.START_RESUME) ||
            (getTypeEnum() == PDU_TYPE.STOP_FREEZE)) {

            return true;
        }

        return false;
    }

    public int getRequestId() {

        if ((getTypeEnum() == PDU_TYPE.ACTION_REQUEST) ||
            (getTypeEnum() == PDU_TYPE.ACTION_RESPONSE) ||
            (getTypeEnum() == PDU_TYPE.CREATE_ENTITY) ||
            (getTypeEnum() == PDU_TYPE.DATA) ||
            (getTypeEnum() == PDU_TYPE.DATA_QUERY) ||
            (getTypeEnum() == PDU_TYPE.SET_DATA)) {

            return ByteArray.get32bits(data, 24);
        }
        else if (getTypeEnum() == PDU_TYPE.ACKNOWLEDGE) {

            return ByteArray.get32bits(data, 28);
        }
        else if (getTypeEnum() == PDU_TYPE.START_RESUME) {

            return ByteArray.get32bits(data, 40);
        }
        else if (getTypeEnum() == PDU_TYPE.STOP_FREEZE) {

            return ByteArray.get32bits(data, 36);
        }
        else if (getTypeEnum() == PDU_TYPE.APPLICATION_CTRL) {

            return ByteArray.get32bits(data, 32);
        }

        return 0;
    }

    public void setTitle() {

        StringBuilder builder = new StringBuilder();

        builder.append(getTypeEnum().description);

        if (hasRequestId()) {

            builder.append(" (");
            builder.append(getRequestId());
            builder.append(")");
        }

        title = builder.toString();
    }

    public PDU copy() {

        PDU pdu = new PDU(Arrays.copyOf(data, data.length));

        pdu.port = port;
        pdu.time = time;

        if (title != null) {

            pdu.title = new String(title);
        }

        if (source != null) {

            pdu.source = new String(source);
        }

        if (initiator != null) {

            pdu.initiator = new String(initiator);
        }

        if (timestamp != null) {

            pdu.timestamp = new String(timestamp);
        }

        return pdu;
    }

    public void decodeWithoutCatch(boolean force) throws IOException {

        if (force) {

            pdu = null;
        }

        if ((pdu == null) && (data != null)) {

            int type = getType();

            switch(type) {

                case 1: // PDU_TYPE_ENTITY_STATE
                    pdu = new EntityState();
                    break;
                case 2: // PDU_TYPE_FIRE
                    pdu = new Fire();
                    break;
                case 3: // PDU_TYPE_DETONATION
                    pdu = new Detonation();
                    break;
                case 11: // PDU_TYPE_CREATE_ENTITY
                    pdu = new CreateEntity();
                    break;
                case 12: // PDU_TYPE_REMOVE_ENTITY
                    pdu = new RemoveEntity();
                    break;
                case 13: // PDU_TYPE_START_RESUME
                    pdu = new StartResume();
                    break;
                case 14: // PDU_TYPE_STOP_FREEZE
                    pdu = new StopFreeze();
                    break;
                case 15: // PDU_TYPE_ACKNOWLEDGE
                    pdu = new Acknowledge();
                    break;
                case 16: // PDU_TYPE_ACTION_REQUEST
                    pdu = new ActionRequest();
                    break;
                case 17: // PDU_TYPE_ACTION_RESPONSE
                    pdu = new ActionResponse();
                    break;
                case 18: // PDU_TYPE_DATA_QUERY
                    pdu = new DataQuery();
                    break;
                case 19: // PDU_TYPE_SET_DATA
                    pdu = new SetData();
                    break;
                case 20: // PDU_TYPE_DATA
                    pdu = new Data();
                    break;
                case 23: // PDU_TYPE_EM_EMISSION
                    pdu = new ElectromagneticEmission();
                    break;
                case 24: // PDU_TYPE_DESIGNATOR
                    pdu = new Designator();
                    break;
                case 25: // PDU_TYPE_TRANSMITTER
                    pdu = new Transmitter();
                    break;
                case 26: // PDU_TYPE_SIGNAL
                    pdu = new Signal();
                    break;
                case 27: // PDU_TYPE_RECEIVER
                    pdu = new Receiver();
                    break;
                case 28: // PDU_TYPE_IFF
                    pdu = new IdentificationFriendOrFoe();
                    break;
                case 43: // PDU_TYPE_POINT_OBJECT_STATE
                    pdu = new  PointObjectState();
                    break;
                case 44: // PDU_TYPE_LINEAR_OBJECT_STATE
                    pdu = new LinearObjectState();
                    break;
                case 200: // PDU_TYPE_APPLICATION_CTRL
                    pdu = new ApplicationControlPDU();
                    break;
                default:
                    pdu = new DefaultPDU();
            }

            ByteArrayInputStream array = new ByteArrayInputStream(data);
            DataInputStream stream = new DataInputStream(array);

            pdu.read(stream);

            if (stream.available() > 0) {

                logger.warn(
                    "Post read bytes available is {} for {}",
                    stream.available(),
                    getClass().getSimpleName());
            }

            stream.close();
        }
    }

    public void decode(boolean force) {

        try {

            decodeWithoutCatch(force);
        }
        catch(IOException exception) {

            logger.error("Caught exception!", exception);
        }
    }

    public boolean encode() {

        boolean encoded = false;

        if (pdu instanceof Writable) {

            Writable writable = (Writable)pdu;

            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(array);

            try {

                writable.write(stream);

                stream.close();

                encoded = true;

                setData(array.toByteArray());

                logger.debug("Encoded {} bytes", getDataLength());
            }
            catch(IOException exception) {

                logger.error("Caught exception!", exception);
            }

        }

        return encoded;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        decode(false);

        if (!buffer.isHTML()) {

            buffer.addThickSeparator(null);
            buffer.addAttribute("LENGTH", getLength());
            buffer.addAttribute("PORT", port);
            buffer.addAttribute("SOURCE", source);
            buffer.addAttribute("TIME", format.format(time));
            buffer.addThickSeparator(null);
        }
        else {

            HypertextBuffer hypertext = (HypertextBuffer)buffer;
            HypertextBuffer preface = new HypertextBuffer();

            preface.addBoldAttribute("LENGTH", getLength());
            preface.addBoldAttribute("PORT", port);
            preface.addBoldAttribute("SOURCE", source);
            preface.addBoldAttribute("TIME", format.format(time));

            String data[][] = new String[1][1];
            data[0][0] = preface.toString();

            hypertext.addTable(1, 0, 1, 1, data);
        }

        buffer.addBreak();
        buffer.addBuffer(pdu);
    }

    public void save(DataOutputStream stream) throws IOException {

        stream.writeUTF(source);
        stream.writeInt(port);
        stream.writeLong(time);
        stream.writeInt(data.length);
        stream.write(data, 0, data.length);
    }

    public static PDU create(DataInputStream stream) throws IOException {

        PDU pdu = new PDU();

        pdu.source = stream.readUTF();
        pdu.port = stream.readInt();
        pdu.time = stream.readLong();
        pdu.data = new byte[stream.readInt()];

        stream.read(pdu.data, 0, pdu.data.length);

        pdu.setTitle();
        pdu.setInitiator();
        pdu.setTimestamp(false);

        return pdu;
    }

    private static boolean isValid(PDU pdu) {

        boolean valid = false;

        // Perform some heuristic checking on the PDU to make sure it's valid.

        PDU_TYPE type = pdu.getTypeEnum();
        PDU_FAMILY family = pdu.getFamilyEnum();


        if (family == PDU_FAMILY.ENTITY_INFORMATION_INTERACTION) {

            if ((type == PDU_TYPE.ENTITY_STATE) ||
                (type == PDU_TYPE.COLLISION) ||
                (type == PDU_TYPE.COLLISION_ELASTIC) ||
                (type == PDU_TYPE.ENTITY_STATE_UPDATE) ||
                (type == PDU_TYPE.ATTRIBUTE)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.WARFARE) {

            if ((type == PDU_TYPE.FIRE) ||
                (type == PDU_TYPE.DETONATION) ||
                (type == PDU_TYPE.DE_FIRE) ||
                (type == PDU_TYPE.ENTITY_DAMAGE_STATUS)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.LOGISTICS) {

            if ((type == PDU_TYPE.SERVICE_REQ) ||
                (type == PDU_TYPE.RESUPPLY_OFFER) ||
                (type == PDU_TYPE.RESUPPLY_RECEIVED) ||
                (type == PDU_TYPE.RESUPPLY_CANCEL) ||
                (type == PDU_TYPE.REPAIR_COMPLETE) ||
                (type == PDU_TYPE.REPAIR_RESPONSE)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.SIMULATION_MANAGEMENT) {

            if ((type == PDU_TYPE.CREATE_ENTITY) ||
                (type == PDU_TYPE.REMOVE_ENTITY) ||
                (type == PDU_TYPE.START_RESUME) ||
                (type == PDU_TYPE.STOP_FREEZE) ||
                (type == PDU_TYPE.ACKNOWLEDGE) ||
                (type == PDU_TYPE.ACTION_REQUEST) ||
                (type == PDU_TYPE.ACTION_RESPONSE) ||
                (type == PDU_TYPE.DATA_QUERY) ||
                (type == PDU_TYPE.SET_DATA) ||
                (type == PDU_TYPE.DATA) ||
                (type == PDU_TYPE.EVENT_REPORT) ||
                (type == PDU_TYPE.COMMENT)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.DISTRIBUTED_EMISSION_REGENERATION) {

            if ((type == PDU_TYPE.EM_EMISSION) ||
                (type == PDU_TYPE.DESIGNATOR) ||
                (type == PDU_TYPE.UNDERWATER_ACOUSTIC) ||
                (type == PDU_TYPE.IFF) ||
                (type == PDU_TYPE.SEES)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.RADIO_COMMUNICATION) {

            if ((type == PDU_TYPE.TRANSMITTER) ||
                (type == PDU_TYPE.SIGNAL) ||
                (type == PDU_TYPE.RECEIVER) ||
                (type == PDU_TYPE.INTERCOM_SIGNAL) ||
                (type == PDU_TYPE.INTERCOM_CONTROL)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.ENTITY_MANAGEMENT) {

            if ((type == PDU_TYPE.AGGREGATE_STATE) ||
                (type == PDU_TYPE.ISGROUPOF) ||
                (type == PDU_TYPE.TRANSFER_OWNERSHIP) ||
                (type == PDU_TYPE.ISPARTOF)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.MINEFIELD) {

            if ((type == PDU_TYPE.MINEFIELD_STATE) ||
                (type == PDU_TYPE.MINEFIELD_QUERY) ||
                (type == PDU_TYPE.MINEFIELD_DATA) ||
                (type == PDU_TYPE.MINEFIELD_RESPONSE_NAK)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.SYNTHETIC_ENVIRONMENT) {

            if ((type == PDU_TYPE.ENVIRONMENTAL_PROCESS) ||
                (type == PDU_TYPE.GRIDDED_DATA) ||
                (type == PDU_TYPE.POINT_OBJECT_STATE) ||
                (type == PDU_TYPE.LINEAR_OBJECT_STATE) ||
                (type == PDU_TYPE.AREAL_OBJECT_STATE)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.SIMULATION_MANAGEMENT_WITH_RELIABILITY) {

            if ((type == PDU_TYPE.CREATE_ENTITY_R) ||
                (type == PDU_TYPE.REMOVE_ENTITY_R) ||
                (type == PDU_TYPE.START_RESUME_R) ||
                (type == PDU_TYPE.STOP_FREEZE_R) ||
                (type == PDU_TYPE.ACKNOWLEDGE_R) ||
                (type == PDU_TYPE.ACTION_REQUEST_R) ||
                (type == PDU_TYPE.ACTION_RESPONSE_R) ||
                (type == PDU_TYPE.DATA_QUERY_R) ||
                (type == PDU_TYPE.SET_DATA_R) ||
                (type == PDU_TYPE.DATA_R) ||
                (type == PDU_TYPE.EVENT_REPORT_R) ||
                (type == PDU_TYPE.COMMENT_R) ||
                (type == PDU_TYPE.RECORD_QUERY_R) ||
                (type == PDU_TYPE.SET_RECORD_R) ||
                (type == PDU_TYPE.RECORD_R)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.INFO_OPS) {

            if ((type == PDU_TYPE.INFO_OPS_ACTION) ||
                (type == PDU_TYPE.INFO_OPS_REPORT)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.LIVE_ENTITY) {

            if ((type == PDU_TYPE.TSPI) ||
                (type == PDU_TYPE.APPEARANCE) ||
                (type == PDU_TYPE.ARTICULATED_PARTS) ||
                (type == PDU_TYPE.LE_FIRE) ||
                (type == PDU_TYPE.LE_DETONATION)) {

                valid = true;
            }
        }
        else if (family == PDU_FAMILY.NON_REAL_TIME) {

            // Do nothing...
        }

        return valid;
    }

    public static PDU createFromPCAP(String source, int port, long time, DataInputStream stream) throws IOException {

        PDU pdu = new PDU();

        pdu.source = source;
        pdu.port = port;
        pdu.time = time;
        pdu.data = new byte[stream.available()];

        stream.read(pdu.data, 0, pdu.data.length);

        if (!isValid(pdu)) {

            return null;
        }

        pdu.setTitle();
        pdu.setInitiator();
        pdu.setTimestamp(true);

        return pdu;
    }

    public static boolean isByteEditable(int type, int index) {

        // PDU header checks
        switch(index) {

            case 2: // PDU type
            case 3: // PDU family
            case 8: // PDU length (1 of 2)
            case 9: // PDU length (2 of 2)
                return false;
        }

        if (type == PDU_TYPE.ENTITY_STATE.value) {

            switch(index) {
                case 19: // VPRecord count
                    return false;
            }
        }

        return true;
    }
}
