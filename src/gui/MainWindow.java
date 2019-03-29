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
import coms.MenuEvent;
import coms.MenuListener;
import coms.UpdateEvent;
import coms.UpdateListener;
import model.MusicGenerator;

@SuppressWarnings("serial")
public class MainWindow extends JFrame 
implements MenuListener, UpdateListener, ButtonListener {
    
    // Default visual settings
    public static final Color PANEL_BACKGROUND = new Color(0, 6, 5);
    public static final Color COMPONENT_BACKGROUND = new Color(0, 40, 32);
    public static final Border BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4,4,4,4),
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 92, 75), 2),
                    BorderFactory.createLineBorder(new Color(8, 145, 119), 2)));

    // Panels
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private JSplitPane splitPane;
    
    // Components
    private _MenuBar menuBar;
    
    // Controller and controls
    private MusicGenerator mgen;
    private boolean sample = false;
    private boolean generated = false;
    private boolean paused = false;
    
    // Constructor
    public MainWindow() {
        
        // Setup
        super("Proc Music");
        Dimension dim = new Dimension(1024, 768);
        mgen = new MusicGenerator();
        
        // Setting up main window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(dim);
        setSize(dim);
        setLocationRelativeTo(null);
        getContentPane().setBackground(PANEL_BACKGROUND);
        
        // Layout, panels, and components
        setLayout(new BorderLayout());
        
        // Creating Panels and components
        leftPanel = new LeftPanel(this, this);
        rightPanel = new RightPanel(mgen);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {}
                    public void paint(Graphics g) {
                        g.setColor(new Color(0, 62, 52));
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
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

    // Gets events from the menu bar
    public void menuItemClicked(MenuEvent e) {
        File file;
        
        switch (e.getID()) {
        
        // File
        case _MenuBar.NEW:
            break;
        case _MenuBar.OPEN:
            file = FileDialog.openFile(this, FileDialog.SAVE_OPEN);
            mgen.openGenerationTable(file);
            break;
        case _MenuBar.CLOSE:
            break;
        case _MenuBar.SAVE:
            file = FileDialog.saveFile(this, FileDialog.SAVE_OPEN);
            mgen.saveGenerationTable(file);
            break;
        case _MenuBar.SAVEAS:
            break;
        case _MenuBar.IMPORT:
            file = FileDialog.openFile(this, FileDialog.IMPORT_EXPORT);
            if (file != null) {
                if (mgen.importSample(file)) {
                    sample = true;
                    leftPanel.addLog("Sample loaded.");
                }
                else {
                    leftPanel.addLog("Failed to load sample.");
                }
            }
            break;
        case _MenuBar.EXPORT:
            if (!sample) {
                leftPanel.addLog("Unable to export music\nFirst import a MIDI sample and generate music.");
                return;
            }
            file = FileDialog.saveFile(this, FileDialog.IMPORT_EXPORT);
            if (file != null) {
                leftPanel.addLog("Exporting MIDI...");
                mgen.exportMIDI(file);
                leftPanel.addLog("Finished");
                }
            break;
        case _MenuBar.EXIT:
            System.exit(0);
            break;
            
        // Window
        case _MenuBar.LEFTPANEL:
            leftPanel.toggleVisible();
            break;
        case _MenuBar.SETTINGS:
            rightPanel.toggleSettingsPanel();
            break;
        }
    }

    // Gets events calling for an update
    public void updateEvent(UpdateEvent e) {
        switch (e.getUpdateType()) {
        case visible:
            splitPane.setDividerSize(10);
            splitPane.setDividerLocation(LeftPanel.MINWIDTH);
            break;
        case invisible:
            splitPane.setDividerSize(0);
            break;
        }
    }

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

            generated = true;
            rightPanel.setNotes(mgen.getNotes());  // Passing the notes to the visualizer.
            leftPanel.addLog("Finished.");
            break;
        // Play/Stop
        case ButtonPanel.PLAY_STOP:
            if (!generated) {
                leftPanel.addLog("Unable to play music.\nNo music has been generated.");
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
            leftPanel.togglePause();
            break;
        }
    }
}