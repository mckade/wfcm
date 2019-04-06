package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import coms.SettingsListener;

@SuppressWarnings("serial")
public class PreferencesPanel extends JPanel
implements ActionListener {
    
    // Listener to send events to
    private SettingsListener listener;
    
    // Components
    private JCheckBox follow;
    
    public PreferencesPanel(SettingsListener listener) {
        // Setup
        this.listener = listener;
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setBorder(Visuals.B_BORDER_TAB);
        // TODO: Change this to grid bag layout eventually.
        setLayout(new FlowLayout(FlowLayout.LEADING));
        
        // Creating components
        follow = new JCheckBox("Follow PlayLine");
        follow.addActionListener(this);
        
        // Getting default preferences
        updatePreferences();
        
        // Adding components
        add(follow);
    }
    
    // Updates the gui preference values.
    public void updatePreferences() {
        follow.setSelected(listener.getFollow());
    }

    // Changes the preferences values and pushes them to music generator.
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == follow)
            listener.setFollow(follow.isSelected());
    }
}
