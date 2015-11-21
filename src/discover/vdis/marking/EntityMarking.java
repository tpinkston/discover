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
import discover.vdis.enums.ENTITY_MARKING;
import discover.vdis.marking.army.ArmyBattalion;
import discover.vdis.marking.army.ArmyBrigade;
import discover.vdis.marking.army.ArmyCompany;
import discover.vdis.marking.army.ArmyDivision;
import discover.vdis.marking.army.ArmyPlatoon;
import discover.vdis.marking.army.ArmyTracking;

/**
 * @author Tony Pinkston
 */
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

        return characterSet;
    }

    public String getMarking() {

        return marking;
    }

    public void setCharacterSet(int value) {

        characterSet = value;
    }

    public void setMarking(String marking) {

        characterSet = 1; // ENTITY_MARKING_ASCII
        this.marking = marking;

        for(int i = 0; i < SIZE; ++i) {

            if (i < this.marking.length()) {

                bytes[i] = (byte)this.marking.charAt(i);
            }
            else {

                bytes[i] = 0x00;
            }
        }
    }

    public void clear() {

        Arrays.fill(bytes, (byte)0x00);

        characterSet = 1; // ENTITY_MARKING_ASCII
        marking = "";
        division = null;
        battalion = null;
        company = null;
        platoon = null;
        vehicle = null;
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

            return marking.matches(".*" + expression + ".*");
        }
        catch(PatternSyntaxException exception) {

            return false;
        }
    }

    @Override
    public String toString() {

        String text = ENTITY_MARKING.get(characterSet).description;

        return (marking + " (" + text + ")");
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof EntityMarking) {

            EntityMarking marking = (EntityMarking)object;

            return ((this.marking.equals(marking.marking)) &&
                    (characterSet == marking.characterSet));
        }

        return false;
    }

    @Override
    public int hashCode() {

        return ((31 * Arrays.hashCode(bytes)) + characterSet);
    }

    @Override
    public void toBuffer(AbstractBuffer buffer) {

        buffer.addAttribute("Value", marking);
        buffer.addAttribute("Type", characterSet, ENTITY_MARKING.class);

        if (division != null) {

            buffer.addAttribute("Division", division.description);
        }

        if (brigade != null) {

            buffer.addAttribute("Brigade", brigade.description);
        }

        if (battalion != null) {

            buffer.addAttribute("Battalion", battalion.description);
        }
    }

    public void read(byte bytes[], int index) {

        characterSet = bytes[index];

        for(int i = 0; i < SIZE; ++i) {

            this.bytes[i] = bytes[index + 1 + i];
        }

        if (characterSet == 1) {

            // ENTITY_MARKING_ASCII
            marking = new String(this.bytes);
            marking = marking.trim();
        }
        else if (characterSet == 2) {

            // ENTITY_MARKING_US_ARMY
            division = ArmyTracking.getValue(
                ArmyDivision.class,
                this.bytes[0]);
            battalion = ArmyTracking.getValue(
                division,
                this.bytes[1]);
            company = ArmyTracking.getValue(
                ArmyCompany.class,
                this.bytes[2]);
            platoon = ArmyTracking.getValue(
                ArmyPlatoon.class,
                this.bytes[3]);

            vehicle = getVehicleNumber();

            if (battalion != null) {

                brigade = battalion.getBrigade();
            }

            StringBuffer buffer = new StringBuffer();

            if ((division == null) ||
                (battalion == null) ||
                (company == null) ||
                (platoon == null) ||
                (vehicle == null)) {

                for(int i = 0; i < this.bytes.length; ++i) {

                    buffer.append(Hexadecimal.toString8(this.bytes[i]));

                    if (i < (this.bytes.length - 1)) {

                        buffer.append("-");
                    }
                }
            }
            else {

                buffer.append(vehicle);
                buffer.append("/");
                buffer.append(platoon.getBumper());
                buffer.append("/");
                buffer.append(company.getBumper());
                buffer.append("/");
                buffer.append(battalion.getBumper());
            }

            marking = buffer.toString();
        }
        else {

            marking = "UNDECODED";
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

        stream.writeByte(characterSet);
        stream.write(bytes);
    }

    private String getVehicleNumber() {

        StringBuffer buffer = new StringBuffer();

        if ((bytes[4] > -1) && (bytes[4] < 10)) {

            buffer.append(bytes[4]);
        }

        if ((bytes[5] > -1) && (bytes[5] < 10)) {

            buffer.append(bytes[5]);
        }

        return buffer.toString();
    }
}
