/**
 * @author Tony Pinkston
 */
package discover.vdis.datum;

import java.io.DataInputStream;
import java.io.IOException;

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

            // DID_CDT_WAYPOINT:
            case 405100:
                record = new CDTWaypoint(id);
                break;

            // DID_CDT_GENERAL_DISCOVERY:
            case 400100:
                record = new CDTGeneralDiscoveryRecord(id);
                break;

            // DID_CDT_SPECIFIC_CONNECTION:
            case 400200:
                record = new CDTSpecificConnectionRecord(id);
                break;

            // DID_CDT_ONESAF_CONFIGURATION:
            case 400400:
                record = new CDTOneSAFConfigurationRecord(id);
                break;

            // DID_CDT_APPLICATION_CONFIGURATION:
            case 400500:
                record = new CDTApplicationConfiguration(id);
                break;

            // DID_CDT_CONTROLLER_PING:
            case 986881:
                record = new CDTOneSAFControllerPingRecord(id);
                break;

            // DID_CDT_CONTROLLER_PING:
            case 986882:
                record = new CDTOneSAFControllerKillRecord(id);
                break;

            // DID_CDT_ONESAF_NODE:
            case 986883:
                record = new CDTOneSAFControllerNodeRecord(id);
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
