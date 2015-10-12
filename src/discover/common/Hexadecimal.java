package discover.common;

import discover.common.buffer.AbstractBuffer;

/**
 * @author Tony Pinkston
 */
public class Hexadecimal {

    private static final String ZERO = "0";

    /**
     * @param value - byte
     *
     * @return Two character string of value in hexadecimal form.
     */
    public static String toString8(byte value) {

        String string = Integer.toHexString(value).toUpperCase();

        if (string.length() < 2) {

            return ("0" + string);
        }
        else if (string.length() > 2) {

            return string.substring(string.length() - 2);
        }
        else {

            return string;
        }
    }

    /**
     * @param value - int
     *
     * @return Four character string of value in hexadecimal form.
     */
    public static String toString32(int value) {

        String string = Integer.toHexString(value).toUpperCase();

        while(string.length() < 8) {

            string = ZERO.concat(string);
        }

        return string;
    }

    public static void toBuffer(
        AbstractBuffer buffer,
        String spacer,
        int width,
        boolean italic,
        byte array[]) {

        final int length = array.length;

        for(int i = 0; i < length;) {

            String line = (Hexadecimal.toString32(i) + spacer);

            StringBuffer hexadecimal = new StringBuffer();
            StringBuffer ascii = new StringBuffer();

            for(int j = 0; (j < width); ++i, ++j) {

                if (i < length) {

                    hexadecimal.append(Hexadecimal.toString8(array[i]));
                }
                else {

                    hexadecimal.append("  ");
                }

                if ((j % 8) != 7) {

                    hexadecimal.append(' ');
                }

                if (i >= length) {

                    ascii.append(' ');
                }
                else if ((array[i] > 31) && (array[i] < 127)) {

                    ascii.append((char)array[i]);
                }
                else {

                    ascii.append('.');
                }
            }

            int count = ((width * 3) - 1);

            while(hexadecimal.length() < count) {

                hexadecimal.append(' ');
            }

            line = line.concat(hexadecimal.toString() + spacer);
            line = line.concat(ascii.toString());

            if (italic) {

                buffer.addFixedWidthItalic(line);
                buffer.addBreak();
            }
            else {

                buffer.addFixedWidthText(line);
                buffer.addBreak();
            }
        }
    }
}
