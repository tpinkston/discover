package discover.gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import discover.gui.Utilities;
import discover.vdis.types.EntityType;
import discover.vdis.types.EntityTypes;
import discover.vdis.types.Septuple;

/**
 * @author Tony Pinkston
 */
public class EntityTypeWidget extends ToggleWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    public final JFormattedTextField fields[] = new JFormattedTextField[7];
    public final JLabel label = new JLabel();

    public EntityTypeWidget(String title) {

        super(title);

        for(int i = 0; i < fields.length; ++i) {

            fields[i] = Utilities.getIntegerField(0);
            fields[i].setValue(0);
            fields[i].setColumns(3);
            fields[i].addActionListener(this);
            fields[i].addFocusListener(this);
        }

        updateLabel();
        fill();
    }

    @Override
    public JComponent getComponent() {

        return panel;
    }

    public void clear() {

        for(int i = 0; i < fields.length; ++i) {

            fields[i].setValue(0);
        }

        updateLabel();
    }

    public long getLongValue() {

        int values[] = new int[7];

        for(int i = 0; i < values.length; ++i) {

            values[i] = Utilities.getIntegerValue(fields[i]);
        }

        long value = Septuple.toLong(
            values[0],
            values[1],
            values[2],
            values[3],
            values[4],
            values[5],
            values[6]);

        return value;
    }

    public void setLongValue(long value) {

        setValue(EntityTypes.getEntityType(value));
    }

    public EntityType getValue() {

        return EntityTypes.getEntityType(getLongValue());
    }

    public void setValue(EntityType type) {

        if (type == null) {

            clear();
        }
        else {

            fields[0].setValue(type.septuple.kind);
            fields[1].setValue(type.septuple.domain);
            fields[2].setValue(type.septuple.country);
            fields[3].setValue(type.septuple.category);
            fields[4].setValue(type.septuple.subcategory);
            fields[5].setValue(type.septuple.specific);
            fields[6].setValue(type.septuple.extension);

            updateLabel();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        updateLabel();
    }

    @Override
    public void focusLost(FocusEvent event) {

        updateLabel();
    }

    @Override
    public void focusGained(FocusEvent event) {

        updateLabel();
    }

    private void updateLabel() {

        EntityType type = getValue();
        label.setText(type.description);
    }

    @Override
    protected void fill() {

        super.fill();

        for(int i = 0; i < 7; ++i) {

            int left = (i == 0) ? 6 : 1;
            int right = (i == 6) ? 6 : 1;

            Utilities.addComponent(
                panel,
                fields[i],
                Utilities.HORIZONTAL,
                i, 0,
                1, 1,
                0.1, 0.0,
                Utilities.getInsets(6, left, 3, right));
        }

        Utilities.addComponent(
            panel,
            label,
            Utilities.HORIZONTAL,
            0, 1,
            7, 1,
            0.0, 0.0,
            Utilities.getInsets(7, 6, 3, 6));
    }
}
