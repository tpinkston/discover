package discover.gui.widgets;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import discover.gui.Utilities;
import discover.vdis.appearance.DefaultAppearance;
import discover.vdis.appearance.LifeformAppearance;
import discover.vdis.appearance.PlatformAirAppearance;
import discover.vdis.appearance.PlatformLandAppearance;
import discover.vdis.bits.Abstract16Bits;
import discover.vdis.bits.Abstract32Bits;
import discover.vdis.bits.Abstract8Bits;
import discover.vdis.bits.AbstractBits;
import discover.vdis.bits.Bits;
import discover.vdis.common.ConditionMaterial;
import discover.vdis.common.EntityCapabilities;
import discover.vdis.common.ExtendedEquipmentAir;
import discover.vdis.common.ExtendedEquipmentCulturalFeature;
import discover.vdis.common.ExtendedEquipmentDefault;
import discover.vdis.common.ExtendedEquipmentLand;
import discover.vdis.common.ExtendedEquipmentLifeform;
import discover.vdis.common.ExtendedEquipmentSupply;
import discover.vdis.common.ExtendedLightsAir;
import discover.vdis.common.ExtendedLightsDefault;
import discover.vdis.common.ExtendedLightsLand;
import discover.vdis.common.ExtendedStatus;
import discover.vdis.common.LifeformAttributes;
import discover.vdis.common.ThermalIndicators;

/**
 * @author Tony Pinkston
 */
public class BitRecordWidget extends ToggleWidget {

    private static final HashMap<Class<?>, String> titles;

    static {

        // Default titles for DIS records that extends AbstractBits:
        titles = new HashMap<Class<?>, String>();
        titles.put(EntityCapabilities.class, "Capabilities");
        titles.put(DefaultAppearance.class, "Default Appearance");
        titles.put(LifeformAppearance.class, "Lifeform Appearance");
        titles.put(PlatformAirAppearance.class, "Air Platform Appearance");
        titles.put(PlatformLandAppearance.class, "Land Platform Appearance");
        titles.put(ExtendedStatus.class, "Status");
        titles.put(ExtendedEquipmentAir.class, "Air Equipment");
        titles.put(ExtendedEquipmentLand.class, "Land Equipment");
        titles.put(ExtendedEquipmentLifeform.class, "Lifeform Equipment");
        titles.put(ExtendedEquipmentCulturalFeature.class, "Cultural Feature Equipment");
        titles.put(ExtendedEquipmentSupply.class, "Supply Equipment");
        titles.put(ExtendedEquipmentDefault.class, "Default Equipment");
        titles.put(LifeformAttributes.class, "Lifeform Attributes");
        titles.put(ConditionMaterial.class, "Condition/Material");
        titles.put(ThermalIndicators.class, "Thermal Indicators");
        titles.put(ExtendedLightsAir.class, "Air Extended Lights");
        titles.put(ExtendedLightsLand.class, "Land Extended Lights");
        titles.put(ExtendedLightsDefault.class, "Default Extended Lights");
    }

    private final JPanel panel = Utilities.getGridBagPanel("");
    private final JTable table = new JTable();
    private final JTextField string = new JTextField(35);
    private AbstractBits value = null;
    private TableModel model = null;

    public BitRecordWidget(AbstractBits bits) {

        super(getTitle(bits));
        this.init(bits);
    }

    public BitRecordWidget(String title, AbstractBits bits) {

        super(title);
        this.init(bits);
    }

    @Override
    public JComponent getComponent() {

        return this.panel;
    }

    public void clear() {

        if (this.value instanceof Abstract8Bits) {

            ((Abstract8Bits)this.value).set((byte)0x00);
        }
        else if (this.value instanceof Abstract16Bits) {

            ((Abstract16Bits)this.value).set((short)0x00);
        }
        else if (this.value instanceof Abstract32Bits) {

            ((Abstract32Bits)this.value).set(0x00);
        }

        this.model.fireTableDataChanged();
    }

    public AbstractBits getValue() {

        return this.value.clone();
    }

    public void setValue(AbstractBits value) {

        this.value = value.clone();
        this.model.fireTableDataChanged();
        this.string.setText(this.value.getBitString());

        super.getLabel().setText(getTitle(this.value));
    }

    public void applyValue(AbstractBits value) {

        boolean applied = false;

        if (this.value instanceof Abstract8Bits) {

            if (value instanceof Abstract8Bits) {

                ((Abstract8Bits)value).set(((Abstract8Bits)this.value).get());
                applied = true;
            }
        }
        else if (this.value instanceof Abstract16Bits) {

            if (value instanceof Abstract16Bits) {

                ((Abstract16Bits)value).set(((Abstract16Bits)this.value).get());
                applied = true;
            }
        }
        else if (this.value instanceof Abstract32Bits) {

            if (value instanceof Abstract32Bits) {

                ((Abstract32Bits)value).set(((Abstract32Bits)this.value).get());
                applied = true;
            }
        }

        if (!applied) {

            logger.error("Mismatch in AbstractBits objects!");
        }
    }

