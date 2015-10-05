/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;

public class IFFFundamentalOperationalData implements Bufferable, Readable {

    private IFFSystemStatus status = new IFFSystemStatus();
    private IFFInformationLayers informationLayers = new IFFInformationLayers();
    private byte dataField1 = 0x00;
    private byte dataField2 = 0x00;
    private short parameter1 = 0x00;
    private short parameter2 = 0x00;
    private short parameter3 = 0x00;
    private short parameter4 = 0x00;
    private short parameter5 = 0x00;
    private short parameter6 = 0x00;
    
    public void clear() {
        
        this.status.set((byte)0x00);
        this.informationLayers.set((byte)0x00);
        this.dataField1 = 0x00;
        this.dataField2 = 0x00;
        this.parameter1 = 0x00;
        this.parameter2 = 0x00;
        this.parameter3 = 0x00;
        this.parameter4 = 0x00;
        this.parameter5 = 0x00;
        this.parameter6 = 0x00;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("System Status");
        buffer.addBuffer(this.status);
        buffer.addTitle("Information Layers");
        buffer.addBuffer(this.informationLayers);
        buffer.addTitle("Data Fields");
        buffer.addAttribute("1", Binary.toString8(this.dataField1));
        buffer.addAttribute("2", Binary.toString8(this.dataField2));
        buffer.addTitle("Parameters");
        buffer.addAttribute("1", Binary.toString16(this.parameter1));
        buffer.addAttribute("2", Binary.toString16(this.parameter2));
        buffer.addAttribute("3", Binary.toString16(this.parameter3));
        buffer.addAttribute("4", Binary.toString16(this.parameter4));
        buffer.addAttribute("5", Binary.toString16(this.parameter5));
        buffer.addAttribute("6", Binary.toString16(this.parameter6));
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.status.read(stream);
        this.dataField1 = stream.readByte();
        this.informationLayers.read(stream);
        this.dataField2 = stream.readByte();
        this.parameter1 = this.readAvailableShort(stream);
        this.parameter2 = this.readAvailableShort(stream);
        this.parameter3 = this.readAvailableShort(stream);
        this.parameter4 = this.readAvailableShort(stream);
        this.parameter5 = this.readAvailableShort(stream);
        
        if (stream.available() > 0) {
            
            this.parameter6 = stream.readShort();
        }
        else {
            
            this.parameter6 = 0x00;
        }
    }
    
    private short readAvailableShort(DataInputStream stream) throws IOException {
        
        if (stream.available() > 1) {
            
            return stream.readShort();
        }
        else {
            
            return 0x00;
        }
    }
}
