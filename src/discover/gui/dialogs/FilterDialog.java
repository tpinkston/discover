package discover.gui.dialogs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;
import discover.gui.tabs.PDUTab.TableFilter;
import discover.gui.tabs.PDUTab.TableModel;
import discover.vdis.PDU;
import discover.vdis.enums.VDIS;

/**
 * @author Tony Pinkston
 */
public class FilterDialog implements ActionListener {

    private static final Logger logger = LoggerFactory.getLogger(FilterDialog.class);

    private static final String CLEAR = "Clear";
    private static final String REVERT = "Revert";
    private static final String OKAY = "Okay";
    private static final String CANCEL = "Cancel";
    private static final String NONE = "None";

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(DiscoverFrame.getFrame(), "") {

        @Override
        public void dispose() {

            FilterDialog.this.disposing();

            super.dispose();
        }
    };

    private final JButton included = new JButton(NONE);
    private final JButton excluded = new JButton(NONE);
    private final JButton clear = new JButton(CLEAR);
    private final JButton revert = new JButton(REVERT);
    private final JButton okay = new JButton(OKAY);
    private final JButton cancel = new JButton(CANCEL);
    private final JComboBox<String> protocol = new JComboBox<>();
    private final JComboBox<String> family = new JComboBox<>();
    private final JComboBox<String> domain = new JComboBox<>();
    private final JComboBox<String> kind = new JComboBox<>();
    private final JTextField marking = new JTextField();
    private final JFormattedTextField port = Utilities.getIntegerField(null);
    private final JFormattedTextField exercise = Utilities.getIntegerField(null);
    private final JFormattedTextField request = Utilities.getIntegerField(null);
    private final JFormattedTextField site = Utilities.getIntegerField(null);
    private final JFormattedTextField application = Utilities.getIntegerField(null);
    private final JFormattedTextField entity = Utilities.getIntegerField(null);
    private final List<Integer> typesIncluded = new ArrayList<Integer>();
    private final List<Integer> typesExcluded = new ArrayList<Integer>();
    private final TableModel model;
    private final TableFilter filter;

    public FilterDialog(
        String title,
        TableModel model,
        TableFilter filter) {

        dialog.setTitle("Filter: " + title);
        this.model = model;
        this.filter = filter;

        configureComponents();

        typesIncluded.addAll(filter.includedTypes);
        typesExcluded.addAll(filter.excludedTypes);

        setWidgetValues();
        modifiedTypes();
        showDialog();
    }

    public FilterDialog(
        String title,
        PDU pdu,
        TableModel model,
        TableFilter filter) {

        int type = pdu.getType();

        dialog.setTitle("Filter: " + title);
        this.model = model;
        this.filter = filter;

        configureComponents();

        port.setValue(pdu.getPort());
        exercise.setValue(pdu.getExercise());
        typesIncluded.add(type);
        included.setText("1");

        Utilities.setComboBoxValue(
            protocol,
            VDIS.PROTOCOL_VERSION,
            pdu.getProtocol());
        Utilities.setComboBoxValue(
            family,
            VDIS.PDU_FAMILY,
            pdu.getFamily());

        if (pdu.hasInitiator()) {

            site.setValue(pdu.getSiteId());
            application.setValue(pdu.getApplicationId());
            entity.setValue(pdu.getEntityId());
        }

        if (pdu.hasRequestId()) {

            request.setValue(pdu.getRequestId());
        }

        if (type == VDIS.PDU_TYPE_ENTITY_STATE) {

            setEntityParametersEditable(true);

            marking.setText(pdu.getMarking());

            Utilities.setComboBoxValue(
                domain,
                VDIS.DOMAIN,
                pdu.getEntityDomain());
            Utilities.setComboBoxValue(
                kind,
                VDIS.ENT_KIND,
                pdu.getEntityKind());
        }
        else {

            setEntityParametersEditable(false);
        }

        showDialog();
    }

    public void configure(PDU pdu) {

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (event.getSource() instanceof JTextField) {

            if (event.getSource() == request) {

                checkLongValue((JTextField) event.getSource());
            }
            else {

                checkIntegerValue((JTextField) event.getSource());
            }
        }
        else if (event.getSource() == included) {

            PDUTypeDialog dialog = new PDUTypeDialog(
                "Included PDU Types",
                this.dialog,
                typesIncluded);

            dialog.apply(typesIncluded);

            modifiedTypes();
        }
        else if (event.getSource() == excluded) {

            PDUTypeDialog dialog = new PDUTypeDialog(
                "Excluded PDU Types",
                this.dialog,
                typesExcluded);

            dialog.apply(typesExcluded);

            modifiedTypes();
        }
        else if (command != null) {

            if (command.equals(CLEAR)) {

                clearAllParameters();
            }
            else if (command.equals(REVERT)) {

                setWidgetValues();
            }
            else if (command.equals(OKAY)) {

                setFilterValues();
                model.fireTableDataChanged();
                dialog.dispose();
            }
            else if (command.equals(CANCEL)) {

                dialog.dispose();
            }
        }
    }

