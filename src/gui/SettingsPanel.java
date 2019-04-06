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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import coms.SettingsListener;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel
implements ChangeListener, ActionListener {
    
    // Listener to send events to
    private SettingsListener listener;
    
    // Components
    // Settings
    private JLabel tempo_label;
    private JSpinner tempo_spinner;
    private JSlider tempo_slider;
    private JLabel noteCount_label;
    private JSpinner noteCount_spinner;
    private JLabel instrument_label;
    private JComboBox<String> instrument_comboBox;
    
    // Constructor
    public SettingsPanel(SettingsListener listener) {
        // Setup
        this.listener = listener;
        setBackground(MainWindow.C_PANEL_BACKGROUND);
        setBorder(MainWindow.B_BORDER_TAB);
        setLayout(new GridBagLayout());
        
        // Creating components
        // Note count
        noteCount_label = new JLabel("Note Count:");
        noteCount_label.setFont(MainWindow.F_HEADING2);
        noteCount_label.setForeground(Color.WHITE);
        noteCount_spinner = new JSpinner(new SpinnerNumberModel(listener.getNoteCount(), 2, 99999, 1));
        noteCount_spinner.getEditor().getComponent(0).setBackground(MainWindow.C_COMPONENT_BACKGROUND);
        noteCount_spinner.getEditor().getComponent(0).setForeground(Color.WHITE);
        noteCount_spinner.setBorder(MainWindow.B_COMPONENT_BORDER);
        noteCount_spinner.addChangeListener(this);
        
        // Tempo
        tempo_label = new JLabel("Tempo");
        tempo_label.setFont(MainWindow.F_HEADING2);
        tempo_label.setForeground(Color.WHITE);
        tempo_spinner = new JSpinner(new SpinnerNumberModel(100, 30, 230, 1));
        tempo_spinner.addChangeListener(this);
        tempo_spinner.getEditor().getComponent(0).setBackground(MainWindow.C_COMPONENT_BACKGROUND);
        tempo_spinner.getEditor().getComponent(0).setForeground(Color.WHITE);
        tempo_spinner.setBorder(MainWindow.B_COMPONENT_BORDER);
        tempo_slider = new JSlider(30, 230, 100);
        tempo_slider.setBackground(MainWindow.C_PANEL_BACKGROUND);
        tempo_slider.setUI(new BasicSliderUI(tempo_slider) {
            public void paintThumb(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = thumbRect;
                g2.setColor(MainWindow.C_DIVIDER);
                g2.fillRect(t.x, t.y, t.width-3, t.height);
                g2.setColor(MainWindow.C_COMPONENT_BORDER);
                g2.drawRect(t.x, t.y, t.width-3, t.height-1);
            }
            public void paintFocus(Graphics g) {}
            public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle t = trackRect;
                g2.setColor(MainWindow.C_DIVIDER);
                g2.fillRect(t.x, t.y+7, t.width, t.height/5);
                g2.setColor(MainWindow.C_COMPONENT_BORDER);
                g2.drawRect(t.x, t.y+7, t.width, t.height/5);
            }
        });
        tempo_slider.addChangeListener(this);
        
        // Instrument
        instrument_label = new JLabel("Instrument:");
        instrument_label.setFont(MainWindow.F_HEADING2);
        instrument_label.setForeground(Color.WHITE);
        String[] list = listener.getInstrumentList();
        Arrays.sort(list);
        instrument_comboBox = new JComboBox<String>(list);
        instrument_comboBox.addActionListener(this);
        
        // Getting default setting values (From mgen)
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
        instrument_comboBox.setSelectedItem(listener.getInstrument());
    }
    
    // Changes the settings and pushes them to music generator.
    // If tempo changes, both slider and spinner change appropriately.
    public void stateChanged(ChangeEvent e) {
        // NoteCount
        if (e.getSource() == noteCount_spinner)
            listener.setNoteCount((int)noteCount_spinner.getValue());
        // Tempo
        else if ((int)tempo_spinner.getValue() != tempo_slider.getValue()) {
            if (e.getSource() == tempo_spinner)
                tempo_slider.setValue((int)tempo_spinner.getValue());
            else if (e.getSource() == tempo_slider)
                tempo_spinner.setValue(tempo_slider.getValue());
            listener.setTempo(tempo_slider.getValue());
        }
    }
    
    // Changes the settings and pushes them to music generator.
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == instrument_comboBox)
            listener.setInstrument((String)instrument_comboBox.getSelectedItem());
    }
}