    @Override
    public void removed() {

        super.removed();

        this.string.removeActionListener(this);
        this.string.removeFocusListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        this.updateValues();
    }

    @Override
    public void focusLost(FocusEvent event) {

        this.updateValues();
    }

    @Override
    protected void fill() {

        super.fill();

        Utilities.addComponent(
            this.panel,
            new JLabel("Bits:"),
            Utilities.NONE,
            0, 0,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(12, 6, 2, 2));
        Utilities.addComponent(
            this.panel,
            this.string,
            Utilities.HORIZONTAL,
            1, 0,
            1, 1,
            0.5, 0.0,
            Utilities.getInsets(6, 4, 2, 4));
       Utilities.addComponent(
            this.panel,
            this.getTablePanel(),
            Utilities.BOTH,
            0, 2,
            2, 1,
            1.0, 1.0,
            Utilities.getInsets(5, 2, 2, 2));
    }

    private void init(AbstractBits bits) {

        super.setRevalidation(true);

        this.value = bits.clone();
        this.model = new TableModel();

        this.table.setModel(this.model);
        this.model.fireTableDataChanged();

        this.string.setText(this.value.getBitString());
        this.string.addActionListener(this);
        this.string.addFocusListener(this);

        this.fill();
    }

    private void updateValues() {

        String string = this.string.getText();

        if ((string != null) && !string.isEmpty()) {

            string = string.trim().replace("-", "");

            try {

                long value = Long.parseLong(string, 2);

                if (this.value instanceof Abstract8Bits) {

                    ((Abstract8Bits)this.value).set((byte)(value & 0xFF));
                }
                else if (this.value instanceof Abstract16Bits) {

                    ((Abstract16Bits)this.value).set((short)(value & 0xFFFF));
                }
                else if (this.value instanceof Abstract32Bits) {

                    ((Abstract32Bits)this.value).set((int)(value & 0xFFFFFFFF));
                }
            }
            catch(NumberFormatException exception) {

                // Do nothing...
            }
        }

        this.string.setText(this.value.getBitString());
        this.model.fireTableDataChanged();
    }

    private JPanel getTablePanel() {

        JScrollPane scroller = new JScrollPane(this.table);
        JPanel panel = Utilities.getGridBagPanel(null);

        scroller.setPreferredSize(new Dimension(250, 200));

        Utilities.addComponent(
            panel,
            scroller,
            Utilities.BOTH,
            0, 0,
            1, 1,
            1.0, 1.0,
            Utilities.getInsets(2, 2, 2, 2));

        return panel;
    }

    private static String getTitle(AbstractBits value) {

        String title = titles.get(value.getClass());

        if (title == null) {

            logger.error(
                "Could not get name for record: {}",
                value.getClass().getSimpleName());

            title = "Unknown Record";
        }

        return title;
    }

    @SuppressWarnings("serial")
    class TableModel extends AbstractTableModel {

        private static final String ERROR = "(ERROR)";

        @Override
        public boolean isCellEditable(int row, int column) { return false; }

        @Override
        public int getRowCount() { return getValue().getValues().size(); }

        @Override
        public int getColumnCount() { return 4; }

        @Override
        public Class<?> getColumnClass(int column) { return String.class; }

        @Override
        public String getColumnName(int column) {

            switch(column) {
                case 0: return "Name";
                case 1: return "Value";
                case 2: return "Position";
                case 3: return "Bits";
            }

            return ERROR;
        }

        @Override
        public Object getValueAt(int row, int column) {

            Bits bits = getValue().getValues().get(row);

            if ((column == 1) && (bits != null) && (bits.handle != null)) {

                return bits.handle.getDescription(value.getValue(bits));
            }
            else switch(column) {

                case 0: return bits.label;
                case 1: return Integer.toString(value.getValue(bits));
                case 2: return Integer.toString(bits.bit);
                case 3: return Integer.toString(bits.count);
            }

            return ERROR;
        }

        @Override
        public void fireTableDataChanged() {

            super.fireTableDataChanged();

            this.setColumnWidths();
        }

        public void setColumnWidths() {

            for(int i = 0; i < this.getColumnCount(); ++i) {

                TableColumn column = table.getColumnModel().getColumn(i);

                column.setResizable(true);
                column.setPreferredWidth(this.getColumnWidth(i));
            }
        }

        private int getColumnWidth(int column) {

            switch(column) {
                case 0: return 80;
                case 1: return 10;
                case 2: return 10;
                case 3: return 10;
            }

            return 100;
        }
    }
}
