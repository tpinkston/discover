/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

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
        
        this.read(stream);
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("BEAM DATA (" + this.beamIdNumber + ")");
        buffer.addAttribute(
            "Function",
            this.function,
            VDIS.BEAM_FUNCTION);
        buffer.addAttribute("Parameter Index", this.beamParameterIndex);
        buffer.addAttribute("Status", (int)this.beamStatus);
        buffer.addAttribute(
            "High Density",
            this.highDensityTrackJam,
            VDIS.YESNO);
        buffer.addAttribute("Targets", this.targetCount);
        buffer.addAttribute("Jamming Technique", this.jammingTechnique);
        buffer.addAttribute("Data Length", this.dataLength);
        
        buffer.addTitle("FUNDAMENTAL PARAMETER DATA");
        buffer.addAttribute("Frequency", this.frequency);
        buffer.addAttribute("Frequency Range", this.frequencyRange);
        buffer.addAttribute("Effective Radiated Power", this.effectiveRadiatedPower);
        buffer.addAttribute("Pulse Repetition Frequency", this.pulseRepetitionFrequency);
        buffer.addAttribute("Pulse Width", this.pulseWidth);
        
        buffer.addTitle("BEAM DATA");
        buffer.addAttribute("Azimuth Center", this.azimuthCenter);
        buffer.addAttribute("Azimuth Sweep", this.azimuthSweep);
        buffer.addAttribute("Elevation Center", this.elevationCenter);
        buffer.addAttribute("Elevation Sweep", this.elevationSweep);
        buffer.addAttribute("Sweep Sync", this.sweepSync);
        
        for(int i = 0, size = this.targets.size(); i < size; ++i) {

            buffer.addBuffer(this.targets.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        
        // 1 byte
        this.dataLength = stream.readUnsignedByte();
        
        // 1 byte
        this.beamIdNumber = stream.readUnsignedByte();
        
        // 2 bytes
        this.beamParameterIndex = stream.readUnsignedShort();

        // Fundamental Parameter Data
        this.frequency = stream.readFloat(); // 4 bytes
        this.frequencyRange = stream.readFloat(); // 4 bytes
        this.effectiveRadiatedPower = stream.readFloat(); // 4 bytes
        this.pulseRepetitionFrequency = stream.readFloat(); // 4 bytes
        this.pulseWidth = stream.readFloat(); // 4 bytes

        // Beam Data
        this.azimuthCenter = stream.readFloat(); // 4 bytes
        this.azimuthSweep = stream.readFloat(); // 4 bytes
        this.elevationCenter = stream.readFloat(); // 4 bytes
        this.elevationSweep = stream.readFloat(); // 4 bytes
        this.sweepSync = stream.readFloat(); // 4 bytes
        
        // 1 byte
        this.function = stream.readUnsignedByte();
        
        // 1 Byte
        this.targetCount = stream.readUnsignedByte();
        
        // 1 byte
        this.highDensityTrackJam = stream.readUnsignedByte();
        
        // 1 byte
        this.beamStatus = stream.readByte();        
        
        // 4 bytes
        this.jammingTechnique = stream.readInt();
        
        for(int i = 0; i < this.targetCount; ++i) {
            
            this.targets.add(new EmitterTarget(stream));
        }
    }
}
