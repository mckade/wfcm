/**
 * @filename ButtonPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Button Panel that holds the basic functions for music generation.
 * Not yet implemented
 */

package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import coms.ButtonEvent;
import coms.ButtonListener;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel
implements ActionListener {
    
    // Buttons
    public static final String GENERATE = "generate";
    public static final String PLAY = "play";
    public static final String STOP = "stop";
    
    private ArrayList<ButtonListener> listeners;
    
    // Components
    private JButton generate;
    private JSpinner noteLength;
    private JButton play;
    private JButton stop;
    
    // Constructor
    public ButtonPanel(Dimension dim) {
        
        // Setup
        listeners = new ArrayList<ButtonListener>();
        
        // Panel settings
        setMinimumSize(dim);
        setBackground(MainWindow.BACKGROUND);
        setBorder(MainWindow.BORDER);
        
        // Layout and components
        setLayout(new GridBagLayout());
        
        // Creating components
        generate = new JButton("Generate");
        generate.addActionListener(this);
        generate.setActionCommand(GENERATE);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(100, 2, 99999, 1);
        noteLength = new JSpinner(spinnerModel);
        play = new JButton("Play");
        play.addActionListener(this);
        play.setActionCommand(PLAY);
        stop = new JButton("Stop");
        stop.addActionListener(this);
        stop.setActionCommand(STOP);
        
        GridBagConstraints gc = new GridBagConstraints();
        
        ////////// Row 1 //////////
        // Generate Button
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5,5,5,5);
        add(generate, gc);
        
        // Generate note length
        gc.gridx = 1;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5,5,5,5);
        add(noteLength, gc);
        
        ////////// Row 2 //////////
        gc.gridy = 1;
        gc.gridx = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5,5,5,5);
        add(play, gc);
        
        gc.gridx = 1;
        add(stop, gc);
    }
    
    public int getNoteLength() {
        return (int)noteLength.getValue();
    }
    
    public void addButtonListener(ButtonListener listener) {
        listeners.add(listener);
    }
    
    private void fireButtonClicked(Object source, String id) {
        for (ButtonListener listener : listeners) {
            listener.buttonClicked(new ButtonEvent(source, id));
        }
    }

    public void actionPerformed(ActionEvent e) {
        fireButtonClicked(e.getSource(), e.getActionCommand());
    }
}
