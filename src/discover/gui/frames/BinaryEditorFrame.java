/**
 * @author Tony Pinkston
 */
package discover.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import discover.Discover;
import discover.common.Binary;
import discover.common.Hexadecimal;
import discover.common.buffer.HypertextBuffer;
import discover.common.buffer.PlainTextBuffer;
import discover.gui.Utilities;
import discover.gui.panels.TextPanel;
import discover.gui.tabs.PlaybackTab;
import discover.vdis.PDU;

@SuppressWarnings("serial")
public class BinaryEditorFrame implements ActionListener, MouseListener {
    
    private static final String TITLE = "Binary Editor";
    private static final String HTML = "text/html";
    private static final String PLAIN = "text/plain";
    private static final String NIL = "---";

    private static final String SAVE = "Save";
    private static final String APPLY = "Apply";
    private static final String UNDO = "Undo";
    private static final String REVERT = "Revert";
    private static final String CANCEL = "Cancel";

    private static final Dimension BINARY_MINIMUM = new Dimension(400, 400);
    private static final Dimension BINARY_PREFERRED = new Dimension(500, 600);
    
    private static final Insets SPACING0 = Utilities.getInsets(1, 2, 0, 0);
    private static final Insets SPACING1 = Utilities.getInsets(1, 0, 1, 0);
    private static final Insets SPACING2 = Utilities.getInsets(1, 0, 1, 2);

    private final Logger logger = Discover.getLogger();
    
    private static final String VALUES[] = { "0", "1" };
    
    private static final int COLUMNS = 4;
    
    private static BinaryEditorFrame instance = null;
    
    private final JFrame frame = new JFrame() {

        @Override
        public void dispose() {

            instance = null;

            cleanup();
            super.dispose();
        }
    };

    private final JLabel offset = new JLabel();
    private final JLabel index = new JLabel();
    private final JButton save = new JButton(SAVE);
    private final JButton apply = new JButton(APPLY);
    private final JButton undo = new JButton(UNDO);
    private final JButton revert = new JButton(REVERT);
    private final JButton cancel = new JButton(CANCEL);
    private final JPopupMenu popup = new JPopupMenu();
    
    private final TextPanel content = new TextPanel(HTML, null);
    private final TextPanel hexadecimal = new TextPanel(PLAIN, Font.MONOSPACED);
    
    private final ClearByte clearByte = new ClearByte();
    private final SetByte setByte = new SetByte();
    
    /** Playback tab from which PDU is listed. */
    private final PlaybackTab tab;

    /** Length of PDU to edit (length of 'bytes' array). */
    private final int length; 
    
    /** Array containing GUI labels for each bit of PDU. */
    private final ByteLabel bytes[];
    
    /** Original PDU buffer data, used to revert user changes. */
    private final byte[] original;
    
    /** Original PDU to edit. */
    private final PDU pdu;
    
    /** Copy of original PDU to. */
    private final PDU copy;
    
    /** List of changes made, most recent and the end of the list. */
    private final LinkedList<Change> changes = new LinkedList<Change>();
    
    public static void create(PlaybackTab tab, PDU pdu) {
        
        if (instance == null) {
            
            instance = new BinaryEditorFrame(tab, pdu);
        }
    }
    
    public static JFrame getFrame() {
        
        if (instance == null) {
            
            return null;
        }
        else {
            
            return instance.frame;
        }
    }
    
