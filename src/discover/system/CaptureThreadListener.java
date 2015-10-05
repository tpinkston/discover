/**
 * @author Tony Pinkston
 */
package discover.system;

import java.util.List;

import discover.vdis.PDU;

public interface CaptureThreadListener {

    public void pdusCaptured(List<PDU> list);
}
