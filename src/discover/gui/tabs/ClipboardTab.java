package discover.gui.tabs;

import java.util.ArrayList;

import discover.vdis.PDU;

/**
 * @author Tony Pinkston
 */
public interface ClipboardTab {

    public void cut(ArrayList<PDU> clipboard);

    public void copy(ArrayList<PDU> clipboard);

    public void paste(ArrayList<PDU> clipboard);

    public void updateClipboardStatus(ArrayList<PDU> clipboard);
}
