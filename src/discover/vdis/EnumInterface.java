package discover.vdis;

public interface EnumInterface {

    /**
     * @return VDIS enumeration value.
     */
    int getValue();

    /**
     * @return Name for enumeration value.
     */
    String getName();

    /**
     * @return One-line description for enumeration value.
     */
    String getDescription();
}
