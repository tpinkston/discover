/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location12;
import discover.vdis.common.Location24;
import discover.vdis.enums.VDIS;

public class Designator extends AbstractPDU {

    private EntityId entity = new EntityId();
    private EntityId object = new EntityId();
    private Location12 beamOffset = new Location12();
    private Location12 spotOffset = new Location12();
    private Location24 spotLocation = new Location24();
    private int spotType = 0;
    private int systemName = 0;
    private int function = 0;
    private int algorithm = 0;
    private int designatorCode = 0;
    private int flashRate = 0;
    private int designatorSystemNumber = 0;
    private float designatorPower = 0.0f;
    private float designatorWavelength = 0.0f;

    public Designator() {

    }
    
    public EntityId getEntity() { return this.entity; }
    public EntityId getObject() { return this.object; }
    public Location12 getBeamOffset() { return this.beamOffset; }
    public Location12 getSpotOffset() { return this.spotOffset; }
    public Location24 getSpotLocation() { return this.spotLocation; }
    public int getSpotType() { return this.spotType; }
    public int getSystemName() { return this.systemName; }
    public int getFunction() { return this.function; }
    public int getAlgorithm() { return this.algorithm; }
    public int getDesignatorCode() { return this.designatorCode; }
    public int getFlashRate() { return this.flashRate; }
    public int getSystemNumber() { return this.designatorSystemNumber; }
    public float getPower() { return this.designatorPower; }
    public float getWavelength() { return this.designatorWavelength; }

    @Override
    public void clear() {

        this.entity.clear();
        this.object.clear();
        this.beamOffset.clear();
        this.spotOffset.clear();
        this.spotLocation.clear();
        this.spotType = 0;
        this.systemName = 0;
        this.function = 0;
        this.algorithm = 0;
        this.designatorCode = 0;
        this.flashRate = 0;
        this.designatorSystemNumber = 0;
        this.designatorPower = 0.0f;
        this.designatorWavelength = 0.0f;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);
        
        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Designating Entity", this.entity.toString());
        buffer.addAttribute("Designated Entity", this.object.toString());
        buffer.addAttribute("Spot Type", this.spotType, VDIS.DESIG_SPOT_TYPE);
        buffer.addAttribute("System Name", this.systemName, VDIS.DESIG_SYSTEM_NAME);
        buffer.addAttribute("System Number", this.designatorSystemNumber);
        buffer.addBreak();
        
        buffer.addTitle("PARAMETERS");
        buffer.addAttribute("Code", this.designatorCode);
        buffer.addAttribute("Power", this.designatorPower);
        buffer.addAttribute("Wave Length", this.designatorWavelength);
        buffer.addAttribute("Flash Rate", this.flashRate);
        buffer.addAttribute("Laser Function", this.function, VDIS.LASER_FUNCTION);
        buffer.addAttribute("Dead Reckoning Algorithm", this.algorithm, VDIS.DEAD_RECKONING);
        buffer.addBreak();
        
        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Spot Location", this.spotLocation.toString());
        buffer.addAttribute("Spot Offset on Object", this.spotOffset.toString());
        buffer.addAttribute("Beam Origin Offset", this.beamOffset.toString());
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 12 bytes (header)
        super.read(stream);
        
        // 6 bytes
        this.entity.read(stream);
        
        // 1 byte
        this.spotType = stream.readUnsignedByte();
        
        // 1 byte
        this.systemName = stream.readUnsignedByte();
        
        // 6 bytes
        this.object.read(stream);
        
        // 2 bytes
        this.designatorCode = stream.readUnsignedShort();
        
        // 4 bytes
        this.designatorPower = stream.readFloat();
        
        // 4 bytes
        this.designatorWavelength = stream.readFloat();
        
        // 12 bytes
        this.spotOffset.read(stream);
        
        // 24 bytes
        this.spotLocation.read(stream);
        
        // 1 byte
        this.algorithm = stream.readUnsignedByte();
        
        // 1 byte
        this.flashRate = stream.readUnsignedByte();
        
        // 1 byte
        this.designatorSystemNumber = stream.readUnsignedByte();
        
        // 1 byte
        this.function = stream.readUnsignedByte();
        
        // 12 bytes
        this.beamOffset.read(stream);
    }
}
