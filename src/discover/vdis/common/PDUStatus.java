package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.EnumInterface;
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

    private EnumInterface tei = null;
    private EnumInterface lvci = null;
    private EnumInterface fti = null;
    private EnumInterface dti = null;
    private EnumInterface cei = null;
    private EnumInterface rai = null;
    private EnumInterface dmi = null;
    private EnumInterface iai = null;

    public byte getValue() { return value; }

    /**
     * Sets enumeration values from byte value depending on the type on
     * which this status is attached (some enumerations apply only to
     * specific PDUs).
     *
     * @param type - Integer value (see {@link PDU_TYPE}).
     */
    public void setEnumValues(int type) {

        tei = null;
        lvci = null;
        fti = null;
        dti = null;
        cei = null;
        rai = null;
        dmi = null;
        iai = null;

        // All PDU types have the CEI value:
        cei = PDU_STATUS_CEI.getValue(Binary.get1Bit(3, value));

        // All other PDU types have selective values:
        if (type == PDU_TYPE.PDU_TYPE_ENTITY_STATE.getValue()) {

            dmi = PDU_STATUS_DMI.getValue(Binary.get1Bit(4, value));
            tei = PDU_STATUS_TEI.getValue(Binary.get1Bit(0, value));
            lvci = PDU_STATUS_LVCI.getValue(Binary.get2Bits(2, value));
        }
        else if (type == PDU_TYPE.PDU_TYPE_FIRE.getValue()) {

            fti = PDU_STATUS_FTI.getValue(Binary.get1Bit(4, value));
            lvci = PDU_STATUS_LVCI.getValue(Binary.get2Bits(2, value));
        }
        else if (type == PDU_TYPE.PDU_TYPE_DETONATION.getValue()) {

            dti = PDU_STATUS_DTI.getValue(Binary.get2Bits(5, value));
            lvci = PDU_STATUS_LVCI.getValue(Binary.get2Bits(2, value));
        }
        else if ((type == PDU_TYPE.PDU_TYPE_EM_EMISSION.getValue()) ||
                 (type == PDU_TYPE.PDU_TYPE_DESIGNATOR.getValue()) ||
                 (type == PDU_TYPE.PDU_TYPE_IFF.getValue())) {

            tei = PDU_STATUS_TEI.getValue(Binary.get1Bit(0, value));
            lvci = PDU_STATUS_LVCI.getValue(Binary.get2Bits(2, value));
        }
        else if ((type == PDU_TYPE.PDU_TYPE_TRANSMITTER.getValue()) ||
                 (type == PDU_TYPE.PDU_TYPE_SIGNAL.getValue()) ||
                 (type == PDU_TYPE.PDU_TYPE_RECEIVER.getValue())) {

            rai = PDU_STATUS_RAI.getValue(Binary.get2Bits(5, value));
            tei = PDU_STATUS_TEI.getValue(Binary.get1Bit(0, value));
            lvci = PDU_STATUS_LVCI.getValue(Binary.get2Bits(2, value));
        }
        else if ((type == PDU_TYPE.PDU_TYPE_INTERCOM_SIGNAL.getValue()) ||
                 (type == PDU_TYPE.PDU_TYPE_INTERCOM_CONTROL.getValue())) {

            iai = PDU_STATUS_IAI.getValue(Binary.get2Bits(5, value));
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        value = stream.readByte();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Status Bits", Binary.toString8(value));

        if ((cei != null) && (cei != PDU_STATUS_CEI.PDU_STATUS_CEI_NOT_COUPLED)) {

            buffer.addAttribute("Coupled Extension", cei.getDescription());
        }

        if ((dmi != null) && (dmi != PDU_STATUS_DMI.PDU_STATUS_DMI_GUISE_MODE)) {

            buffer.addAttribute("Disguise Mode", dmi.getDescription());
        }

        if (dti != null) {

            buffer.addAttribute("Detonation Type", dti.getDescription());
        }

        if (fti != null) {

            buffer.addAttribute("Fire Type", fti.getDescription());
        }

        if ((tei != null) && (tei != PDU_STATUS_TEI.PDU_STATUS_TEI_NO_DIFF)) {

            buffer.addAttribute("Transferred Entity", tei.getDescription());
        }

        if ((lvci != null) && (lvci != PDU_STATUS_LVCI.PDU_STATUS_LVCI_NO_STATEMENT)) {

            buffer.addAttribute("Simulation Type", lvci.getDescription());
        }

        if ((rai != null) && (rai != PDU_STATUS_RAI.PDU_STATUS_RAI_NO_STATEMENT)) {

            buffer.addAttribute("Radio Attached", rai.getDescription());
        }

        if ((iai != null) && (iai != PDU_STATUS_IAI.PDU_STATUS_IAI_NO_STATEMENT)) {

            buffer.addAttribute("Intercom Attached", iai.getDescription());
        }
    }
}
