/**
 * @filename SettingsPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Holds settings and modifiers to change how music is generated.
 * - Note count
 * - Tempo
 * - Instrument
 */

package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel
implements ChangeListener {
    
    // Components
    JLabel settings;
    JLabel tempo_label;
    JSpinner tempo_spinner;
    JSlider tempo_slider;

    JComboBox instList;
    
    // Constructor
    public SettingsPanel() {
        // Setup
        setBackground(MainWindow.PANEL_BACKGROUND);
        setBorder(MainWindow.PANEL_BORDER);
        setLayout(new GridBagLayout());
        
        // Creating components
        // Label
        settings = new JLabel("Settings and Modifiers", JLabel.CENTER);
        settings.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        settings.setForeground(Color.WHITE);
        
        // Tempo
        tempo_label = new JLabel("Tempo");
        tempo_label.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        tempo_label.setForeground(Color.WHITE);
        tempo_spinner = new JSpinner(new SpinnerNumberModel(100, 30, 230, 1));
        tempo_spinner.addChangeListener(this);
        tempo_slider = new JSlider(30, 230, 100);
        tempo_slider.addChangeListener(this);
        
        // Instrument
        
        // Adding components
        GridBagConstraints gc = new GridBagConstraints();
        
        ////////// Row 1 //////////
        // Settings label
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0,5,0,0);
        add(settings, gc);
        
        ////////// Row 2 //////////
        // Tempo label
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0,5,0,0);
        add (tempo_label, gc);
        
        ////////// Row 3 //////////
        // Tempo spinner
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0;
        gc.insets = new Insets(5,5,5,5);
        add(tempo_slider, gc);
        
        // Tempo slider
        gc.gridx = 1;
        gc.weightx = 1;
        gc.insets = new Insets(0,0,0,0);
        add(tempo_spinner, gc);
    }
    
    // Sets the tempo manually within 30-230
    public void setTempo(int tempo) {
        if (tempo < 30 || tempo > 230)
            return;
        tempo_slider.setValue(tempo);
        tempo_spinner.setValue(tempo);
    }

    // Sets the instruments available for use.
    public void setInstStrings(String[] inst) {
    }
    
    // Returns the tempo.
    public int getTempo() {return (int)tempo_spinner.getValue();}

    // If a tempo component changes, extends to the others.
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == tempo_spinner)
            tempo_slider.setValue((int)tempo_spinner.getValue());
        else if (e.getSource() == tempo_slider)
            tempo_spinner.setValue(tempo_slider.getValue());
    }
}
