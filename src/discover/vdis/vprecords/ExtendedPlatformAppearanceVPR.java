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
import discover.vdis.enums.COLORS;
import discover.vdis.enums.ENT_DOMAIN;
import discover.vdis.enums.PL_DECAL_SCHEME;
import discover.vdis.enums.PL_PAINT_SCHEME;
import discover.vdis.enums.VP_RECORD_TYPE;

/**
 * @author Tony Pinkston
 */
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
        setDomain(domain);
    }

    @Override
    public int getLength() {

        return LENGTH;
    }

    public int getPaintScheme() { return paintScheme; }
    public int getDecalScheme() { return decalScheme; }
    public int getPrimaryColor() { return primaryColor; }
    public int getSecondaryColor() { return secondaryColor; }
    public Abstract16Bits getEquipment() { return equipment; }
    public Abstract32Bits getLights() { return lights; }
    public ConditionMaterial getPrimaryCondition() { return primaryCondition; }
    public ConditionMaterial getSecondaryCondition() { return secondaryCondition; }
    public ThermalIndicators getThermalIndicators() { return thermalIndicators; }
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

        createSubRecords();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        String title = VP_RECORD_TYPE.getValue(getRecordType()).getDescription();

        String domain = ENT_DOMAIN.getValue(getDomain()).getDescription();

        domain = domain.toUpperCase();

        buffer.addTitle(title.toUpperCase());

        buffer.addAttribute("Paint", paintScheme, PL_PAINT_SCHEME.class);
        buffer.addAttribute("Decal", decalScheme, PL_DECAL_SCHEME.class);

        buffer.addTitle("PRIMARY APPEARANCE");
        buffer.addAttribute("Color", primaryColor, COLORS.class);
        buffer.addText("Condition ");
        buffer.addBuffer(primaryCondition);

        buffer.addTitle("SECONDARY APPEARANCE");
        buffer.addAttribute("Color", secondaryColor, COLORS.class);
        buffer.addText("Condition ");
        buffer.addBuffer(secondaryCondition);

        buffer.addTitle("LIGHTS (" + domain + ")");
        buffer.addBuffer(lights);

        buffer.addTitle("THERMAL INDICATORS");
        buffer.addBuffer(thermalIndicators);

        buffer.addTitle("STATUS");
        buffer.addBuffer(status);

        buffer.addTitle("EQUIPMENT (" + domain + ")");
        buffer.addBuffer(equipment);
    }

    /**
     * Assumes first byte has already been written (record type enumeration).
     */
    @Override
    public void read(DataInputStream stream) throws IOException {

        createSubRecords();

        // 1 Byte
        paintScheme = stream.readUnsignedByte();

        // 2 Bytes
        decalScheme = stream.readUnsignedShort();

        // 1 Byte
        primaryCondition.read(stream);

        // 1 Byte
        primaryColor =  stream.readUnsignedByte();

        // 1 Byte
        secondaryCondition.read(stream);

        // 1 Byte
        secondaryColor =  stream.readUnsignedByte();

         // 4 Byte
        lights.read(stream);

        // 1 Byte
        thermalIndicators.read(stream);

        // 1 Byte
        status.read(stream);

        // 2 bytes
        equipment.read(stream);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        super.write(stream); // Writes record type (1 byte)

        stream.writeByte(paintScheme);
        stream.writeShort(decalScheme);

        primaryCondition.write(stream);
        stream.writeByte(primaryColor);

        secondaryCondition.write(stream);
        stream.writeByte(secondaryColor);

        lights.write(stream);
        thermalIndicators.write(stream);
        status.write(stream);
        equipment.write(stream);
    }

    private void createSubRecords() {

        if (getDomain() == 1) {

            // LAND
            lights = new ExtendedLightsLand();
            equipment = new ExtendedEquipmentLand();
        }
        else if (getDomain() == 2) {

            // AIR
            lights = new ExtendedLightsAir();
            equipment = new ExtendedEquipmentAir();
        }
        else {

            lights = new ExtendedLightsDefault();
            equipment = new ExtendedEquipmentDefault();
        }
    }
}
