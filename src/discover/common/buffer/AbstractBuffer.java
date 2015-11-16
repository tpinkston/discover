package discover.common.buffer;

import java.text.NumberFormat;

import discover.vdis.EnumInterface;
import discover.vdis.Enumerations;

/**
 * @author Tony Pinkston
 */
public abstract class AbstractBuffer {

    private static final String THIN_TEXT_SEPARATOR =
        "----------------------------------------" +
        "----------------------------------------";
    private static final String THICK_TEXT_SEPARATOR =
        "========================================" +
        "========================================";

    private static final NumberFormat number;

    private static final String NULL = "(null)";

    static {

        number = NumberFormat.getInstance();
        number.setMaximumFractionDigits(4);
    }

    private final StringBuilder buffer = new StringBuilder();

    public abstract boolean isHTML();

    @Override
    public final String toString() {

        return buffer.toString();
    }

    public void addBuffer(Bufferable bufferable) {

        bufferable.toBuffer(this);
    }

    public void listStart() {

        buffer.append(isHTML() ? "<ul>" : "\n");
    }

    public void listFinished() {

        buffer.append(isHTML() ? "</ul>" : "\n");
    }

    public void listItemStart() {

        buffer.append(isHTML() ? "<li>" : " - ");
    }

    public void listItemFinished() {

        buffer.append(isHTML() ? "</li>" : "\n");
    }

    public void addBreak() {

        buffer.append(isHTML() ? "<br/>" : "\n");
    }

    public void addText(String text) {

        if (text == null) {

            buffer.append(NULL);
        }
        else {

            buffer.append(text);
        }
    }

    public void addSeparator() {

        if (isHTML()) {

            buffer.append("<hr noshade/>");
        }
        else {

            addText(THIN_TEXT_SEPARATOR);
            addBreak();
        }
    }

    public void addThickSeparator(String title) {

        if (isHTML()) {

            buffer.append("<hr noshade/>");
        }
        else if (title != null) {

            String spaced = (" " + title + " ");

            int length = spaced.length();
            int half = ((80 - length) / 2);

            addText(THICK_TEXT_SEPARATOR.substring(0, half));
            addText(spaced);

            if ((length % 2) == 0) {

                addText(THICK_TEXT_SEPARATOR.substring(0, half));
            }
            else {

                addText(THICK_TEXT_SEPARATOR.substring(0, (half + 1)));
            }
        }
        else {

            addText(THICK_TEXT_SEPARATOR);
            addBreak();
        }
    }

    public void addTitle(String title) {

        addBold(title);
        addBreak();
    }

    public void addBold(String text) {

        addHTML("<b>");
        addText(text);
        addHTML("</b>");
    }

    public void addItalic(String text) {

        addHTML("<i>");
        addText(text);
        addHTML("</i>");
    }

    public void addFixedWidthItalic(String text) {

        addHTML("<i><tt>");
        addText(text);
        addHTML("</tt></i>");
    }

    public void addFixedWidthText(String text) {

        addHTML("<tt>");
        addText(text);
        addHTML("</tt>");
    }

    public void addLabel(String label) {

        addText(label);
        addText(": ");
    }

    public void addBoldLabel(String label) {

        addHTML("<b>");
        addLabel(label);
        addHTML("</b>");
    }

    public void addAttribute(String name, String value) {

        addLabel(name);
        addItalic(value);
        addBreak();
    }

    public void addBoldAttribute(String name, String value) {

        addBoldLabel(name);
        addItalic(value);
        addBreak();
    }

    public void addAttribute(String name, Integer value) {

        if (value == null) {

            addAttribute(name, NULL);
        }
        else {

            addAttribute(name, value.toString());
        }
    }

    public void addBoldAttribute(String name, Integer value) {

        if (value == null) {

            addBoldAttribute(name, NULL);
        }
        else {

            addBoldAttribute(name, value.toString());
        }
    }

    public void addAttribute(String name, Long value) {

        if (value == null) {

            addAttribute(name, NULL);
        }
        else {

            addAttribute(name, value.toString());
        }
    }

    public void addAttribute(String name, Float value) {

        if (value == null) {

            addAttribute(name, NULL);
        }
        else {

            addAttribute(name, number.format(value));
        }
    }

    public void addAttribute(
            String name,
            int value,
            Class<? extends EnumInterface> type) {

        String text = Enumerations.getDescription(value, type);

        text.concat(" [0x");
        text.concat(Integer.toHexString(value).toUpperCase());
        text.concat("]");

        addAttribute(name, text);
    }

    public void addListAttribute(String name, String value) {

        listItemStart();
        addLabel(name);
        addItalic(value);
        listItemFinished();
    }

    public void addListAttribute(
            String name,
            int value,
            Class<? extends EnumInterface> type) {

        String text = Enumerations.getDescription(value, type);

        text.concat(" [0x");
        text.concat(Integer.toHexString(value).toUpperCase());
        text.concat("]");

        addListAttribute(name, text);
    }

    private void addHTML(String string) {

        if (isHTML()) {

            buffer.append(string);
        }
    }
}
