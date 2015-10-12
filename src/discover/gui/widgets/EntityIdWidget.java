package discover.gui.widgets;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import discover.gui.Utilities;
import discover.vdis.common.EntityId;

/**
 * @author Tony Pinkston
 */
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

        for(int i = 0; i < fields.length; ++i) {

            fields[i] = Utilities.getIntegerField(0);
            fields[i].setValue(0);
            fields[i].setColumns(5);

            Utilities.addComponent(
                getPanel(),
                new JLabel(LABELS[i]),
                Utilities.HORIZONTAL,
                (i * 2), 0,
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(8, 2, 2, 2));
            Utilities.addComponent(
                getPanel(),
                fields[i],
                Utilities.HORIZONTAL,
                ((i * 2) + 1), 0,
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(2, 2, 2, 6));
        }

        if (id != null) {

            setValue(id);
        }
    }

    public void clear() {

        setValue(null);
    }

    public void getValue(EntityId id) {

        id.setSite(Utilities.getIntegerValue(fields[0]));
        id.setApplication(Utilities.getIntegerValue(fields[1]));
        id.setEntity(Utilities.getIntegerValue(fields[2]));
    }

    public void setValue(EntityId id) {

        if (id != null) {

            fields[0].setValue(id.getSite());
            fields[1].setValue(id.getApplication());
            fields[2].setValue(id.getEntity());
        }
        else for(int i = 0; i < fields.length; ++i) {

            fields[i].setValue(0);
        }
    }
}
