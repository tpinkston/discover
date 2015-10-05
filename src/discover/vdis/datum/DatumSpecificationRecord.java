/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import discover.Discover;
import discover.common.Common;
import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;

public class DatumSpecificationRecord implements Bufferable, Readable, Writable {

    private static final Logger logger = Discover.getLogger();

    private final List<FixedDatumRecord> fixed = new ArrayList<FixedDatumRecord>();
    private final List<AbstractDatumRecord> variable = new ArrayList<AbstractDatumRecord>();
    private long fixedLength = 0;
    private long variableLength = 0;

    public List<FixedDatumRecord> getFixed() { return this.fixed; }
    public List<AbstractDatumRecord> getVariable() { return this.variable; }

    public long getFixedLength() { return this.fixedLength; }
    public long getVariableLength() { return this.variableLength; }

    public void clear() {

        this.fixed.clear();
        this.variable.clear();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("FIXED DATUM RECORDS (" + this.fixedLength + ")");

        for(int i = 0, size = this.fixed.size(); i < size; ++i) {

            buffer.addBoldLabel("Record " + (i + 1));
            buffer.addBreak();
            buffer.addBuffer(this.fixed.get(i));
        }

        buffer.addBreak();
        buffer.addTitle("VARIABLE DATUM RECORDS (" + this.variableLength + ")");

        for(int i = 0, size = this.variable.size(); i < size; ++i) {

            buffer.addBoldLabel("Record " + (i + 1));
            buffer.addBreak();
            buffer.addBuffer(this.variable.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        AbstractDatumRecord record = null;
        this.fixedLength = Common.toUnsigned32(stream.readInt()); // 4 bytes
        this.variableLength = Common.toUnsigned32(stream.readInt()); // 4 bytes

        for(int i = 0; i < this.fixedLength; ++i) {

            this.fixed.add(new FixedDatumRecord(stream));
        }

        for(int i = 0; i < this.variableLength; ++i) {

            record = DatumRecordFactory.getVariableRecord(stream);

            if (record != null) {

                this.variable.add(record);
            }
        }
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(this.fixed.size());
        stream.writeInt(this.variable.size());

        for(FixedDatumRecord record : this.fixed) {

            record.write(stream);
        }

        for(AbstractDatumRecord record : this.variable) {

            if (record instanceof Writable) {

                Writable writable = (Writable)record;

                writable.write(stream);
            }
            else {

                logger.severe(
                    "Variable Datum Record is not writable: " +
                    record.getClass().getName());
            }
        }
    }
}
