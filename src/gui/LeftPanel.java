/**
 * @filename LeftPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The left half of the main window.
 * Consists of the basic generation buttons and log.
 * Can toggle visibility of this panel.
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
    
    // Listener to send events to.
    private UpdateListener listener;
    
    // Panels
    private GenerationButtonPanel generationButtonPanel;
    private LogPanel logPanel;
    
    // Control
    private boolean visible = true;

    // Constructor
    public LeftPanel(UpdateListener ulistener, ButtonListener blistener) {
        // Setup
        listener = ulistener;
        setMinimumSize(new Dimension(MINWIDTH, 0));
        setLayout(new BorderLayout());
        
        // Creating panels
        generationButtonPanel = new GenerationButtonPanel(blistener);
        logPanel = new LogPanel();
        
        // Adding panels
        add(generationButtonPanel, BorderLayout.NORTH);
        add(logPanel, BorderLayout.CENTER);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        logPanel.updateVisuals();
        generationButtonPanel.updateVisuals();
    }
    
    // Toggles the visibility of the panel.
    // Fires an update event to the listener to update the divider. (MainWindow)
    public void toggleVisible() {
        setVisible(visible = !visible);
        if (visible)
            fireUpdateEvent(this, UpdateType.visible);
        else
            fireUpdateEvent(this, UpdateType.invisible);
    }
    
    // Add a log to the log panel.
    public void addLog(String log) {logPanel.addLog(log);}
    
    // Fires an update event.
    private void fireUpdateEvent(Object source, UpdateType updateType) {
        listener.updateEvent(new UpdateEvent(source, updateType));
    }
}
