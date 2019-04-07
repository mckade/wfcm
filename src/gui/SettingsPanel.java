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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import coms.SettingsListener;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel
implements ChangeListener, ActionListener {
    
    // Listener to send events to
    private SettingsListener listener;
    
    // Components
    // Settings
    private JLabel noteCount_label;
    private _JSpinner noteCount;
    private JLabel tempo_label;
    private SpinSlider tempo;
    private JLabel instrument_label;
    private _JComboBox instrument;
    private JLabel keySignature_label;
    private SpinSlider keySignature;
    
    // Constructor
    public SettingsPanel(SettingsListener listener) {
        // Setup
        this.listener = listener;
        setLayout(new GridBagLayout());
        
        // Creating components
        // Note count
        noteCount_label = new JLabel("Note Count:");
        noteCount = new _JSpinner(listener.getNoteCount(), 2, 99999, 1);
        noteCount.addChangeListener(this);
        // Tempo
        tempo_label = new JLabel("Tempo");
        tempo = new SpinSlider(30, 230, 100, 1);
        tempo.setChangeListener(this);
        // Instrument
        instrument_label = new JLabel("Instrument:");
        String[] list = listener.getInstrumentList();
        Arrays.sort(list);
        instrument = new _JComboBox(list);
        instrument.addActionListener(this);
        // Key Signature
        keySignature_label = new JLabel("Key Signature Weight %");
        keySignature = new SpinSlider(0, 100, 0, 1);
        keySignature.setChangeListener(this);
        
        // Adding components
        GridBagConstraints gc = new GridBagConstraints();
        int offset;
        
        ////////// Row 1 //////////
        offset = 5;
        // Note Count
        // Label
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, offset, 0, 0);
        add(noteCount_label, gc);
        // Spinner
        offset += noteCount_label.getPreferredSize().width + 5;
        gc.insets = new Insets(0, offset, 0, 0);
        add(noteCount, gc);
        
        // Instrument
        // Label
        offset += noteCount.getPreferredSize().width + 10;
        gc.insets = new Insets(0, offset, 0, 0);
        add(instrument_label, gc);
        // ComboBox
        offset += instrument_label.getPreferredSize().width + 5;
        gc.insets = new Insets(0, offset, 0, 0);
        add(instrument, gc);
        
        ////////// Row 2/3 //////////
        // Tempo
        // label
        gc.gridy = 1;
        gc.insets = new Insets(0, 5, 0, 0);
        add(tempo_label, gc);
        // SpinSlider 
        gc.gridy = 2;
        add(tempo, gc);
        
        ////////// Row 4/5 //////////
        // Key Signature
        // Label
        gc.gridy = 3;
        add(keySignature_label, gc);
        // SpinSlider
        gc.gridy = 4;
        add(keySignature, gc);
        
        // Getting default setting values (From mgen)
        updateSettings();
    }
    
    // Updates visuals.
    public void updateVisuals() {
        setBackground(Visuals.C_PANEL_BACKGROUND);
        setBorder(Visuals.B_BORDER_TAB);
        noteCount_label.setFont(Visuals.F_HEADING2);
        noteCount_label.setForeground(Visuals.C_FONTCOLOR1);
        noteCount.updateVisuals();
        tempo_label.setFont(Visuals.F_HEADING2);
        tempo_label.setForeground(Visuals.C_FONTCOLOR1);
        tempo.updateVisuals();
        instrument_label.setFont(Visuals.F_HEADING2);
        instrument_label.setForeground(Visuals.C_FONTCOLOR1);
        instrument.updateVisuals();
        keySignature_label.setFont(Visuals.F_HEADING2);
        keySignature_label.setForeground(Visuals.C_FONTCOLOR1);
        keySignature.updateVisuals();
    }
    
    // Updates the gui setting values.
    public void updateSettings() {
        noteCount.setValue(listener.getNoteCount());
        tempo.setValue(listener.getTempo());
        instrument.setSelectedItem(listener.getInstrument());
        keySignature.setValue((int)(listener.getKeySignatureWeight()*100));
    }
    
    // Changes the settings and pushes them to music generator.
    // If tempo changes, both slider and spinner change appropriately.
    public void stateChanged(ChangeEvent e) {
        // NoteCount
        if (e.getSource() == noteCount)
            listener.setNoteCount((int)noteCount.getValue());
        // Tempo
        else if (e.getSource() == tempo)
            listener.setTempo(tempo.getValue());
        else if (e.getSource() == keySignature)
            listener.setKeySignatureWeight(((double)keySignature.getValue())/100);
    }
    
    // Changes the settings and pushes them to music generator.
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == instrument)
            listener.setInstrument((String)instrument.getSelectedItem());
    }
}
