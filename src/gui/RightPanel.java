/**
 * @filename RightPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The right half of the main window.
 * Consists of the visualizer and settings
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;

import coms.SettingsListener;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {
    
    // Minimum width of the panel.
    static final int MINWIDTH = 600;
    
    // Panels
    private VisualizerPanel visualizerPanel;
    private SettingsPanel settingsPanel;
    
    // Visible panels
    private boolean settingsVis = true;
    
    // Constructor
    RightPanel(SettingsListener listener) {
        // Setup
        setMinimumSize(new Dimension(MINWIDTH, 200));
        setLayout(new BorderLayout());
        
        // Creating panels
        visualizerPanel = new VisualizerPanel();
        settingsPanel = new SettingsPanel(listener);
        
        // Adding panels
        add(visualizerPanel, BorderLayout.CENTER);
        add(settingsPanel, BorderLayout.SOUTH);
    }
    
    // Toggles the visibility of the settings panel.
    void toggleSettingsPanel() {
        settingsPanel.setVisible(settingsVis = !settingsVis);
    }
    
    // Passes note information to the visualizer.
    public void setNotes(Rectangle[] notes) {
        visualizerPanel.setNotes(notes);
    }

    // Updates the gui setting values.
    void updateSettings() {
        settingsPanel.updateSettings();
    }

    // Updates the scroll position of the visualizer.
    void updateScroll(double percentage) {
        visualizerPanel.setScroll(percentage);
    }
}
