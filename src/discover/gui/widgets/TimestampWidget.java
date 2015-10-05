/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.common.Timestamp;

public class TimestampWidget extends ToggleWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    public final JCheckBox absolute = new JCheckBox("Absolute");
    public final JFormattedTextField minutes = Utilities.getIntegerField(0);
    public final JFormattedTextField seconds = Utilities.getFloatField(0.0f, null);

    public TimestampWidget(String title) {

        super(title);

        this.minutes.setColumns(5);
        this.seconds.setColumns(5);
        this.absolute.setSelected(false);

        this.fill();
    }

    @Override
    public JComponent getComponent() {

        return this.panel;
    }

    public Timestamp getValue() {

        Timestamp timestamp = new Timestamp();

        this.getValue(timestamp);

        return timestamp;
    }

    public void getValue(Timestamp timestamp) {

        timestamp.setAbsolute(this.absolute.isSelected());
        timestamp.setMinutes(Utilities.getIntegerValue(this.minutes));
        timestamp.setSeconds(Utilities.getFloatValue(this.seconds));
    }

    public void setValue(Timestamp timestamp) {

        this.absolute.setSelected(timestamp.isAbsolute());
        this.minutes.setValue(timestamp.getMinutes());
        this.seconds.setValue(timestamp.getSeconds());
    }

    @Override
    protected void fill() {

        super.fill();

        Utilities.addComponent(
            this.panel,
            new JLabel("Minutes:"),
            Utilities.NONE,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 2, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.minutes,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel,
            new JLabel("Seconds:"),
            Utilities.NONE,
            2, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(8, 20, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.seconds,
            Utilities.HORIZONTAL,
            3, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(4, 2, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.absolute,
            Utilities.HORIZONTAL,
            4, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 20, 2, 2));
    }
}
