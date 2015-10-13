package discover.vdis.vprecords;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.Bufferable;

/**
 * @author Tony Pinkston
 */
public abstract class AbstractVPRecord implements Bufferable, Readable, Writable {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractVPRecord.class);

    private int type = -1; // VP_RECORD_TYPE
    private int domain = -1;

    protected AbstractVPRecord(int value) {

        type = value;
    }

    public abstract int getLength();

    public int getRecordType() {

        return type;
    }

    public void setRecordType(int value) {

        type = value;
    }

    public int getDomain() {

        return domain;
    }

    public void setDomain(int value) {

        domain = value;
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(type);
    }
}
