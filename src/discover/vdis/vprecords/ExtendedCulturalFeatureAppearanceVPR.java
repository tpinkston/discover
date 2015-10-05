/**
 * @author Tony Pinkston
 */
package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.bits.Abstract16Bits;
import discover.vdis.common.ExtendedEquipmentCulturalFeature;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.enums.VDIS;

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
    public Abstract16Bits getEquipment() { return this.equipment; }
    
    public void setEquipment(Abstract16Bits equipment) {
        
        // Intentionally throws NPE if null...
        equipment.getClass();
        
        this.equipment = equipment;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, super.type);
        
        buffer.addTitle(title.toUpperCase());
        buffer.addTitle("STATUS");
        buffer.addBuffer(this.status);
        buffer.addTitle("EQUIPMENT");
        buffer.addBuffer(this.equipment);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        stream.skipBytes(12);

        // 1 Byte
        this.status.read(stream);
        
        // 2 bytes
        this.equipment.read(stream);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        // 12 bytes padding
        stream.writeInt(0x00);
        stream.writeInt(0x00);
        stream.writeInt(0x00);
        
        this.status.write(stream);
        this.equipment.write(stream);
    }
}
