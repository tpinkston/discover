package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location12;
import discover.vdis.common.Location24;
import discover.vdis.enums.DEAD_RECKONING;
import discover.vdis.enums.DESIG_SPOT_TYPE;
import discover.vdis.enums.DESIG_SYSTEM_NAME;
import discover.vdis.enums.LASER_FUNCTION;

/**
 * @author Tony Pinkston
 */
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

    public EntityId getEntity() { return entity; }
    public EntityId getObject() { return object; }
    public Location12 getBeamOffset() { return beamOffset; }
    public Location12 getSpotOffset() { return spotOffset; }
    public Location24 getSpotLocation() { return spotLocation; }
    public int getSpotType() { return spotType; }
    public int getSystemName() { return systemName; }
    public int getFunction() { return function; }
    public int getAlgorithm() { return algorithm; }
    public int getDesignatorCode() { return designatorCode; }
    public int getFlashRate() { return flashRate; }
    public int getSystemNumber() { return designatorSystemNumber; }
    public float getPower() { return designatorPower; }
    public float getWavelength() { return designatorWavelength; }

    @Override
    public void clear() {

        entity.clear();
        object.clear();
        beamOffset.clear();
        spotOffset.clear();
        spotLocation.clear();
        spotType = 0;
        systemName = 0;
        function = 0;
        algorithm = 0;
        designatorCode = 0;
        flashRate = 0;
        designatorSystemNumber = 0;
        designatorPower = 0.0f;
        designatorWavelength = 0.0f;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Designating Entity", entity.toString());
        buffer.addAttribute("Designated Entity", object.toString());
        buffer.addAttribute("Spot Type", spotType, DESIG_SPOT_TYPE.class);
        buffer.addAttribute("System Name", systemName, DESIG_SYSTEM_NAME.class);
        buffer.addAttribute("System Number", designatorSystemNumber);
        buffer.addBreak();

        buffer.addTitle("PARAMETERS");
        buffer.addAttribute("Code", designatorCode);
        buffer.addAttribute("Power", designatorPower);
        buffer.addAttribute("Wave Length", designatorWavelength);
        buffer.addAttribute("Flash Rate", flashRate);
        buffer.addAttribute("Laser Function", function, LASER_FUNCTION.class);
        buffer.addAttribute("Dead Reckoning Algorithm", algorithm, DEAD_RECKONING.class);
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Spot Location", spotLocation.toString());
        buffer.addAttribute("Spot Offset on Object", spotOffset.toString());
        buffer.addAttribute("Beam Origin Offset", beamOffset.toString());
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 12 bytes (header)
        super.read(stream);

        // 6 bytes
        entity.read(stream);

        // 1 byte
        spotType = stream.readUnsignedByte();

        // 1 byte
        systemName = stream.readUnsignedByte();

        // 6 bytes
        object.read(stream);

        // 2 bytes
        designatorCode = stream.readUnsignedShort();

        // 4 bytes
        designatorPower = stream.readFloat();

        // 4 bytes
        designatorWavelength = stream.readFloat();

        // 12 bytes
        spotOffset.read(stream);

        // 24 bytes
        spotLocation.read(stream);

        // 1 byte
        algorithm = stream.readUnsignedByte();

        // 1 byte
        flashRate = stream.readUnsignedByte();

        // 1 byte
        designatorSystemNumber = stream.readUnsignedByte();

        // 1 byte
        function = stream.readUnsignedByte();

        // 12 bytes
        beamOffset.read(stream);
    }
}
