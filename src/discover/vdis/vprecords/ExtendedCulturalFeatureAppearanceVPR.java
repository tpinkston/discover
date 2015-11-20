package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.bits.Abstract16Bits;
import discover.vdis.common.ExtendedEquipmentCulturalFeature;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.enums.VP_RECORD_TYPE;

/**
 * @author Tony Pinkston
 */
public class ExtendedCulturalFeatureAppearanceVPR extends ExtendedAppearanceVPR {

    public static final int LENGTH = 16;

    private ExtendedStatus status = new ExtendedStatus();
    private Abstract16Bits equipment = new ExtendedEquipmentCulturalFeature();

    public ExtendedCulturalFeatureAppearanceVPR() {

        super(31); // VP_RECORD_TYPE_EXT_CULT_FEAT_APP
    }

    @Override
    public int getLength() {

        return LENGTH;
    }

    public ExtendedStatus getStatus() { return status; }
    public Abstract16Bits getEquipment() { return equipment; }

    public void setEquipment(Abstract16Bits equipment) {

        // Intentionally throws NPE if null...
        equipment.getClass();

        this.equipment = equipment;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VP_RECORD_TYPE.get(getRecordType()).description;

        buffer.addTitle(title.toUpperCase());
        buffer.addTitle("STATUS");
        buffer.addBuffer(status);
        buffer.addTitle("EQUIPMENT");
        buffer.addBuffer(equipment);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        stream.skipBytes(12);

        // 1 Byte
        status.read(stream);

        // 2 bytes
        equipment.read(stream);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        // 12 bytes padding
        stream.writeInt(0x00);
        stream.writeInt(0x00);
        stream.writeInt(0x00);

        status.write(stream);
        equipment.write(stream);
    }
}
