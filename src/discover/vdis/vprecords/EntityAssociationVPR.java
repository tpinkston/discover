package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
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

    public EntityId getEntityId() { return entity; }
    public int getStatus() { return status; }
    public int getType() { return type; }
    public int getConnection() { return connection; }
    public int getStation() { return station; }
    public int getMembership() { return membership; }
    public int getChange() { return change; }
    public int getGroup() { return group; }

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

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, getRecordType());

        buffer.addTitle(title.toUpperCase());
        buffer.addAttribute(
            "Type",
            VDIS.getDescription(VDIS.PHYS_ASSOC_TYPE, type));
        buffer.addAttribute(
            "Status",
            VDIS.getDescription(VDIS.ENT_ASSOC_STATUS, status));
        buffer.addAttribute(
            "Connection",
            VDIS.getDescription(VDIS.PHYS_CONN_TYPE, connection));
        buffer.addAttribute(
            "Station",
            VDIS.getDescription(VDIS.STATION_NAME, station));
        buffer.addAttribute(
            "Membership",
            VDIS.getDescription(VDIS.GRP_MEM_TYPE, membership));

        buffer.addAttribute("Entity", entity.toString());
        buffer.addAttribute("Change", change);
        buffer.addAttribute("Group", group);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        change = stream.readUnsignedByte();
        status = stream.readUnsignedByte();
        type = stream.readUnsignedByte();

        entity.read(stream); // 6 bytes

        station = stream.readUnsignedShort();
        connection = stream.readUnsignedByte();
        membership = stream.readUnsignedByte();
        group = stream.readUnsignedShort();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(change);
        stream.writeByte(status);
        stream.writeByte(type);

        entity.write(stream);

        stream.writeShort(station);
        stream.writeByte(connection);
        stream.writeByte(membership);
        stream.writeShort(group);
    }
}
