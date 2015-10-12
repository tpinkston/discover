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

/**
 * @author Tony Pinkston
 */
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

        pane.setContentType(type);

        fontName = ((font != null) ? font : pane.getName());
        fontSize = pane.getFont().getSize();
        size.setText(Integer.toString(fontSize));

        JToolBar tools = new JToolBar();

        tools.setFloatable(false);
        tools.add(increase);
        tools.add(decrease);
        tools.addSeparator();
        tools.add(new JLabel("Size: "));
        tools.add(size);

        scroller.setViewportView(pane);

        Utilities.addComponent(
            panel,
            tools,
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            null);

        Utilities.addComponent(
            panel,
            scroller,
            Utilities.BOTH,
            0, 1,
            1, 1,
            1.0, 1.0,
            null);

        setFontSize(pane.getFont().getSize());
        pane.setEditable(false);
    }

    public JPanel getPanel() {

        return panel;
    }

    public void setTextPreferredSize(Dimension dimension) {

        pane.setPreferredSize(dimension);
    }

    public void setText(String text) {

      pane.setText(text);
    }

    public void setCaretPosition(int position) {

        pane.setCaretPosition(position);
    }

    public JScrollPane getScroller() {

        return scroller;
    }

    public void setVerticalScrollBarPolicy(int policy) {

        scroller.setVerticalScrollBarPolicy(policy);
    }

    public void setHorizontalScrollBarPolicy(int policy) {

        scroller.setHorizontalScrollBarPolicy(policy);
    }

    /**
     * Found that on some LAFs this has no effect, not sure why.
     *
     * @param size
     */
    protected void setFontSize(int size) {

        Font font = pane.getFont();

        fontSize = size;

        pane.setFont(new Font(
            fontName,
            font.getStyle(),
            fontSize));

        decrease.setEnabled(fontSize > MIN_FONT_SIZE);
        increase.setEnabled(fontSize < MAX_FONT_SIZE);

        this.size.setText(Integer.toString(fontSize));
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

            setFontSize(fontSize + 1);
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

            setFontSize(fontSize - 1);
        }
    }
}
