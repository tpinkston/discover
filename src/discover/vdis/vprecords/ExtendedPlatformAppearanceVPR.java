/**
 * @author Tony Pinkston
 */
package discover.vdis.vprecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Abstract32Bits;
import discover.vdis.common.ConditionMaterial;
import discover.vdis.common.ExtendedEquipmentAir;
import discover.vdis.common.ExtendedEquipmentDefault;
import discover.vdis.common.ExtendedEquipmentLand;
import discover.vdis.common.ExtendedLightsAir;
import discover.vdis.common.ExtendedLightsDefault;
import discover.vdis.common.ExtendedLightsLand;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.common.ThermalIndicators;
import discover.vdis.enums.VDIS;

public class ExtendedPlatformAppearanceVPR extends ExtendedAppearanceVPR {

    public static final int LENGTH = 16;

    private int paintScheme = 0; // PLAT_PAINT_SCHEME_DEFAULT
    private int decalScheme = 0; // PLAT_DECAL_SCHEME_NONE
    private int primaryColor = 0; // COLOR_NOT_SPECIFIED
    private int secondaryColor = 0; // COLOR_NOT_SPECIFIED
    private Abstract16Bits equipment = null;
    private Abstract32Bits lights = null;
    private ConditionMaterial primaryCondition = new ConditionMaterial();
    private ConditionMaterial secondaryCondition = new ConditionMaterial();
    private ThermalIndicators thermalIndicators = new ThermalIndicators();
    private ExtendedStatus status = new ExtendedStatus();
    
    public ExtendedPlatformAppearanceVPR() {
        
        this(0);
    }
    
    public ExtendedPlatformAppearanceVPR(int domain) {
        
        super(20); // VP_RECORD_TYPE_EXT_PLATFORM_APP
        super.setDomain(domain);
    }

    @Override
    public int getLength() {
        
        return LENGTH;
    }

    public int getPaintScheme() { return this.paintScheme; }
    public int getDecalScheme() { return this.decalScheme; }
    public int getPrimaryColor() { return this.primaryColor; }
    public int getSecondaryColor() { return this.secondaryColor; }
    public Abstract16Bits getEquipment() { return this.equipment; }
    public Abstract32Bits getLights() { return this.lights; }
    public ConditionMaterial getPrimaryCondition() { return this.primaryCondition; }
    public ConditionMaterial getSecondaryCondition() { return this.secondaryCondition; }
    public ThermalIndicators getThermalIndicators() { return this.thermalIndicators; }
    public ExtendedStatus getStatus() { return status; }

    public void setPaintScheme(int paintScheme) {
        
        this.paintScheme = paintScheme;
    }

    public void setDecalScheme(int decalScheme) {
    
        this.decalScheme = decalScheme;
    }

    public void setPrimaryColor(int primaryColor) {
    
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
    
        this.secondaryColor = secondaryColor;
    }
    
    public void setEquipment(Abstract16Bits equipment) {
        
        // Intentionally throws NPE if null...
        equipment.getClass();
        
        this.equipment = equipment;
    }
    
    public void setLights(Abstract32Bits lights) {
        
        // Intentionally throws NPE if null...
        lights.getClass();
        
        this.lights = lights;
    }

    @Override
    public void setDomain(int domain) {
        
        super.setDomain(domain);
        
        this.createSubRecords();
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VDIS.getDescription(VDIS.VP_RECORD_TYPE, super.type);
        
        String domain = VDIS.getDescription(VDIS.DOMAIN, super.domain);
        
        domain = domain.toUpperCase();
        
        buffer.addTitle(title.toUpperCase());
        
        buffer.addAttribute(
            "Paint", 
            VDIS.getDescription(VDIS.PL_PAINT_SCHEME, this.paintScheme));
        buffer.addAttribute(
            "Decal", 
            VDIS.getDescription(VDIS.PL_DECAL_SCHEME, this.decalScheme));
        
        buffer.addTitle("PRIMARY APPEARANCE");
        buffer.addAttribute(
            "Color", 
            VDIS.getDescription(VDIS.COLORS, this.primaryColor));
        buffer.addText("Condition ");
        buffer.addBuffer(this.primaryCondition);
        
        buffer.addTitle("SECONDARY APPEARANCE");
        buffer.addAttribute(
            "Color", 
            VDIS.getDescription(VDIS.COLORS, this.secondaryColor));
        buffer.addText("Condition ");
        buffer.addBuffer(this.secondaryCondition);
        
        buffer.addTitle("LIGHTS (" + domain + ")");
        buffer.addBuffer(this.lights);

        buffer.addTitle("THERMAL INDICATORS");
        buffer.addBuffer(this.thermalIndicators);
        
        buffer.addTitle("STATUS");
        buffer.addBuffer(this.status);
        
        buffer.addTitle("EQUIPMENT (" + domain + ")");
        buffer.addBuffer(this.equipment);
    }

    /**
     * Assumes first byte has already been written (record type enumeration).
     */
    @Override
    public void read(DataInputStream stream) throws IOException {

        this.createSubRecords();
        
        // 1 Byte
        this.paintScheme = stream.readUnsignedByte();

        // 2 Bytes
        this.decalScheme = stream.readUnsignedShort();

        // 1 Byte
        this.primaryCondition.read(stream);

        // 1 Byte
        this.primaryColor =  stream.readUnsignedByte();

        // 1 Byte
        this.secondaryCondition.read(stream);

        // 1 Byte
        this.secondaryColor =  stream.readUnsignedByte();
        
         // 4 Byte
        this.lights.read(stream);

        // 1 Byte
        this.thermalIndicators.read(stream);

        // 1 Byte
        this.status.read(stream);
        
        // 2 bytes
        this.equipment.read(stream);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)
        
        stream.writeByte(this.paintScheme);
        stream.writeShort(this.decalScheme);
        
        this.primaryCondition.write(stream);
        stream.writeByte(this.primaryColor);
        
        this.secondaryCondition.write(stream);
        stream.writeByte(this.secondaryColor);
        
        this.lights.write(stream);
        this.thermalIndicators.write(stream);
        this.status.write(stream);
        this.equipment.write(stream);
    }
    
    private void createSubRecords() {
        
        if (super.domain == 1) {
            
            // LAND
            this.lights = new ExtendedLightsLand();
            this.equipment = new ExtendedEquipmentLand();
        }
        else if (super.domain == 2) {
            
            // AIR
            this.lights = new ExtendedLightsAir();
            this.equipment = new ExtendedEquipmentAir();
        }
        else {
            
            this.lights = new ExtendedLightsDefault();
            this.equipment = new ExtendedEquipmentDefault();
        }
    }
}
