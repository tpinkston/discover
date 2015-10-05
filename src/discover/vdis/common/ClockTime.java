/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.DateFormat;

import discover.common.Common;
import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;

public class ClockTime implements Bufferable, Readable {

    private static final int MILLIS_PER_SECOND = 1000;
    private static final int MILLIS_PER_MINUTE = (MILLIS_PER_SECOND * 60);
    private static final int MILLIS_PER_HOUR = (MILLIS_PER_MINUTE * 60);

    private static final DateFormat format = DateFormat.getDateTimeInstance();

    private Timestamp timestamp = new Timestamp();
    private long hours = 0L;
    private long system = 0L;
    private String date = null;
    
    public void clear() {
        
        this.timestamp.clear();
        this.hours = 0L;
        this.system = 0L;
        this.date = null;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Hours", this.hours);
        buffer.addAttribute("Time Past Hour", this.timestamp.toString());
        buffer.addAttribute("Epoch", this.date);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.hours = Common.toUnsigned32(stream.readInt());
        this.timestamp.read(stream);
        
        this.system = (this.hours * MILLIS_PER_HOUR);
        this.system += (this.timestamp.getMinutes() * MILLIS_PER_MINUTE);
        this.system += (int)(this.timestamp.getSeconds() * MILLIS_PER_SECOND);

        this.date = format.format(this.system);
    }
}
