package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import coms.SettingsListener;

@SuppressWarnings("serial")
public class PreferencesPanel extends JPanel
implements ActionListener {
    
    // Listener to send events to
    private SettingsListener listener;
    
    // Components
    private _JCheckBox follow;
    private _JCheckBox loadSampleSettings;
    
    public PreferencesPanel(SettingsListener listener) {
        // Setup
        this.listener = listener;
        setLayout(new FlowLayout(FlowLayout.LEADING));
        
        // Creating components
        follow = new _JCheckBox("Follow PlayLine");
        follow.addActionListener(this);
        loadSampleSettings = new _JCheckBox("Load settings of imported sample");
        loadSampleSettings.addActionListener(this);
        
        // Adding components
        add(follow);
        add(loadSampleSettings);
        
        // Getting default preferences
        updatePreferences();
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setBorder(Visuals.B_BORDER_TAB);
        follow.updateVisuals();
        loadSampleSettings.updateVisuals();
    }
    
    // Updates the gui preference values.
    public void updatePreferences() {
        follow.setSelected(listener.getFollow());
        loadSampleSettings.setSelected(listener.getLoadSampleSettings());
    }

    // Changes the preferences values and pushes them to music generator.
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == follow)
            listener.setFollow(follow.isSelected());
        else if (e.getSource() == loadSampleSettings)
            listener.setLoadSampleSettings(loadSampleSettings.isSelected());
    }
}
