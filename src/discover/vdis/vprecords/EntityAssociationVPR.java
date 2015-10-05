/**
 * @author Tony Pinkston
 */
package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

public class EntityAssociationVPR extends AbstractVPRecord {
    
    public static final int LENGTH = 16;

    private final EntityId entity = new EntityId();
    private int status = 0;
    private int type = 0;
    private int connection = 0;
    private int station = 0;
    private int membership = 0;
    private int change = 0;
    private int group = 0;
    
    public EntityAssociationVPR() {
        
        super(4); // VP_RECORD_TYPE_ENTITY_ASSOC
    }

    @Override
    public int getLength() {
        
        return LENGTH;
    }
    
    public EntityId getEntityId() { return this.entity; }
    public int getStatus() { return this.status; }
    public int getType() { return this.type; }
    public int getConnection() { return this.connection; }
    public int getStation() { return this.station; }
    public int getMembership() { return this.membership; }
    public int getChange() { return this.change; }
    public int getGroup() { return this.group; }

    public void setStatus(int status) {
    
        this.status = status;
    }

    public void setType(int type) {
    
        this.type = type;
    }

    public void setConnection(int connection) {
    
        this.connection = connection;
    }
    
    public void setStation(int station) {
    
        this.station = station;
    }
    
    public void setMembership(int membership) {
    
        this.membership = membership;
    }

    public void setChange(int change) {
    
        this.change = change;
    }
    
    public void setGroup(int group) {
    
        this.group = group;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, super.type);
        
        buffer.addTitle(title.toUpperCase());
        buffer.addAttribute(
            "Type", 
            VDIS.getDescription(VDIS.PHYS_ASSOC_TYPE, this.type));
        buffer.addAttribute(
            "Status", 
            VDIS.getDescription(VDIS.ENT_ASSOC_STATUS, this.status));
        buffer.addAttribute(
            "Connection", 
            VDIS.getDescription(VDIS.PHYS_CONN_TYPE, this.connection));
        buffer.addAttribute(
            "Station", 
            VDIS.getDescription(VDIS.STATION_NAME, this.station));
        buffer.addAttribute(
            "Membership", 
            VDIS.getDescription(VDIS.GRP_MEM_TYPE, this.membership));

        buffer.addAttribute("Entity", this.entity.toString());
        buffer.addAttribute("Change", this.change);
        buffer.addAttribute("Group", this.group);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.change = stream.readUnsignedByte();
        this.status = stream.readUnsignedByte();
        this.type = stream.readUnsignedByte();

        this.entity.read(stream); // 6 bytes
        
        this.station = stream.readUnsignedShort();
        this.connection = stream.readUnsignedByte();
        this.membership = stream.readUnsignedByte();
        this.group = stream.readUnsignedShort();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(this.change);
        stream.writeByte(this.status);
        stream.writeByte(this.type);
       
        this.entity.write(stream);
       
        stream.writeShort(this.station);
        stream.writeByte(this.connection);
        stream.writeByte(this.membership);
        stream.writeShort(this.group);
    }
}
