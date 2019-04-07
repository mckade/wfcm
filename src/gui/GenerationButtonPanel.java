/**
 * @filename GenerationButtonPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Holds the basic music generation functions.
 * - Generate: Generates the music
 * - Recycle: Uses the current visualizer table as a sample, and generates music.
 */

package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import coms.ButtonEvent;
import coms.ButtonListener;

@SuppressWarnings("serial")
public class GenerationButtonPanel extends JPanel
implements ActionListener {
    
    // Button ids
    public static final String GENERATE = "generate";
    public static final String RECYCLE = "recycle";
    
    // Listener to send events to.
    private ButtonListener listener;
    
    // Components
    private _JButton generate;
    private _JButton recycle;
    
    // Constructor
    public GenerationButtonPanel(ButtonListener listener) {
        // Setup
        this.listener = listener;
        setLayout(new FlowLayout(FlowLayout.LEADING));
        
        // Creating components
        // Generate
        generate = new _JButton("Generate");
        generate.addActionListener(this);
        generate.setActionCommand(GENERATE);
        // Recycle
        recycle = new _JButton("Recycle");
        recycle.addActionListener(this);
        recycle.setActionCommand(RECYCLE);
        
        // Adding components
        add(generate);
        add(recycle);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setBorder(Visuals.B_PANEL_BORDER);
        generate.updateVisuals();
        recycle.updateVisuals();
        recycle.setPreferredSize(generate.getPreferredSize());
    }
    
    // Sends an event to the listener that a button was clicked. (MainWindow)
    // Passes on the button id.
    private void fireButtonClicked(Object source, String id) {
        listener.buttonClicked(new ButtonEvent(source, id));
    }

    // Fires a button clicked event.
    public void actionPerformed(ActionEvent e) {
        fireButtonClicked(e.getSource(), e.getActionCommand());
    }
}
