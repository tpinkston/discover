/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.appearance.GenericObjectAppearance;
import discover.vdis.appearance.SpecificObjectAppearance;

public class LinearSegment implements Bufferable, Readable {

    private GenericObjectAppearance generic = new GenericObjectAppearance();
    private SpecificObjectAppearance specific = new SpecificObjectAppearance();
    private Location24 location = new Location24();
    private Orientation orientation = new Orientation();
    private int length = 0;
    private int width = 0;
    private int height = 0;
    private int depth = 0;
    private int segment = 0;
    private int modifications = 0;

    public LinearSegment() {

    }

    public LinearSegment(DataInputStream stream) throws IOException {

        this.read(stream);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addSeparator();
        buffer.addTitle("SEGMENT " + this.segment);
        buffer.addAttribute("Modifications", this.modifications);
        buffer.addAttribute("Location", this.location.toString());
        buffer.addAttribute("Orientation", this.orientation.toString());
        buffer.addBreak();

        buffer.addTitle("DIMENSIONS");
        buffer.addAttribute("Length (meters)", this.length);
        buffer.addAttribute("Width (meters)", this.width);
        buffer.addAttribute("Height (meters)", this.height);
        buffer.addAttribute("Depth (meters)", this.depth);
        buffer.addBreak();

        buffer.addTitle("GENERIC APPEARANCE");
        buffer.addBuffer(this.generic);
        buffer.addBreak();

        buffer.addTitle("SPECIFIC APPEARANCE");
        buffer.addBuffer(this.specific);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.segment = stream.readUnsignedByte();
        this.modifications = stream.readUnsignedByte();
        this.generic.read(stream);
        this.specific.read(stream);
        this.location.read(stream);
        this.orientation.read(stream);
        this.length = stream.readUnsignedShort();
        this.width = stream.readUnsignedShort();
        this.height = stream.readUnsignedShort();
        this.depth = stream.readUnsignedShort();

        stream.readInt(); // 32 bits padding
    }
}
