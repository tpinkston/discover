package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class PDUStatus implements Bufferable, Readable {

    /** Transferred Entity Indicator */
    public static enum TEI { NO_DIFF, DIFF }

    /** Live Virtual Constructive Indicator */
    public static enum LVCI { NO_STATEMENT, LIVE, VIRTUAL, CONSTRUCTIVE }

    /** Fire Type Indicator */
    public static enum FTI { MUNITION, EXPENDABLE }

    /** Detonation Type Indicator */
    public static enum DTI { MUNITION, EXPENDABLE, NON_MUNITION_EXPLOSION }

    /** Coupled Extension Indicator */
    public static enum CEI { NOT_COUPLED, COUPLED }

    /** Radio Attached Indicator */
    public static enum RAI { NO_STATEMENT, NOT_ATTACHED, ATTACHED }

    /** Intercom Attached Indicator */
    public static enum IAI { NO_STATEMENT, NOT_ATTACHED, ATTACHED }

    /** Disguise Mode Indicator */
    public static enum DMI { GUISE_MODE, DISGUISE_MODE }

    private byte value = 0x00;

    private TEI tei = null;
    private LVCI lvci = null;
    private FTI fti = null;
    private DTI dti = null;
    private CEI cei = null;
    private RAI rai = null;
    private IAI iai = null;
    private DMI dmi = null;

    public byte getValue() { return value; }

    /**
     * Sets enumeration values from byte value depending on the type on
     * which this status is attached (some enumerations apply only to
     * specific PDUs).
     *
     * @param type - {@link PDUType}
     */
    public void setEnumValues(int type) {

        tei = null;
        lvci = null;
        fti = null;
        dti = null;
        cei = null;
        rai = null;
        iai = null;
        dmi = null;

        // All PDU types have the CEI value:
        setCEI();

        // All other PDU types have selective values:

        if (type == VDIS.PDU_TYPE_ENTITY_STATE) {

            setDMI();
            setTEI();
            setLVCI();
        }
        else if (type == VDIS.PDU_TYPE_FIRE) {

            setFTI();
            setLVCI();
        }
        else if (type == VDIS.PDU_TYPE_DETONATION) {

            setDTI();
            setLVCI();
        }
        else if ((type == VDIS.PDU_TYPE_EM_EMISSION) ||
                 (type == VDIS.PDU_TYPE_DESIGNATOR) ||
                 (type == VDIS.PDU_TYPE_IFF)) {

            setTEI();
            setLVCI();
        }
        else if ((type == VDIS.PDU_TYPE_TRANSMITTER) ||
                 (type == VDIS.PDU_TYPE_SIGNAL) ||
                 (type == VDIS.PDU_TYPE_RECEIVER)) {

            setRAI();
            setTEI();
            setLVCI();
        }
        else if ((type == VDIS.PDU_TYPE_INTERCOM_SIGNAL) ||
                 (type == VDIS.PDU_TYPE_INTERCOM_CONTROL)) {

            setIAI();
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        value = stream.readByte();
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Status Bits", Binary.toString8(value));

        if ((cei != null) && (cei != CEI.NOT_COUPLED)) {

            buffer.addAttribute("Coupled Extension", cei.toString());
        }

        if ((dmi != null) && (dmi != DMI.GUISE_MODE)) {

            buffer.addAttribute("Disguise Mode", dmi.toString());
        }

        if (dti != null) {

            buffer.addAttribute("Detonation Type", dti.toString());
        }

        if (fti != null) {

            buffer.addAttribute("Fire Type", fti.toString());
        }

        if ((tei != null) && (tei != TEI.NO_DIFF)) {

            buffer.addAttribute("Transferred Entity", tei.toString());
        }

        if ((lvci != null) && (lvci != LVCI.NO_STATEMENT)) {

            buffer.addAttribute("Simulation Type", lvci.toString());
        }

        if ((rai != null) && (rai != RAI.NO_STATEMENT)) {

            buffer.addAttribute("Radio Attached", rai.toString());
        }

        if ((iai != null) && (iai != IAI.NO_STATEMENT)) {

            buffer.addAttribute("Intercom Attached", iai.toString());
        }
    }

    private void setCEI() {

        if (Binary.get1Bit(3, value) == 1) {

            cei = CEI.COUPLED;
        }
        else {

            cei = CEI.NOT_COUPLED;
        }
    }

    private void setTEI() {

        if (Binary.get1Bit(0, value) == 1) {

            tei = TEI.DIFF;
        }
        else {

            tei = TEI.NO_DIFF;
        }
    }

    private void setDMI() {

        if (Binary.get1Bit(4, value) == 1) {

            dmi = DMI.DISGUISE_MODE;
        }
        else {

            dmi = DMI.GUISE_MODE;
        }
    }

    private void setFTI() {

        if (Binary.get1Bit(4, value) == 1) {

            fti = FTI.EXPENDABLE;
        }
        else {

            fti = FTI.MUNITION;
        }
    }

    private void setDTI() {

        int ordinal = Binary.get2Bits(5, value);

        switch(ordinal) {

            case 0:
                dti = DTI.MUNITION;
                break;
            case 1:
                dti = DTI.EXPENDABLE;
                break;
            case 2:
                dti = DTI.NON_MUNITION_EXPLOSION;
                break;
        }
    }

    private void setRAI() {

        int ordinal = Binary.get2Bits(5, value);

        switch(ordinal) {

            case 0:
                rai = RAI.NO_STATEMENT;
                break;
            case 1:
                rai = RAI.NOT_ATTACHED;
                break;
            case 2:
                rai = RAI.ATTACHED;
                break;
        }
    }

    private void setIAI() {

        int ordinal = Binary.get2Bits(5, value);

        switch(ordinal) {

            case 0:
                iai = IAI.NO_STATEMENT;
                break;
            case 1:
                iai = IAI.NOT_ATTACHED;
                break;
            case 2:
                iai = IAI.ATTACHED;
                break;
        }
    }

    private void setLVCI() {

        int ordinal = Binary.get2Bits(2, value);

        switch(ordinal) {

            case 0:
                lvci = LVCI.NO_STATEMENT;
                break;
            case 1:
                lvci = LVCI.LIVE;
                break;
            case 2:
                lvci = LVCI.VIRTUAL;
                break;
            case 3:
                lvci = LVCI.CONSTRUCTIVE;
                break;
        }
    }
}
