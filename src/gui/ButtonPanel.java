/**
 * @filename ButtonPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Button Panel that holds the basic functions for music generation.
 * Not yet implemented
 */

package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public static final String PLAY_STOP = "playstop";
    public static final String PAUSE_RESUME = "pause";
    
    // Listener to send events to
    private ButtonListener listener;
    
    // Components
    private JButton generate;
    private JSpinner noteLength;
    private JButton play_stop;
    private JButton pause_resume;
    
    // Control
    boolean playing = false;
    boolean paused = false;
    
    // Constructor
    public ButtonPanel(ButtonListener listener) {
        
        // Setup
        this.listener = listener;
        setBackground(MainWindow.BACKGROUND);
        setBorder(MainWindow.BORDER);
        
        // Layout and components
        setLayout(new GridBagLayout());
        
        // Creating components
        generate = new JButton("Generate");
        generate.addActionListener(this);
        generate.setActionCommand(GENERATE);
        noteLength = new JSpinner(new SpinnerNumberModel(100, 2, 99999, 1));
        play_stop = new JButton("Play");
        play_stop.addActionListener(this);
        play_stop.setActionCommand(PLAY_STOP);
        pause_resume = new JButton("Pause");
        pause_resume.setEnabled(false);
        pause_resume.addActionListener(this);
        pause_resume.setActionCommand(PAUSE_RESUME);
        
        // Adding components
        GridBagConstraints gc = new GridBagConstraints();
        
        ////////// Row 1 //////////
        // Generate Button
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5,5,5,5);
        add(generate, gc);
        
        // Generate note length
        gc.gridx = 1;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        add(noteLength, gc);
        
        ////////// Row 2 //////////
        // Play/Stop Button
        gc.gridy = 1;
        gc.gridx = 0;
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.LINE_START;
        add(play_stop, gc);
        
        // Pause Button
        gc.gridx = 1;
        add(pause_resume, gc);
    }
    
    // Switches the function of the play_stop button
    public void togglePlayStop() {
        playing = !playing;
        paused = false;
        pause_resume.setText("Pause");
        if (playing) {
            play_stop.setText("Stop");
            pause_resume.setEnabled(true);
        }
        else {
            play_stop.setText("Play");
            pause_resume.setEnabled(false);
        }
    }
    
    // Toggles the pause switch for the music.
    public void togglePause() {
        paused = !paused;
        if (paused) {
            pause_resume.setText("Resume");
        }
        else {
            pause_resume.setText("Pause");
        }
    }
    
    // Returns the note length from the spinner.
    public int getNoteLength() {
        return (int)noteLength.getValue();
    }
    
    // Sends out an event to each listener.
    private void fireButtonClicked(Object source, String id) {
        listener.buttonClicked(new ButtonEvent(source, id));
    }

    // Fires an event that a button has been clicked.
    public void actionPerformed(ActionEvent e) {
        fireButtonClicked(e.getSource(), e.getActionCommand());
    }
}
