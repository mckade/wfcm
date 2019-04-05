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
import java.awt.Rectangle;

import javax.swing.*;

import coms.UpdateEvent;
import coms.UpdateListener;

@SuppressWarnings("serial")
public class VisualizerPanel extends JPanel
implements UpdateListener {
    
    // Components
    private VisualizerGraphics visualizer;
    private JScrollPane scrollPane;
    private JScrollBar hbar;
    private double scrollLimit = 0.8;
    
    // Control
    private boolean follow = true;
    
    // Constructor
    public VisualizerPanel() {
        // Setup
        setBackground(MainWindow.PANEL_BACKGROUND);
        setBorder(MainWindow.PANEL_BORDER);
        setLayout(new BorderLayout());
        
        // Creating components
        visualizer = new VisualizerGraphics(this);
        scrollPane = new JScrollPane(visualizer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(8);
        hbar = scrollPane.getHorizontalScrollBar();
        
        // Adding components
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Sets the note that the visualizer will draw.
    public void setNotes(Rectangle[] notes) {
        visualizer.setNotes(notes);
    }
    
    // Updates the position of the playLine.
    public void setPlayLine(double percentage) {
        visualizer.setPlayLine(percentage); 
    }

    // Updates the scroll position of the visualizer.
    public void setScroll(double percentage) {
        setPlayLine(percentage);
        if(!follow || hbar.getValueIsAdjusting())
            return;

        double windowWidth = scrollPane.getViewportBorderBounds().width;

        // represents the updated position of the currently playing note
        double curLeftEdge = percentage * hbar.getMaximum();
        double rightBounds = scrollLimit * windowWidth + hbar.getValue();
        // check if the left edge has passed the scroll limit point of the window
        // if it has, move scroll to the right bounds
        if(curLeftEdge > rightBounds)
            hbar.setValue((int)rightBounds);
    }

    public void updateEvent(UpdateEvent e) {
        scrollPane.invalidate();
        scrollPane.validate();
    }
}
