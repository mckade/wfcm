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

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import coms.ButtonListener;
import coms.SettingsListener;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {
    
    // Minimum width of the panel.
    static final int MINWIDTH = 600;
    
    // Panels
    private MusicControlPanel musicControlPanel;
    private VisualizerPanel visualizerPanel;
    private SettingsPanel settingsPanel;
    private PreferencesPanel preferencesPanel;
    private JTabbedPane tabPane;
    
    // Control
    private boolean settingsVisable = true;
    
    // Constructor
    public RightPanel(SettingsListener slistener, ButtonListener blistener) {
        // Setup
        setMinimumSize(new Dimension(MINWIDTH, 200));
        setLayout(new BorderLayout());
        
        // Creating panels
        musicControlPanel = new MusicControlPanel(blistener);
        visualizerPanel = new VisualizerPanel(slistener);
        settingsPanel = new SettingsPanel(slistener);
        preferencesPanel = new PreferencesPanel(slistener);
        tabPane = new JTabbedPane();
        tabPane.addTab("Settings", settingsPanel);
        tabPane.addTab("Preferences", preferencesPanel);
        
        // Adding panels
        add(musicControlPanel, BorderLayout.NORTH);
        add(visualizerPanel, BorderLayout.CENTER);
        add(tabPane, BorderLayout.SOUTH);
    }
    
    // Toggles the visibility of the settings panel.
    public void toggleSettingsPanel() {
        tabPane.setVisible(settingsVisable = !settingsVisable);
    }
    
    // Updates the settings, preferences, and visualizer.
    public void fullUpdate() {
        settingsPanel.updateSettings();
        preferencesPanel.updatePreferences();
        visualizerPanel.updateVisualizer();
    }

    // Scrolls the window to keep up with the playing music
    public void updatePlayLine() {
        visualizerPanel.updatePlayLine();
    }
    public void scrollToEnd() {visualizerPanel.scrollToEnd();}
}
