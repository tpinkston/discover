/**
 * @author Tony Pinkston
 */
package discover.gui.dialogs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import discover.gui.Utilities;
import discover.gui.frames.DiscoverFrame;

public class RemovePortDialog implements ActionListener {

    @SuppressWarnings("serial")
    private final JDialog dialog = new JDialog(
        DiscoverFrame.getFrame(), 
        "Remove Port") {

            @Override
            public void dispose() {

                RemovePortDialog.this.disposing();
                
                super.dispose();
            }
    };
    
    private final JButton okay = new JButton("Okay");
    private final JButton cancel = new JButton("Cancel");
    private final List<JRadioButton> buttons = new ArrayList<JRadioButton>();
    
    private final List<Integer> ports = new ArrayList<Integer>();

    public RemovePortDialog(Set<Integer> choices) {
     
        this.fill(choices);
        
        this.okay.addActionListener(this);
        this.cancel.addActionListener(this);
        
        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.pack();
        this.dialog.setModal(true);
        
        Utilities.center(DiscoverFrame.getFrame(), this.dialog);
        
        this.dialog.setVisible(true);
    }
    
    public List<Integer> getPorts() {
        
        return this.ports;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == this.okay) {
            
            this.removePorts();
        }
        else if (event.getSource() == this.cancel) {
            
            this.dialog.dispose();
        }
    }
    
    private void disposing() {
        
        this.okay.removeActionListener(this);
        this.cancel.removeActionListener(this);
    }
    
    private void removePorts() {
        
        for(JRadioButton button : this.buttons) {
            
            if (button.isSelected()) {
                
                try {
                    
                    Integer port = Integer.parseInt(button.getText());
                    
                    if (port != null) {
                        
                        this.ports.add(port);
                    }
                }
                catch(NumberFormatException exception) {
                    
                }
            }
        }
        
        this.dialog.dispose();
    }
    
    private void fill(Set<Integer> choices) {
        
        Utilities.setGridBagLayout(this.dialog.getContentPane());

        Utilities.addComponent(
            this.dialog.getContentPane(),
            new JLabel("Select ports to remove:"),
            Utilities.HORIZONTAL,
            0, 0,
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 5, 5, 5));

        int count = 0;
        
        for(Integer integer : choices) {
            
            String name = Integer.toString(integer);
            JRadioButton button = new JRadioButton(name);
            
            button.setActionCommand(name);
            
            buttons.add(button);
            
            Utilities.addComponent(
                this.dialog.getContentPane(),
                button,
                Utilities.HORIZONTAL,
                0, (count + 1),
                1, 1,
                0.0, 0.0,
                Utilities.getInsets(0, 20, 0, 5));
            
            count++;
        }

        Utilities.addComponent(
            this.dialog.getContentPane(),                
            this.getButtonPanel(),
            Utilities.HORIZONTAL,
            0, (choices.size() + 1),
            1, 1,
            1.0, 0.0,
            Utilities.getInsets(10, 5, 2, 5));
    }

    private JPanel getButtonPanel() {
        
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 2));
        
        panel.add(this.okay);
        panel.add(this.cancel);
        
        return panel;
    }
}
