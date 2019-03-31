/**
 * @filename ButtonPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Holds the basic music generation functions.
 * - Generate: Generates the music
 * - Play/Stop: Plays & stops the music.
 * - Pause/Resume: Pauses & resumes the music.
 * - Recycle: Uses the current visualizer table as a sample, and generates music.
 */

package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import coms.ButtonEvent;
import coms.ButtonListener;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel
implements ActionListener {
    
    // Button ids
    public static final String GENERATE = "generate";
    public static final String RECYCLE = "recycle";
    public static final String PLAY_STOP = "playstop";
    public static final String PAUSE_RESUME = "pause";
    
    // Listener to send events to.
    private ButtonListener listener;
    
    // Components
    private _Button generate;
    private _Button recycle;
    private _Button play_stop;
    private _Button pause_resume;
    
    // Control
    boolean playing = false;
    boolean paused = false;
    
    // Constructor
    public ButtonPanel(ButtonListener listener) {
        
        // Setup
        this.listener = listener;
        setBackground(MainWindow.PANEL_BACKGROUND);
        setBorder(MainWindow.PANEL_BORDER);
        setLayout(new GridBagLayout());
        
        // Creating components
        generate = new _Button("Generate");
        generate.addActionListener(this);
        generate.setActionCommand(GENERATE);
        recycle = new _Button("Recycle");
        recycle.setPreferredSize(generate.getPreferredSize());
        recycle.addActionListener(this);
        recycle.setActionCommand(RECYCLE);
        play_stop = new _Button("Play");
        play_stop.setPreferredSize(generate.getPreferredSize());
        play_stop.addActionListener(this);
        play_stop.setActionCommand(PLAY_STOP);
        pause_resume = new _Button("Pause");
        pause_resume.setPreferredSize(generate.getPreferredSize());
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
        
        // Recycle Button
        gc.gridx = 1;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        add(recycle, gc);
        
        ////////// Row 2 //////////
        // Play/Stop Button
        gc.gridy = 1;
        gc.gridx = 0;
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.LINE_START;
        add(play_stop, gc);
        
        // Pause/Resume Button
        gc.gridx = 1;
        add(pause_resume, gc);
    }
    
    // Toggles the play/stop button between views.
    public void togglePlayStop() {
        playing = !playing;
        paused = true;
        togglePauseResume();
        if (playing) {
            play_stop.setText("Stop");
            pause_resume.setEnabled(true);
        }
        else {
            play_stop.setText("Play");
            pause_resume.setEnabled(false);
        }
    }
    
    // Toggles the pause/resume button between views.
    public void togglePauseResume() {
        paused = !paused;
        if (paused)
            pause_resume.setText("Resume");
        else
            pause_resume.setText("Pause");
    }
    
    // Sends an event to the listener that a button was clicked.
    // Passes on the button id.
    private void fireButtonClicked(Object source, String id) {
        listener.buttonClicked(new ButtonEvent(source, id));
    }

    // Fires a button clicked event.
    public void actionPerformed(ActionEvent e) {
        fireButtonClicked(e.getSource(), e.getActionCommand());
    }
}
