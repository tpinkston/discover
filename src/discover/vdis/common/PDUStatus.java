package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.PDU_STATUS_CEI;
import discover.vdis.enums.PDU_STATUS_DMI;
import discover.vdis.enums.PDU_STATUS_DTI;
import discover.vdis.enums.PDU_STATUS_FTI;
import discover.vdis.enums.PDU_STATUS_IAI;
import discover.vdis.enums.PDU_STATUS_LVCI;
import discover.vdis.enums.PDU_STATUS_RAI;
import discover.vdis.enums.PDU_STATUS_TEI;
import discover.vdis.enums.PDU_TYPE;

/**
 * @author Tony Pinkston
 */
public class PDUStatus implements Bufferable, Readable {

    private byte value = 0x00;

    private PDU_STATUS_TEI tei = null;
    private PDU_STATUS_LVCI lvci = null;
    private PDU_STATUS_FTI fti = null;
    private PDU_STATUS_DTI dti = null;
    private PDU_STATUS_CEI cei = null;
    private PDU_STATUS_RAI rai = null;
    private PDU_STATUS_DMI dmi = null;
    private PDU_STATUS_IAI iai = null;

    public byte getValue() { return value; }

    /**
     * Sets enumeration values from byte value depending on the type on
     * which this status is attached (some enumerations apply only to
     * specific PDUs).
     *
     * @param type - {@link PDU_TYPE}
     */
    public void setEnumValues(PDU_TYPE type) {

        tei = null;
        lvci = null;
        fti = null;
        dti = null;
        cei = null;
        rai = null;
        dmi = null;
        iai = null;

        // All PDU types have the CEI value:
        cei = PDU_STATUS_CEI.get(Binary.get1Bit(3, value));

        // All other PDU types have selective values:
        if (type == PDU_TYPE.ENTITY_STATE) {

            dmi = PDU_STATUS_DMI.get(Binary.get1Bit(4, value));
            tei = PDU_STATUS_TEI.get(Binary.get1Bit(0, value));
            lvci = PDU_STATUS_LVCI.get(Binary.get2Bits(2, value));
        }
        else if (type == PDU_TYPE.FIRE) {

            fti = PDU_STATUS_FTI.get(Binary.get1Bit(4, value));
            lvci = PDU_STATUS_LVCI.get(Binary.get2Bits(2, value));
        }
        else if (type == PDU_TYPE.DETONATION) {

            dti = PDU_STATUS_DTI.get(Binary.get2Bits(5, value));
            lvci = PDU_STATUS_LVCI.get(Binary.get2Bits(2, value));
        }
        else if ((type == PDU_TYPE.EM_EMISSION) ||
                 (type == PDU_TYPE.DESIGNATOR) ||
                 (type == PDU_TYPE.IFF)) {

            tei = PDU_STATUS_TEI.get(Binary.get1Bit(0, value));
            lvci = PDU_STATUS_LVCI.get(Binary.get2Bits(2, value));
        }
        else if ((type == PDU_TYPE.TRANSMITTER) ||
                 (type == PDU_TYPE.SIGNAL) ||
                 (type == PDU_TYPE.RECEIVER)) {

            rai = PDU_STATUS_RAI.get(Binary.get2Bits(5, value));
            tei = PDU_STATUS_TEI.get(Binary.get1Bit(0, value));
            lvci = PDU_STATUS_LVCI.get(Binary.get2Bits(2, value));
        }
        else if ((type == PDU_TYPE.INTERCOM_SIGNAL) ||
                 (type == PDU_TYPE.INTERCOM_CONTROL)) {

            iai = PDU_STATUS_IAI.get(Binary.get2Bits(5, value));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        value = stream.readByte();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Status Bits", Binary.toString8(value));

        if ((cei != null) && (cei != PDU_STATUS_CEI.NOT_COUPLED)) {

            buffer.addAttribute("Coupled Extension", cei.description);
        }

        if ((dmi != null) && (dmi != PDU_STATUS_DMI.GUISE_MODE)) {

            buffer.addAttribute("Disguise Mode", dmi.description);
        }

        if (dti != null) {

            buffer.addAttribute("Detonation Type", dti.description);
        }

        if (fti != null) {

            buffer.addAttribute("Fire Type", fti.description);
        }

        if ((tei != null) && (tei != PDU_STATUS_TEI.NO_DIFF)) {

            buffer.addAttribute("Transferred Entity", tei.description);
        }

        if ((lvci != null) && (lvci != PDU_STATUS_LVCI.NO_STATEMENT)) {

            buffer.addAttribute("Simulation Type", lvci.description);
        }

        if ((rai != null) && (rai != PDU_STATUS_RAI.NO_STATEMENT)) {

            buffer.addAttribute("Radio Attached", rai.description);
        }

        if ((iai != null) && (iai != PDU_STATUS_IAI.NO_STATEMENT)) {

            buffer.addAttribute("Intercom Attached", iai.description);
        }
    }
}
