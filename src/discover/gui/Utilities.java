package discover.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import discover.gui.frames.DiscoverFrame;
import discover.vdis.common.EntityId;
import discover.vdis.enums.Value;
import discover.vdis.enums.Values;
import discover.vdis.types.EntityType;
import discover.vdis.types.ObjectType;

/**
 * @author Tony Pinkston
 */
public class Utilities {

    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    public static final int HORIZONTAL = GridBagConstraints.HORIZONTAL;
    public static final int VERTICAL = GridBagConstraints.VERTICAL;
    public static final int BOTH = GridBagConstraints.BOTH;
    public static final int NONE = GridBagConstraints.NONE;

    public static final String ICONS_PATH = "icons/";
    public static final String EMPTY_ENUM = "    ";

    /**
     * @param file - Image file name.
     *
     * @return {@link ImageIcon}
     */
    public static ImageIcon getImageIcon(String file) {

        URL url = Utilities.class.getResource(ICONS_PATH + file);

        if (url == null) {

            return null;
        }
        else {

            return new ImageIcon(url);
        }
    }

    /**
     * Set etched border on the given component, title is optional.
     *
     * @param component - {@link JComponent}
     * @param title - String title (null for no title)
     *
     * @return The border that gets set on the panel.
     */
    public static Border setBorder(
        JComponent component,
        String title) {

        Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

        if (title == null) {

            component.setBorder(border);
        }
        else {

            border = BorderFactory.createTitledBorder(border, title);

            component.setBorder(border);
        }

        return border;
    }

    /**
     * Configures JComboBox to work with VDIS enumeration.
     *
     * @param box - {@link JComboBox}
     * @param type - Enumeration type.
     * @param includeEmpty - True if blank (all or none) is added.
     */
    public static void configureComboBox(
        JComboBox<String> box,
        Class<? extends Value> type,
        boolean includeEmpty) {

        final List<? extends Value> list = Values.values(type, true);
        
        box.removeAllItems();

        if (includeEmpty) {

            box.addItem(EMPTY_ENUM);
        }

        for(Value value : list) {

            box.addItem(value.description);
        }
    }

    /**
     * Sets selected item for JComboBox based on enumeration value.
     *
     * @param box - {@link JComboBox}
     * @param type - Enumeration type.
     * @param value - Enumeration value.
     */
    public static void setComboBoxValue(
        JComboBox<String> box,
        Class<? extends Value> type,
        Integer value) {

        if ((value == null) || (value.intValue() < 0)) {

            // Assume item at zero index is EMPTY_ENUM
            box.setSelectedIndex(0);
        }
        else {

            Value v = Values.get(value, type);

            box.setSelectedItem(v.description);
        }
    }

    /**
     * Returns integer enumeration associated with selected item.
     *
     * @param box - {@link JComboBox}
     * @param type - Enumeration type.
     * @return
     */
    public static Integer getComboboxValue(
            JComboBox<String> box,
            Class<? extends Value> type) {

        String description = (String)box.getSelectedItem();

        if (description != null) {

            final List<? extends Value> list = Values.values(type, true);

            for(Value v : list) {

                if (description.equals(v.description)) {

                    return Integer.valueOf(v.value);
                }
            }
        }

        return null;
    }

    /**
     * Used for dialog windows, centers the 'component' in the center of the
     * 'parent'.
     *
     * @param parent - Parent {@link Component}
     * @param component - {@link Component} to locate.
     */
    public static void center(Component parent, Component component) {

        Dimension parentSize = parent.getSize();
        Dimension componentSize = component.getSize();
        Point location = new Point(parent.getLocation());

        location.x += ((parentSize.width / 2) - (componentSize.width / 2));
        location.y += ((parentSize.height / 2) - (componentSize.height / 2));

        component.setLocation(location);
    }

    /**
     * Sets the layout for the container to a new {@link GridBagLayout}.
     *
     * @param container - {@link Container}
     */
    public static void setGridBagLayout(Container container) {

        container.setLayout(new GridBagLayout());
    }

