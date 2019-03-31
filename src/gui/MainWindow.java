/**
 * @filename MainWindow.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The main window of the graphical user interface.
 * Holds two halves of the gui.
 * - The left consisting of the buttons and log.
 * - The right consisting of the visualizer and settings.
 * 
 * Handles varies events between components.
 * - Menu bar items
 * - Split pane updates
 * 
 * Holds default visuals for the gui.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import coms.ButtonEvent;
import coms.ButtonListener;
import coms.UpdateEvent;
import coms.UpdateListener;
import model.MusicGenerator;

@SuppressWarnings("serial")
public class MainWindow extends JFrame 
implements UpdateListener, ButtonListener {
    
    // Both Default visuals
    public static final Color BORDER_OUTER = new Color(0, 92, 75);
    public static final Color BORDER_INNER = new Color(8, 145, 119);
    public static final Color BORDER_HOVERED = new Color(80, 55, 110);
    public static final Color BORDER_CLICKED = new Color(180, 76, 180);
    public static final Color DIVIDER = new Color(0, 62, 52);
    // Panel visuals
    public static final Color PANEL_BACKGROUND = new Color(0, 6, 5);
    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4,4,4,4),
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_OUTER, 2),
                    BorderFactory.createLineBorder(BORDER_INNER, 2)));
    // Component visuals
    public static final Color COMPONENT_BACKGROUND = new Color(0, 40, 32);
    public static final Color COMPONENT_BORDER_INNER = new Color(14, 184, 152);

    // Panels
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private JSplitPane splitPane;
    
    // Components
    private _MenuBar menuBar;
    
    // Controller and controls
    private MusicGenerator mgen;
    private File file = null;
    private boolean sample = false;
    private boolean playable = false;
    private boolean paused = false;
    
    // Constructor
    public MainWindow() {
        // Setup
        super("Proc Music");
        Dimension dim = new Dimension(1024, 768);
        mgen = new MusicGenerator();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(dim);
        setSize(dim);
        setLocationRelativeTo(null);
        getContentPane().setBackground(PANEL_BACKGROUND);
        setLayout(new BorderLayout());
        
        // Creating Panels and components
        leftPanel = new LeftPanel(this, this);
        rightPanel = new RightPanel();
        rightPanel.setInstStrings(mgen.getInstStrings());
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {}
                    public void paint(Graphics g) {
                        g.setColor(DIVIDER);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                    }
                };
            }
        });
        menuBar = new _MenuBar(this);
        
        // Adding Panels and components
        add(splitPane, BorderLayout.CENTER);
        setJMenuBar(menuBar);
        
        // Finished setup
        setVisible(true);
    }

    // Handles events from the menu bar.
    public void menuItemClicked(ButtonEvent e) {
        File tfile;
        
        switch (e.getID()) {
        // File
        // New
        case _MenuBar.NEW:
            // NYI
            break;
        // Open    
        case _MenuBar.OPEN:
            file = FileDialog.openFile(this, FileDialog.SAVE_OPEN);
            mgen.openVisualizerTable(file);
            break;
        // Close    
        case _MenuBar.CLOSE:
            // NYI
            break;
        // Save    
        case _MenuBar.SAVE:
            if (file == null)
                file = FileDialog.saveFile(this, FileDialog.SAVE_OPEN);
            if (file != null)
                mgen.saveVisualizerTable(file);
            break;
        // SaveAs    
        case _MenuBar.SAVEAS:
            tfile = FileDialog.saveFile(this, FileDialog.SAVE_OPEN);
            if (tfile != null) {
                file = tfile;
                mgen.saveVisualizerTable(file);
            }
            break;
        // Import   
        case _MenuBar.IMPORT:
            tfile = FileDialog.openFile(this, FileDialog.IMPORT_EXPORT);
            if (tfile != null) {
                if (mgen.importSample(tfile)) {
                    leftPanel.addLog("Loading sample...");
                    sample = true;
                    playable = true;
                    rightPanel.setNotes(mgen.getSampleNotes());
                    rightPanel.setTempo((int)mgen.getTempo());
                    leftPanel.addLog("Sample loaded.");
                }
                else {
                    leftPanel.addLog("Failed to load sample.");
                }
            }
            break;
        // Export    
        case _MenuBar.EXPORT:
            if (!sample) {
                leftPanel.addLog("Unable to export MIDI\nFirst import a MIDI sample and generate music.");
                return;
            }
            tfile = FileDialog.saveFile(this, FileDialog.IMPORT_EXPORT);
            if (tfile != null) {
                leftPanel.addLog("Exporting MIDI...");
                mgen.exportMIDI(file);
                leftPanel.addLog("Finished");
            }
            break;
        // Exit   
        case _MenuBar.EXIT:
            System.exit(0);
            break;
            
        // Window
        // LeftPanel
        case _MenuBar.LEFTPANEL:
            leftPanel.toggleVisible();
            break;
        // Settings   
        case _MenuBar.SETTINGS:
            rightPanel.toggleSettingsPanel();
            break;
        }
    }

    // Handles events calling for an update
    public void updateEvent(UpdateEvent e) {
        switch (e.getUpdateType()) {
        case visible:
            splitPane.setDividerSize(10);
            splitPane.setDividerLocation(LeftPanel.MINWIDTH);
            break;
        case invisible:
            splitPane.setDividerSize(0);
            break;
        case music:
            leftPanel.togglePlayStop();
            paused = false;
            break;
        default:
        }
    }

    // Handles events for when a button is clicked.
    public void buttonClicked(ButtonEvent e) {
        // Checking if a sample has been imported.
        if (!sample) {
            leftPanel.addLog("Unable to complete action.\nFirst import a MIDI sample.");
            return;
        }
        
        switch (e.getID()) {
        // Generate
        case ButtonPanel.GENERATE:
            leftPanel.addLog("Generating Music...");
            if(mgen.stopSong()) {
                leftPanel.togglePlayStop();
            }
            mgen.generateMusic(leftPanel.getNoteLength(), rightPanel.getTempo());
            rightPanel.setNotes(mgen.getNotes());  // Passing the notes to the visualizer.
            leftPanel.addLog("Finished.");
            break;
        // Play/Stop
        case ButtonPanel.PLAY_STOP:
            if (!playable) {
                leftPanel.addLog("Unable to play music.\nNo music to play.");
                return;
            }
            
            // Toggling play/stop button
            if (!mgen.isPlaying()) {
                mgen.playSong();
                leftPanel.addLog("Playing music...");
            }
            else {
                mgen.stopSong();
                leftPanel.addLog("Stopped music");
            }
            leftPanel.togglePlayStop();
            break;
        // Pause
        case ButtonPanel.PAUSE_RESUME:
            paused = !paused;
            if (paused) {
                leftPanel.addLog("Paused music");
            }
            else {
                leftPanel.addLog("Unpaused music");
            }
            leftPanel.togglePauseResume();
            break;
        }
    }
}