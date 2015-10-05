/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Common;
import discover.common.Readable;
import discover.common.Writable;

public class Orientation implements Readable, Writable  {

    public static final int LENGTH = 12;

    private static final NumberFormat formatter = NumberFormat.getInstance();

    static {
        
        formatter.setMinimumFractionDigits(1);
        formatter.setMaximumFractionDigits(4);
    }

    private float psi = 0.0f;
    private float theta = 0.0f;
    private float phi = 0.0f;
    
    public Orientation() { }

    public Orientation(float psi, float theta, float phi) {
        
        this.psi = psi;
        this.theta = theta;
        this.phi = phi;
    }

    public float getPsi() { return this.psi; }
    public void setPsi(float psi) { this.psi = psi; }

    public float getTheta() { return this.theta; }
    public void setTheta(float theta) { this.theta = theta; }

    public float getPhi() { return this.phi; }
    public void setPhi(float phi) { this.phi = phi; }
    
    public void clear() {
        
        this.psi = 0.0f;
        this.theta = 0.0f;
        this.phi = 0.0f;
    }

    @Override
    public String toString() {
        
        float tempPsi = (float)Common.clampToPI(this.psi);
        float tempTheta = (float)Common.clampToHalfPI(this.theta);
        float tempPhi = (float)Common.clampToPI(this.phi);

        return "(psi: " + formatter.format(tempPsi) + 
               ", theta: " + formatter.format(tempTheta) +
               ", phi: " + formatter.format(tempPhi) + ")";
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        this.setPsi(stream.readFloat());
        this.setTheta(stream.readFloat());
        this.setPhi(stream.readFloat());
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeFloat(this.psi);
        stream.writeFloat(this.theta);
        stream.writeFloat(this.phi);
    }
}
