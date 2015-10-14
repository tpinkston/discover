package discover.gui.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Binary;
import discover.common.Common;
import discover.common.Hexadecimal;
import discover.gui.Utilities;

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
public class ConversionFrame extends JFrame implements ActionListener {

    private static ConversionFrame instance = null;

    private static final Logger logger = LoggerFactory.getLogger(ConversionFrame.class);

    private static final NumberFormat format = NumberFormat.getInstance();

    static {

        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(12);
    }

    private final JButton convert = new JButton("Convert");
    private final JButton reset = new JButton("Reset");

    private final JRadioButton ascii = new JRadioButton("ASCII");
    private final JRadioButton integer = new JRadioButton("Integer");
    private final JRadioButton floating = new JRadioButton("Float");

    private final JRadioButton decimal = new JRadioButton("Dec");
    private final JRadioButton hexadecimal = new JRadioButton("Hex");
    private final JRadioButton binary = new JRadioButton("Bin");

    private final JRadioButton size8bits = new JRadioButton("8-bit");
    private final JRadioButton size16bits = new JRadioButton("16-bit");
    private final JRadioButton size32bits = new JRadioButton("32-bit");
    private final JRadioButton size64bits = new JRadioButton("64-bit");

    private final JTextField input = new JTextField(9);
    private final JTextArea output = new JTextArea(5, 9);

    private final JScrollPane scroller = new JScrollPane(output);

    public static JFrame getFrame() {

        return instance;
    }

    public static void setVisible() {

        if (instance == null) {

            instance = new ConversionFrame();
        }

        if (!instance.isVisible()) {

            instance.setVisible(true);
        }
    }

