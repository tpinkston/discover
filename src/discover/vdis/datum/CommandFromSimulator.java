package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.vdis.common.EntityId;
import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;

/**
 * @author Tony Pinkston
 */
public class CommandFromSimulator extends AbstractDatumRecord implements Writable {

    private EntityId id = new EntityId();
    private EntityType type = null;
    private String name = null;

    public CommandFromSimulator(int id) {

        super(id);
    }

    public EntityId getEntityId() { return id; }
    public EntityType getEntityType() { return type; }
    public String getName() { return name; }

    public void setName(String name) {

        this.name = name;
    }

    public void setEntityType(EntityType type) {

        this.type = type;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Datum Id", getDatumId());
        buffer.addAttribute("Datum Length (bytes)", Integer.toString(getValueSizeInBytes()));

        if (id != null) {

            buffer.addAttribute("Entity Id", id.toString());
        }
        else {

            buffer.addAttribute("Entity Id", "(null)");
        }

        buffer.addAttribute("Entity Name", name);

        if (type != null) {

            buffer.addLabel("Entity Type:");
            buffer.addBreak();
            type.toBuffer(buffer);
        }
        else {

            buffer.addAttribute("Entity Type", "(null)");
        }

        buffer.addBreak();
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        super.read(stream); // Record length (DatumId already read)

        type = EntityTypes.read(stream);

        id.read(stream);

        int length = stream.readUnsignedShort();
        byte bytes[] = new byte[length];

        stream.read(bytes, 0, length);
        stream.skipBytes(8 - (length % 8));

        name = new String(bytes);
        name.trim();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        final int length = name.length();
        final int padding = (8 - (length % 8));
        final int size = (length + padding);

        // Write DatumId
        stream.writeInt(getDatumId()); // 4 bytes

        // Write record length (bits), add 16 bytes for EntityType and EntityId
        stream.writeInt((size + 16) * 8);

        type.write(stream); // 8 bytes
        id.write(stream); // 6 bytes

        stream.writeShort(length); // 2 bytes
        stream.writeBytes(name);

        for(int i = 0; i < padding; ++i) {

            stream.writeByte(0);
        }
    }
}
