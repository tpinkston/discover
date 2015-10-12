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

    // TODO: Make private
    protected int type = -1; // VP_RECORD_TYPE
    protected int domain = -1;

    protected AbstractVPRecord(int type) {

        this.type = type;
    }

    public abstract int getLength();

    public int getRecordType() {

        return type;
    }

    public void setRecordType(int type) {

        this.type = type;
    }

    public int getDomain() {

        return domain;
    }

    public void setDomain(int domain) {

        this.domain = domain;
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(type);
    }
}
