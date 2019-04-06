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
    private _Button play_pause;
    private _Button skipLeft;
    private _Button skipRight;
    
    // Control
    boolean playing = false;

    public MusicControlPanel(ButtonListener listener) {
        // Setup
        this.listener = listener;
        setBackground(MainWindow.PANEL_BACKGROUND);
        setBorder(MainWindow.PANEL_BORDER);
        setLayout(new FlowLayout(FlowLayout.LEADING));
        
        // Creating components
        // TODO: Change to circular buttons with images.
        play_pause = new _Button("Play|Pause");
        play_pause.addActionListener(this);
        play_pause.setActionCommand(PLAY_PAUSE);
        skipLeft = new _Button("Skip Left");
        skipLeft.addActionListener(this);
        skipLeft.setActionCommand(SKIPLEFT);
        skipRight = new _Button("Skip Right");
        skipRight.addActionListener(this);
        skipRight.setActionCommand(SKIPRIGHT);
        
        // Adding components
        add(play_pause);
        add(skipLeft);
        add(skipRight);
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
