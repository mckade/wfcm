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
    private _JScrollPane settingsScrollPane;
    private PreferencesPanel preferencesPanel;
    private _JScrollPane preferencesScrollPane;
    private _JTabbedPane tabPane;
    
    // Control
    private boolean settingsVisable = true;
    
    // Constructor
    public RightPanel(SettingsListener slistener, ButtonListener blistener) {
        // Setup
        setMinimumSize(new Dimension(MINWIDTH, 0));
        setLayout(new BorderLayout());
        
        // Creating panels
        musicControlPanel = new MusicControlPanel(blistener);
        visualizerPanel = new VisualizerPanel(slistener);
        settingsPanel = new SettingsPanel(slistener);
        settingsScrollPane = new _JScrollPane(settingsPanel);
        preferencesPanel = new PreferencesPanel(slistener);
        preferencesScrollPane = new _JScrollPane(preferencesPanel);
        tabPane = new _JTabbedPane();
        tabPane.setPreferredSize(new Dimension(0, 175));
        tabPane.addTab("Settings", settingsScrollPane);
        tabPane.addTab("Preferences", preferencesScrollPane);
        
        // Adding panels
        add(musicControlPanel, BorderLayout.NORTH);
        add(visualizerPanel, BorderLayout.CENTER);
        add(tabPane, BorderLayout.SOUTH);
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_PANEL_BACKGROUND);
        musicControlPanel.updateVisuals();
        visualizerPanel.updateVisuals();
        settingsPanel.updateVisuals();
        settingsScrollPane.updateVisuals();
        preferencesPanel.updateVisuals();
        preferencesScrollPane.updateVisuals();
        tabPane.updateVisuals();
    }
    
    // Toggles the view between play and pause.
    public void togglePlayPauseView() {
        musicControlPanel.togglePlayPauseView();
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
    
    // Scrolls to the end of the visualizer
    public void scrollToEnd() {
        visualizerPanel.scrollToEnd();
    }
    
    // Scrolls to the beginning of the visualizer
    public void scrollToBeginning() {
        visualizerPanel.scrollToBeginning();
    }
}
