/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import discover.gui.Utilities;
import discover.vdis.common.EntityId;

public class EntityIdWidget extends Widget {

    private static final String LABELS[] = {

        "Site:",
        "Application:",
        "Entity:"
    };

    public final JFormattedTextField fields[] = new JFormattedTextField[3];

    public EntityIdWidget() {

        this(null);
    }

    public EntityIdWidget(EntityId id) {

        super("Entity Id");

        for(int i = 0; i < this.fields.length; ++i) {

            this.fields[i] = Utilities.getIntegerField(0);
            this.fields[i].setValue(0);
            this.fields[i].setColumns(5);

            Utilities.addComponent(
                super.getPanel(),
                new JLabel(LABELS[i]),
                Utilities.HORIZONTAL,
                (i * 2), 0,
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(8, 2, 2, 2));
            Utilities.addComponent(
                super.getPanel(),
                this.fields[i],
                Utilities.HORIZONTAL,
                ((i * 2) + 1), 0,
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(2, 2, 2, 6));
        }

        if (id != null) {

            this.setValue(id);
        }
    }

    public void clear() {

        this.setValue(null);
    }

    public void getValue(EntityId id) {

        id.setSite(Utilities.getIntegerValue(this.fields[0]));
        id.setApplication(Utilities.getIntegerValue(this.fields[1]));
        id.setEntity(Utilities.getIntegerValue(this.fields[2]));
    }

    public void setValue(EntityId id) {

        if (id != null) {

            this.fields[0].setValue(id.getSite());
            this.fields[1].setValue(id.getApplication());
            this.fields[2].setValue(id.getEntity());
        }
        else for(int i = 0; i < this.fields.length; ++i) {

            this.fields[i].setValue(0);
        }
    }
}
