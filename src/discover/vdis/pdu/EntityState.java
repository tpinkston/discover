/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.appearance.AbstractAppearance;
import discover.vdis.appearance.DefaultAppearance;
import discover.vdis.common.DeadReckoning;
import discover.vdis.common.EntityCapabilities;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location24;
import discover.vdis.common.Orientation;
import discover.vdis.common.PDUHeader;
import discover.vdis.common.Velocity;
import discover.vdis.enums.VDIS;
import discover.vdis.marking.EntityMarking;
import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;
import discover.vdis.vprecords.AbstractVPRecord;
import discover.vdis.vprecords.ArticulatedPartVPR;
import discover.vdis.vprecords.DeadReckoningVPR;
import discover.vdis.vprecords.DefaultVPR;
import discover.vdis.vprecords.EntityAssociationVPR;
import discover.vdis.vprecords.EntityOffsetVPR;
import discover.vdis.vprecords.ExtendedAppearanceVPR;
import discover.vdis.vprecords.ExtendedCulturalFeatureAppearanceVPR;
import discover.vdis.vprecords.ExtendedPlatformAppearanceVPR;
import discover.vdis.vprecords.ExtendedSupplyAppearanceVPR;
import discover.vdis.vprecords.LegacyExtendedLifeformAppearanceVPR;

public class EntityState extends AbstractPDU implements Writable {

    private EntityId entityId = new EntityId();
    private int forceId = 0;
    private EntityType entityType = null;
    private EntityType alternateType = null;
    private Velocity velocity = new Velocity();
    private Location24 location = new Location24();
    private Orientation orientation = new Orientation();
    private AbstractAppearance appearance = null;
    private DeadReckoning deadReckoning = new DeadReckoning();
    private EntityMarking marking = new EntityMarking();
    private EntityCapabilities capabilities = new EntityCapabilities();
    private List<AbstractVPRecord> records = new ArrayList<AbstractVPRecord>();

    public EntityState() {

        this.entityType = EntityTypes.getEntityType(0);
        this.alternateType = this.entityType;
        this.appearance = new DefaultAppearance();
    }

    public EntityId getEntityId() { return this.entityId; }

    public int getForceId() { return this.forceId; }

    public EntityType getEntityType() { return this.entityType; }

    public EntityType getAlternateType() { return this.alternateType; }

    public EntityMarking getMarking() { return this.marking; }

    public AbstractAppearance getAppearance() { return this.appearance; }

    public Location24 getLocation() { return this.location; }

    public Velocity getVelocity() { return this.velocity; }

    public Orientation getOrientation() { return this.orientation; }

    public DeadReckoning getDeadReckoning() { return this.deadReckoning; }

    public EntityCapabilities getCapabilities() { return this.capabilities; }

    public List<AbstractVPRecord> getRecords() { return this.records; }

    public void setEntityId(EntityId value) {

        this.checkObject(value);
        this.entityId = value;
    }

    public void setForceId(int value) {

        this.forceId = value;
    }

    public void setEntityType(EntityType value) {

        this.checkObject(value);
        this.entityType = value;
    }

    public void setAlternateType(EntityType value) {

        this.checkObject(value);
        this.alternateType = value;
    }

    public void setMarking(EntityMarking value) {

        this.checkObject(value);
        this.marking = value;
    }

    public void setAppearance(AbstractAppearance value) {

        this.checkObject(value);
        this.appearance = value;
    }

