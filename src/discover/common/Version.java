package discover.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;

/**
 * Defines application version of data saved to and loaded from data file.
 *
 * @author Tony Pinkston
 */
public enum Version {

    PDU_STALKER(0x37ABCE94E600L),
    DISCOVER_V1(0x12718185480L),
    DISCOVER_V2(0x12760315C80L),
    DISCOVER_V4(0x130F4293800L), // Addition of Builder tab.
    PCAP_SWAP_V2_4(0xD4C3B2A102000400L),
    PCAP_NOSWAP_V2_4(0xA1B2C3D400020004L);

    private final long value;

    private Version(long value) {

        this.value = value;
    }

    public final long getValue() { return value; }

    public static Version getLatest() {

        return DISCOVER_V4;
    }

    public static Version getVersion(long value) {

        for(Version version : values()) {

            if (version.value == value) {

                return version;
            }
        }

        return null;
    }

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DateFormat formatter = DateFormat.getDateTimeInstance();

        for(Version version : values()) {

            System.out.print(version + ": ");
            System.out.print(version.getValue() + ", ");
            System.out.print(formatter.format(version.getValue()));

            System.out.println();
        }

        try {

            while(true) {

                System.out.println();
                System.out.print("long value: ");

                String string = reader.readLine();
                Long value = Long.parseLong(string);

                System.out.println(formatter.format(value.longValue()));
            }
        }
        catch(Exception exception) {

            System.err.println(
                exception.getClass().getSimpleName() + ": " +
                exception.getMessage());
        }
    }
}