    private void disposing() {

        clear.removeActionListener(this);
        revert.removeActionListener(this);
        okay.removeActionListener(this);
        cancel.removeActionListener(this);
        included.removeActionListener(this);
        excluded.removeActionListener(this);
    }

    private void modifiedTypes() {

        if (typesIncluded.isEmpty()) {

            included.setText(NONE);
        }
        else {

            included.setText(Integer.toString(typesIncluded.size()));
        }

        if (typesExcluded.isEmpty()) {

            excluded.setText(NONE);
        }
        else {

            excluded.setText(Integer.toString(typesExcluded.size()));
        }

        if (typesIncluded.isEmpty() && typesExcluded.isEmpty()) {

            setEntityParametersEditable(false);
        }
        else if (typesIncluded.contains(VDIS.PDU_TYPE_ENTITY_STATE)) {

            setEntityParametersEditable(true);
        }
        else {

            setEntityParametersEditable(false);
        }
    }

    private void checkIntegerValue(JTextField component) {

        String value = component.getText();

        try {

            if (!value.isEmpty()) {

                Integer.parseInt(value);
            }
        }
        catch(NumberFormatException exception) {

            JOptionPane.showMessageDialog(
                component,
                "Invalid number: " + value,
                "Error",
                JOptionPane.ERROR_MESSAGE);

            component.setText("");
        }
    }

    private void checkLongValue(JTextField component) {

        String value = component.getText();

        try {

            if (!value.isEmpty()) {

                Long.parseLong(value);
            }
        }
        catch(NumberFormatException exception) {

            JOptionPane.showMessageDialog(
                component,
                "Invalid number: " + value,
                "Error",
                JOptionPane.ERROR_MESSAGE);

            component.setText("");
        }
    }

    private void setEntityParametersEditable(boolean editable) {

        marking.setEnabled(editable);
        marking.setEnabled(editable);
        domain.setEnabled(editable);
        kind.setEnabled(editable);

        if (!editable) {

            clearEntityParameters();
        }
    }

    private void clearAllParameters() {

        included.setText(NONE);
        excluded.setText(NONE);

        protocol.setSelectedIndex(0);
        family.setSelectedIndex(0);
        port.setValue(null);
        exercise.setValue(null);
        request.setValue(null);
        site.setValue(null);
        application.setValue(null);
        entity.setValue(null);
        typesIncluded.clear();
        typesExcluded.clear();

        modifiedTypes();
        clearEntityParameters();
    }

    private void clearEntityParameters() {

        marking.setText("");
        domain.setSelectedIndex(0);
        kind.setSelectedIndex(0);
    }

    /**
     * Sets widget values from current filter values.
     */
    private void setWidgetValues() {

        site.setValue(filter.site);
        application.setValue(filter.application);
        entity.setValue(filter.entity);

        port.setValue(filter.port);
        exercise.setValue(filter.exercise);
        request.setValue(filter.request);

        Utilities.setComboBoxValue(
            protocol,
            VDIS.PROTOCOL_VERSION,
            filter.protocol);
        Utilities.setComboBoxValue(
            family,
            VDIS.PDU_FAMILY,
            filter.family);
        Utilities.setComboBoxValue(
            domain,
            VDIS.DOMAIN,
            filter.domain);
        Utilities.setComboBoxValue(
            kind,
            VDIS.ENT_KIND,
            filter.kind);

        if (!filter.includedTypes.contains(VDIS.PDU_TYPE_ENTITY_STATE)) {

            setEntityParametersEditable(false);
        }
        else {

            setEntityParametersEditable(true);

            if (filter.marking == null) {

                marking.setText("");
            }
            else {

                marking.setText(filter.marking);
            }
        }
    }

    /**
     * Sets filter values using user specified widget values.
     */
    private void setFilterValues() {

        filter.site = (Integer)site.getValue();
        filter.application = (Integer)application.getValue();
        filter.entity = (Integer)entity.getValue();
        filter.port = (Integer)port.getValue();
        filter.exercise = (Integer)exercise.getValue();
        filter.request = (Integer)request.getValue();
        filter.marking = marking.getText().trim();

        filter.protocol = Utilities.getComboboxValue(
            protocol,
            VDIS.PROTOCOL_VERSION);
        filter.family = Utilities.getComboboxValue(
            family,
            VDIS.PDU_FAMILY);
        filter.domain = Utilities.getComboboxValue(
            domain,
            VDIS.DOMAIN);
        filter.kind = Utilities.getComboboxValue(
            kind,
            VDIS.ENT_KIND);

        filter.includedTypes.clear();
        filter.includedTypes.addAll(typesIncluded);
        filter.excludedTypes.clear();
        filter.excludedTypes.addAll(typesExcluded);

        if (filter.marking.isEmpty()) {

            filter.marking = null;
        }

        logger.debug("New Filter:\n" + filter.toString());
    }

