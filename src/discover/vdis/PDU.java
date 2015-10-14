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
import discover.vdis.enums.VDIS;
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

            return VDIS.getDescription(VDIS.PDU_TYPE, getType());
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

    public void setType(byte type) {

        ByteArray.set8Bits(data, 2, type);
    }

    public int getFamily() {

        return ByteArray.get8bits(data, 3);
    }

    public void setFamily(byte family) {

        ByteArray.set8Bits(data, 3, family);
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
        else switch(getType()) {

            case VDIS.PDU_TYPE_ACKNOWLEDGE:
            case VDIS.PDU_TYPE_ACTION_REQUEST:
            case VDIS.PDU_TYPE_ACTION_RESPONSE:
            case VDIS.PDU_TYPE_APPLICATION_CTRL:
            case VDIS.PDU_TYPE_CREATE_ENTITY:
            case VDIS.PDU_TYPE_DESIGNATOR:
            case VDIS.PDU_TYPE_DETONATION:
            case VDIS.PDU_TYPE_FIRE:
            case VDIS.PDU_TYPE_EM_EMISSION:
            case VDIS.PDU_TYPE_IFF:
            case VDIS.PDU_TYPE_ENTITY_STATE:
            case VDIS.PDU_TYPE_TRANSMITTER:
            case VDIS.PDU_TYPE_RECEIVER:
            case VDIS.PDU_TYPE_SIGNAL:
            case VDIS.PDU_TYPE_DATA:
            case VDIS.PDU_TYPE_DATA_QUERY:
            case VDIS.PDU_TYPE_SET_DATA:
            case VDIS.PDU_TYPE_START_RESUME:
            case VDIS.PDU_TYPE_STOP_FREEZE:
            case VDIS.PDU_TYPE_POINT_OBJECT_STATE:
                return true;
            default:
                return false;
        }
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

        if ((getType() == VDIS.PDU_TYPE_FIRE) ||
            (getType() == VDIS.PDU_TYPE_DETONATION) ||
            (getType() == VDIS.PDU_TYPE_APPEARANCE)) {

            return true;
        }
        else {

            return hasRequestId();
        }
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

        switch(getType()) {

            case VDIS.PDU_TYPE_DETONATION:
            case VDIS.PDU_TYPE_FIRE:
            case VDIS.PDU_TYPE_ENTITY_STATE:
            case VDIS.PDU_TYPE_TRANSMITTER:
                return true;
            default:
                return false;
        }
    }

    public long getEntityType() {

        switch(getType()) {

            case VDIS.PDU_TYPE_DETONATION:
                return ByteArray.get64bits(data, 72);
            case VDIS.PDU_TYPE_FIRE:
                return ByteArray.get64bits(data, 64);
            case VDIS.PDU_TYPE_ENTITY_STATE:
            case VDIS.PDU_TYPE_TRANSMITTER:
                return ByteArray.get64bits(data, 20);
            default:
                return 0x0;
        }
    }

    public int getEntityKind() {

        switch(getType()) {

            case VDIS.PDU_TYPE_DETONATION:
                return ByteArray.get8bits(data, 72);
            case VDIS.PDU_TYPE_FIRE:
                return ByteArray.get8bits(data, 64);
            case VDIS.PDU_TYPE_ENTITY_STATE:
            case VDIS.PDU_TYPE_TRANSMITTER:
                return ByteArray.get8bits(data, 20);
            default:
                return 0x0;
        }
    }

    public int getEntityDomain() {

        switch(getType()) {

            case VDIS.PDU_TYPE_DETONATION:
                return ByteArray.get8bits(data, 73);
            case VDIS.PDU_TYPE_FIRE:
                return ByteArray.get8bits(data, 65);
            case VDIS.PDU_TYPE_ENTITY_STATE:
            case VDIS.PDU_TYPE_TRANSMITTER:
                return ByteArray.get8bits(data, 21);
            default:
                return 0x0;
        }
    }

    public boolean hasMarking() {

        return (getType() == VDIS.PDU_TYPE_ENTITY_STATE);
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

        switch(getType()) {

            case VDIS.PDU_TYPE_ACKNOWLEDGE:
            case VDIS.PDU_TYPE_ACTION_REQUEST:
            case VDIS.PDU_TYPE_ACTION_RESPONSE:
            case VDIS.PDU_TYPE_CREATE_ENTITY:
            case VDIS.PDU_TYPE_DATA:
            case VDIS.PDU_TYPE_DATA_QUERY:
            case VDIS.PDU_TYPE_SET_DATA:
            case VDIS.PDU_TYPE_START_RESUME:
            case VDIS.PDU_TYPE_STOP_FREEZE:
            case VDIS.PDU_TYPE_APPLICATION_CTRL:
                return true;
            default:
                return false;
        }
    }

    public int getRequestId() {

        switch(getType()) {

            case VDIS.PDU_TYPE_ACTION_REQUEST:
            case VDIS.PDU_TYPE_ACTION_RESPONSE:
            case VDIS.PDU_TYPE_CREATE_ENTITY:
            case VDIS.PDU_TYPE_DATA:
            case VDIS.PDU_TYPE_DATA_QUERY:
            case VDIS.PDU_TYPE_SET_DATA:
                return ByteArray.get32bits(data, 24);
            case VDIS.PDU_TYPE_ACKNOWLEDGE:
                return ByteArray.get32bits(data, 28);
            case VDIS.PDU_TYPE_STOP_FREEZE:
                return ByteArray.get32bits(data, 36);
            case VDIS.PDU_TYPE_START_RESUME:
                return ByteArray.get32bits(data, 40);
            case VDIS.PDU_TYPE_APPLICATION_CTRL:
                return ByteArray.get32bits(data, 32);
            default:
                return 0;
        }
    }

    public void setTitle() {

        StringBuilder builder = new StringBuilder();

        builder.append(VDIS.getDescription(VDIS.PDU_TYPE, getType()));

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

        int pduType = pdu.getType();
        int protocolFamily = pdu.getFamily();

        switch(protocolFamily) {

            case 1:  // Entity Information/Interaction

                switch(pduType) {

                    case VDIS.PDU_TYPE_ENTITY_STATE:
                    case VDIS.PDU_TYPE_COLLISION:
                    case VDIS.PDU_TYPE_COLLISION_ELASTIC:
                    case VDIS.PDU_TYPE_ENTITY_STATE_UPDATE:
                    case VDIS.PDU_TYPE_ATTRIBUTE:
                        valid = true;

                    break;
                }
                break;

            case 2:  // Warfare

                switch(pduType) {

                    case VDIS.PDU_TYPE_FIRE:
                    case VDIS.PDU_TYPE_DETONATION:
                    case VDIS.PDU_TYPE_DE_FIRE:
                    case VDIS.PDU_TYPE_ENTITY_DAMAGE_STATUS:
                        valid = true;

                    break;
                }
                break;

            case 3:  // Logistics

                switch(pduType) {

                    case VDIS.PDU_TYPE_SERVICE_REQ:
                    case VDIS.PDU_TYPE_RESUPPLY_OFFER:
                    case VDIS.PDU_TYPE_RESUPPLY_RECEIVED:
                    case VDIS.PDU_TYPE_RESUPPLY_CANCEL:
                    case VDIS.PDU_TYPE_REPAIR_COMPLETE:
                    case VDIS.PDU_TYPE_REPAIR_RESPONSE:
                        valid = true;

                    break;
                }
                break;

            case 5:  // Simulation Management

                switch(pduType) {

                    case VDIS.PDU_TYPE_CREATE_ENTITY:
                    case VDIS.PDU_TYPE_REMOVE_ENTITY:
                    case VDIS.PDU_TYPE_START_RESUME:
                    case VDIS.PDU_TYPE_STOP_FREEZE:
                    case VDIS.PDU_TYPE_ACKNOWLEDGE:
                    case VDIS.PDU_TYPE_ACTION_REQUEST:
                    case VDIS.PDU_TYPE_ACTION_RESPONSE:
                    case VDIS.PDU_TYPE_DATA_QUERY:
                    case VDIS.PDU_TYPE_SET_DATA:
                    case VDIS.PDU_TYPE_DATA:
                    case VDIS.PDU_TYPE_EVENT_REPORT:
                    case VDIS.PDU_TYPE_COMMENT:
                        valid = true;

                    break;
                }
                break;

            case 6:  // Distributed Emission Regeneration

                switch(pduType) {

                    case VDIS.PDU_TYPE_EM_EMISSION:
                    case VDIS.PDU_TYPE_DESIGNATOR:
                    case VDIS.PDU_TYPE_UNDERWATER_ACOUSTIC:
                    case VDIS.PDU_TYPE_IFF:
                    case VDIS.PDU_TYPE_SEES:
                        valid = true;

                    break;
                }
                break;

            case 4:  // Radio Communications

                switch(pduType) {

                    case VDIS.PDU_TYPE_TRANSMITTER:
                    case VDIS.PDU_TYPE_SIGNAL:
                    case VDIS.PDU_TYPE_RECEIVER:
                    case VDIS.PDU_TYPE_INTERCOM_SIGNAL:
                    case VDIS.PDU_TYPE_INTERCOM_CONTROL:
                        valid = true;

                    break;
                }
                break;

            case 7:  // Entity Management

                switch(pduType) {

                    case VDIS.PDU_TYPE_AGGREGATE_STATE:
                    case VDIS.PDU_TYPE_ISGROUPOF:
                    case VDIS.PDU_TYPE_TRANSFER_OWNERSHIP:
                    case VDIS.PDU_TYPE_ISPARTOF:
                        valid = true;

                    break;
                }
                break;

            case 8:  // Minefield

                switch(pduType) {

                    case VDIS.PDU_TYPE_MINEFIELD_STATE:
                    case VDIS.PDU_TYPE_MINEFIELD_QUERY:
                    case VDIS.PDU_TYPE_MINEFIELD_DATA:
                    case VDIS.PDU_TYPE_MINEFIELD_RESPONSE_NAK:
                        valid = true;

                    break;
                }
                break;

            case 9:  // Synthetic Environment

                switch(pduType) {

                    case VDIS.PDU_TYPE_ENVIRONMENTAL_PROCESS:
                    case VDIS.PDU_TYPE_GRIDDED_DATA:
                    case VDIS.PDU_TYPE_POINT_OBJECT_STATE:
                    case VDIS.PDU_TYPE_LINEAR_OBJECT_STATE:
                    case VDIS.PDU_TYPE_AREAL_OBJECT_STATE:
                        valid = true;

                    break;
                }
                break;

            case 10:  // Simulation Management with Reliability

                switch(pduType) {

                    case VDIS.PDU_TYPE_CREATE_ENTITY_R:
                    case VDIS.PDU_TYPE_REMOVE_ENTITY_R:
                    case VDIS.PDU_TYPE_START_RESUME_R:
                    case VDIS.PDU_TYPE_STOP_FREEZE_R:
                    case VDIS.PDU_TYPE_ACKNOWLEDGE_R:
                    case VDIS.PDU_TYPE_ACTION_REQUEST_R:
                    case VDIS.PDU_TYPE_ACTION_RESPONSE_R:
                    case VDIS.PDU_TYPE_DATA_QUERY_R:
                    case VDIS.PDU_TYPE_SET_DATA_R:
                    case VDIS.PDU_TYPE_DATA_R:
                    case VDIS.PDU_TYPE_EVENT_REPORT_R:
                    case VDIS.PDU_TYPE_COMMENT_R:
                    case VDIS.PDU_TYPE_RECORD_QUERY_R:
                    case VDIS.PDU_TYPE_SET_RECORD_R:
                    case VDIS.PDU_TYPE_RECORD_R:
                        valid = true;

                    break;
                }
                break;

            case 13:  // Information Operations

                switch(pduType) {

                    case VDIS.PDU_TYPE_INFO_OPS_ACTION:
                    case VDIS.PDU_TYPE_INFO_OPS_REPORT:
                        valid = true;

                    break;
                }
                break;

            case 11:  // Live Entity (LE)

                switch(pduType) {

                    case VDIS.PDU_TYPE_TSPI:
                    case VDIS.PDU_TYPE_APPEARANCE:
                    case VDIS.PDU_TYPE_ARTICULATED_PARTS:
                    case VDIS.PDU_TYPE_LE_FIRE:
                    case VDIS.PDU_TYPE_LE_DETONATION:
                        valid = true;

                    break;
                }
                break;

            case 12:  // Non-Real-Time Protocol

                switch(pduType) {

                    default:
                    break;
                }
                break;
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

        if (type == 1) {

            // PDU_TYPE_ENTITY_STATE
            switch(index) {
                case 19: // VPRecord count
                    return false;
            }
        }

        return true;
    }
}