    private ConversionFrame() {

        super("Number Conversion");

        ButtonGroup type = new ButtonGroup();
        type.add(ascii);
        type.add(integer);
        type.add(floating);

        ButtonGroup base = new ButtonGroup();
        base.add(decimal);
        base.add(hexadecimal);
        base.add(binary);

        ButtonGroup size = new ButtonGroup();
        size.add(size8bits);
        size.add(size16bits);
        size.add(size32bits);
        size.add(size64bits);

        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        tools.add(ascii);
        tools.add(integer);
        tools.add(floating);
        tools.addSeparator();
        tools.add(decimal);
        tools.add(hexadecimal);
        tools.add(binary);
        tools.addSeparator();
        tools.add(size8bits);
        tools.add(size16bits);
        tools.add(size32bits);
        tools.add(size64bits);
        tools.addSeparator();
        tools.add(reset);

        input.addActionListener(this);
        convert.addActionListener(this);
        reset.addActionListener(this);
        ascii.addActionListener(this);
        integer.addActionListener(this);
        floating.addActionListener(this);

        output.setEditable(false);
        integer.setSelected(true);
        decimal.setSelected(true);
        size32bits.setSelected(true);

        JPanel panel = Utilities.getGridBagPanel(null);

        Utilities.addComponent(
            panel,
            input,
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(5, 5, 2, 1));
        Utilities.addComponent(
            panel,
            convert,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(5, 1, 2, 5));
        Utilities.addComponent(
            panel,
            scroller,
            Utilities.BOTH,
            0, 1,
            2, 1,
            1.0, 1.0,
            Utilities.getInsets(2, 5, 5, 5));

        add(tools, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(750, 300));
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        Utilities.center(DiscoverFrame.getFrame(), this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Object source = event.getSource();

        if ((source == convert) || (source == input)) {

            convert();
        }
        else if (source == reset) {

            input.setText("");
            output.setText("");
        }
        else if (source == ascii) {

            size8bits.setSelected(true);
            size8bits.setEnabled(true);
            size16bits.setEnabled(false);
            size32bits.setEnabled(false);
            size64bits.setEnabled(false);

            decimal.setEnabled(false);
            hexadecimal.setEnabled(false);
            binary.setEnabled(false);
        }
        else if (source == integer) {

            size8bits.setEnabled(true);
            size16bits.setEnabled(true);
            size32bits.setEnabled(true);
            size64bits.setEnabled(true);

            decimal.setEnabled(true);
            hexadecimal.setEnabled(true);
            binary.setEnabled(true);
        }
        else if (source == floating) {

            if (size8bits.isSelected() || size16bits.isSelected()) {

                size32bits.setSelected(true);
            }

            size8bits.setEnabled(false);
            size16bits.setEnabled(false);
            size32bits.setEnabled(true);
            size64bits.setEnabled(true);

            decimal.setEnabled(true);
            hexadecimal.setEnabled(true);
            binary.setEnabled(true);
        }
    }

    private void convert() {

        StringBuffer buffer = new StringBuffer();

        try {

            Number number = getNumber();

            if (number == null) {

                buffer.append("\n");
                buffer.append("Could not determine number!\n");
            }
            else {

                buffer.append("\n");
                buffer.append(number.getClass().getSimpleName());
                buffer.append(": ");
                buffer.append(number);

                if (number instanceof Float) {

                    buffer.append(" (");
                    buffer.append(format.format(number));
                    buffer.append(")");
                }
                else if (number instanceof Double) {

                    buffer.append(" (");
                    buffer.append(format.format(number));
                    buffer.append(")");
                }

                buffer.append("\n");

                convertToHexadecimal(number, buffer);
                convertToBinary(number, buffer);

                if (size8bits.isSelected()) {

                    convertToASCII(number, buffer);
                }
            }
        }
        catch(NumberFormatException exception) {

            buffer.append("\n");
            buffer.append("Input error: ");
            buffer.append(exception.getMessage());
            buffer.append("\n");
        }

        output.append(buffer.toString());
        output.scrollRectToVisible(new Rectangle(
            0,
            (output.getHeight() - 2),
            1,
            1));
    }

    private void convertToASCII(Number number, StringBuffer buffer) {

        final char value = (char)number.byteValue();

        buffer.append("ASCII: ");

        if (isPrintable(value)) {

            buffer.append("\"");
            buffer.append(value);
            buffer.append("\"\n");
        }
        else {

            buffer.append("(unprintable)\n");
        }
    }

    private void convertToBinary(Number number, StringBuffer buffer) {

        String string = null;
        byte array[] = Common.getByteArray(number);

        if (array != null) {

            string = new String();

            for(int i = 0; i < array.length; ++i) {

                string = string.concat(Binary.toString8(array[i]));

                if ((i + 1) < array.length) {

                    string = string.concat("-");
                }
            }
        }

        buffer.append("Binary: ");
        buffer.append(string);
        buffer.append("\n");
    }

    private void convertToHexadecimal(Number number, StringBuffer buffer) {

        String string = null;
        byte array[] = Common.getByteArray(number);

        if (array != null) {

            string = new String();

            for(int i = 0; i < array.length; ++i) {

                string = string.concat(Hexadecimal.toString8(array[i]));

                if ((i + 1) < array.length) {

                    string = string.concat("-");
                }
            }
        }

        buffer.append("Hexadecimal: ");
        buffer.append(string);
        buffer.append("\n");
    }

    private boolean isPrintable(char value) {

        return ((value > 31) && (value < 127));
    }

    private Number getNumber() throws NumberFormatException {

        final String value = input.getText();
        final boolean floating = this.floating.isSelected();
        final int size = getNumberSize();

        if (size > 0) {

            if (ascii.isSelected()) {

                if (value.length() > 1) {

                    throw new NumberFormatException(
                        "Expecting a single ASCII character!");
                }
                else {

                    return new Byte((byte)value.charAt(0));
                }
            }
            else if (decimal.isSelected()) {

                return Common.getNumber(value, size, Common.DEC, floating);
            }
            else if (hexadecimal.isSelected()) {

                return Common.getNumber(value, size, Common.HEX, floating);
            }
            else if (binary.isSelected()) {

                return Common.getNumber(value, size, Common.BIN, floating);
            }
        }

        return null;
    }

    private int getNumberSize() {

        int size = 0;

        if (size64bits.isSelected()) {

            size = Common.SIZE64;
        }
        else if (size32bits.isSelected()) {

            size = Common.SIZE32;
        }
        else if (size16bits.isSelected()) {

            size = Common.SIZE16;
        }
        else if (size8bits.isSelected()) {

            size = Common.SIZE8;
        }
        else {

            logger.error("No size selection!");
        }

        return size;
    }
}

