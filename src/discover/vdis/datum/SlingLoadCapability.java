package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.enums.DATUM_IDS;
import discover.vdis.enums.HOOK_TYPE;

/**
 * @author Tony Pinkston
 */
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

        buffer.addAttribute("Datum Id", getDatumId(), DATUM_IDS.class);
        buffer.addAttribute("Datum Length (bytes)", Integer.toString(getValueSizeInBytes()));
        buffer.addAttribute("Payload", payload.toString());
        buffer.addAttribute("Carrier", carrier.toString());
        buffer.addAttribute("Hook Type", hookType, HOOK_TYPE.class);
        buffer.addAttribute("Drag Coefficient", dragCoeffficient);
        buffer.addAttribute("Current Mass (kg)", currentMass);
        buffer.addLabel("Lines Needed");

        if (linesNeeded < 1) {

            buffer.addItalic("None");
        }
        else {

            buffer.listStart();

            for(int i = 0; i < linesNeeded; ++i) {

                buffer.listItemStart();
                buffer.addLabel("Line Length (m)");
                buffer.addItalic(formatter.format(lengths.get(i)));
                buffer.addLabel(", Hook Offset (m)");
                buffer.addItalic(formatter.format(offsets.get(i)));
                buffer.listItemFinished();
            }

            buffer.listFinished();
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (record type already read)

        payload.read(stream); // 6 bytes
        carrier.read(stream); // 6 bytes
        dragCoeffficient = stream.readFloat(); // 4 bytes
        currentMass = stream.readFloat(); // 4 bytes
        stream.skipBytes(2); // 2 bytes padding
        hookType = stream.readUnsignedByte(); // 1 byte
        linesNeeded = stream.readUnsignedByte(); // 1 byte

        for(int i = 0; i < linesNeeded; ++i) {

            lengths.add(stream.readFloat()); // 4 bytes
            offsets.add(stream.readFloat()); // 4 bytes
        }
    }
}