    private BinaryEditorFrame(PlaybackTab tab, PDU pdu) {
        
        this.frame.setTitle(TITLE + ": " + tab.getTabName());
        this.tab = tab;
        this.pdu = pdu;
        this.copy = pdu.copy();
        this.length = this.pdu.getLength();
        this.original = Arrays.copyOf(pdu.getData(), this.length);
        this.bytes = new ByteLabel[this.length];
        
        System.arraycopy(this.pdu.getData(), 0, this.original, 0, this.length);
        
        this.setOriginalBytes();
        this.updateLabels(-1, -1);
        
        this.save.setEnabled(false);
        this.save.setActionCommand(SAVE);
        this.save.addActionListener(this);
        this.save.setToolTipText("Saves PDU and closes window.");
        
        this.apply.setEnabled(false);
        this.apply.setActionCommand(APPLY);
        this.apply.addActionListener(this);
        this.apply.setToolTipText("Applies changes to PDU data.");
        
        this.undo.setEnabled(false);
        this.undo.setActionCommand(UNDO);
        this.undo.addActionListener(this);
        this.undo.setToolTipText("Undo last change to PDU data.");
        
        this.revert.setEnabled(false);
        this.revert.setActionCommand(REVERT);
        this.revert.addActionListener(this);
        this.revert.setToolTipText("Reverts PDU to original data.");
        
        this.cancel.setEnabled(true);
        this.cancel.setActionCommand(CANCEL);
        this.cancel.addActionListener(this);
        this.cancel.setToolTipText("Cancel changes and closes window.");
        
        this.fill();
        
        this.popup.add(this.clearByte);
        this.popup.add(this.setByte);
        
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setPreferredSize(new Dimension(800, 600));
        this.frame.pack();

        this.show(this.copy);
        
        Utilities.center(DiscoverFrame.getFrame(), this.frame);
        
        this.frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();
        
        if (command == null) {
            
        }
        else if (command.equals(SAVE)) {
            
            this.saveChanges();
            this.frame.dispose();
        }
        else if (command.equals(APPLY)) {
            
            this.applyChanges();
        }
        else if (command.equals(UNDO)) {
            
            this.undoLastChange();
        }
       else if (command.equals(REVERT)) {

            this.revertChanges();
        }
        else if (command.equals(CANCEL)) {

            this.frame.dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        
        if (!event.isPopupTrigger() &&
            (event.getClickCount() == 2) &&
            (event.getButton() == MouseEvent.BUTTON1)) {
                
            BitLabel label = (BitLabel)event.getSource();
            
            if (label.isEditable()) {
                
                label.toggle();

                this.modified(label, new Boolean(!label.isSet()));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        
        if (event.isPopupTrigger()) {
            
            BitLabel label1 = (BitLabel)event.getSource();
            ByteLabel label2 = this.bytes[label1.getIndex()];

            if (label2.isEditable()) {
                
                this.clearByte.setEnabled(label2.getValue() != (byte)0);
                this.clearByte.label = label2;
                this.setByte.label = label2;

                this.popup.show(
                    (Component)event.getSource(),
                    event.getX(),
                    event.getY());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) {

        if (event.getSource() instanceof BitLabel) {
            
            BitLabel label = (BitLabel)event.getSource();
            
            this.updateLabels(label.getOffset(), label.getIndex());
        }
   }

    @Override
    public void mouseExited(MouseEvent event) {

        this.updateLabels(-1, -1);
    }
    
    private void applyChanges() {
        
        this.copy.setData(this.getUpdatedBytes());
        
        this.show(this.copy);
        
        this.apply.setEnabled(false);
        this.save.setEnabled(true);
    }
    
    private void undoLastChange() {
        
        if (this.changes.size() == 1) {
            
            this.revertChanges();
        }
        else if (this.changes.size() > 1) {
            
            Change change = this.changes.removeLast();
            
            if (change.source instanceof ByteLabel) {
                
                ByteLabel label = (ByteLabel)change.source;
                Byte previous = (Byte)change.previous;
                
                label.setValue(previous.byteValue());
            }
            else if (change.source instanceof BitLabel) {
                
                BitLabel label = (BitLabel)change.source;
                Boolean previous = (Boolean)change.previous;
                
                label.set(previous.booleanValue() ? 1 : 0);
            }
        }
    }
    
    private void revertChanges() {
        
        this.copy.setData(this.original);
        
        this.show(this.copy);
        
        this.changes.clear();
        
        this.save.setEnabled(false);
        this.apply.setEnabled(false);
        this.undo.setEnabled(false);
        this.revert.setEnabled(false);
        
        this.setOriginalBytes();
    }
    
    private void saveChanges() {
        
        this.pdu.setData(this.getUpdatedBytes());
        this.pdu.decode(true);
        this.tab.modified(this.pdu);
    }
    
    private void modified(Object source, Object previous) {
        
        this.save.setEnabled(false);
        this.apply.setEnabled(true);
        this.undo.setEnabled(true);
        this.revert.setEnabled(true);
        
        this.changes.addLast(new Change(source, previous));
    }
    
    private void show(PDU pdu) {
        
        if (pdu == null) {
            
            this.content.setText("");
            this.hexadecimal.setText("");
        }
        else {
            
            pdu.decode(true);
            
            try {

                HypertextBuffer buffer = new HypertextBuffer();
                
                buffer.addText("<html><body>");
                buffer.addBuffer(pdu);
                buffer.addText("</body></html>");

                this.content.setText(buffer.toString());
                this.content.setCaretPosition(0);
            }
            catch(Exception exception) {
                
                logger.log(Level.SEVERE, "Caught exception!", exception);
            }
            
            try {
                
                PlainTextBuffer buffer = new PlainTextBuffer();
                
                Hexadecimal.toBuffer(
                    buffer, 
                    "  -  ", 
                    4, 
                    false, 
                    pdu.getData());
                
                this.hexadecimal.setText(buffer.toString());
                this.hexadecimal.setCaretPosition(0);
            }
            catch(Exception exception) {
                
                logger.log(Level.SEVERE, "Caught exception!", exception);
            }
        }
    }
    
    private byte[] getUpdatedBytes() {
        
        byte array[] = new byte[this.length];
        
        for(int index = 0; index < this.length; ++index) {
            
            array[index] = this.bytes[index].getValue();
        }

        return array;
    }
    
    private void setOriginalBytes() {
        
        for(int index = 0; index < this.length; ++index) {
            
            if (this.bytes[index] == null) {
                
                this.bytes[index] = new ByteLabel(
                    this.pdu, 
                    index, 
                    this.original[index]);
            }
            else {
                
                this.bytes[index].setValue(this.original[index]);
            }
        }
    }

    private void updateLabels(int offset, int index) {
        
        if (offset < 0) {
            
            this.offset.setText(NIL);
        }
        else {
            
            this.offset.setText(Integer.toString(offset));
        }

        if (index < 0) {
            
            this.index.setText(NIL);
        }
        else {
            
            this.index.setText(Integer.toString(index));
        }
    }
    
    private void error(String message) {
        
        JOptionPane.showMessageDialog(
            this.frame, 
            message, 
            TITLE, 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void cleanup() {
        
        this.frame.removeAll();
        
        this.save.removeActionListener(this);
        this.apply.removeActionListener(this);
        this.undo.removeActionListener(this);
        this.revert.removeActionListener(this);
        this.cancel.removeActionListener(this);
        
        for(ByteLabel label : this.bytes) {
            
            for(BitLabel bit : label.labels) {
                
                bit.removeMouseListener(this);
            }
        }
    }

    private void fill() {
        
        JPanel panel = new JPanel(new BorderLayout());
        JPanel labels = new JPanel(new GridBagLayout());
        JPanel buttons = new JPanel(new GridLayout(1, 5));
        JToolBar status = new JToolBar();
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JScrollPane scroller = new JScrollPane(labels);
        
        scroller.setMinimumSize(BINARY_MINIMUM);
        scroller.setPreferredSize(BINARY_PREFERRED);
        
        buttons.add(this.save);
        buttons.add(this.apply);
        buttons.add(this.undo);
        buttons.add(this.revert);
        buttons.add(this.cancel);
        
        tabs.addTab("Content", this.content.getPanel());
        tabs.addTab("Hexadecimal", this.hexadecimal.getPanel());
        
        status.setFloatable(false);
        status.add(new JLabel("Bit: "));
        status.add(this.offset);
        status.addSeparator();
        status.add(new JLabel("Byte: "));
        status.add(this.index);
        
        panel.add(status, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        
        split.setLeftComponent(panel);
        split.setRightComponent(tabs);

        int index = 0;
        int gridy = 0;
        
        while(index < this.length) {
            
            int gridx = 0;
            
            Utilities.addComponent(
                labels, 
                new JLabel(Hexadecimal.toString32(index) + ":   "), 
                Utilities.HORIZONTAL,
                gridx, gridy, 
                1, 1, 
                0.0, 0.0, 
                SPACING0);
            
            gridx++;
            
            for(int column = 0; column < COLUMNS; ++index, ++column) {
                
                if (index < this.length) {
                    
                    gridx = this.addByte(labels, index, column, gridx, gridy);
                }
            }
            
            ++gridy;
        }
        
        this.frame.add(split, BorderLayout.CENTER);
    }
    
    private int addByte(
        JPanel panel, 
        int index, 
        int column, 
        int gridx, 
        int gridy) {
        
        if (column > 0) {
        
            this.addSeparator(panel, gridx, gridy);
            ++gridx;
        }
        
        for(int i = 0; i < 8; ++i) {
            
            Insets spacing = SPACING1;

            if ((i == 0) && (column == 0)) {
                
                spacing = SPACING0;
            }
            else if ((i == 7) && (column == (COLUMNS - 1))) {
                
                spacing = SPACING2;
            }

            try {
                
                Utilities.addComponent(
                    panel, 
                    this.bytes[index].labels[7 - i], 
                    Utilities.HORIZONTAL,
                    gridx, gridy, 
                    1, 1, 
                    0.0, 0.0, 
                    spacing);
            }
            catch(ArrayIndexOutOfBoundsException exception) {
                
                throw exception;
            }
            
            ++gridx;
        }
        
        return gridx;
    }
    
    private void addSeparator(JPanel panel, int x, int y) {
        
        Utilities.addComponent(
            panel, 
            new JLabel("-"),
            Utilities.HORIZONTAL,
            x, y, 
            1, 1, 
            0.0, 0.0, 
            Utilities.getInsets(1, 1, 1, 1));
    }

    public static byte setBit(int index, byte bits, boolean value) {
     
        if (value) {
            
            return (byte)(bits | (1 << index));
        }
        else {

            return (byte)(bits & ~(1 << index));
        }
    }
    
    class Change {
        
        final Object source;
        final Object previous;
        
        public Change(Object source, Object previous) {
            
            this.source = source;
            this.previous = previous;
        }
    }
    
    class ClearByte extends AbstractAction {

        static final byte ZERO_BYTE = (byte)0;
        
        ByteLabel label = null;
        
        public ClearByte() {
            
            super("Clear Byte Value");
            
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Set all bits in selected byte to zero.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            Byte previous = new Byte(this.label.getValue());
            
            this.label.setValue(ZERO_BYTE);
            
            modified(this.label, previous);
        }
    }
    
    class SetByte extends AbstractAction {

        ByteLabel label = null;
        
        public SetByte() {
            
            super("Set Byte Value");
            
            super.putValue(
                Action.SHORT_DESCRIPTION,
                "Set value of byte.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            String input = JOptionPane.showInputDialog(
                frame, 
                "Enter new byte value as decimal (-128 to 127):", 
                Integer.toString(this.label.getValue()));

            if (input != null) {
                
                try {
                    
                    Byte previous = new Byte(this.label.getValue());
                    Byte value = Byte.parseByte(input);
                    
                    this.label.setValue(value.byteValue());

                    modified(this.label, previous);
                }
                catch(NumberFormatException exception) {
                
                    error("Invalid number format!");
                }
            }
        }
    }
    
    class ByteLabel {
        
        final BitLabel labels[] = new BitLabel[8];

        /**
         * @param index - Index of source byte in PDU data buffer.
         * @param value - Value of source byte.
         */
        public ByteLabel(PDU pdu, int index, byte value) {

            for(int bit = 0; bit < 8; ++bit) {
                
                this.labels[bit] = new BitLabel(
                    index, 
                    bit, 
                    Binary.get1Bit(bit, value),
                    PDU.isByteEditable(pdu.getType(), index));
            }
        }
        
        void setValue(byte value) {
            
            for(int bit = 0; bit < 8; ++bit) {
                
                this.labels[bit].set(Binary.get1Bit(bit, value));
            }
        }
        
        byte getValue() {
            
            byte value = 0x00;
            
            for(int bit = 0; bit < 8; ++bit) {
                
                value = setBit(bit, value, this.labels[bit].isSet());
            }

            return value;
        }
        
        public boolean isEditable() {
            
            for(BitLabel label : this.labels) {
            
                if (!label.isEditable()) {
                    
                    return false;
                }
            }
            
            return true;
        }
        
        @Override
        public String toString() {
            
            StringBuffer buffer = new StringBuffer();
            
            for(int bit = 8; bit >= 0; --bit) {
                
                buffer.append(this.labels[bit].getText());
            }
            
            return buffer.toString();
        }
    }
    
    class BitLabel extends JLabel {
        
        /** Index of source byte in PDU data (0 thru PDU length minus 1) */
        final int index;
        
        /** Index of bit in source byte (0 thru 7) */
        final int bit;
        
        /** Index of bit in PDU data (0 thru [bit + [index * 8]]) */
        final int offset;
        
        /** True of bit can be toggled. */
        final boolean editable;
        
        BitLabel(int index, int bit, int value, boolean editable) {

            this.index = index;
            this.bit = bit;
            this.offset = ((this.index * 8) + (7 - this.bit));
            this.editable = editable;

            this.set(value);
            
            super.addMouseListener(BinaryEditorFrame.this);
            
            if (!this.editable) {
                
                super.setForeground(Color.GRAY);
            }
        }
        
        int getIndex() { return this.index; }

        int getBit() { return this.bit; }

        int getOffset() { return this.offset; }
        
        boolean isEditable() { return this.editable; }
        
        boolean isSet() { return super.getText().equals(VALUES[1]); }
        
        void toggle() {
            
            this.set(this.isSet() ? 0 : 1);
        }
        
        void set(int value) {

            super.setText(VALUES[value]);
        }
    }
}
