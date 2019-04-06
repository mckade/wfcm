package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import coms.ButtonEvent;
import coms.ButtonListener;

@SuppressWarnings("serial")
public class MusicControlPanel extends JPanel
implements ActionListener {
    
    // Button ids
    public static final String PLAY_PAUSE = "play_pause";
    public static final String SKIPLEFT = "skipleft";
    public static final String SKIPRIGHT = "skipright";
    
    // Listener to send events to.
    private ButtonListener listener;
    
    // Components
    private _JButton play_pause;
    private _JButton skipLeft;
    private _JButton skipRight;
    
    // Control
    boolean playing = false;

    public MusicControlPanel(ButtonListener listener) {
        // Setup
        this.listener = listener;
        setBackground(MainWindow.C_PANEL_BACKGROUND);
        setBorder(MainWindow.B_PANEL_BORDER);
        setLayout(new FlowLayout(FlowLayout.LEADING));
        
        // Creating components
        // TODO: Change to circular buttons with images.
        play_pause = new _JButton("Play");
        play_pause.addActionListener(this);
        play_pause.setActionCommand(PLAY_PAUSE);
        skipLeft = new _JButton("Skip Left");
        skipLeft.addActionListener(this);
        skipLeft.setActionCommand(SKIPLEFT);
        skipRight = new _JButton("Skip Right");
        skipRight.addActionListener(this);
        skipRight.setActionCommand(SKIPRIGHT);
        
        // Adding components
        add(play_pause);
        add(skipLeft);
        add(skipRight);
    }
    
    // Toggles the view between play and pause.
    public void togglePlayPauseView() {
        playing = !playing;
        if (playing)
            play_pause.setText("Pause");
        else
            play_pause.setText("Play");
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
