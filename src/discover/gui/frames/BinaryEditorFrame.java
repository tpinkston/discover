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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.common.Binary;
import discover.common.Hexadecimal;
import discover.common.buffer.HypertextBuffer;
import discover.common.buffer.PlainTextBuffer;
import discover.gui.Utilities;
import discover.gui.panels.TextPanel;
import discover.gui.tabs.PlaybackTab;
import discover.vdis.PDU;

/**
 * @author Tony Pinkston
 */
@SuppressWarnings("serial")
public class BinaryEditorFrame
        extends JFrame
        implements ActionListener, MouseListener {

    private static final Logger logger = LoggerFactory.getLogger(BinaryEditorFrame.class);

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

    private static final String VALUES[] = { "0", "1" };

    private static final int COLUMNS = 4;

    private static BinaryEditorFrame instance = null;

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

        return instance;
    }

    private BinaryEditorFrame(PlaybackTab tab, PDU pdu) {

        super(TITLE + ": " + tab.getName());

        this.tab = tab;
        this.pdu = pdu;

        copy = pdu.copy();
        length = this.pdu.getLength();
        original = Arrays.copyOf(pdu.getData(), length);
        bytes = new ByteLabel[length];

        System.arraycopy(this.pdu.getData(), 0, original, 0, length);

        setOriginalBytes();
        updateLabels(-1, -1);

        save.setEnabled(false);
        save.setActionCommand(SAVE);
        save.addActionListener(this);
        save.setToolTipText("Saves PDU and closes window.");

        apply.setEnabled(false);
        apply.setActionCommand(APPLY);
        apply.addActionListener(this);
        apply.setToolTipText("Applies changes to PDU data.");

        undo.setEnabled(false);
        undo.setActionCommand(UNDO);
        undo.addActionListener(this);
        undo.setToolTipText("Undo last change to PDU data.");

        revert.setEnabled(false);
        revert.setActionCommand(REVERT);
        revert.addActionListener(this);
        revert.setToolTipText("Reverts PDU to original data.");

        cancel.setEnabled(true);
        cancel.setActionCommand(CANCEL);
        cancel.addActionListener(this);
        cancel.setToolTipText("Cancel changes and closes window.");

        fill();

        popup.add(clearByte);
        popup.add(setByte);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        pack();

        show(copy);

        Utilities.center(DiscoverFrame.getFrame(), this);

        setVisible(true);
    }

    @Override
    public void dispose() {

        instance = null;

        cleanup();
        super.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (command == null) {

        }
        else if (command.equals(SAVE)) {

            saveChanges();
            dispose();
        }
        else if (command.equals(APPLY)) {

            applyChanges();
        }
        else if (command.equals(UNDO)) {

            undoLastChange();
        }
       else if (command.equals(REVERT)) {

            revertChanges();
        }
        else if (command.equals(CANCEL)) {

            dispose();
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

                modified(label, new Boolean(!label.isSet()));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {

        if (event.isPopupTrigger()) {

            BitLabel label1 = (BitLabel)event.getSource();
            ByteLabel label2 = bytes[label1.getIndex()];

            if (label2.isEditable()) {

                clearByte.setEnabled(label2.getValue() != (byte)0);
                clearByte.label = label2;
                setByte.label = label2;

                popup.show(
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

            updateLabels(label.getOffset(), label.getIndex());
        }
   }

    @Override
    public void mouseExited(MouseEvent event) {

        updateLabels(-1, -1);
    }

    private void applyChanges() {

        copy.setData(getUpdatedBytes());

        show(copy);

        apply.setEnabled(false);
        save.setEnabled(true);
    }

    private void undoLastChange() {

        if (changes.size() == 1) {

            revertChanges();
        }
        else if (changes.size() > 1) {

            Change change = changes.removeLast();

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

        copy.setData(original);

        show(copy);

        changes.clear();

        save.setEnabled(false);
        apply.setEnabled(false);
        undo.setEnabled(false);
        revert.setEnabled(false);

        setOriginalBytes();
    }

    private void saveChanges() {

        pdu.setData(getUpdatedBytes());
        pdu.decode(true);
        tab.modified(pdu);
    }

    private void modified(Object source, Object previous) {

        save.setEnabled(false);
        apply.setEnabled(true);
        undo.setEnabled(true);
        revert.setEnabled(true);

        changes.addLast(new Change(source, previous));
    }

    private void show(PDU pdu) {

        if (pdu == null) {

            content.setText("");
            hexadecimal.setText("");
        }
        else {

            pdu.decode(true);

            try {

                HypertextBuffer buffer = new HypertextBuffer();

                buffer.addText("<html><body>");
                buffer.addBuffer(pdu);
                buffer.addText("</body></html>");

                content.setText(buffer.toString());
                content.setCaretPosition(0);
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }

            try {

                PlainTextBuffer buffer = new PlainTextBuffer();

                Hexadecimal.toBuffer(
                    buffer,
                    "  -  ",
                    4,
                    false,
                    pdu.getData());

                hexadecimal.setText(buffer.toString());
                hexadecimal.setCaretPosition(0);
            }
            catch(Exception exception) {

                logger.error("Caught exception!", exception);
            }
        }
    }

    private byte[] getUpdatedBytes() {

        byte array[] = new byte[length];

        for(int index = 0; index < length; ++index) {

            array[index] = bytes[index].getValue();
        }

        return array;
    }

    private void setOriginalBytes() {

        for(int index = 0; index < length; ++index) {

            if (bytes[index] == null) {

                bytes[index] = new ByteLabel(
                    pdu,
                    index,
                    original[index]);
            }
            else {

                bytes[index].setValue(original[index]);
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
            this,
            message,
            TITLE,
            JOptionPane.ERROR_MESSAGE);
    }

    private void cleanup() {

        removeAll();

        save.removeActionListener(this);
        apply.removeActionListener(this);
        undo.removeActionListener(this);
        revert.removeActionListener(this);
        cancel.removeActionListener(this);

        for(ByteLabel label : bytes) {

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

        buttons.add(save);
        buttons.add(apply);
        buttons.add(undo);
        buttons.add(revert);
        buttons.add(cancel);

        tabs.addTab("Content", content);
        tabs.addTab("Hexadecimal", hexadecimal);

        status.setFloatable(false);
        status.add(new JLabel("Bit: "));
        status.add(offset);
        status.addSeparator();
        status.add(new JLabel("Byte: "));
        status.add(index);

        panel.add(status, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        split.setLeftComponent(panel);
        split.setRightComponent(tabs);

        int index = 0;
        int gridy = 0;

        while(index < length) {

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

                if (index < length) {

                    gridx = addByte(labels, index, column, gridx, gridy);
                }
            }

            ++gridy;
        }

        add(split, BorderLayout.CENTER);
    }

    private int addByte(
        JPanel panel,
        int index,
        int column,
        int gridx,
        int gridy) {

        if (column > 0) {

            addSeparator(panel, gridx, gridy);
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
                    bytes[index].labels[7 - i],
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

            Byte previous = new Byte(label.getValue());

            label.setValue(ZERO_BYTE);

            modified(label, previous);
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
                BinaryEditorFrame.this,
                "Enter new byte value as decimal (-128 to 127):",
                Integer.toString(label.getValue()));

            if (input != null) {

                try {

                    Byte previous = new Byte(label.getValue());
                    Byte value = Byte.parseByte(input);

                    label.setValue(value.byteValue());

                    modified(label, previous);
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

                labels[bit] = new BitLabel(
                    index,
                    bit,
                    Binary.get1Bit(bit, value),
                    PDU.isByteEditable(pdu.getType(), index));
            }
        }

        void setValue(byte value) {

            for(int bit = 0; bit < 8; ++bit) {

                labels[bit].set(Binary.get1Bit(bit, value));
            }
        }

        byte getValue() {

            byte value = 0x00;

            for(int bit = 0; bit < 8; ++bit) {

                value = setBit(bit, value, labels[bit].isSet());
            }

            return value;
        }

        public boolean isEditable() {

            for(BitLabel label : labels) {

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

                buffer.append(labels[bit].getText());
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
            offset = ((this.index * 8) + (7 - this.bit));
            this.editable = editable;

            set(value);

            super.addMouseListener(BinaryEditorFrame.this);

            if (!this.editable) {

                super.setForeground(Color.GRAY);
            }
        }

        int getIndex() { return index; }

        int getBit() { return bit; }

        int getOffset() { return offset; }

        boolean isEditable() { return editable; }

        boolean isSet() { return super.getText().equals(VALUES[1]); }

        void toggle() {

            set(isSet() ? 0 : 1);
        }

        void set(int value) {

            super.setText(VALUES[value]);
        }
    }
}