    private void configureComponents() {

        port.setColumns(10);

        site.setColumns(5);
        site.setHorizontalAlignment(JTextField.RIGHT);
        application.setColumns(5);
        application.setHorizontalAlignment(JTextField.RIGHT);
        entity.setColumns(5);
        entity.setHorizontalAlignment(JTextField.RIGHT);

        Utilities.configureComboBox(
            protocol,
            VDIS.PROTOCOL_VERSION,
            true);
        Utilities.configureComboBox(
            family,
            VDIS.PDU_FAMILY,
            true);
        Utilities.configureComboBox(
            domain,
            VDIS.DOMAIN,
            true);
        Utilities.configureComboBox(
            kind,
            VDIS.ENT_KIND,
            true);

        clear.setActionCommand(CLEAR);
        clear.addActionListener(this);

        revert.setActionCommand(REVERT);
        revert.addActionListener(this);

        okay.setActionCommand(OKAY);
        okay.addActionListener(this);

        cancel.setActionCommand(CANCEL);
        cancel.addActionListener(this);

        domain.setEnabled(false);

        kind.setEnabled(false);

        included.addActionListener(this);

        excluded.addActionListener(this);
    }

    private void showDialog() {

        fill();

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void fill() {

        int y = -1;

        Utilities.setGridBagLayout(dialog.getContentPane());

        Utilities.addItem(
            dialog.getContentPane(),
            port,
            "Port:",
            ++y);
        Utilities.addItem(
            dialog.getContentPane(),
            exercise,
            "Exercise",
            ++y);
        Utilities.addItem(
            dialog.getContentPane(),
            protocol,
            "Protocol:",
            ++y);
        Utilities.addItem(
            dialog.getContentPane(),
            family,
            "Family:",
            ++y);
        Utilities.addItem(
            dialog.getContentPane(),
            included,
            "Included Types:",
            ++y);
        Utilities.addItem(
            dialog.getContentPane(),
            excluded,
            "Excluded Types:",
            ++y);
        Utilities.addComponent(
            dialog.getContentPane(),
            new EntityIdPanel(),
            Utilities.HORIZONTAL,
            0, ++y,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 0, 0, 0));
        Utilities.addComponent(
            dialog.getContentPane(),
            new EntityStatePanel(),
            Utilities.HORIZONTAL,
            0, ++y,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 0, 0, 0));
        Utilities.addComponent(
            dialog.getContentPane(),
            new MiscellaneousPanel(),
            Utilities.HORIZONTAL,
            0, ++y,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 0, 0, 0));
        Utilities.addComponent(
            dialog.getContentPane(),
            new ButtonPanel(),
            Utilities.HORIZONTAL,
            0, ++y,
            2, 1,
            1.0, 1.0,
            Utilities.getInsets(10, 6, 1, 6));

        Utilities.center(DiscoverFrame.getFrame(), dialog);
    }

    @SuppressWarnings("serial")
    class MiscellaneousPanel extends JPanel {

        public MiscellaneousPanel() {

            Utilities.setGridBagLayout(this);
            Utilities.addItem(this, request, "Request Id:", 0);
            Utilities.setBorder(this, "Miscellaneous");
        }
    }

    @SuppressWarnings("serial")
    class EntityStatePanel extends JPanel {

        public EntityStatePanel() {

            Utilities.setGridBagLayout(this);
            Utilities.addItem(this, marking, "Marking:", 0);
            Utilities.addItem(this, domain, "Domain:", 1);
            Utilities.addItem(this, kind, "Kind:", 2);
            Utilities.setBorder(this, "Entity State");
        }
    }

    @SuppressWarnings("serial")
    class EntityIdPanel extends JPanel {

        public EntityIdPanel() {

            super(new GridLayout(1, 3, 10, 10));
            super.add(site);
            super.add(application);
            super.add(entity);
            Utilities.setBorder(this, "Entity Id");
        }
    }

    @SuppressWarnings("serial")
    class ButtonPanel extends JPanel {

        public ButtonPanel() {

            Utilities.setGridBagLayout(this);

            Utilities.addComponent(
                this,
                clear,
                Utilities.HORIZONTAL,
                0, 0,
                1, 1,
                0.5, 0.5,
                Utilities.getInsets(1, 1, 1, 1));
            Utilities.addComponent(
                this,
                revert,
                Utilities.HORIZONTAL,
                1, 0,
                1, 1,
                0.5, 0.5,
                Utilities.getInsets(1, 1, 1, 1));
            Utilities.addComponent(
                this,
                okay,
                Utilities.HORIZONTAL,
                0, 1,
                1, 1,
                0.5, 0.5,
                Utilities.getInsets(1, 1, 1, 1));
            Utilities.addComponent(
                this,
                cancel,
                Utilities.HORIZONTAL,
                1, 1,
                1, 1,
                0.5, 0.5,
                Utilities.getInsets(1, 1, 1, 1));
        }
    }
}
