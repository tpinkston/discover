package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Binary;
import discover.common.Readable;

/**
 * @author Tony Pinkston
 */
public class Timestamp implements Readable {

    private static final float TIME_UNIT_TO_SECONDS;
    private static final float SECONDS_TO_TIME_UNITS;

    private static final NumberFormat formatter;

    private int value = 0;
    private int minutes = 0;
    private float seconds = 0.0f;
    private boolean absolute = false;

    static {

        TIME_UNIT_TO_SECONDS = (float)(3600.0 / (Math.pow(2.0, 31.0) - 1.0));
        SECONDS_TO_TIME_UNITS = (1.0f / TIME_UNIT_TO_SECONDS);

        formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(3);
    }

    public Timestamp() {

        updateValue();
    }

    public Timestamp(int value) {

        this.value = value;
        updateAttributes();
    }

    public int getValue() { return value; }
    public int getMinutes() { return minutes; }
    public float getSeconds() { return seconds; }
    public boolean isAbsolute() { return absolute; }

    public void setValue(int value) {

        this.value = value;
        updateAttributes();
    }

    public void setValue(Timestamp timestamp) {

        value = timestamp.value;
        updateAttributes();
    }

    public void setMinutes(int minutes) {

        this.minutes = minutes;
        updateValue();
    }

    public void setSeconds(float seconds) {

        this.seconds = seconds;
        updateValue();
    }

    public void setAbsolute(boolean absolute) {

        this.absolute = absolute;
        updateValue();
    }

    public void add(float timeSeconds) {

        int timeMinutes = (int)(timeSeconds / 60.0f);

        timeSeconds -= (60.0f * timeMinutes);

        minutes += timeMinutes;
        seconds += timeSeconds;
        updateValue();
    }

    public void clear() {

        minutes = 0;
        seconds = 0.0f;
        absolute = false;
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        value = stream.readInt();
        updateAttributes();
    }

    public void write(DataOutputStream stream) throws IOException {

        stream.writeInt(value);
    }

    @Override
    public Timestamp clone() {

        Timestamp copy = new Timestamp();

        copy.absolute = absolute;
        copy.minutes = minutes;
        copy.seconds = seconds;
        copy.updateValue();

        return copy;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();

        buffer.append(minutes);
        buffer.append("m ");
        buffer.append(formatter.format(seconds));
        buffer.append(absolute ? "s ABS" : "s REL");

        return buffer.toString();
    }

    private void updateAttributes() {

        float time = 0.0f;

        absolute = (Binary.get1Bit(0, value) > 0);

        time = (TIME_UNIT_TO_SECONDS * (value >>> 1));

        minutes = (int)(time / 60.0);

        time -= (minutes * 60.0);

        seconds = time;
    }

    private void updateValue() {

        float time = (seconds + (minutes * 60.0f));

        long units = (long)(time * SECONDS_TO_TIME_UNITS);

        units = units << 1;

        value = (int)(units & 0xFFFFFFFF);

        value = Binary.set1Bit(0, value, (absolute ? 1 : 0));
    }
}
