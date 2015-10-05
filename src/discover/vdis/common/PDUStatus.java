/**
 * @author Tony Pinkston
 */
package discover.vdis.common;

import java.io.DataInputStream;
import java.io.IOException;

import discover.common.Binary;
import discover.common.Readable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;

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
    
    public byte getValue() { return this.value; }
    
    /**
     * Sets enumeration values from byte value depending on the type on
     * which this status is attached (some enumerations apply only to
     * specific PDUs).
     * 
     * @param type - {@link PDUType}
     */
    public void setEnumValues(int type) {

        this.tei = null;
        this.lvci = null;
        this.fti = null;
        this.dti = null;
        this.cei = null;
        this.rai = null;
        this.iai = null;
        this.dmi = null;

        // All PDU types have the CEI value:
        this.setCEI();
        
        // All other PDU types have selective values:
        
        if (type == VDIS.PDU_TYPE_ENTITY_STATE) {
            
            this.setDMI();
            this.setTEI();
            this.setLVCI();
        }
        else if (type == VDIS.PDU_TYPE_FIRE) {
            
            this.setFTI();
            this.setLVCI();
        }
        else if (type == VDIS.PDU_TYPE_DETONATION) {
            
            this.setDTI();
            this.setLVCI();
        }
        else if ((type == VDIS.PDU_TYPE_EM_EMISSION) ||
                 (type == VDIS.PDU_TYPE_DESIGNATOR) ||
                 (type == VDIS.PDU_TYPE_IFF)) {
            
            this.setTEI();
            this.setLVCI();
        }
        else if ((type == VDIS.PDU_TYPE_TRANSMITTER) ||
                 (type == VDIS.PDU_TYPE_SIGNAL) ||
                 (type == VDIS.PDU_TYPE_RECEIVER)) {
                   
            this.setRAI();
            this.setTEI();
            this.setLVCI();
        }
        else if ((type == VDIS.PDU_TYPE_INTERCOM_SIGNAL) ||
                 (type == VDIS.PDU_TYPE_INTERCOM_CONTROL)) {
                   
            this.setIAI();
        }
    }
     
    @Override
    public void read(DataInputStream stream) throws IOException {
        
        this.value = stream.readByte();
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {
        
        buffer.addAttribute("Status Bits", Binary.toString8(this.value));

        if ((this.cei != null) && (this.cei != CEI.NOT_COUPLED)) {
            
            buffer.addAttribute("Coupled Extension", this.cei.toString());
        }
        
        if ((this.dmi != null) && (this.dmi != DMI.GUISE_MODE)) {
            
            buffer.addAttribute("Disguise Mode", this.dmi.toString());
        }
        
        if (this.dti != null) {
            
            buffer.addAttribute("Detonation Type", this.dti.toString());
        }
        
        if (this.fti != null) {
            
            buffer.addAttribute("Fire Type", this.fti.toString());
        }
        
        if ((this.tei != null) && (this.tei != TEI.NO_DIFF)) {
            
            buffer.addAttribute("Transferred Entity", this.tei.toString());
        }
        
        if ((this.lvci != null) && (this.lvci != LVCI.NO_STATEMENT)) {
            
            buffer.addAttribute("Simulation Type", this.lvci.toString());
        }
        
        if ((this.rai != null) && (this.rai != RAI.NO_STATEMENT)) {
            
            buffer.addAttribute("Radio Attached", this.rai.toString());
        }
        
        if ((this.iai != null) && (this.iai != IAI.NO_STATEMENT)) {
            
            buffer.addAttribute("Intercom Attached", this.iai.toString());
        }
    }
    
    private void setCEI() {
        
        if (Binary.get1Bit(3, this.value) == 1) {
            
            this.cei = CEI.COUPLED;
        }
        else {
            
            this.cei = CEI.NOT_COUPLED;
        }
    }
    
    private void setTEI() {
        
        if (Binary.get1Bit(0, this.value) == 1) {
            
            this.tei = TEI.DIFF;
        }
        else {
            
            this.tei = TEI.NO_DIFF;
        }
    }
    
    private void setDMI() {
        
        if (Binary.get1Bit(4, this.value) == 1) {
            
            this.dmi = DMI.DISGUISE_MODE;
        }
        else {
            
            this.dmi = DMI.GUISE_MODE;
        }
    }
    
    private void setFTI() {
        
        if (Binary.get1Bit(4, this.value) == 1) {
            
            this.fti = FTI.EXPENDABLE;
        }
        else {
            
            this.fti = FTI.MUNITION;
        }
    }

    private void setDTI() {
        
        int ordinal = Binary.get2Bits(5, this.value);
        
        switch(ordinal) {
        
            case 0: 
                this.dti = DTI.MUNITION;
                break;
            case 1:
                this.dti = DTI.EXPENDABLE;
                break;
            case 2:
                this.dti = DTI.NON_MUNITION_EXPLOSION;
                break;
        }
    }

    private void setRAI() {
        
        int ordinal = Binary.get2Bits(5, this.value);
        
        switch(ordinal) {
        
            case 0: 
                this.rai = RAI.NO_STATEMENT;
                break;
            case 1:
                this.rai = RAI.NOT_ATTACHED;
                break;
            case 2:
                this.rai = RAI.ATTACHED;
                break;
        }
    }

    private void setIAI() {
        
        int ordinal = Binary.get2Bits(5, this.value);
        
        switch(ordinal) {
        
            case 0: 
                this.iai = IAI.NO_STATEMENT;
                break;
            case 1:
                this.iai = IAI.NOT_ATTACHED;
                break;
            case 2:
                this.iai = IAI.ATTACHED;
                break;
        }
    }

    private void setLVCI() {
        
        int ordinal = Binary.get2Bits(2, this.value);
        
        switch(ordinal) {
        
            case 0: 
                this.lvci = LVCI.NO_STATEMENT;
                break;
            case 1:
                this.lvci = LVCI.LIVE;
                break;
            case 2:
                this.lvci = LVCI.VIRTUAL;
                break;
            case 3:
                this.lvci = LVCI.CONSTRUCTIVE;
                break;
        }
    }
}