    public boolean hasExtendedAppearance() {

        if (!this.records.isEmpty()) {

            for(AbstractVPRecord record : this.records) {

                if (record instanceof ExtendedAppearanceVPR) {

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void clear() {

        this.entityId.clear();
        this.forceId = 0;
        this.entityType = null;
        this.alternateType = null;
        this.velocity.clear();
        this.location.clear();
        this.orientation.clear();
        this.appearance = null;
        this.deadReckoning.clear();
        this.marking.clear();
        this.capabilities.set(0);
        this.records.clear();
    }

    public int calculateLength() {

        int length = 0;

        length += PDUHeader.LENGTH;
        length += EntityId.LENGTH;
        length += 2; // Force Id and number of variable parameter records.
        length += EntityType.LENGTH; // Primary
        length += EntityType.LENGTH; // Alternate
        length += Velocity.LENGTH;
        length += Location24.LENGTH;
        length += Orientation.LENGTH;
        length += AbstractAppearance.LENGTH;
        length += DeadReckoning.LENGTH;
        length += EntityMarking.LENGTH;
        length += EntityCapabilities.LENGTH;

        for(AbstractVPRecord record : this.records) {

            length += record.getLength();
        }

        return length;
    }

    public boolean hasAssociations() {

        for(AbstractVPRecord record : this.records) {

            if (record instanceof EntityAssociationVPR) {

                return true;
            }
        }

        return false;
    }

    public void getAssociations(List<AbstractVPRecord> list) {

        list.clear();

        for(AbstractVPRecord record : this.records) {

            if ((record instanceof EntityAssociationVPR) ||
                (record instanceof EntityOffsetVPR)) {

                list.add(record);
            }
        }
    }

    public void getArticulations(List<AbstractVPRecord> list) {

        list.clear();

        for(AbstractVPRecord record : this.records) {

            if (record instanceof ArticulatedPartVPR) {

                list.add(record);
            }
        }
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Entity", this.entityId.toString());
        buffer.addAttribute("Force", this.forceId, VDIS.FORCE_ID);
        buffer.addTitle("MARKING");
        buffer.addBuffer(this.marking);
        buffer.addBreak();

        buffer.addTitle("TYPE");
        this.entityType.toBuffer(buffer);
        buffer.addBreak();

        buffer.addTitle("ALTERNATE TYPE");
        this.alternateType.toBuffer(buffer);
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Velocity", this.velocity.toString());
        buffer.addAttribute("Location(GCC)", this.location.toStringGCC());
        buffer.addAttribute("Location(GDC)", this.location.toStringGDC());
        buffer.addAttribute("Orientation", this.orientation.toString());
        buffer.addBreak();

        if (this.appearance == null) {

            buffer.addTitle("APPEARANCE NOT AVAILABLE");
        }
        else {

            buffer.addTitle(this.appearance.getName().toUpperCase());
            buffer.addBuffer(this.appearance);
        }

        buffer.addBreak();

        buffer.addTitle("DEAD RECKONING");
        buffer.addBuffer(this.deadReckoning);
        buffer.addBreak();

        buffer.addTitle("CAPABILITIES");
        buffer.addBuffer(this.capabilities);

        if (!this.records.isEmpty()) {

            for(int i = 0, size = this.records.size(); (i < size); ++i) {

                AbstractVPRecord record = this.records.get(i);

                buffer.addBreak();
                buffer.addSeparator();
                buffer.addBuffer(record);
            }
        }

        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header = 12 bytes)

        this.entityId.read(stream); // 6 bytes
        this.forceId = stream.readUnsignedByte(); // 1 byte

        int recordCount = stream.readUnsignedByte(); // 1 byte

        this.entityType = EntityTypes.read(stream); // 8 bytes
        this.alternateType = EntityTypes.read(stream); // 8 bytes
        this.velocity.read(stream); // 12 bytes
        this.location.read(stream); // 24 bytes
        this.orientation.read(stream); // 12 bytes

        this.appearance = AbstractAppearance.get(this.entityType);
        this.appearance.read(stream);

        this.deadReckoning.read(stream);
        this.marking.read(stream);
        this.capabilities.read(stream);

        for(int i = 0; (i < recordCount); ++i) {

            int type = stream.readUnsignedByte();

            AbstractVPRecord record = this.getVPRecord(type);

            if (record == null) {

                logger.error("Could not create VP record for {}", type);
            }
            else {

                // Some records are dependent on the entity domain to be
                // decoded.
                record.setDomain(this.entityType.septuple.domain);

                // The reading of record from the stream should assume
                // that the first byte (out of 16) has already been read
                // to get the record type.
                record.read(stream);

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

        int length = this.calculateLength();

        super.getHeader().setLength(length);
        super.getHeader().write(stream);

        this.entityId.write(stream);
        stream.writeByte(this.forceId);
        stream.writeByte(this.records.size());
        this.entityType.write(stream);
        this.alternateType.write(stream);
        this.velocity.write(stream);
        this.location.write(stream);
        this.orientation.write(stream);
        this.appearance.write(stream);
        this.deadReckoning.write(stream);
        this.marking.write(stream);
        this.capabilities.write(stream);

        for(AbstractVPRecord record : this.records) {

            record.write(stream);
        }
    }

    private void checkObject(Object object) {

        if (object == null) {

            throw new IllegalArgumentException("Cannot set null attribute!");
        }
    }

    private AbstractVPRecord getVPRecord(int type) {

        AbstractVPRecord record = null;

        switch(type) {

            case 0: // VP_RECORD_TYPE_ARTICULATED_PART
                record = new ArticulatedPartVPR();
                break;
            case 4: // VP_RECORD_TYPE_ENTITY_ASSOC
                record = new EntityAssociationVPR();
                break;
            case 20: // VP_RECORD_TYPE_EXT_PLATFORM_APP
                record = new ExtendedPlatformAppearanceVPR();
                break;
            case 25: // VP_RECORD_TYPE_ENTITY_OFFSET
                record = new EntityOffsetVPR();
                break;
            case 26: // VP_RECORD_TYPE_DEAD_RECKONING
                record = new DeadReckoningVPR();
                break;
            case 30: // VP_RECORD_TYPE_LEGACY_EXT_LIFEFORM_APP
                record = new LegacyExtendedLifeformAppearanceVPR();
                break;
            case 31: // VP_RECORD_TYPE_EXT_CULT_FEAT_APP
                record = new ExtendedCulturalFeatureAppearanceVPR();
                break;
            case 32: // VP_RECORD_TYPE_EXT_SUPPLY_APP
                record = new ExtendedSupplyAppearanceVPR();
                break;
            default:
                record = new DefaultVPR(type);
        }

        return record;
    }
}
