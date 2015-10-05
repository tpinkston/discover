/**
 * @author Tony Pinkston
 */
package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Hexadecimal;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location12;
import discover.vdis.common.Location24;
import discover.vdis.common.SpreadSpectrum;
import discover.vdis.enums.VDIS;
import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;

public class Transmitter extends AbstractPDU {

    private EntityId entityId = new EntityId();
    private EntityType radioType = null;
    private Location24 location = new Location24();
    private Location12 relativeLocation = new Location12();
    private SpreadSpectrum spreadSpectrum = new SpreadSpectrum();
    private int transmitState = 0;
    private int inputSource = 0;
    private int antennaPattern = 0;
    private int majorModulation = 0;
    private int modulationDetail = 0;
    private int radioSystem = 0;
    private int cryptoSystem = 0;
    private int radioId = 0;
    private int antennaPatternsLength = 0;
    private int modulationParametersLength = 0;
    private int cryptoKey = 0;
    private long frequency = 0L;
    private float bandwidth = 0.0f;
    private float power = 0.0f;
    private byte antennaPatterns[] = null;
    private byte modulationParameters[] = null;

    public Transmitter() {

    }
    
    public EntityId getEntityId() { return this.entityId; }
    public EntityType getRadioType() { return this.radioType; }
    public int getTransmitState() { return this.transmitState; }
    public int getInputSource() { return this.inputSource; }
    public Location24 getLocation() { return this.location; }
    public Location12 getRelativeLocation() { return this.relativeLocation; }
    public int getAntennaPattern() { return this.antennaPattern; }
    public int getRadioId() { return this.radioId; }

    public void setTransmitState(int state) {
    
        this.transmitState = state;
    }

    public void setInputSource(int source) {
    
        this.inputSource = source;
    }

    public void setAntennaPattern(int pattern) {
    
        this.antennaPattern = pattern;
    }

    public void setRadioId(int id) {
    
        this.radioId = id;
    }
    
