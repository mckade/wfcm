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
    private _Button generate;
    private _Button recycle;
    
    // Constructor
    public GenerationButtonPanel(ButtonListener listener) {
        // Setup
        this.listener = listener;
        setBackground(MainWindow.PANEL_BACKGROUND);
        setBorder(MainWindow.PANEL_BORDER);
        setLayout(new FlowLayout(FlowLayout.LEADING));
        
        // Creating components
        // Generate
        generate = new _Button("Generate");
        generate.addActionListener(this);
        generate.setActionCommand(GENERATE);
        // Recycle
        recycle = new _Button("Recycle");
        recycle.setPreferredSize(generate.getPreferredSize());
        recycle.addActionListener(this);
        recycle.setActionCommand(RECYCLE);
        
        // Adding components
        add(generate);
        add(recycle);
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
