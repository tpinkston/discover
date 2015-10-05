/**
 * @author Tony Pinkston
 */
package discover.vdis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import discover.Discover;
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

public class PDU implements Bufferable {
    
    protected static final DateFormat format = DateFormat.getDateTimeInstance();

    private static Logger logger = Discover.getLogger();
    
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
        
        if (this.data == null) {
            
            throw new NullPointerException();
        }
    }
    
    public AbstractPDU getPDU() {
        
        return this.pdu;
    }
    
    public void setPDU(AbstractPDU pdu) {
        
        this.pdu = pdu;
    }

    public String getSource() {
        
        return this.source;
    }
    
    public void setSource(String source) {
        
        this.source = source;
    }
    
    public String getTitle() {
        
        if ((this.title == null) || this.title.isEmpty()) {
        
            return VDIS.getDescription(VDIS.PDU_TYPE, this.getType());
        }
        
        return this.title;
    }
    
    public String getInitiator() {
        
        return this.initiator;
    }
    
    public String getTimestamp() {
        
        return this.timestamp;
    }

    public int getPort() {
        
        return this.port;
    }
    
    public void setPort(int port) {
        
        this.port = port;
    }
    
    public long getTime() {
        
        return this.time;
    }
    
    public void setTime(long time) {
        
        this.time = time;
    }
    
    public int getProtocol() {
        
        return ByteArray.get8bits(this.data, 0);
    }
    
    public void setProtocol(byte protocol) {
        
        ByteArray.set8Bits(this.data, 0, protocol);
    }
    
    public int getExercise() {
        
        if (this.data != null) {
            
            return ByteArray.get8bits(this.data, 1);
        }
        else {
            
            return 0;
        }
    }
    
    public void setExercise(byte exercise) {
        
        ByteArray.set8Bits(this.data, 1, exercise);
    }
    
    public int getType() {
        
        return ByteArray.get8bits(this.data, 2);
    }
    
    public void setType(byte type) {
        
        ByteArray.set8Bits(this.data, 2, type);
    }
    
    public int getFamily() {
        
        return ByteArray.get8bits(this.data, 3);
    }
    
    public void setFamily(byte family) {
        
        ByteArray.set8Bits(this.data, 3, family);
    }
    
    public int getLength() {
        
        return ByteArray.get16bits(this.data, 8);
    }
    
    public void setLength(int length) {
        
        ByteArray.set16Bits(this.data, 8, length);
    }
    
    public int getStatus() {
        
        return ByteArray.get8bits(this.data, 10);
    }
    
    public void setStatus(byte status) {
        
        ByteArray.set8Bits(this.data, 10, status);
    }
    
    public byte[] getData() {
        
        return this.data;
    }
    
    public void setData(byte[] bytes) {
        
        this.data = bytes;
        this.setTitle();
        this.setInitiator();
        this.setTimestamp(true);
    }
    
    public int getDataLength() {
        
        return this.data.length;
    }
    
    public boolean hasEntityId() {
        
        if (this.data == null) {
            
            return false;
        }
        else switch(this.getType()) {
            
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
        
        if (!this.hasEntityId()) {
            
            return null;
        }
        else {
            
            EntityId id = new EntityId();
            
            this.getEntityId(id);
            
            return id;
        }
    }
    
    public void getEntityId(EntityId entityId) {
        
        if (this.hasEntityId()) {
            
            entityId.set(
                this.getSiteId(), 
                this.getApplicationId(), 
                this.getEntityId());
        }
    }
    
    public int getSiteId() {
        
        if ((this.data != null) && this.hasEntityId()) {
            
            return ByteArray.get16bits(this.data, 12);
        }
        
        return 0xFFFF;
    }
    
    public int getApplicationId() {
        
        if ((this.data != null) && this.hasEntityId()) {
            
            return ByteArray.get16bits(this.data, 14);
        }
        
        return 0xFFFF;
    }
    
    public int getEntityId() {
        
        if ((this.data != null) && this.hasEntityId()) {
            
            return ByteArray.get16bits(this.data, 16);
        }
        
        return 0xFFFF;
    }
    
    public boolean hasRecipient() {
        
        if ((this.getType() == VDIS.PDU_TYPE_FIRE) ||
            (this.getType() == VDIS.PDU_TYPE_DETONATION) ||
            (this.getType() == VDIS.PDU_TYPE_APPEARANCE)) {
            
            return true;
        }
        else {
            
            return this.hasRequestId();
        }
    }
    
    public EntityId getRecipient() {
        
        if (!this.hasRecipient()) {
            
            return null;
        }
        else {
            
            EntityId id = new EntityId();
            
            this.getRecepient(id);
            
            return id;
        }
    }
    
    public void getRecepient(EntityId id) {
        
        if (this.hasRecipient()) {
            
            id.setSite(ByteArray.get16bits(this.data, 18));
            id.setApplication(ByteArray.get16bits(this.data, 20));
            id.setEntity(ByteArray.get16bits(this.data, 22));
        }
    }
    
    public boolean hasInitiator() {
        
        return (this.initiator != null);
    }
    
    public void setInitiator() {
        
        if (!this.hasEntityId()) {
            
            this.initiator = null;
        }
        else {
            
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append(this.getSiteId());
            builder.append(", ");
            builder.append(this.getApplicationId());
            builder.append(", ");
            builder.append(this.getEntityId());
            builder.append(")");

            this.initiator = builder.toString();
        }
    }
    
    public void setTimestamp(boolean force) {
        
        if (force || (this.timestamp == null)) {

            Timestamp timestamp = new Timestamp(ByteArray.get32bits(
                this.data, 
                4));
            
            this.timestamp = timestamp.toString();
        }
    }
    
    public boolean hasEntityType() {
        
        switch(this.getType()) {

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
        
        switch(this.getType()) {

            case VDIS.PDU_TYPE_DETONATION:
                return ByteArray.get64bits(this.data, 72);
            case VDIS.PDU_TYPE_FIRE:
                return ByteArray.get64bits(this.data, 64);
            case VDIS.PDU_TYPE_ENTITY_STATE:
            case VDIS.PDU_TYPE_TRANSMITTER:
                return ByteArray.get64bits(this.data, 20);
            default:
                return 0x0;
        }
    }

    public int getEntityKind() {
        
        switch(this.getType()) {

            case VDIS.PDU_TYPE_DETONATION:
                return ByteArray.get8bits(this.data, 72);
            case VDIS.PDU_TYPE_FIRE:
                return ByteArray.get8bits(this.data, 64);
            case VDIS.PDU_TYPE_ENTITY_STATE:
            case VDIS.PDU_TYPE_TRANSMITTER:
                return ByteArray.get8bits(this.data, 20);
            default:
                return 0x0;
        }
    }

    public int getEntityDomain() {
        
        switch(this.getType()) {

            case VDIS.PDU_TYPE_DETONATION:
                return ByteArray.get8bits(this.data, 73);
            case VDIS.PDU_TYPE_FIRE:
                return ByteArray.get8bits(this.data, 65);
            case VDIS.PDU_TYPE_ENTITY_STATE:
            case VDIS.PDU_TYPE_TRANSMITTER:
                return ByteArray.get8bits(this.data, 21);
            default:
                return 0x0;
        }
    }
    
    public boolean hasMarking() {
        
        return (this.getType() == VDIS.PDU_TYPE_ENTITY_STATE);
    }
    
    public String getMarking() {
        
        if (!this.hasMarking()) {
            
            return null;
        }
        else {
            
            EntityMarking marking = new EntityMarking();
            
            marking.read(this.data, 128);
            
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
                return ByteArray.get32bits(this.data, 24);
            case VDIS.PDU_TYPE_ACKNOWLEDGE:
                return ByteArray.get32bits(this.data, 28);
            case VDIS.PDU_TYPE_STOP_FREEZE:
                return ByteArray.get32bits(this.data, 36);
            case VDIS.PDU_TYPE_START_RESUME:
                return ByteArray.get32bits(this.data, 40);
            case VDIS.PDU_TYPE_APPLICATION_CTRL:
                return ByteArray.get32bits(this.data, 32);
            default:
                return 0;
        }
    }
    
    public void setTitle() {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append(VDIS.getDescription(VDIS.PDU_TYPE, this.getType()));

        if (this.hasRequestId()) {
            
            builder.append(" [");
            builder.append(this.getRequestId());
            builder.append("]");
        }
        
        this.title = builder.toString();
    }
    
    public PDU copy() {
        
        PDU pdu = new PDU(Arrays.copyOf(this.data, this.data.length));

        pdu.port = this.port;
        pdu.time = this.time;
        
        if (this.title != null) {
            
            pdu.title = new String(this.title);
        }
        
        if (this.source != null) {
            
            pdu.source = new String(this.source);
        }
        
        if (this.initiator != null) {
            
            pdu.initiator = new String(this.initiator);
        }
        
        if (this.timestamp != null) {
            
            pdu.timestamp = new String(this.timestamp);
        }
        
        return pdu;
    }
    
    public void decodeWithoutCatch(boolean force) throws IOException {
        
        if (force) {
            
            this.pdu = null;
        }

        if ((this.pdu == null) && (this.data != null)) {

            int type = this.getType();
            
            switch(type) {

                case 1: // PDU_TYPE_ENTITY_STATE
                    this.pdu = new EntityState();
                    break;
                case 2: // PDU_TYPE_FIRE
                    this.pdu = new Fire();
                    break;
                case 3: // PDU_TYPE_DETONATION
                    this.pdu = new Detonation();
                    break;
                case 11: // PDU_TYPE_CREATE_ENTITY
                    this.pdu = new CreateEntity();
                    break;
                case 12: // PDU_TYPE_REMOVE_ENTITY
                    this.pdu = new RemoveEntity();
                    break;
                case 13: // PDU_TYPE_START_RESUME
                    this.pdu = new StartResume();
                    break;
                case 14: // PDU_TYPE_STOP_FREEZE
                    this.pdu = new StopFreeze();
                    break;
                case 15: // PDU_TYPE_ACKNOWLEDGE
                    this.pdu = new Acknowledge();
                    break;
                case 16: // PDU_TYPE_ACTION_REQUEST
                    this.pdu = new ActionRequest();
                    break;
                case 17: // PDU_TYPE_ACTION_RESPONSE
                    this.pdu = new ActionResponse();
                    break;
                case 18: // PDU_TYPE_DATA_QUERY
                    this.pdu = new DataQuery();
                    break;
                case 19: // PDU_TYPE_SET_DATA
                    this.pdu = new SetData();
                    break;
                case 20: // PDU_TYPE_DATA
                    this.pdu = new Data();
                    break;
                case 23: // PDU_TYPE_EM_EMISSION
                    this.pdu = new ElectromagneticEmission();
                    break;
                case 24: // PDU_TYPE_DESIGNATOR
                    this.pdu = new Designator();
                    break;
                case 25: // PDU_TYPE_TRANSMITTER
                    this.pdu = new Transmitter();
                    break;
                case 26: // PDU_TYPE_SIGNAL
                    this.pdu = new Signal();
                    break;
                case 27: // PDU_TYPE_RECEIVER
                    this.pdu = new Receiver();
                    break;
                case 28: // PDU_TYPE_IFF
                    this.pdu = new IdentificationFriendOrFoe();
                    break;
                case 43: // PDU_TYPE_POINT_OBJECT_STATE
                    this.pdu = new  PointObjectState();
                    break;
                case 44: // PDU_TYPE_LINEAR_OBJECT_STATE
                    this.pdu = new LinearObjectState();
                    break;
                case 200: // PDU_TYPE_APPLICATION_CTRL
                    this.pdu = new ApplicationControlPDU();
                    break;
                default:
                    this.pdu = new DefaultPDU();
            }
            
            ByteArrayInputStream array = new ByteArrayInputStream(this.data);
            DataInputStream stream = new DataInputStream(array);
            
            this.pdu.read(stream);
            
            if (stream.available() > 0) {
                
                logger.warning(
                    "Post read bytes available is " + stream.available() +
                    " for " + this.getClass().getSimpleName());
            }
            
            stream.close();
        }
    }
    
    public void decode(boolean force) {
        
        try {
            
            this.decodeWithoutCatch(force);
        }
        catch(IOException exception) {
            
            logger.log(Level.SEVERE, "Caught exception!", exception);
        }
    }
    
    public boolean encode() {

        boolean encoded = false;
        
        if (this.pdu instanceof Writable) {
            
            Writable writable = (Writable)this.pdu;
            
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(array);

            try {
                
                writable.write(stream);

                stream.close();
                
                encoded = true;

                this.setData(array.toByteArray());
                
                logger.fine("Encoded " + this.getDataLength() + " bytes");
            }
            catch(IOException exception) {
                
                logger.log(Level.SEVERE, "Caught exception!", exception);
            }
            
        }
        
        return encoded;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {
      
        this.decode(false);
        
        if (!buffer.isHTML()) {
            
            buffer.addThickSeparator(null);
            buffer.addAttribute("LENGTH", this.getLength());
            buffer.addAttribute("PORT", this.port);
            buffer.addAttribute("SOURCE", this.source);
            buffer.addAttribute("TIME", format.format(this.time));
            buffer.addThickSeparator(null);
        }
        else {
            
            HypertextBuffer hypertext = (HypertextBuffer)buffer;
            HypertextBuffer preface = new HypertextBuffer();
            
            preface.addBoldAttribute("LENGTH", this.getLength());
            preface.addBoldAttribute("PORT", this.port);
            preface.addBoldAttribute("SOURCE", this.source);
            preface.addBoldAttribute("TIME", format.format(this.time));

            String data[][] = new String[1][1];
            data[0][0] = preface.toString();
            
            hypertext.addTable(1, 0, 1, 1, data);
        }

        buffer.addBreak();
        buffer.addBuffer(this.pdu);
    }

    public void save(DataOutputStream stream) throws IOException {

        stream.writeUTF(this.source);
        stream.writeInt(this.port);
        stream.writeLong(this.time);
        stream.writeInt(this.data.length);
        stream.write(this.data, 0, this.data.length);
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

        int protocolVersion = pdu.getProtocol();
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
