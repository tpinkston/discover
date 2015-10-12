package discover.gui.widgets;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.common.Timestamp;

/**
 * @author Tony Pinkston
 */
public class TimestampWidget extends ToggleWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    public final JCheckBox absolute = new JCheckBox("Absolute");
    public final JFormattedTextField minutes = Utilities.getIntegerField(0);
    public final JFormattedTextField seconds = Utilities.getFloatField(0.0f, null);

    public TimestampWidget(String title) {

        super(title);

        minutes.setColumns(5);
        seconds.setColumns(5);
        absolute.setSelected(false);

        fill();
    }

    @Override
    public JComponent getComponent() {

        return panel;
    }

    public Timestamp getValue() {

        Timestamp timestamp = new Timestamp();

        this.getValue(timestamp);

        return timestamp;
    }

    public void getValue(Timestamp timestamp) {

        timestamp.setAbsolute(absolute.isSelected());
        timestamp.setMinutes(Utilities.getIntegerValue(minutes));
        timestamp.setSeconds(Utilities.getFloatValue(seconds));
    }

    public void setValue(Timestamp timestamp) {

        absolute.setSelected(timestamp.isAbsolute());
        minutes.setValue(timestamp.getMinutes());
        seconds.setValue(timestamp.getSeconds());
    }

    @Override
    protected void fill() {

        super.fill();

        Utilities.addComponent(
            panel,
            new JLabel("Minutes:"),
            Utilities.NONE,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
        Utilities.addComponent(
            panel,
            minutes,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            new JLabel("Seconds:"),
            Utilities.NONE,
            2, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 20, 2, 2));
        Utilities.addComponent(
            panel,
            seconds,
            Utilities.HORIZONTAL,
            3, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            panel,
            absolute,
            Utilities.HORIZONTAL,
            4, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 20, 2, 2));
    }
}
