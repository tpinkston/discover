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
import discover.vdis.enums.FORCE_ID;
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

/**
 * @author Tony Pinkston
 */
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

        entityType = EntityTypes.getEntityType(0);
        alternateType = entityType;
        appearance = new DefaultAppearance();
    }

    public EntityId getEntityId() { return entityId; }

    public int getForceId() { return forceId; }

    public EntityType getEntityType() { return entityType; }

    public EntityType getAlternateType() { return alternateType; }

    public EntityMarking getMarking() { return marking; }

    public AbstractAppearance getAppearance() { return appearance; }

    public Location24 getLocation() { return location; }

    public Velocity getVelocity() { return velocity; }

    public Orientation getOrientation() { return orientation; }

    public DeadReckoning getDeadReckoning() { return deadReckoning; }

    public EntityCapabilities getCapabilities() { return capabilities; }

    public List<AbstractVPRecord> getRecords() { return records; }

    public void setEntityId(EntityId value) {

        checkObject(value);
        entityId = value;
    }

    public void setForceId(int value) {

        forceId = value;
    }

    public void setEntityType(EntityType value) {

        checkObject(value);
        entityType = value;
    }

    public void setAlternateType(EntityType value) {

        checkObject(value);
        alternateType = value;
    }

    public void setMarking(EntityMarking value) {

        checkObject(value);
        marking = value;
    }

    public void setAppearance(AbstractAppearance value) {

        checkObject(value);
        appearance = value;
    }

    public boolean hasExtendedAppearance() {

        if (!records.isEmpty()) {

            for(AbstractVPRecord record : records) {

                if (record instanceof ExtendedAppearanceVPR) {

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void clear() {

        entityId.clear();
        forceId = 0;
        entityType = null;
        alternateType = null;
        velocity.clear();
        location.clear();
        orientation.clear();
        appearance = null;
        deadReckoning.clear();
        marking.clear();
        capabilities.set(0);
        records.clear();
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

        for(AbstractVPRecord record : records) {

            length += record.getLength();
        }

        return length;
    }

    public boolean hasAssociations() {

        for(AbstractVPRecord record : records) {

            if (record instanceof EntityAssociationVPR) {

                return true;
            }
        }

        return false;
    }

    public void getAssociations(List<AbstractVPRecord> list) {

        list.clear();

        for(AbstractVPRecord record : records) {

            if ((record instanceof EntityAssociationVPR) ||
                (record instanceof EntityOffsetVPR)) {

                list.add(record);
            }
        }
    }

    public void getArticulations(List<AbstractVPRecord> list) {

        list.clear();

        for(AbstractVPRecord record : records) {

            if (record instanceof ArticulatedPartVPR) {

                list.add(record);
            }
        }
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Entity", entityId.toString());
        buffer.addAttribute("Force", forceId, FORCE_ID.class);
        buffer.addTitle("MARKING");
        buffer.addBuffer(marking);
        buffer.addBreak();

        buffer.addTitle("TYPE");
        entityType.toBuffer(buffer);
        buffer.addBreak();

        buffer.addTitle("ALTERNATE TYPE");
        alternateType.toBuffer(buffer);
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Velocity", velocity.toString());
        buffer.addAttribute("Location(GCC)", location.toStringGCC());
        buffer.addAttribute("Location(GDC)", location.toStringGDC());
        buffer.addAttribute("Orientation", orientation.toString());
        buffer.addBreak();

        if (appearance == null) {

            buffer.addTitle("APPEARANCE NOT AVAILABLE");
        }
        else {

            buffer.addTitle(appearance.getName().toUpperCase());
            buffer.addBuffer(appearance);
        }

        buffer.addBreak();

        buffer.addTitle("DEAD RECKONING");
        buffer.addBuffer(deadReckoning);
        buffer.addBreak();

        buffer.addTitle("CAPABILITIES");
        buffer.addBuffer(capabilities);

        if (!records.isEmpty()) {

            for(int i = 0, size = records.size(); (i < size); ++i) {

                AbstractVPRecord record = records.get(i);

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

        entityId.read(stream); // 6 bytes
        forceId = stream.readUnsignedByte(); // 1 byte

        int recordCount = stream.readUnsignedByte(); // 1 byte

        entityType = EntityTypes.read(stream); // 8 bytes
        alternateType = EntityTypes.read(stream); // 8 bytes
        velocity.read(stream); // 12 bytes
        location.read(stream); // 24 bytes
        orientation.read(stream); // 12 bytes

        appearance = AbstractAppearance.get(entityType);
        appearance.read(stream);

        deadReckoning.read(stream);
        marking.read(stream);
        capabilities.read(stream);

        for(int i = 0; (i < recordCount); ++i) {

            int type = stream.readUnsignedByte();

            AbstractVPRecord record = getVPRecord(type);

            if (record == null) {

                logger.error("Could not create VP record for {}", type);
            }
            else {

                // Some records are dependent on the entity domain to be
                // decoded.
                record.setDomain(entityType.septuple.domain);

                // The reading of record from the stream should assume
                // that the first byte (out of 16) has already been read
                // to get the record type.
                record.read(stream);

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

        int length = calculateLength();

        super.getHeader().setLength(length);
        super.getHeader().write(stream);

        entityId.write(stream);
        stream.writeByte(forceId);
        stream.writeByte(records.size());
        entityType.write(stream);
        alternateType.write(stream);
        velocity.write(stream);
        location.write(stream);
        orientation.write(stream);
        appearance.write(stream);
        deadReckoning.write(stream);
        marking.write(stream);
        capabilities.write(stream);

        for(AbstractVPRecord record : records) {

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
