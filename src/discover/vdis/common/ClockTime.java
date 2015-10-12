package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.DateFormat;

import discover.common.Common;
import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;

/**
 * @author Tony Pinkston
 */
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

        timestamp.clear();
        hours = 0L;
        system = 0L;
        date = null;
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Hours", hours);
        buffer.addAttribute("Time Past Hour", timestamp.toString());
        buffer.addAttribute("Epoch", date);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        hours = Common.toUnsigned32(stream.readInt());
        timestamp.read(stream);

        system = (hours * MILLIS_PER_HOUR);
        system += (timestamp.getMinutes() * MILLIS_PER_MINUTE);
        system += (int)(timestamp.getSeconds() * MILLIS_PER_SECOND);

        date = format.format(system);
    }
}
