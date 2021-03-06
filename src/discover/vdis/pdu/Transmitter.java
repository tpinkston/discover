package discover.vdis.pdu;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Hexadecimal;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.common.Location12;
import discover.vdis.common.Location24;
import discover.vdis.common.SpreadSpectrum;
import discover.vdis.enums.AMPLITUDE;
import discover.vdis.enums.AMPLITUDE_AND_ANGLE;
import discover.vdis.enums.ANGLE;
import discover.vdis.enums.ANTENNA_PATTERN_TYPE;
import discover.vdis.enums.COMBINATION;
import discover.vdis.enums.CRYPTO_SYS;
import discover.vdis.enums.INPUT_SOURCE;
import discover.vdis.enums.MAJOR_MODULATION;
import discover.vdis.enums.PULSE;
import discover.vdis.enums.RADIO_SYSTEM;
import discover.vdis.enums.TRANSMIT_STATE;
import discover.vdis.enums.UNMODULATED;
import discover.vdis.enums.Value;
import discover.vdis.enums.Values;
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
        buffer.addAttribute("Transmit State", transmitState, TRANSMIT_STATE.class);
        buffer.addAttribute("Input Source", inputSource, INPUT_SOURCE.class);
        buffer.addBreak();

        buffer.addTitle("SPECIFICATION");
        buffer.addAttribute("Radio System", radioSystem, RADIO_SYSTEM.class);
        buffer.addAttribute("Frequency (Hz)", frequency);
        buffer.addAttribute("Bandwidth", bandwidth);
        buffer.addAttribute("Power (dBm)", power);
        buffer.addAttribute("Antenna Pattern", antennaPattern, ANTENNA_PATTERN_TYPE.class);
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
            MAJOR_MODULATION.get(majorModulation).description);

        Class<? extends Value> detailType = AMPLITUDE.class;

        switch(majorModulation) {

            case 1: // MAJ_MOD_AMPLITUDE
                detailType = AMPLITUDE.class;
                break;
            case 2: // MAJ_MOD_AMPLITUDE_AND_ANGLE
                detailType = AMPLITUDE_AND_ANGLE.class;
                break;
            case 3: // MAJ_MOD_ANGLE
                detailType = ANGLE.class;
                break;
            case 4: // MAJ_MOD_COMBINATION
                detailType = COMBINATION.class;
                break;
            case 5: // MAJ_MOD_PULSE
                detailType = PULSE.class;
                break;
            case 6: // MAJ_MOD_UNMODULATED
                detailType = UNMODULATED.class;
                break;
        }

        buffer.addAttribute(
            "Detail",
            Values.get(modulationDetail, detailType).description);

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
        buffer.addAttribute("System", cryptoSystem, CRYPTO_SYS.class);
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
