/**
 * @author Tony Pinkston
 */
package discover.gui.dialogs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import discover.Discover;
import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;
import discover.gui.tabs.PDUTab.TableFilter;
import discover.gui.tabs.PDUTab.TableModel;
import discover.vdis.PDU;
import discover.vdis.enums.VDIS;

public class FilterDialog implements ActionListener {

    private static final Logger logger = Discover.getLogger();
    
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

        this.dialog.setTitle("Filter: " + title);
        this.model = model;
        this.filter = filter;

        this.configureComponents();

        this.typesIncluded.addAll(this.filter.includedTypes);
        this.typesExcluded.addAll(this.filter.excludedTypes);

        this.setWidgetValues();
        this.modifiedTypes();
        this.showDialog();
    }

    public FilterDialog(
        String title,
        PDU pdu,
        TableModel model, 
        TableFilter filter) {
        
        int type = pdu.getType();
        
        this.dialog.setTitle("Filter: " + title);
        this.model = model;
        this.filter = filter;

        this.configureComponents();
        
        this.port.setValue(pdu.getPort());
        this.exercise.setValue(pdu.getExercise());
        this.typesIncluded.add(type);
        this.included.setText("1");
        
        Utilities.setComboBoxValue(
            this.protocol,
            VDIS.PROTOCOL_VERSION,
            pdu.getProtocol());
        Utilities.setComboBoxValue(
            this.family,
            VDIS.PDU_FAMILY,
            pdu.getFamily());

        if (pdu.hasInitiator()) {
            
            this.site.setValue(pdu.getSiteId());
            this.application.setValue(pdu.getApplicationId());
            this.entity.setValue(pdu.getEntityId());
        }
        
        if (pdu.hasRequestId()) {
            
            this.request.setValue(pdu.getRequestId());
        }
        
        if (type == VDIS.PDU_TYPE_ENTITY_STATE) {

            this.setEntityParametersEditable(true);

            this.marking.setText(pdu.getMarking());

            Utilities.setComboBoxValue(
                this.domain,
                VDIS.DOMAIN,
                pdu.getEntityDomain());
            Utilities.setComboBoxValue(
                this.kind,
                VDIS.ENT_KIND,
                pdu.getEntityKind());
        }
        else {

            this.setEntityParametersEditable(false);
        }

        this.showDialog();
    }
    
    public void configure(PDU pdu) {
        
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (event.getSource() instanceof JTextField) {
            
            if (event.getSource() == this.request) {
                
                this.checkLongValue((JTextField)event.getSource());
            }
            else {
                
                this.checkIntegerValue((JTextField)event.getSource());
            }
        }
        else if (event.getSource() == this.included) {
         
            PDUTypeDialog dialog = new PDUTypeDialog(
                "Included PDU Types", 
                this.dialog, 
                this.typesIncluded);
            
            dialog.apply(this.typesIncluded);

            this.modifiedTypes();
        }
        else if (event.getSource() == this.excluded) {
            
            
            PDUTypeDialog dialog = new PDUTypeDialog(
                "Excluded PDU Types", 
                this.dialog, 
                this.typesExcluded);
            
            dialog.apply(this.typesExcluded);
            
            this.modifiedTypes();
        }
        else if (command != null) {

            if (command.equals(CLEAR)) {

                this.clearAllParameters();
            }
            else if (command.equals(REVERT)) {

                this.setWidgetValues();
            }
            else if (command.equals(OKAY)) {

                this.setFilterValues();
                this.model.fireTableDataChanged();
                this.dialog.dispose();
            }
            else if (command.equals(CANCEL)) {

                this.dialog.dispose();
            }
        }
    }
    
    private void disposing() {
        
        this.clear.removeActionListener(this);
        this.revert.removeActionListener(this);
        this.okay.removeActionListener(this);
        this.cancel.removeActionListener(this);
        this.included.removeActionListener(this);
        this.excluded.removeActionListener(this);
    }
    
    private void modifiedTypes() {
        
        if (this.typesIncluded.isEmpty()) {

            this.included.setText(NONE);
        }
        else {
            
            this.included.setText(Integer.toString(this.typesIncluded.size()));
        }
        
        if (this.typesExcluded.isEmpty()) {

            this.excluded.setText(NONE);
        }
        else {
            
            this.excluded.setText(Integer.toString(this.typesExcluded.size()));
        }

        if (this.typesIncluded.isEmpty() && this.typesExcluded.isEmpty()) {
            
            this.setEntityParametersEditable(false);
        }
        else if (this.typesIncluded.contains(VDIS.PDU_TYPE_ENTITY_STATE)) {
            
            this.setEntityParametersEditable(true);
        }
        else {
            
            this.setEntityParametersEditable(false);
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

        this.marking.setEnabled(editable);
        this.marking.setEnabled(editable);
        this.domain.setEnabled(editable);
        this.kind.setEnabled(editable);

        if (!editable) {

            this.clearEntityParameters();
        }
    }

    private void clearAllParameters() {

        this.included.setText(NONE);
        this.excluded.setText(NONE);

        this.protocol.setSelectedIndex(0);
        this.family.setSelectedIndex(0);
        this.port.setValue(null);
        this.exercise.setValue(null);
        this.request.setValue(null);
        this.site.setValue(null);
        this.application.setValue(null);
        this.entity.setValue(null);
        this.typesIncluded.clear();
        this.typesExcluded.clear();
        
        this.modifiedTypes();
        this.clearEntityParameters();
    }

    private void clearEntityParameters() {

        this.marking.setText("");
        this.domain.setSelectedIndex(0);
        this.kind.setSelectedIndex(0);
    }

    /**
     * Sets widget values from current filter values.
     */
    private void setWidgetValues() {

        this.site.setValue(this.filter.site);
        this.application.setValue(this.filter.application);
        this.entity.setValue(this.filter.entity);

        this.port.setValue(this.filter.port);
        this.exercise.setValue(this.filter.exercise);
        this.request.setValue(this.filter.request);
        
        Utilities.setComboBoxValue(
            this.protocol, 
            VDIS.PROTOCOL_VERSION, 
            this.filter.protocol);
        Utilities.setComboBoxValue(
            this.family, 
            VDIS.PDU_FAMILY, 
            this.filter.family);
        Utilities.setComboBoxValue(
            this.domain, 
            VDIS.DOMAIN, 
            this.filter.domain);
        Utilities.setComboBoxValue(
            this.kind, 
            VDIS.ENT_KIND, 
            this.filter.kind);

        if (!this.filter.includedTypes.contains(VDIS.PDU_TYPE_ENTITY_STATE)) {

            this.setEntityParametersEditable(false);
        }
        else {

            this.setEntityParametersEditable(true);

            if (this.filter.marking == null) {
                
                this.marking.setText("");
            }
            else {
                
                this.marking.setText(this.filter.marking);
            }
        }
    }

    /**
     * Sets filter values using user specified widget values.
     */
    private void setFilterValues() {

        this.filter.site = (Integer)this.site.getValue();
        this.filter.application = (Integer)this.application.getValue();
        this.filter.entity = (Integer)this.entity.getValue();
        this.filter.port = (Integer)this.port.getValue();
        this.filter.exercise = (Integer)this.exercise.getValue();
        this.filter.request = (Integer)this.request.getValue();
        this.filter.marking = this.marking.getText().trim();

        this.filter.protocol = Utilities.getComboboxValue(
            this.protocol,
            VDIS.PROTOCOL_VERSION);
        this.filter.family = Utilities.getComboboxValue(
            this.family,
            VDIS.PDU_FAMILY);
        this.filter.domain = Utilities.getComboboxValue(
            this.domain,
            VDIS.DOMAIN);
        this.filter.kind = Utilities.getComboboxValue(
            this.kind,
            VDIS.ENT_KIND);

        this.filter.includedTypes.clear();
        this.filter.includedTypes.addAll(this.typesIncluded);
        this.filter.excludedTypes.clear();
        this.filter.excludedTypes.addAll(this.typesExcluded);

        if (this.filter.marking.isEmpty()) {
            
            this.filter.marking = null;
        }
        
        logger.finer("New Filter:\n" + this.filter.toString());
    }

    private void configureComponents() {

        this.port.setColumns(10);
        
        this.site.setColumns(5);
        this.site.setHorizontalAlignment(JTextField.RIGHT);
        this.application.setColumns(5);
        this.application.setHorizontalAlignment(JTextField.RIGHT);
        this.entity.setColumns(5);
        this.entity.setHorizontalAlignment(JTextField.RIGHT);

        Utilities.configureComboBox(
            this.protocol, 
            VDIS.PROTOCOL_VERSION, 
            true);
        Utilities.configureComboBox(
            this.family, 
            VDIS.PDU_FAMILY, 
            true);
        Utilities.configureComboBox(
            this.domain, 
            VDIS.DOMAIN, 
            true);
        Utilities.configureComboBox(
            this.kind, 
            VDIS.ENT_KIND, 
            true);

        this.clear.setActionCommand(CLEAR);
        this.clear.addActionListener(this);

        this.revert.setActionCommand(REVERT);
        this.revert.addActionListener(this);

        this.okay.setActionCommand(OKAY);
        this.okay.addActionListener(this);

        this.cancel.setActionCommand(CANCEL);
        this.cancel.addActionListener(this);
        
        this.domain.setEnabled(false);

        this.kind.setEnabled(false);

        this.included.addActionListener(this);
        
        this.excluded.addActionListener(this);
    }
    
    private void showDialog() {
        
        this.fill();

        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.pack();
        this.dialog.setModal(true);
        this.dialog.setResizable(false);
        this.dialog.setVisible(true);
    }

    private void fill() {

        int y = -1;

        Utilities.setGridBagLayout(this.dialog.getContentPane());
        
        Utilities.addItem(
            this.dialog.getContentPane(),
            this.port,
            "Port:",
            ++y);
        Utilities.addItem(
            this.dialog.getContentPane(),
            this.exercise,
            "Exercise",
            ++y);
        Utilities.addItem(
            this.dialog.getContentPane(),
            this.protocol,
            "Protocol:",
            ++y);
       Utilities.addItem(
            this.dialog.getContentPane(),
            this.family,
            "Family:",
            ++y);
        Utilities.addItem(
            this.dialog.getContentPane(),
            this.included,
            "Included Types:",
            ++y);
        Utilities.addItem(
            this.dialog.getContentPane(),
            this.excluded,
            "Excluded Types:",
            ++y);
        Utilities.addComponent(
            this.dialog.getContentPane(),
            new EntityIdPanel(),
            Utilities.HORIZONTAL,
            0, ++y,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 0, 0, 0));
        Utilities.addComponent(
            this.dialog.getContentPane(),
            new EntityStatePanel(),
            Utilities.HORIZONTAL,
            0, ++y,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 0, 0, 0));
        Utilities.addComponent(
            this.dialog.getContentPane(),
            new MiscellaneousPanel(),
            Utilities.HORIZONTAL,
            0, ++y,
            2, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 0, 0, 0));
        Utilities.addComponent(
            this.dialog.getContentPane(),
            new ButtonPanel(),
            Utilities.HORIZONTAL,
            0, ++y,
            2, 1,
            1.0, 1.0,
            Utilities.getInsets(10, 6, 1, 6));

        Utilities.center(DiscoverFrame.getFrame(), this.dialog);
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
