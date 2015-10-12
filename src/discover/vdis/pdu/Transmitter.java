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

/**
 * @author Tony Pinkston
 */
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

    public EntityId getEntityId() { return entityId; }
    public EntityType getRadioType() { return radioType; }
    public int getTransmitState() { return transmitState; }
    public int getInputSource() { return inputSource; }
    public Location24 getLocation() { return location; }
    public Location12 getRelativeLocation() { return relativeLocation; }
    public int getAntennaPattern() { return antennaPattern; }
    public int getRadioId() { return radioId; }

    public void setTransmitState(int state) {

        transmitState = state;
    }

    public void setInputSource(int source) {

        inputSource = source;
    }

    public void setAntennaPattern(int pattern) {

        antennaPattern = pattern;
    }

    public void setRadioId(int id) {

        radioId = id;
    }

    @Override
    public void clear() {

        entityId.clear();
        radioId = 0;
        radioType = null;
        transmitState = 0;
        inputSource = 0;
        location.clear();
        relativeLocation.clear();
        antennaPattern = 0;
        spreadSpectrum.set((short)0);
        majorModulation = 0;
        modulationDetail = 0;
        radioSystem = 0;
        cryptoSystem = 0;
        antennaPatternsLength = 0;
        modulationParametersLength = 0;
        cryptoKey = 0;
        frequency = 0L;
        bandwidth = 0.0f;
        power = 0.0f;
        antennaPatterns = null;
        modulationParameters = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        super.toBuffer(buffer);

        buffer.addTitle("IDENTIFICATION");
        buffer.addAttribute("Entity", entityId.toString());
        buffer.addAttribute("Radio", radioId);
        buffer.addBreak();

        buffer.addTitle("TYPE");
        radioType.toBuffer(buffer);
        buffer.addBreak();

        buffer.addTitle("SPATIAL");
        buffer.addAttribute("Location", location.toString());
        buffer.addAttribute("Relative Location", relativeLocation.toString());
        buffer.addBreak();

        buffer.addTitle("STATUS");
        buffer.addAttribute(
            "Transmit State",
            VDIS.getDescription(VDIS.TRANSMIT_STATE, transmitState));
        buffer.addAttribute(
            "Input Source",
            VDIS.getDescription(VDIS.INPUT_SOURCE, inputSource));
        buffer.addBreak();

        buffer.addTitle("SPECIFICATION");
        buffer.addAttribute(
            "Radio System",
            VDIS.getDescription(VDIS.RADIO_SYSTEM, radioSystem));
        buffer.addAttribute("Frequency (Hz)", frequency);
        buffer.addAttribute("Bandwidth", bandwidth);
        buffer.addAttribute("Power (dBm)", power);
        buffer.addAttribute(
            "Antenna Pattern",
            VDIS.getDescription(
                VDIS.ANTENNA_PATTERN_TYPE,
                antennaPattern));
        buffer.addLabel("Antenna Pattern Parameters");

        if (antennaPatterns == null) {

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
                antennaPatterns);
        }

        buffer.addBreak();

        buffer.addTitle("MODULATION");
        buffer.addAttribute(
            "Modulation",
            VDIS.getDescription(VDIS.MAJOR_MODULATION, majorModulation));

        int detailType = VDIS.AMPLITUDE;

        switch(majorModulation) {

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
            VDIS.getDescription(detailType, modulationDetail));

        buffer.addLabel("Parameters");

        if (modulationParameters == null) {

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
                modulationParameters);
        }

        buffer.addBreak();

        buffer.addTitle("SPREAD SPECTRUM");
        buffer.addBuffer(spreadSpectrum);
        buffer.addBreak();

        buffer.addTitle("CRYPTO");
        buffer.addAttribute(
            "System",
            VDIS.getDescription(VDIS.CRYPTO_SYS, cryptoSystem));
        buffer.addAttribute("Key", cryptoKey);
        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // (header)

        entityId.read(stream);
        radioId = stream.readUnsignedShort();
        radioType = EntityTypes.read(stream);
        transmitState = stream.readUnsignedByte();
        inputSource = stream.readUnsignedByte();

        // Padding (Transmitter VPR count under V-DIS)
        stream.skipBytes(2);

        location.read(stream);
        relativeLocation.read(stream);
        antennaPattern = stream.readUnsignedShort();
        antennaPatternsLength = stream.readUnsignedShort();
        frequency = stream.readLong();
        bandwidth = stream.readFloat();
        power = stream.readFloat();
        spreadSpectrum.read(stream);
        majorModulation = stream.readUnsignedShort();
        modulationDetail = stream.readUnsignedShort();
        radioSystem = stream.readUnsignedShort();
        cryptoSystem = stream.readUnsignedShort();
        cryptoKey = stream.readUnsignedShort();
        modulationParametersLength = stream.readUnsignedByte();

        // Padding
        stream.readByte();
        stream.readByte();
        stream.readByte();

        if (modulationParametersLength > 0) {

            modulationParameters = new byte[modulationParametersLength];

            for(int i = 0; i < modulationParametersLength; ++i) {

                modulationParameters[i] = stream.readByte();
            }
        }

        if (antennaPatternsLength > 0) {

            modulationParameters = new byte[antennaPatternsLength];

            for(int i = 0; i < antennaPatternsLength; ++i) {

                antennaPatterns[i] = stream.readByte();
            }
        }
    }
}
