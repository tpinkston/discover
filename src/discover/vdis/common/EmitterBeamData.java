package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class EmitterBeamData implements Bufferable, Readable {

    private List<EmitterTarget> targets = new ArrayList<EmitterTarget>();
    private int function = 0;
    private int highDensityTrackJam = 0;
    private int beamIdNumber = 0;
    private int beamParameterIndex = 0;
    private int dataLength = 0;
    private int targetCount = 0;
    private float frequency = 0.0f;
    private float frequencyRange = 0.0f;
    private float effectiveRadiatedPower = 0.0f;
    private float pulseRepetitionFrequency = 0.0f;
    private float pulseWidth = 0.0f;
    private float azimuthCenter = 0.0f;
    private float azimuthSweep = 0.0f;
    private float elevationCenter = 0.0f;
    private float elevationSweep = 0.0f;
    private float sweepSync = 0.0f;
    private byte beamStatus = 0;
    private long jammingTechnique = 0L;

    public EmitterBeamData(DataInputStream stream) throws IOException {

        read(stream);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("BEAM DATA (" + beamIdNumber + ")");
        buffer.addAttribute(
            "Function",
            function,
            VDIS.BEAM_FUNCTION);
        buffer.addAttribute("Parameter Index", beamParameterIndex);
        buffer.addAttribute("Status", (int)beamStatus);
        buffer.addAttribute(
            "High Density",
            highDensityTrackJam,
            VDIS.YESNO);
        buffer.addAttribute("Targets", targetCount);
        buffer.addAttribute("Jamming Technique", jammingTechnique);
        buffer.addAttribute("Data Length", dataLength);

        buffer.addTitle("FUNDAMENTAL PARAMETER DATA");
        buffer.addAttribute("Frequency", frequency);
        buffer.addAttribute("Frequency Range", frequencyRange);
        buffer.addAttribute("Effective Radiated Power", effectiveRadiatedPower);
        buffer.addAttribute("Pulse Repetition Frequency", pulseRepetitionFrequency);
        buffer.addAttribute("Pulse Width", pulseWidth);

        buffer.addTitle("BEAM DATA");
        buffer.addAttribute("Azimuth Center", azimuthCenter);
        buffer.addAttribute("Azimuth Sweep", azimuthSweep);
        buffer.addAttribute("Elevation Center", elevationCenter);
        buffer.addAttribute("Elevation Sweep", elevationSweep);
        buffer.addAttribute("Sweep Sync", sweepSync);

        for(int i = 0, size = targets.size(); i < size; ++i) {

            buffer.addBuffer(targets.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        // 1 byte
        dataLength = stream.readUnsignedByte();

        // 1 byte
        beamIdNumber = stream.readUnsignedByte();

        // 2 bytes
        beamParameterIndex = stream.readUnsignedShort();

        // Fundamental Parameter Data
        frequency = stream.readFloat(); // 4 bytes
        frequencyRange = stream.readFloat(); // 4 bytes
        effectiveRadiatedPower = stream.readFloat(); // 4 bytes
        pulseRepetitionFrequency = stream.readFloat(); // 4 bytes
        pulseWidth = stream.readFloat(); // 4 bytes

        // Beam Data
        azimuthCenter = stream.readFloat(); // 4 bytes
        azimuthSweep = stream.readFloat(); // 4 bytes
        elevationCenter = stream.readFloat(); // 4 bytes
        elevationSweep = stream.readFloat(); // 4 bytes
        sweepSync = stream.readFloat(); // 4 bytes

        // 1 byte
        function = stream.readUnsignedByte();

        // 1 Byte
        targetCount = stream.readUnsignedByte();

        // 1 byte
        highDensityTrackJam = stream.readUnsignedByte();

        // 1 byte
        beamStatus = stream.readByte();

        // 4 bytes
        jammingTechnique = stream.readInt();

        for(int i = 0; i < targetCount; ++i) {

            targets.add(new EmitterTarget(stream));
        }
    }
}