    @Override
    public void clear() {
        
        this.entityId.clear();
        this.radioId = 0;
        this.radioType = null;
        this.transmitState = 0;
        this.inputSource = 0;
        this.location.clear();
        this.relativeLocation.clear();
        this.antennaPattern = 0;
        this.spreadSpectrum.set((short)0);
        this.majorModulation = 0;
        this.modulationDetail = 0;
        this.radioSystem = 0;
        this.cryptoSystem = 0;
        this.antennaPatternsLength = 0;
        this.modulationParametersLength = 0;
        this.cryptoKey = 0;
        this.frequency = 0L;
        this.bandwidth = 0.0f;
        this.power = 0.0f;
        this.antennaPatterns = null;
        this.modulationParameters = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Entity", this.entityId.toString());
        buffer.addAttribute("Radio", this.radioId);
        buffer.addBreak();

        buffer.addTitle("TYPE");
        this.radioType.toBuffer(buffer);
        buffer.addBreak();
        
        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Location", this.location.toString());
        buffer.addAttribute("Relative Location", this.relativeLocation.toString());
        buffer.addBreak();

        buffer.addTitle("STATUS");
        buffer.addAttribute(
            "Transmit State", 
            VDIS.getDescription(VDIS.TRANSMIT_STATE, this.transmitState));
        buffer.addAttribute(
            "Input Source", 
            VDIS.getDescription(VDIS.INPUT_SOURCE, this.inputSource));
        buffer.addBreak();

        buffer.addTitle("SPECIFICATION");
        buffer.addAttribute(
            "Radio System", 
            VDIS.getDescription(VDIS.RADIO_SYSTEM, this.radioSystem));
        buffer.addAttribute("Frequency (Hz)", this.frequency);
        buffer.addAttribute("Bandwidth", this.bandwidth);
        buffer.addAttribute("Power (dBm)", this.power);
        buffer.addAttribute(
            "Antenna Pattern", 
            VDIS.getDescription(
                VDIS.ANTENNA_PATTERN_TYPE, 
                this.antennaPattern));
        buffer.addLabel("Antenna Pattern Parameters");
        
        if (this.antennaPatterns == null) {
            
            buffer.addText("None");
            buffer.addBreak();
        }
        else {

            buffer.addBreak();
            Hexadecimal.toBuffer(
                buffer,
                " - ", 
                4, 
                false, 
                this.antennaPatterns);
        }
        
        buffer.addBreak();

        buffer.addTitle("MODULATION");
        buffer.addAttribute(
            "Modulation", 
            VDIS.getDescription(VDIS.MAJOR_MODULATION, this.majorModulation));
        
        int detailType = VDIS.AMPLITUDE;
        
        switch(this.majorModulation) {
            
            case 1: // MAJ_MOD_AMPLITUDE
                detailType = VDIS.AMPLITUDE;
                break;
            case 2: // MAJ_MOD_AMPLITUDE_AND_ANGLE
                detailType = VDIS.AMPLITUDE_AND_ANGLE;
                break;
            case 3: // MAJ_MOD_ANGLE
                detailType = VDIS.ANGLE;
                break;
            case 4: // MAJ_MOD_COMBINATION
                detailType = VDIS.COMBINATION;
                break;
            case 5: // MAJ_MOD_PULSE
                detailType = VDIS.PULSE;
                break;
            case 6: // MAJ_MOD_UNMODULATED
                detailType = VDIS.UNMODULATED;
                break;
        }

        buffer.addAttribute(
            "Detail", 
            VDIS.getDescription(detailType, this.modulationDetail));
        
        buffer.addLabel("Parameters");
        
        if (this.modulationParameters == null) {
            
            buffer.addText("None");
            buffer.addBreak();
        }
        else {

            buffer.addBreak();
            Hexadecimal.toBuffer(
                buffer,
                " - ", 
                4, 
                false, 
                this.modulationParameters);
        }
        
        buffer.addBreak();

        buffer.addTitle("SPREAD SPECTRUM");
        buffer.addBuffer(this.spreadSpectrum);
        buffer.addBreak();
        
        buffer.addTitle("CRYPTO");
        buffer.addAttribute(
            "System", 
            VDIS.getDescription(VDIS.CRYPTO_SYS, this.cryptoSystem));
        buffer.addAttribute("Key", this.cryptoKey);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)
        
        this.entityId.read(stream);
        this.radioId = stream.readUnsignedShort();
        this.radioType = EntityTypes.read(stream);
        this.transmitState = stream.readUnsignedByte();
        this.inputSource = stream.readUnsignedByte();
        
        // Padding (Transmitter VPR count under V-DIS)
        stream.skipBytes(2);
        
        this.location.read(stream);
        this.relativeLocation.read(stream);
        this.antennaPattern = stream.readUnsignedShort();
        this.antennaPatternsLength = stream.readUnsignedShort();
        this.frequency = stream.readLong();
        this.bandwidth = stream.readFloat();
        this.power = stream.readFloat();
        this.spreadSpectrum.read(stream);
        this.majorModulation = stream.readUnsignedShort();
        this.modulationDetail = stream.readUnsignedShort(); 
        this.radioSystem = stream.readUnsignedShort();
        this.cryptoSystem = stream.readUnsignedShort();
        this.cryptoKey = stream.readUnsignedShort();
        this.modulationParametersLength = stream.readUnsignedByte();

        // Padding
        stream.readByte();
        stream.readByte();
        stream.readByte();

        if (this.modulationParametersLength > 0) {
        
            this.modulationParameters = new byte[this.modulationParametersLength];
            
            for(int i = 0; i < this.modulationParametersLength; ++i) {
                
                this.modulationParameters[i] = stream.readByte();
            }
        }

        if (this.antennaPatternsLength > 0) {
        
            this.modulationParameters = new byte[this.antennaPatternsLength];
            
            for(int i = 0; i < this.antennaPatternsLength; ++i) {
                
                this.antennaPatterns[i] = stream.readByte();
            }
        }
    }
}
