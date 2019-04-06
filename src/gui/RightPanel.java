/**
 * @filename RightPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The right half of the main window.
 * Consists of the visualizer and settings
 */

package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

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
        setBackground(MainWindow.C_PANEL_BACKGROUND);
        setLayout(new BorderLayout());
        
        // Creating panels
        musicControlPanel = new MusicControlPanel(blistener);
        visualizerPanel = new VisualizerPanel(slistener);
        settingsPanel = new SettingsPanel(slistener);
        preferencesPanel = new PreferencesPanel(slistener);
        tabPane = new JTabbedPane();
        tabPane.addTab("Settings", settingsPanel);
        tabPane.addTab("Preferences", preferencesPanel);
        tabPane.setBackground(MainWindow.C_COMPONENT_BACKGROUND);
        tabPane.setForeground(MainWindow.C_COMPONENT_BORDER);
        tabPane.setBorder(MainWindow.B_PANEL_BORDER);
        tabPane.setUI(new BasicTabbedPaneUI() {
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                    boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(tabPane.getBackground());
                g2.fillRect(x, y, w, h);
            }
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                    boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(2));
                if(getFocusIndex() == tabIndex) {
                    g2.setColor(MainWindow.C_BORDER_CLICKED);
                    g2.drawRect(x, y, w, h);
                }
                else {
                    g2.setColor(MainWindow.C_BORDER_OUTER);
                    g2.drawRect(x, y-1, w, h);
                }
            }
        });
        
        // Adding panels
        add(musicControlPanel, BorderLayout.NORTH);
        add(visualizerPanel, BorderLayout.CENTER);
        add(tabPane, BorderLayout.SOUTH);
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
    public void scrollToEnd() {visualizerPanel.scrollToEnd();}
}
