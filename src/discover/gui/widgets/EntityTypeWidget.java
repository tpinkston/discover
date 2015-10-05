/**
 * @author Tony Pinkston
 */
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

public class EntityTypeWidget extends ToggleWidget {

    private final JPanel panel = Utilities.getGridBagPanel("");

    public final JFormattedTextField fields[] = new JFormattedTextField[7];
    public final JLabel label = new JLabel();

    public EntityTypeWidget(String title) {

        super(title);

        for(int i = 0; i < this.fields.length; ++i) {

            this.fields[i] = Utilities.getIntegerField(0);
            this.fields[i].setValue(0);
            this.fields[i].setColumns(3);
            this.fields[i].addActionListener(this);
            this.fields[i].addFocusListener(this);
        }

        this.updateLabel();
        this.fill();
    }

    @Override
    public JComponent getComponent() {

        return this.panel;
    }

    public void clear() {

        for(int i = 0; i < this.fields.length; ++i) {

            this.fields[i].setValue(0);
        }

        this.updateLabel();
    }

    public long getLongValue() {

        int values[] = new int[7];

        for(int i = 0; i < values.length; ++i) {

            values[i] = Utilities.getIntegerValue(this.fields[i]);
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

        this.setValue(EntityTypes.getEntityType(value));
    }

    public EntityType getValue() {

        return EntityTypes.getEntityType(this.getLongValue());
    }

    public void setValue(EntityType type) {

        if (type == null) {

            this.clear();
        }
        else {

            this.fields[0].setValue(type.septuple.kind);
            this.fields[1].setValue(type.septuple.domain);
            this.fields[2].setValue(type.septuple.country);
            this.fields[3].setValue(type.septuple.category);
            this.fields[4].setValue(type.septuple.subcategory);
            this.fields[5].setValue(type.septuple.specific);
            this.fields[6].setValue(type.septuple.extension);

            this.updateLabel();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        this.updateLabel();
    }

    @Override
    public void focusLost(FocusEvent event) {

        this.updateLabel();
    }

    @Override
    public void focusGained(FocusEvent event) {

        this.updateLabel();
    }

    private void updateLabel() {

        EntityType type = this.getValue();
        this.label.setText(type.description);
    }

    @Override
    protected void fill() {

        super.fill();

        for(int i = 0; i < 7; ++i) {

            int left = (i == 0) ? 6 : 1;
            int right = (i == 6) ? 6 : 1;

            Utilities.addComponent(
                this.panel,
                this.fields[i],
                Utilities.HORIZONTAL,
                i, 0,
                1, 1,
                0.1, 0.0,
                Utilities.getInsets(6, left, 3, right));
        }

        Utilities.addComponent(
            this.panel,
            this.label,
            Utilities.HORIZONTAL,
            0, 1,
            7, 1,
            0.0, 0.0,
            Utilities.getInsets(7, 6, 3, 6));
    }
}
