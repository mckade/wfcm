/**
 * @filename RightPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The right half of the main window.
 * Consists of the visualizer and settings
 */

package gui;

import model.MusicGenerator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {
    
    // Minimum width of the panel.
    public static final int MINWIDTH = 600;
    
    // Panels
    private VisualizerPanel visualizerPanel;
    private SettingsPanel settingsPanel;
    
    // Visible panels
    private boolean settingsVis = true;
    
    // Constructor
    public RightPanel() {
        
        // Setup
        Dimension dim = new Dimension(MINWIDTH, 200);
        
        // Panel settings
        setMinimumSize(dim);
        
        // Layout and panels
        setLayout(new BorderLayout());
        
        // Creating panels
        visualizerPanel = new VisualizerPanel();
        settingsPanel = new SettingsPanel();
        
        // Adding panels
        add(visualizerPanel, BorderLayout.CENTER);
        add(settingsPanel, BorderLayout.SOUTH);
    }
    
    // Toggles the visibility of the settings panel.
    public void toggleSettingsPanel() {
        settingsPanel.setVisible(settingsVis = !settingsVis);
    }
    
    public void setTempo(int tempo) {
        settingsPanel.setTempo(tempo);
    }

    public void setInstStrings(String[] s) {settingsPanel.setInstStrings(s);}
    
    public int getTempo() {
        return settingsPanel.getTempo();
    }
    
    // Passes note information to the visualizer.
    // TODO: Set parameter
    public void setNotes(Rectangle[] notes) {
        visualizerPanel.setNotes(notes);
    }
}
