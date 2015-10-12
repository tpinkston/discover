package discover.system;

import java.util.List;

import discover.vdis.PDU;

/**
 * @author Tony Pinkston
 */
public interface CaptureThreadListener {

    public void pdusCaptured(List<PDU> list);
}
