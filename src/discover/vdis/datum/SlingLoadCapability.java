/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.VDIS;

public class SlingLoadCapability extends AbstractDatumRecord {

    private static final NumberFormat formatter;

    static {

        formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(4);
    }

    private EntityId payload = new EntityId();
    private EntityId carrier = new EntityId();
    private int hookType = 0;
    private List<Float> lengths = new ArrayList<Float>();
    private List<Float> offsets = new ArrayList<Float>();
    private int linesNeeded = 0;
    private float dragCoeffficient = 0.0f;
    private float currentMass = 0.0f;

    public SlingLoadCapability(int id) {

        super(id);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Datum Id", super.getDatumId(), VDIS.DATUM_IDS);
        buffer.addAttribute("Datum Length (bytes)", Integer.toString(super.getValueSizeInBytes()));
        buffer.addAttribute("Payload", this.payload.toString());
        buffer.addAttribute("Carrier", this.carrier.toString());
        buffer.addAttribute("Hook Type", this.hookType, VDIS.HOOK_TYPE);
        buffer.addAttribute("Drag Coefficient", this.dragCoeffficient);
        buffer.addAttribute("Current Mass (kg)", this.currentMass);
        buffer.addLabel("Lines Needed");

        if (this.linesNeeded < 1) {

            buffer.addItalic("None");
        }
        else {

            buffer.listStart();

            for(int i = 0; i < this.linesNeeded; ++i) {

                buffer.listItemStart();
                buffer.addLabel("Line Length (m)");
                buffer.addItalic(formatter.format(this.lengths.get(i)));
                buffer.addLabel(", Hook Offset (m)");
                buffer.addItalic(formatter.format(this.offsets.get(i)));
                buffer.listItemFinished();
            }

            buffer.listFinished();
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)

        this.payload.read(stream); // 6 bytes
        this.carrier.read(stream); // 6 bytes
        this.dragCoeffficient = stream.readFloat(); // 4 bytes
        this.currentMass = stream.readFloat(); // 4 bytes
        stream.skipBytes(2); // 2 bytes padding
        this.hookType = stream.readUnsignedByte(); // 1 byte
        this.linesNeeded = stream.readUnsignedByte(); // 1 byte

        for(int i = 0; i < this.linesNeeded; ++i) {

            this.lengths.add(stream.readFloat()); // 4 bytes
            this.offsets.add(stream.readFloat()); // 4 bytes
        }
    }
}
