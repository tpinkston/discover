/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.buffer.AbstractBuffer;

public class MountDismountData extends AbstractDatumRecord {

    public MountDismountData(int id) {

        super(id);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

    }

    @Override
    public void read(DataInputStream stream) throws IOException {

    }
}
