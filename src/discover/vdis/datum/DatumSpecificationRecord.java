package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Common;
import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;

/**
 * @author Tony Pinkston
 */
public class DatumSpecificationRecord implements Bufferable, Readable, Writable {

    private static final Logger logger = LoggerFactory.getLogger(DatumSpecificationRecord.class);

    private final List<FixedDatumRecord> fixed = new ArrayList<FixedDatumRecord>();
    private final List<AbstractDatumRecord> variable = new ArrayList<AbstractDatumRecord>();
    private long fixedLength = 0;
    private long variableLength = 0;

    public List<FixedDatumRecord> getFixed() { return fixed; }
    public List<AbstractDatumRecord> getVariable() { return variable; }

    public long getFixedLength() { return fixedLength; }
    public long getVariableLength() { return variableLength; }

    public void clear() {

        fixed.clear();
        variable.clear();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addTitle("FIXED DATUM RECORDS (" + fixedLength + ")");

        for(int i = 0, size = fixed.size(); i < size; ++i) {

            buffer.addBoldLabel("Record " + (i + 1));
            buffer.addBreak();
            buffer.addBuffer(fixed.get(i));
        }

        buffer.addBreak();
        buffer.addTitle("VARIABLE DATUM RECORDS (" + variableLength + ")");

        for(int i = 0, size = variable.size(); i < size; ++i) {

            buffer.addBoldLabel("Record " + (i + 1));
            buffer.addBreak();
            buffer.addBuffer(variable.get(i));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        AbstractDatumRecord record = null;
        fixedLength = Common.toUnsigned32(stream.readInt()); // 4 bytes
        variableLength = Common.toUnsigned32(stream.readInt()); // 4 bytes

        for(int i = 0; i < fixedLength; ++i) {

            fixed.add(new FixedDatumRecord(stream));
        }

        for(int i = 0; i < variableLength; ++i) {

            record = DatumRecordFactory.getVariableRecord(stream);

            if (record != null) {

                variable.add(record);
            }
        }
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(fixed.size());
        stream.writeInt(variable.size());

        for(FixedDatumRecord record : fixed) {

            record.write(stream);
        }

        for(AbstractDatumRecord record : variable) {

            if (record instanceof Writable) {

                Writable writable = (Writable)record;

                writable.write(stream);
            }
            else {

                logger.error(
                    "Variable Datum Record is not writable: {}",
                    record.getClass().getName());
            }
        }
    }
}