    /**
     * @return New {@link JPanel} with {@link GridBagLayout}
     */
    public static JPanel getGridBagPanel(String title) {

        JPanel panel = new JPanel(new GridBagLayout());

        if (title != null) {

            if (title.isEmpty()) {

                Utilities.setBorder(panel, null);
            }
            else {

                Utilities.setBorder(panel, title);
            }
        }

        return panel;
    }

    /**
     * Sets the minimum size of the component.
     *
     * @param component - {@link JComponent}
     * @param width - int
     * @param height - int
     */
    public static void setMinimumSize(
        JComponent component,
        int width,
        int height) {

        component.setMinimumSize(new Dimension(width, height));
    }

    /**
     * Sets the minimum size of the component.
     *
     * @param component - {@link JComponent}
     * @param width - int
     * @param height - int
     */
    public static void setMaximumSize(
        JComponent component,
        int width,
        int height) {

        component.setMaximumSize(new Dimension(width, height));
    }

    /**
     * Sets the minimum size of the component.
     *
     * @param component - {@link JComponent}
     * @param width - int
     * @param height - int
     */
    public static void setPreferredSize(
        JComponent component,
        int width,
        int height) {

        component.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Adds the Component object to the Container object with the given
     * parameters, assumes that the container has the correct layout:
     * GridBagLayout.
     *
     * @param container - {@link Container}
     * @param component - {@link Component}
     * @param fill - integer
     * @param x - integer
     * @param y - integer
     * @param width - integer
     * @param height - integer
     * @param weightx - double
     * @param weighty - double
     * @param insets - {@link Insets}
     *
     * @see java.awt.GridBagConstraints
     */
    public static void addComponent(
        Container container,
        Component component,
        int fill,
        int x,
        int y,
        int width,
        int height,
        double weightx,
        double weighty,
        Insets insets) {

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.fill = fill;

        if (insets != null) {

            constraints.insets = insets;
        }

        container.add(component, constraints);
    }

    /**
     * Adds component item with a label to the given container.
     *
     * @param container - {@link Container}
     * @param component - {@link Component}
     * @param label - String
     * @param row - int
     */
    public static void addItem(
        Container container,
        Component component,
        String label,
        int row) {

        addItem(container, component, label, row, 1);
    }

    /**
     * Adds component item with a label to the given container.
     *
     * @param container - {@link Container}
     * @param component - {@link Component}
     * @param label - String
     * @param row - int
     * @param width - int
     */
    public static void addItem(
        Container container,
        Component component,
        String label,
        int row,
        int width) {

        addComponent(
            container,
            new JLabel(label),
            HORIZONTAL,
            0, row,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 1, 1));
        addComponent(
            container,
            component,
            HORIZONTAL,
            1, row,
            width, 1,
            0.5, 0.0,
            Utilities.getInsets(2, 1, 1, 2));
    }

