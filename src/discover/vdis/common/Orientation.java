package discover.vdis.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

import discover.common.Common;
import discover.common.Readable;
import discover.common.Writable;

/**
 * @author Tony Pinkston
 */
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

    public float getPsi() { return psi; }
    public void setPsi(float psi) { this.psi = psi; }

    public float getTheta() { return theta; }
    public void setTheta(float theta) { this.theta = theta; }

    public float getPhi() { return phi; }
    public void setPhi(float phi) { this.phi = phi; }

    public void clear() {

        psi = 0.0f;
        theta = 0.0f;
        phi = 0.0f;
    }

    @Override
    public String toString() {

        float tempPsi = (float)Common.clampToPI(psi);
        float tempTheta = (float)Common.clampToHalfPI(theta);
        float tempPhi = (float)Common.clampToPI(phi);

        return "(psi: " + formatter.format(tempPsi) +
               ", theta: " + formatter.format(tempTheta) +
               ", phi: " + formatter.format(tempPhi) + ")";
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        setPsi(stream.readFloat());
        setTheta(stream.readFloat());
        setPhi(stream.readFloat());
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeFloat(psi);
        stream.writeFloat(theta);
        stream.writeFloat(phi);
    }
}
