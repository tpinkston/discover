/**
 * @author Tony Pinkston
 */
package discover.gui.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import discover.gui.Utilities;

public class TextPanel {

    private static final String INCREASE = "font_increase.gif";
    private static final String DECREASE = "font_decrease.gif";
    private static final int MIN_FONT_SIZE = 6;
    private static final int MAX_FONT_SIZE = 30;

    private final JPanel panel = Utilities.getGridBagPanel(null);
    private final JScrollPane scroller = new JScrollPane();
    private final JEditorPane pane = new JEditorPane();
    private final JLabel size = new JLabel();

    private final AbstractAction increase = new Increase();
    private final AbstractAction decrease = new Decrease();

    private final String fontName;
    private int fontSize;

    /**
     * @param type - Content type for JEditorPane
     * @param font - Font name for JEditorPane (null for default)
     */
    public TextPanel(String type, String font) {

        this.pane.setContentType(type);

        if (font != null) {

            this.fontName = font;
        }
        else {

            this.fontName = this.pane.getName();
        }

        this.fontSize = this.pane.getFont().getSize();
        this.size.setText(Integer.toString(this.fontSize));

        JToolBar tools = new JToolBar();

        tools.setFloatable(false);
        tools.add(this.increase);
        tools.add(this.decrease);
        tools.addSeparator();
        tools.add(new JLabel("Size: "));
        tools.add(this.size);

        this.scroller.setViewportView(this.pane);

        Utilities.addComponent(
            this.panel,
            tools,
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            null);

        Utilities.addComponent(
            this.panel,
            this.scroller,
            Utilities.BOTH,
            0, 1,
            1, 1,
            1.0, 1.0,
            null);

        this.setFontSize(this.pane.getFont().getSize());
        this.pane.setEditable(false);
    }

    public JPanel getPanel() {

        return this.panel;
    }

    public void setTextPreferredSize(Dimension dimension) {

        this.pane.setPreferredSize(dimension);
    }

    public void setText(String text) {

      this.pane.setText(text);
    }

    public void setCaretPosition(int position) {

        this.pane.setCaretPosition(position);
    }

    public JScrollPane getScroller() {

        return this.scroller;
    }

    public void setVerticalScrollBarPolicy(int policy) {

        this.scroller.setVerticalScrollBarPolicy(policy);
    }

    public void setHorizontalScrollBarPolicy(int policy) {

        this.scroller.setHorizontalScrollBarPolicy(policy);
    }

    /**
     * Found that on some LAFs this has no effect, not sure why.
     *
     * @param size
     */
    protected void setFontSize(int size) {

        Font font = this.pane.getFont();

        this.fontSize = size;

        this.pane.setFont(new Font(
            this.fontName,
            font.getStyle(),
            this.fontSize));

        this.decrease.setEnabled(this.fontSize > MIN_FONT_SIZE);
        this.increase.setEnabled(this.fontSize < MAX_FONT_SIZE);

        this.size.setText(Integer.toString(this.fontSize));
    }

    @SuppressWarnings("serial")
    class Increase extends AbstractAction {

        public Increase() {

            super("Increase");
            super.putValue(Action.SMALL_ICON, Utilities.getImageIcon(INCREASE));
            super.putValue(Action.SHORT_DESCRIPTION, "Increases font size.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            setFontSize(TextPanel.this.fontSize + 1);
        }
    }

    @SuppressWarnings("serial")
    class Decrease extends AbstractAction {

        public Decrease() {

            super("Decrease");
            super.putValue(Action.SMALL_ICON, Utilities.getImageIcon(DECREASE));
            super.putValue(Action.SHORT_DESCRIPTION, "Decreases font size.");
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            setFontSize(TextPanel.this.fontSize - 1);
        }
    }
}
