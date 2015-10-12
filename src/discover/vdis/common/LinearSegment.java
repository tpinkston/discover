package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.appearance.GenericObjectAppearance;
import discover.vdis.appearance.SpecificObjectAppearance;

/**
 * @author Tony Pinkston
 */
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

        read(stream);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addSeparator();
        buffer.addTitle("SEGMENT " + segment);
        buffer.addAttribute("Modifications", modifications);
        buffer.addAttribute("Location", location.toString());
        buffer.addAttribute("Orientation", orientation.toString());
        buffer.addBreak();

        buffer.addTitle("DIMENSIONS");
        buffer.addAttribute("Length (meters)", length);
        buffer.addAttribute("Width (meters)", width);
        buffer.addAttribute("Height (meters)", height);
        buffer.addAttribute("Depth (meters)", depth);
        buffer.addBreak();

        buffer.addTitle("GENERIC APPEARANCE");
        buffer.addBuffer(generic);
        buffer.addBreak();

        buffer.addTitle("SPECIFIC APPEARANCE");
        buffer.addBuffer(specific);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        segment = stream.readUnsignedByte();
        modifications = stream.readUnsignedByte();
        generic.read(stream);
        specific.read(stream);
        location.read(stream);
        orientation.read(stream);
        length = stream.readUnsignedShort();
        width = stream.readUnsignedShort();
        height = stream.readUnsignedShort();
        depth = stream.readUnsignedShort();

        stream.readInt(); // 32 bits padding
    }
}
