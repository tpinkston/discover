/**
 * @author Tony Pinkston
 */
package discover.gui.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import discover.Discover;
import discover.common.Binary;
import discover.common.Common;
import discover.common.Hexadecimal;
import discover.gui.Utilities;

public class ConversionFrame implements ActionListener {

    private static ConversionFrame instance = null;

    private static final Logger logger = Discover.getLogger();
    
    private static final NumberFormat format = NumberFormat.getInstance();

    static {
        
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(12);
    }
    
    private final JFrame frame = new JFrame("Number Conversion");

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
        
        if (instance == null) {
            
            return null;
        }
        else {
            
            return instance.frame;
        }
    }
    
    public static void setVisible() {
        
        if (instance == null) {
            
            instance = new ConversionFrame();
        }
        
        if (!instance.frame.isVisible()) {
            
            instance.frame.setVisible(true);
        }
    }

    public ConversionFrame() {
        
        ButtonGroup type = new ButtonGroup();
        type.add(this.ascii);
        type.add(this.integer);
        type.add(this.floating);
        
        ButtonGroup base = new ButtonGroup();
        base.add(this.decimal);
        base.add(this.hexadecimal);
        base.add(this.binary);
        
        ButtonGroup size = new ButtonGroup();
        size.add(this.size8bits);
        size.add(this.size16bits);
        size.add(this.size32bits);
        size.add(this.size64bits);
        
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        tools.add(this.ascii);
        tools.add(this.integer);
        tools.add(this.floating);
        tools.addSeparator();
        tools.add(this.decimal);
        tools.add(this.hexadecimal);
        tools.add(this.binary);
        tools.addSeparator();
        tools.add(this.size8bits);
        tools.add(this.size16bits);
        tools.add(this.size32bits);
        tools.add(this.size64bits);
        tools.addSeparator();
        tools.add(this.reset);
        
        this.input.addActionListener(this);
        this.convert.addActionListener(this);
        this.reset.addActionListener(this);
        this.ascii.addActionListener(this);
        this.integer.addActionListener(this);
        this.floating.addActionListener(this);
        
        this.output.setEditable(false);
        this.integer.setSelected(true);
        this.decimal.setSelected(true);
        this.size32bits.setSelected(true);
        
        JPanel panel = Utilities.getGridBagPanel(null);
        
        Utilities.addComponent(
            panel, 
            this.input, 
            Utilities.HORIZONTAL,
            0, 0, 
            1, 1, 
            1.0, 0.0, 
            Utilities.getInsets(5, 5, 2, 1));
        Utilities.addComponent(
            panel, 
            this.convert, 
            Utilities.HORIZONTAL,
            1, 0, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(5, 1, 2, 5));
        Utilities.addComponent(
            panel, 
            this.scroller, 
            Utilities.BOTH,
            0, 1, 
            2, 1, 
            1.0, 1.0, 
            Utilities.getInsets(2, 5, 5, 5));
        
        this.frame.add(tools, BorderLayout.NORTH);
        this.frame.add(panel, BorderLayout.CENTER);
        this.frame.setMinimumSize(new Dimension(750, 300));
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        Utilities.center(DiscoverFrame.getFrame(), this.frame);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {

        Object source = event.getSource();
        
        if ((source == this.convert) || (source == this.input)) {
            
            this.convert();
        }
        else if (source == this.reset) {
            
            this.input.setText("");
            this.output.setText("");
        }
        else if (source == this.ascii) {
            
            this.size8bits.setSelected(true);
            this.size8bits.setEnabled(true);
            this.size16bits.setEnabled(false);
            this.size32bits.setEnabled(false);
            this.size64bits.setEnabled(false);
            
            this.decimal.setEnabled(false);
            this.hexadecimal.setEnabled(false);
            this.binary.setEnabled(false);
        }
        else if (source == this.integer) {
            
            this.size8bits.setEnabled(true);
            this.size16bits.setEnabled(true);
            this.size32bits.setEnabled(true);
            this.size64bits.setEnabled(true);
            
            this.decimal.setEnabled(true);
            this.hexadecimal.setEnabled(true);
            this.binary.setEnabled(true);
        }
        else if (source == this.floating) {
            
            if (this.size8bits.isSelected() || this.size16bits.isSelected()) {
                
                this.size32bits.setSelected(true);
            }
            
            this.size8bits.setEnabled(false);
            this.size16bits.setEnabled(false);
            this.size32bits.setEnabled(true);
            this.size64bits.setEnabled(true);
            
            this.decimal.setEnabled(true);
            this.hexadecimal.setEnabled(true);
            this.binary.setEnabled(true);
        }
    }
    
    private void convert() {
        
        StringBuffer buffer = new StringBuffer();
        
        try {
            
            Number number = this.getNumber();
            
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
                    buffer.append(format.format((Float)number));
                    buffer.append(")");
                }
                else if (number instanceof Double) {
                    
                    buffer.append(" (");
                    buffer.append(format.format((Double)number));
                    buffer.append(")");
                }
                
                buffer.append("\n");
                
                this.convertToHexadecimal(number, buffer);
                this.convertToBinary(number, buffer);
                
                if (this.size8bits.isSelected()) {
                    
                    this.convertToASCII(number, buffer);
                }
            }
        }
        catch(NumberFormatException exception) {
            
            buffer.append("\n");
            buffer.append("Input error: ");
            buffer.append(exception.getMessage());
            buffer.append("\n");
        }

        this.output.append(buffer.toString());
        this.output.scrollRectToVisible(new Rectangle(
            0,
            (this.output.getHeight() - 2),
            1,
            1));
    }
    
    private void convertToASCII(Number number, StringBuffer buffer) {

        final char value = (char)number.byteValue();
        
        buffer.append("ASCII: ");

        if (this.isPrintable(value)) {
            
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
        
        final String value = this.input.getText();
        final boolean floating = this.floating.isSelected();
        final int size = this.getSize();

        if (size > 0) {
            
            if (this.ascii.isSelected()) {
                
                if (value.length() > 1) {
                    
                    throw new NumberFormatException(
                        "Expecting a single ASCII character!");
                }
                else {
                    
                    return new Byte((byte)value.charAt(0));
                }
            }
            else if (this.decimal.isSelected()) {
                
                return Common.getNumber(value, size, Common.DEC, floating);
            }
            else if (this.hexadecimal.isSelected()) {
                
                return Common.getNumber(value, size, Common.HEX, floating);
            }
            else if (this.binary.isSelected()) {
                
                return Common.getNumber(value, size, Common.BIN, floating);
            }
        }

        return null;
    }

    private int getSize() {
        
        int size = 0;
        
        if (this.size64bits.isSelected()) {

            size = Common.SIZE64;
        }
        else if (this.size32bits.isSelected()) {

            size = Common.SIZE32;
        }
        else if (this.size16bits.isSelected()) {

            size = Common.SIZE16;
        }
        else if (this.size8bits.isSelected()) {

            size = Common.SIZE8;
        }
        else {
            
            logger.severe("No size selection!");
        }
        
        return size;
    }
}

