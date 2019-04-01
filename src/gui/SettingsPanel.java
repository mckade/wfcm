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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import coms.SettingsListener;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel
implements ChangeListener, ActionListener {
    
    // Components
    JLabel settings;
    JLabel tempo_label;
    JSpinner tempo_spinner;
    JSlider tempo_slider;
    JLabel noteCount_label;
    JSpinner noteCount_spinner;
    JLabel instrument_label;
    JComboBox<String> instrument_comboBox;
    
    // Control
    Object changed;
    
    // Listener to send events to
    SettingsListener listener;
    
    // Constructor
    public SettingsPanel(SettingsListener listener) {
        // Setup
        this.listener = listener;
        setBackground(MainWindow.PANEL_BACKGROUND);
        setBorder(MainWindow.PANEL_BORDER);
        setLayout(new GridBagLayout());
        
        // Creating components
        // Settings Label
        settings = new JLabel("Settings and Modifiers", JLabel.CENTER);
        settings.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        settings.setForeground(Color.WHITE);
        
        // Note count
        noteCount_label = new JLabel("Note Count:");
        noteCount_label.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        noteCount_label.setForeground(Color.WHITE);
        noteCount_spinner = new JSpinner(new SpinnerNumberModel(listener.getNoteCount(), 2, 99999, 1));
//        noteCount_spinner.getEditor().getComponent(0).setBackground(MainWindow.COMPONENT_BACKGROUND);
//        noteCount_spinner.getEditor().getComponent(0).setForeground(Color.WHITE);
//        noteCount_spinner.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(MainWindow.BORDER_OUTER, 2),
//                BorderFactory.createLineBorder(MainWindow.BORDER_INNER, 2)));
//        
        noteCount_spinner.addChangeListener(this);
        
        // Tempo
        tempo_label = new JLabel("Tempo");
        tempo_label.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        tempo_label.setForeground(Color.WHITE);
        tempo_spinner = new JSpinner(new SpinnerNumberModel(100, 30, 230, 1));
        tempo_spinner.addChangeListener(this);
        tempo_slider = new JSlider(30, 230, 100);
        tempo_slider.addChangeListener(this);
        tempo_slider.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (listener.getTempo() != tempo_slider.getValue())
                    listener.setTempo(tempo_slider.getValue());
            }
        });
        
        // Instrument
        instrument_label = new JLabel("Instrument:");
        instrument_label.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        instrument_label.setForeground(Color.WHITE);
        String[] list = listener.getInstrumentList();
        Arrays.sort(list);
        instrument_comboBox = new JComboBox<String>(list);
        instrument_comboBox.addActionListener(this);
        
        // Getting default setting values
        updateSettings();
        
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
        gc.insets = new Insets(0,5,5,0);
        add(settings, gc);
        
        ////////// Row 2 //////////
        ////////// Column 1 //////////
        // Note count label (column 1)
        gc.gridy = 1;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0,5,0,0);
        add(noteCount_label, gc);
        
        // Note count spinner (column 1)
        gc.insets = new Insets(0,noteCount_label.getPreferredSize().width + 10,0,0);
        add(noteCount_spinner, gc);
        
        ////////// Column 2 //////////
        // Instrument label
        gc.gridx = 1;
        gc.insets = new Insets(0,5,0,0);
        add(instrument_label, gc);
        
        // Instrument comboBox
        gc.insets = new Insets(0,instrument_label.getPreferredSize().width + 10,0,0);
        add(instrument_comboBox, gc);
        
        ////////// Row 3 //////////
         // Tempo label
         gc.gridx = 0;
         gc.gridy = 2;
         gc.insets = new Insets(0,5,0,0);
         add(tempo_label, gc);
     
        ////////// Row 4 //////////
        // Tempo spinner
        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(5,5,5,5);
        add(tempo_slider, gc);
        
        // Tempo slider
        gc.gridx = 1;
        gc.weightx = 1;
        gc.insets = new Insets(0,0,0,0);
        add(tempo_spinner, gc);
    }
    
    // Updates the gui setting values.
    public void updateSettings() {
        noteCount_spinner.setValue(listener.getNoteCount());
        tempo_slider.setValue(listener.getTempo());
        instrument_comboBox.setSelectedItem((String)listener.getInstrument());
    }
    
    // Changes the settings and pushes them to music generator.
    // If tempo changes, both slider and spinner change appropriately.
    public void stateChanged(ChangeEvent e) {
        // NoteCount
        if (e.getSource() == noteCount_spinner)
            listener.setNoteCount((int)noteCount_spinner.getValue());
        // Tempo
        else if ((int)tempo_spinner.getValue() != tempo_slider.getValue()) {
            if (e.getSource() == tempo_spinner) {
                tempo_slider.setValue((int)tempo_spinner.getValue());
                listener.setTempo(tempo_slider.getValue());
            }
            else if (e.getSource() == tempo_slider) {
                tempo_spinner.setValue(tempo_slider.getValue());
            }
        }
    }
    
    // Changes the settings and pushes them to music generator.
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == instrument_comboBox)
            listener.setInstrument((String)instrument_comboBox.getSelectedItem());
    }
}
