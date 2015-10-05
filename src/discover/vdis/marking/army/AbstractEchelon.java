/**
 * @author Tony Pinkston
 */
package discover.vdis.marking.army;

public abstract class AbstractEchelon {

    public final int value;
    public final String name;
    public final String description;

    protected AbstractEchelon(int value, String name, String description) {

        this.value = value;
        this.name = name;
        this.description = description;
    }

    public int getBV() { return -1; }
    public int getCV() { return -1; }
    public int getPV() { return -1; }
    public int getPRV() { return -1; }
    public String getBumper() { return null; }
}
