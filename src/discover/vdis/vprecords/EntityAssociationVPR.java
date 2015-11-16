package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.ENT_ASSOC_STATUS;
import discover.vdis.enums.GRP_MEM_TYPE;
import discover.vdis.enums.PHYS_ASSOC_TYPE;
import discover.vdis.enums.PHYS_CONN_TYPE;
import discover.vdis.enums.STATION_NAME;
import discover.vdis.enums.VP_RECORD_TYPE;

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

    public void setStatus(int value) {

        status = value;
    }

    public void setType(int value) {

        type = value;
    }

    public void setConnection(int value) {

        connection = value;
    }

    public void setStation(int value) {

        station = value;
    }

    public void setMembership(int value) {

        membership = value;
    }

    public void setChange(int value) {

        change = value;
    }

    public void setGroup(int value) {

        group = value;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VP_RECORD_TYPE.getValue(getRecordType()).getDescription();

        buffer.addTitle(title.toUpperCase());
        buffer.addAttribute(
            "Type",
            type,
            PHYS_ASSOC_TYPE.class);
        buffer.addAttribute(
            "Status",
            status,
            ENT_ASSOC_STATUS.class);
        buffer.addAttribute(
            "Connection",
            connection,
            PHYS_CONN_TYPE.class);
        buffer.addAttribute(
            "Station",
            station,
            STATION_NAME.class);
        buffer.addAttribute(
            "Membership",
            membership,
            GRP_MEM_TYPE.class);

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
