package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.ExtendedEquipmentLifeform;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.common.LifeformAttributes;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class LegacyExtendedLifeformAppearanceVPR extends ExtendedAppearanceVPR {

    public static final int LENGTH = 16;

    private final ExtendedEquipmentLifeform equipment;
    private final ExtendedStatus status;
    private final LifeformAttributes attributes;

    private int clothing = 0;
    private int primaryColor = 0;
    private int secondaryColor = 0;

    public LegacyExtendedLifeformAppearanceVPR() {

        super(30); //  VP_RECORD_TYPE_LEGACY_EXT_LIFEFORM_APP;

        equipment = new ExtendedEquipmentLifeform();
        status = new ExtendedStatus();
        attributes = new LifeformAttributes();
    }

    @Override
    public int getLength() {

        return LENGTH;
    }

    public ExtendedEquipmentLifeform getEquipment() { return equipment; }
    public ExtendedStatus getStatus() { return status; }
    public LifeformAttributes getAttributes() { return attributes; }
    public int getClothing() { return clothing; }
    public int getPrimaryColor() { return primaryColor; }
    public int getSecondaryColor() { return secondaryColor; }

    public void setClothing(int clothing) {

        this.clothing = clothing;
    }

    public void setPrimaryColor(int primaryColor) {

        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {

        this.secondaryColor = secondaryColor;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, getRecordType());

        buffer.addTitle(title.toUpperCase());

        buffer.addAttribute(
            "Clothing",
            VDIS.getDescription(VDIS.LF_CLOTH_SCHEME, clothing));
        buffer.addAttribute(
            "Primary Color",
            VDIS.getDescription(VDIS.COLORS, primaryColor));
        buffer.addAttribute(
            "Secondary Color",
            VDIS.getDescription(VDIS.COLORS, secondaryColor));

        buffer.addTitle("EQUIPMENT");
        buffer.addBuffer(equipment);

        buffer.addTitle("STATUS");
        buffer.addBuffer(status);

        buffer.addTitle("ATTRIBUTES");
        buffer.addBuffer(attributes);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        clothing = stream.readUnsignedByte();
        primaryColor = stream.readUnsignedByte();
        secondaryColor = stream.readUnsignedByte();
        equipment.read(stream);
        status.read(stream);
        stream.skipBytes(1);
        attributes.read(stream);
        stream.skipBytes(4);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(clothing);
        stream.writeByte(primaryColor);
        stream.writeByte(secondaryColor);
        equipment.write(stream);
        status.write(stream);
        stream.writeByte(0x00);
        attributes.write(stream);
        stream.writeInt(0x00);
    }
}
