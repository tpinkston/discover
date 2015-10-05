/**
 * @author Tony Pinkston
 */
package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.ExtendedEquipmentLifeform;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.common.LifeformAttributes;
import discover.vdis.enums.VDIS;

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

        this.equipment = new ExtendedEquipmentLifeform();
        this.status = new ExtendedStatus();
        this.attributes = new LifeformAttributes();
    }

    @Override
    public int getLength() {

        return LENGTH;
    }

    public ExtendedEquipmentLifeform getEquipment() { return this.equipment; }
    public ExtendedStatus getStatus() { return this.status; }
    public LifeformAttributes getAttributes() { return this.attributes; }
    public int getClothing() { return this.clothing; }
    public int getPrimaryColor() { return this.primaryColor; }
    public int getSecondaryColor() { return this.secondaryColor; }

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

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, super.type);

        buffer.addTitle(title.toUpperCase());

        buffer.addAttribute(
            "Clothing",
            VDIS.getDescription(VDIS.LF_CLOTH_SCHEME, this.clothing));
        buffer.addAttribute(
            "Primary Color",
            VDIS.getDescription(VDIS.COLORS, this.primaryColor));
        buffer.addAttribute(
            "Secondary Color",
            VDIS.getDescription(VDIS.COLORS, this.secondaryColor));

        buffer.addTitle("EQUIPMENT");
        buffer.addBuffer(this.equipment);

        buffer.addTitle("STATUS");
        buffer.addBuffer(this.status);

        buffer.addTitle("ATTRIBUTES");
        buffer.addBuffer(this.attributes);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.clothing = stream.readUnsignedByte();
        this.primaryColor = stream.readUnsignedByte();
        this.secondaryColor = stream.readUnsignedByte();
        this.equipment.read(stream);
        this.status.read(stream);
        stream.skipBytes(1);
        this.attributes.read(stream);
        stream.skipBytes(4);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(this.clothing);
        stream.writeByte(this.primaryColor);
        stream.writeByte(this.secondaryColor);
        this.equipment.write(stream);
        this.status.write(stream);
        stream.writeByte(0x00);
        this.attributes.write(stream);
        stream.writeInt(0x00);
    }
}
