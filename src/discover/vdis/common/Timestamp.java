/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Binary;
import discover.common.Readable;

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
        
        this.updateValue();
    }
    
    public Timestamp(int value) {
        
        this.value = value;
        this.updateAttributes();
    }

    public int getValue() { return this.value; }
    public int getMinutes() { return this.minutes; }
    public float getSeconds() { return this.seconds; }
    public boolean isAbsolute() { return this.absolute; }

    public void setValue(int value) {
    
        this.value = value;
        this.updateAttributes();
    }

    public void setValue(Timestamp timestamp) {
    
        this.value = timestamp.value;
        this.updateAttributes();
    }
    
    public void setMinutes(int minutes) {
    
        this.minutes = minutes;
        this.updateValue();
    }
    
    public void setSeconds(float seconds) {
    
        this.seconds = seconds;
        this.updateValue();
    }
    
    public void setAbsolute(boolean absolute) {
    
        this.absolute = absolute;
        this.updateValue();
    }
    
    public void add(float seconds) {

        int minutes = (int)(seconds / 60.0f);
        
        seconds -= (60.0f * minutes);
        
        this.minutes += minutes;
        this.seconds += seconds;
        this.updateValue();
    }

    public void clear() {
    
        this.minutes = 0;
        this.seconds = 0.0f;
        this.absolute = false;
    }
    
    @Override
    public void read(DataInputStream stream) throws IOException {

        this.value = stream.readInt();
        this.updateAttributes();
    }
    
    public void write(DataOutputStream stream) throws IOException {
        
        stream.writeInt(this.value);
    }
    
    @Override
    public Timestamp clone() {
        
        Timestamp copy = new Timestamp();
        
        copy.absolute = this.absolute;
        copy.minutes = this.minutes;
        copy.seconds = this.seconds;
        copy.updateValue();

        return copy;
    }
    
    @Override
    public String toString() {
        
        StringBuffer buffer = new StringBuffer();

        buffer.append(this.minutes);
        buffer.append("m ");
        buffer.append(formatter.format(this.seconds));
        buffer.append(this.absolute ? "s ABS" : "s REL");
        
        return buffer.toString();
    }
    
    private void updateAttributes() {
        
        float time = 0.0f;
        
        this.absolute = (Binary.get1Bit(0, this.value) > 0);

        time = (TIME_UNIT_TO_SECONDS * (this.value >>> 1));
        
        this.minutes = (int)(time / 60.0);
        
        time -= (this.minutes * 60.0);
        
        this.seconds = time;
    }
    
    private void updateValue() {
        
        float time = (this.seconds + (this.minutes * 60.0f));
        
        long units = (long)(time * SECONDS_TO_TIME_UNITS);

        units = units << 1;
        
        this.value = (int)(units & 0xFFFFFFFF);

        this.value = Binary.set1Bit(0, this.value, (this.absolute ? 1 : 0));
    }
}
