/**
 * @filename VisualizerPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Visualizer panel that allows for the user to interact with
 * the music generation process.
 */

package gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import coms.SettingsListener;
import coms.UpdateEvent;
import coms.UpdateListener;

@SuppressWarnings("serial")
public class VisualizerPanel extends JPanel
implements UpdateListener {
    
    // Components
    private VisualizerGraphics visualizer;
    private _JScrollPane scrollPane;
    private JScrollBar hbar;
    private double scrollLimit = 0.8;
    
    // Listener to send events to
    SettingsListener listener;
    
    // Constructor
    public VisualizerPanel(SettingsListener listener) {
        // Setup
        this.listener = listener;
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setBorder(Visuals.B_PANEL_BORDER);
        setLayout(new BorderLayout());
        
        // Creating components
        visualizer = new VisualizerGraphics(this, listener);
        scrollPane = new _JScrollPane(visualizer);
        hbar = scrollPane.getHorizontalScrollBar();
        
        // Getting default values
        updateVisualizer();
        
        // Adding components
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Resets the scroll bar
    // Updates visualizer graphics
    public void updateVisualizer() {
        visualizer.updateVisualizer();
        hbar.setValue(0);
    }
    
    // Updates the scroll position of the visualizer.
    public void updatePlayLine() {
        visualizer.updatePlayLine();
        
        // Check whether or not the scroll follows the play line.
        if(!listener.getFollow() || hbar.getValueIsAdjusting())
            return;

        // Getting width of viewable area
        double windowWidth = scrollPane.getViewport().getWidth();

        // represents the updated position of the currently playing note
        double curLeftEdge = listener.getPlayTime() * visualizer.getNoteArea();
        double rightBounds = scrollLimit * windowWidth + hbar.getValue();
        // check if the left edge has passed the scroll limit point of the window
        // if it has, move scroll to the right bounds
        if(curLeftEdge > rightBounds)
            hbar.setValue((int)rightBounds);

        // if the left edge is too far to the left, scroll left
        if(curLeftEdge < hbar.getValue())
            hbar.setValue((int)curLeftEdge);
    }

    // Scrolls to the end of the visualizer
    public void scrollToEnd() {
        hbar.setValue(hbar.getMaximum());
    }
    
    // Scrolls to the beginning of the visualizer
    public void scrollToBeginning() {
        hbar.setValue(0);
    }

    // Update event to update the scrollPane
    public void updateEvent(UpdateEvent e) {
        // TODO fix this
//        scrollPane.invalidate();
//        scrollPane.validate();
    }
}
