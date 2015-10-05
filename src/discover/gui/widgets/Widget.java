/**
 * @author Tony Pinkston
 */
package discover.gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import discover.gui.Utilities;

public abstract class Widget implements ActionListener, FocusListener, MouseListener {

    private final JPanel panel;
    
    protected Widget(String title) {
        
        this.panel = Utilities.getGridBagPanel(title);
    }
    
    public JPanel getPanel() {
        
        return this.panel;
    }
    
    public void setTitle(String title) {
        
        Border border = this.panel.getBorder();
        
        if ((border != null) && (border instanceof TitledBorder)) { 
            
            ((TitledBorder)border).setTitle(title);
        }
    }
    
    public String getTitle() {
        
        Border border = this.panel.getBorder();
        
        if ((border != null) && (border instanceof TitledBorder)) { 
            
            return ((TitledBorder)border).getTitle();
        }
        
        return null;
    }
    
    /**
     * Called when widget gets removed from container.
     */
    public void removed() {
        
    }

    @Override
    public void actionPerformed(ActionEvent event) {

    }

    @Override
    public void focusGained(FocusEvent event) {

    }

    @Override
    public void focusLost(FocusEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) { 
        
    }

    @Override
    public void mouseExited(MouseEvent event) { 
        
    }

    @Override
    public void mousePressed(MouseEvent event) { 
        
    }

    @Override
    public void mouseReleased(MouseEvent event) { 
        
    }

    @Override
    public void mouseClicked(MouseEvent event) { 

    }
}
