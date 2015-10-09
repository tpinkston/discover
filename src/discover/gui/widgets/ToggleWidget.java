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

        this.label = new JLabel(title);
        this.label.addMouseListener(this);
        this.label.setToolTipText("Click to toggle visibility.");
    }

    public abstract JComponent getComponent();

    public JLabel getLabel() {

        return this.label;
    }

    public void setText(String text) {

        this.getLabel().setText(text);
    }

    public void setVisible(boolean visible) {

        if (this.visible != visible) {

            this.visible = visible;
            this.update();
        }
    }

    public void setRevalidation(boolean revalidation) {

        this.revalidation = revalidation;
    }

    public void toggle() {

        this.visible = !this.visible;
        this.update();
    }

    public boolean isVisible() {

        return this.visible;
    }

    public void removed() {

        super.removed();

        this.label.removeMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent event) {

        if (event.getSource() == this.label) {

            this.toggle();
        }
    }

    protected void update() {

        this.updateIcon();
        this.updateVisibility();
    }

    protected void updateIcon() {

        if (this.visible) {

            this.getLabel().setIcon(HIDE_ICON);
        }
        else {

            this.getLabel().setIcon(SHOW_ICON);
        }
    }

    protected void updateVisibility() {

        JComponent component = this.getComponent();

        if (component != null) {

            component.setVisible(this.visible);

            if (this.revalidation) {

                component.revalidate();
            }
        }
    }

    protected void fill() {

        this.update();

        Utilities.addComponent(
            super.getPanel(),
            this.getLabel(),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
        Utilities.addComponent(
            super.getPanel(),
            this.getComponent(),
            Utilities.BOTH,
            0, 1,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(2, 2, 2, 2));
    }
}