    /**
     * Adds component items with a label to the given container.
     *
     * @param container - {@link Container}
     * @param component1 - {@link Component}
     * @param component2 - {@link Component}
     * @param label - String
     * @param row - int
     */
    public static void addItem(
        Container container,
        Component component1,
        Component component2,
        String label,
        int row) {

        addComponent(
            container,
            new JLabel(label),
            Utilities.HORIZONTAL,
            0, row,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 1, 1));
        addComponent(
            container,
            component1,
            Utilities.HORIZONTAL,
            1, row,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(6, 2, 1, 1));
        addComponent(
            container,
            component2,
            Utilities.HORIZONTAL,
            2, row,
            1, 1,
            0.0, 0.0,
            Utilities.getInsets(2, 2, 1, 2));
    }

    public static File getSaveFile(String title, JFileChooser chooser) {

        JFrame frame = DiscoverFrame.getFrame();
        File file = null;
        int choice = chooser.showDialog(frame, title);

        if (choice == JFileChooser.APPROVE_OPTION) {

            file = chooser.getSelectedFile();

            if ((file != null) && file.exists()) {

                if (!file.isFile()) {

                    JOptionPane.showMessageDialog(
                        frame,
                        "Not a file: " + file.getName(),
                        title,
                        JOptionPane.ERROR_MESSAGE);

                    file = null;
                }
                else if (!file.canWrite()) {

                    JOptionPane.showMessageDialog(
                        frame,
                        "File cannnot be over-written: " + file.getName(),
                        title,
                        JOptionPane.ERROR_MESSAGE);

                    file = null;
                }
                else {

                    choice = JOptionPane.showConfirmDialog(
                        frame,
                        "Overwrite existing file: " + file.getName() + "?",
                        title,
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                    if (choice != JOptionPane.YES_OPTION) {

                        file = null;
                    }
                }
            }
        }

        return file;
    }

    /**
     * @param component - {@link JTextField}
     *
     * @return Float value or null if text field is empty or invalid.
     */
    public static Float getFloatValue(JTextField component) {

        String value = component.getText();

        if (value.isEmpty()) {

            return null;
        }
        else try {

            return Float.parseFloat(value.trim().replace(",", ""));
        }
        catch(NumberFormatException exception) {

            return null;
        }
    }

    /**
     * @param component - {@link JTextField}
     *
     * @return Double value or null if text field is empty or invalid.
     */
    public static Double getDoubleValue(JTextField component) {

        String value = component.getText();

        if (value.isEmpty()) {

            return null;
        }
        else try {

            return Double.parseDouble(value.trim().replace(",", ""));
        }
        catch(NumberFormatException exception) {

            return null;
        }
    }

    /**
     * @param component - {@link JTextField}
     *
     * @return Integer value or null if text field is empty or invalid.
     */
    public static Integer getIntegerValue(JTextField component) {

        String value = component.getText();

        if (value.isEmpty()) {

            return null;
        }
        else try {

            return Integer.parseInt(value.trim().replace(",", ""));
        }
        catch(NumberFormatException exception) {

            return null;
        }
    }

    /**
     * @param component - {@link JTextField}
     *
     * @return Long value or null if text field is empty or invalid.
     */
    public static Long getLongValue(JTextField component) {

        String value = component.getText();

        if (value.isEmpty()) {

            return null;
        }
        else try {

            return Long.parseLong(value.trim().replace(",", ""));
        }
        catch(NumberFormatException exception) {

            return null;
        }
    }

    /**
     * Create Insets object with the given spacing values.
     *
     * @param top - integer
     * @param left - integer
     * @param bottom - integer
     * @param right - integer
     *
     * @return {@link Insets}
     */
    public static Insets getInsets(int top, int left, int bottom, int right) {

        return new Insets(top, left, bottom, right);
    }

    /**
     * Creates a JFormattedTextField object formatted for integers.
     *
     * @param value - Default integer value
     *
     * @return {@link JFormattedTextField}
     */
    public static JFormattedTextField getIntegerField(Integer value) {

        IntegerFormatter formatter = new IntegerFormatter(value);
        JFormattedTextField field = new JFormattedTextField(formatter);

        field.setHorizontalAlignment(JTextField.RIGHT);
        field.addFocusListener(new FormattedTextFieldFocusListener(field));

        return field;
    }

    /**
     * Extracts int value from JFormattedTextField
     *
     * @param field - {@link JFormattedTextField}
     *
     * @return int
     */
    public static int getIntegerValue(JFormattedTextField field) {

        Object value = field.getValue();

        if (value instanceof Number) {

            return ((Number)value).intValue();
        }
        else if (value instanceof String) {

            return Integer.parseInt((String)value);
        }
        else {

            return 0;
        }
    }

    /**
     * Creates a JFormattedTextField object formatted for floaters.
     *
     * @param value - Default float value
     *
     * @return {@link JFormattedTextField}
     */
    public static JFormattedTextField getFloatField(
        Float value,
        NumberFormat format) {

        FloatFormatter formatter = new FloatFormatter(value);

        if (format != null) {

            formatter.setFormat(format);
        }

        JFormattedTextField field = new JFormattedTextField(formatter);

        field.setHorizontalAlignment(JTextField.RIGHT);
        field.addFocusListener(new FormattedTextFieldFocusListener(field));

        return field;
    }

    /**
     * Extracts float value from JFormattedTextField
     *
     * @param field - {@link JFormattedTextField}
     *
     * @return float
     */
    public static float getFloatValue(JFormattedTextField field) {

        Object value = field.getValue();

        if (value instanceof Number) {

            return ((Number)value).floatValue();
        }
        else if (value instanceof String) {

            return Float.parseFloat((String)value);
        }
        else {

            return 0.0f;
        }
    }

    /**
     * @return {@link Comparator}
     */
    public static Comparator<?> getComparator(Class<?> type) {

        Comparator<?> comparator = null;

        if (type == String.class) {

            comparator = new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {

                    return o1.compareTo(o2);
                }
            };
        }
        else if (type == Integer.class) {

            comparator = new Comparator<Integer>() {

                @Override
                public int compare(Integer o1, Integer o2) {

                    return o1.compareTo(o2);
                }
            };
        }
        else if (type == Long.class) {

            comparator = new Comparator<Long>() {

                @Override
                public int compare(Long o1, Long o2) {

                    return o1.compareTo(o2);
                }
            };
        }
        else if (type ==  EntityId.class) {

            comparator = new Comparator<EntityId>() {

                @Override
                public int compare(EntityId o1, EntityId o2) {

                    return o1.compareTo(o2);
                }
            };
        }
        else if (type ==  EntityType.class) {

            comparator = new Comparator<EntityType>() {

                @Override
                public int compare(EntityType o1, EntityType o2) {

                    return o1.compareTo(o2);
                }
            };
        }
        else if (type ==  ObjectType.class) {

            comparator = new Comparator<ObjectType>() {

                @Override
                public int compare(ObjectType o1, ObjectType o2) {

                    return o1.compareTo(o2);
                }
            };
        }
        else {

            logger.error(
                "Could not create comparator for class: {}",
                type.getName());
        }

        return comparator;
    }

    public static class FormattedTextFieldFocusListener implements FocusListener {

        private final JFormattedTextField field;

        public FormattedTextFieldFocusListener(JFormattedTextField field) {

            this.field = field;
        }

        @Override
        public void focusLost(FocusEvent event) {

        }

        @Override
        public void focusGained(FocusEvent event) {

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    field.selectAll();
                }
            });
        }
    }

    @SuppressWarnings("serial")
    static class IntegerFormatter extends AbstractFormatter {

        Integer defaultValue;

        public IntegerFormatter(Integer defaultValue) {

            this.defaultValue = defaultValue;
        }

        @Override
        public Object stringToValue(String text) throws ParseException {

            try {

                return Integer.parseInt(text);
            }
            catch(NumberFormatException exception) {

                return defaultValue;
            }
        }

        @Override
        public String valueToString(Object value) throws ParseException {

            if (value instanceof Number) {

                return Integer.toString(((Number)value).intValue());
            }
            else if (value instanceof String) {

                Object object = stringToValue(value.toString());

                if (object != null) {

                    return object.toString();
                }
            }

            return null;
        }
    }

    @SuppressWarnings("serial")
    static class FloatFormatter extends AbstractFormatter {

        Float defaultValue;
        NumberFormat format = null;

        public FloatFormatter(Float defaultValue) {

            this.defaultValue = defaultValue;
        }

        public void setFormat(NumberFormat format) {

            this.format = format;
        }

        @Override
        public Object stringToValue(String text) throws ParseException {

            Object object = null;

            try {

                text = text.replaceAll(",", "");

                object = new Float(Float.parseFloat(text));
            }
            catch(NumberFormatException exception) {

                object = defaultValue;
            }

//            System.out.println(
//                "FloatFormatter.stringToValue(" + text + ") = " + object);

            return object;
        }

        @Override
        public String valueToString(Object object) throws ParseException {

            String string = null;

            if (object instanceof Number) {

                float value = ((Number)object).floatValue();

                if (format != null) {

                    string = format.format(value);
                }
                else {

                    string = Float.toString(value);
                }
            }
            else if (object instanceof String) {

                Object value = stringToValue(object.toString());

                if (value != null) {

                    string = value.toString();
                }
            }

//            System.out.println(
//                "FloatFormatter.valueToString(" + object + ") = " + string);

            return string;
        }
    }
}
