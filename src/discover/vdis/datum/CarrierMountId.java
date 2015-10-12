package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;

/**
 * @author Tony Pinkston
 */
public class CarrierMountId extends AbstractDatumRecord {

    public CarrierMountId(int id) {

        super(id);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

    }

    @Override
    public void read(DataInputStream stream) throws IOException {

    }
}
