package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Tony Pinkston
 */
public class DatumRecordFactory {

    public static AbstractDatumRecord getVariableRecord(
        DataInputStream stream) throws IOException {

        AbstractDatumRecord record = null;

        // Read the datum ID:
        int id = stream.readInt();

        switch(id) {

            // DID_COMMAND_FROM_SIMULATOR:
            case 15505:
                record = new CommandFromSimulator(id);
                break;

            // DID_TASK_ORG_FORCE_ID_AFFILIATION:
            case 11250:
                record = new ForceIdentification(id);
                break;

            // DID_SLING_LOAD_CAPABILITY:
            case 20030:
                record = new SlingLoadCapability(id);
                break;

            default:
                record = new VariableDatumRecord(id);
        }

        if (stream.available() > 0) {

            // Variable datum records should starting reading from the 5th
            // byte as the the first 4 bytes of the record have already been
            // read to get the datum ID...
            record.read(stream);
        }

        return record;
    }
}
