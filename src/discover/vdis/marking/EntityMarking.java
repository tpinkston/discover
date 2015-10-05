/**
 * @author Tony Pinkston
 */
package discover.vdis.marking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

import discover.common.Hexadecimal;
import discover.common.Readable;
import discover.common.Writable;
import discover.common.buffer.AbstractBuffer;
import discover.common.buffer.Bufferable;
import discover.vdis.enums.VDIS;
import discover.vdis.marking.army.ArmyBattalion;
import discover.vdis.marking.army.ArmyBrigade;
import discover.vdis.marking.army.ArmyCompany;
import discover.vdis.marking.army.ArmyDivision;
import discover.vdis.marking.army.ArmyPlatoon;
import discover.vdis.marking.army.ArmyTracking;

public class EntityMarking implements Bufferable, Readable, Writable {

    public static final int LENGTH = 12;

    private static final int SIZE = 11;

    private final byte bytes[] = new byte[SIZE];
    private int characterSet = 1; // ENTITY_MARKING_ASCII
    private ArmyDivision division = null;
    private ArmyBrigade brigade = null;
    private ArmyBattalion battalion = null;
    private ArmyCompany company = null;
    private ArmyPlatoon platoon = null;
    private String vehicle = null;
    private String marking = "";
    
    public int getCharacterSet() {
        
        return this.characterSet;
    }
    
    public String getMarking() {
        
        return this.marking;
    }

    public void setCharacterSet(int value) {
    
        this.characterSet = value;
    }

    public void setMarking(String marking) {
    
        this.characterSet = 1; // ENTITY_MARKING_ASCII
        this.marking = marking;

        for(int i = 0; i < SIZE; ++i) {
            
            if (i < this.marking.length()) {
                
                this.bytes[i] = (byte)this.marking.charAt(i);
            }
            else {
                
                this.bytes[i] = 0x00;
            }
        }
    }

    public void clear() {
        
        Arrays.fill(this.bytes, (byte)0x00);
        
        this.characterSet = 1; // ENTITY_MARKING_ASCII
        this.marking = "";
        this.division = null;
        this.battalion = null;
        this.company = null;
        this.platoon = null;
        this.vehicle = null;
    }
    
    /**
     * MUST return true if expression is null!
     * 
     * @param expression - Regular expression or null.
     * 
     * @return True if expression is null or matches the marking string value.
     */
    public boolean matches(String expression) {
        
        if (expression == null) {
            
            return true;
        }
        else try {
            
            return this.marking.matches(".*" + expression + ".*");
        }
        catch(PatternSyntaxException exception) {
            
            return false;
        }
    }
    
    public String toString() {
        
        String description = VDIS.getDescription(
            VDIS.ENTITY_MARKING, 
            this.characterSet);
        
        return (this.marking + " (" + description + ")");
    }

    @Override
    public boolean equals(Object object) {
        
        if (object instanceof EntityMarking) {
        
            EntityMarking marking = (EntityMarking)object;
            
            return ((this.marking.equals(marking.marking)) &&
                    (this.characterSet == marking.characterSet));
        }
        
        return false;
    }
    
    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Value", this.marking);
        buffer.addAttribute(
            "Type",
            this.characterSet,
            VDIS.ENTITY_MARKING);
        
        if (this.division != null) {
            
            buffer.addAttribute("Division", this.division.description);
        }
        
        if (this.brigade != null) {
            
            buffer.addAttribute("Brigade", this.brigade.description);
        }
        
        if (this.battalion != null) {
            
            buffer.addAttribute("Battalion", this.battalion.description);
        }
    }

    public void read(byte bytes[], int index) {
        
        this.characterSet = bytes[index];
        
        for(int i = 0; i < SIZE; ++i) {
            
            this.bytes[i] = bytes[index + 1 + i];
        }

        if (this.characterSet == 1) {
            
            // ENTITY_MARKING_ASCII
            this.marking = new String(this.bytes);
            this.marking = this.marking.trim();
        }
        else if (this.characterSet == 2) {

            // ENTITY_MARKING_US_ARMY
            this.division = ArmyTracking.getValue(
                ArmyDivision.class, 
                this.bytes[0]);
            this.battalion = ArmyTracking.getValue(
                division,
                this.bytes[1]);
            this.company = ArmyTracking.getValue(
                ArmyCompany.class, 
                this.bytes[2]);
            this.platoon = ArmyTracking.getValue(
                ArmyPlatoon.class, 
                this.bytes[3]);
            
            this.vehicle = getVehicleNumber();
            
            if (this.battalion != null) {
                
                this.brigade = this.battalion.getBrigade();
            }

            StringBuffer buffer = new StringBuffer();
            
            if ((this.division == null) ||
                (this.battalion == null) ||
                (this.company == null) ||
                (this.platoon == null) ||
                (this.vehicle == null)) {
                
                for(int i = 0; i < this.bytes.length; ++i) {
                    
                    buffer.append(Hexadecimal.toString8(this.bytes[i]));
                    
                    if (i < (this.bytes.length - 1)) {
                        
                        buffer.append("-");
                    }
                }
            }
            else {
                
                buffer.append(this.vehicle);
                buffer.append("/");
                buffer.append(this.platoon.getBumper());
                buffer.append("/");
                buffer.append(this.company.getBumper());
                buffer.append("/");
                buffer.append(this.battalion.getBumper());
            }

            this.marking = buffer.toString();
        }
        else {
            
            this.marking = "UNDECODED";
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {

        byte bytes[] = new byte[LENGTH];

        stream.read(bytes, 0, LENGTH);
        
        this.read(bytes, 0);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {

        stream.writeByte(this.characterSet);
        stream.write(this.bytes);
    }
    
    private String getVehicleNumber() {

        StringBuffer buffer = new StringBuffer();

        if ((this.bytes[4] > -1) && (this.bytes[4] < 10)) {
            
            buffer.append(this.bytes[4]);
        }

        if ((this.bytes[5] > -1) && (this.bytes[5] < 10)) {
            
            buffer.append(this.bytes[5]);
        }

        return buffer.toString();
    }
}
