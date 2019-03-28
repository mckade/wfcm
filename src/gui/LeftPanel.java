/**
 * @filename LeftPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The left half of the main window.
 * Consists of the buttons and log.
 * 
 * Can toggle the visibility of both the
 * buttons and log panels
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import coms.ButtonListener;
import coms.UpdateEvent;
import coms.UpdateListener;
import coms.UpdateType;

@SuppressWarnings("serial")
public class LeftPanel extends JPanel {
    
    // Minimum width of the panel.
    public static final int MINWIDTH = 250;
    
    // Holds listeners to send events to.
    private UpdateListener listener;
    
    // Panels
    private ButtonPanel buttonPanel;
    private LogPanel logPanel;
    
    // Control
    private boolean visible = true;

    // Constructor
    public LeftPanel(UpdateListener ulistener, ButtonListener blistener) {
        
        // Setup
        listener = ulistener;
        Dimension dim = new Dimension(MINWIDTH, MINWIDTH);
        setMinimumSize(dim);
        setLayout(new BorderLayout());
        
        // Creating panels
        buttonPanel = new ButtonPanel(blistener);
        logPanel = new LogPanel();
        
        // Adding panels
        add(buttonPanel, BorderLayout.NORTH);
        add(logPanel, BorderLayout.CENTER);
    }
    
    // Toggles the visibility of the panel.
    public void toggleVisible() {
        setVisible(visible = !visible);
        if (visible)
            fireUpdateEvent(this, UpdateType.visible);
        else
            fireUpdateEvent(this, UpdateType.invisible);
    }
    
    // Switches the function of the play_stop button
    public void togglePlayStop() {
        buttonPanel.togglePlayStop();
    }
    
    // Toggles the pause switch for the music.
    public void togglePause() {
        buttonPanel.togglePause();
    }
    
    // Get the note length from the button panel.
    public int getNoteLength() {
        return buttonPanel.getNoteLength();
    }
    
    // Add a log to the log panel.
    public void addLog(String log) {
        logPanel.addLog(log);
    }
    
    // Sends out an event to update the divider position
    private void fireUpdateEvent(Object source, UpdateType updateType) {
        listener.updateEvent(new UpdateEvent(source, updateType));
    }
}
