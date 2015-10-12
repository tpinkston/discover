package discover.gui.widgets;

import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import discover.gui.Utilities;

/**
 * @author Tony Pinkston
 */
public abstract class ToggleWidget extends Widget {

    private static final ImageIcon HIDE_ICON = Utilities.getImageIcon(
        "component_hide.png");
    private static final ImageIcon SHOW_ICON = Utilities.getImageIcon(
        "component_show.png");

    private final JLabel label;
    private boolean visible = false;
    private boolean revalidation = false;

    protected ToggleWidget(String title) {

        super(null);

        label = new JLabel(title);
        label.addMouseListener(this);
        label.setToolTipText("Click to toggle visibility.");
    }

    public abstract JComponent getComponent();

    public JLabel getLabel() {

        return label;
    }

    public void setText(String text) {

        getLabel().setText(text);
    }

    public void setVisible(boolean visible) {

        if (this.visible != visible) {

            this.visible = visible;
            update();
        }
    }

    public void setRevalidation(boolean revalidation) {

        this.revalidation = revalidation;
    }

    public void toggle() {

        visible = !visible;
        update();
    }

    public boolean isVisible() {

        return visible;
    }

    @Override
    public void removed() {

        super.removed();

        label.removeMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent event) {

        if (event.getSource() == label) {

            toggle();
        }
    }

    protected void update() {

        updateIcon();
        updateVisibility();
    }

    protected void updateIcon() {

        if (visible) {

            getLabel().setIcon(HIDE_ICON);
        }
        else {

            getLabel().setIcon(SHOW_ICON);
        }
    }

    protected void updateVisibility() {

        JComponent component = getComponent();

        if (component != null) {

            component.setVisible(visible);

            if (revalidation) {

                component.revalidate();
            }
        }
    }

    protected void fill() {

        update();

        Utilities.addComponent(
            getPanel(),
            getLabel(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            getPanel(),
            getComponent(),
            Utilities.BOTH,
            0, 1,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
    }
}
