/**
 * @filename MainWindow.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * The main window of the graphical user interface.
 * Holds two halves of the gui.
 * - The left consisting of the generation buttons and log.
 * - The right consisting of the visualizer, settings, and music control.
 *
 * Handles varies events between components.
 * - Menu bar items
 * - Split pane updates
 *
 * Holds default visuals for the gui.
 */

package gui;

import java.awt.BorderLayout;
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
    
    // Panels
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private JSplitPane splitPane;
    
    // Components
    private _JMenuBar menuBar;
    
    // Controller
    private MusicGenerator mgen;

    // Saved Info
    private File file = null;

    // Control
    private boolean sample = false;
    private boolean playable = false;
    
    // Constructor
    public MainWindow() {
        // Setup
        super("Proc Music");
        Visuals.setThemeDark();
        Dimension dim = new Dimension(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(dim);
        setSize(dim);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Visuals.C_PANEL_BACKGROUND);
        setLayout(new BorderLayout());
        
        // Controller
        mgen = new MusicGenerator(this);

        // Creating Panels
        // Left: (Generation Buttons and Log)
        // Right: (Music Control, Settings, Preferences, Visualizer)
        leftPanel = new LeftPanel(this, this);
        rightPanel = new RightPanel(mgen, this);
        // Movable divider between Left and Right
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPane.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {}
                    public void paint(Graphics g) {
                        g.setColor(Visuals.C_DIVIDER1);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                    }
                };
            }
        });

        // Creating Components
        menuBar = new _JMenuBar(this);
        
        // Adding Panels and components
        add(splitPane, BorderLayout.CENTER);
        setJMenuBar(menuBar);
        
        updateVisuals();
        
        // Finished setup
        setVisible(true);
    }
    
    // Updates the visuals
    private void updateVisuals() {
        leftPanel.updateVisuals();
        rightPanel.updateVisuals();
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
        case playTime:
            mgen.setPlayTime(e.getPlayTime());
            rightPanel.updatePlayLine();
            break;
        case music:
            mgen.pauseSong();
            rightPanel.togglePlayPauseView();
            leftPanel.addLog("Music Finished Playing.");
            break;
        default:
        }
    }

    // Handles events from the menu bar.
    public void menuItemClicked(ButtonEvent e) {
        File tfile; // Temp file holding
        
        switch (e.getID()) {
        // File
        // New
        case _JMenuBar.NEW:
            // NYI
            break;
        // Open    
        case _JMenuBar.OPEN:
            file = FileDialog.openFile(this, FileDialog.SAVE_OPEN);
            mgen.openVisualizerTable(file);
            break;
        // Close    
        case _JMenuBar.CLOSE:
            // NYI
            break;
        // Save    
        case _JMenuBar.SAVE:
            if (file == null)   // Checking if file has a save location.
                file = FileDialog.saveFile(this, FileDialog.SAVE_OPEN);
            if (file != null)   // Saving at file location.
                mgen.saveVisualizerTable(file);
            break;
        // SaveAs    
        case _JMenuBar.SAVEAS:
            tfile = FileDialog.saveFile(this, FileDialog.SAVE_OPEN);
            if (tfile == null) break;   // Canceled
            file = tfile;   // Updating current file save location
            mgen.saveVisualizerTable(file);
            break;
        // Import   
        case _JMenuBar.IMPORT:
            tfile = FileDialog.openFile(this, FileDialog.IMPORT_EXPORT);
            if (tfile == null) break;   // Canceled
            if (mgen.isPlaying()) {
                mgen.pauseSong();
                rightPanel.togglePlayPauseView();
            }
            leftPanel.addLog("Loading MIDI...");
            if (mgen.importSample(tfile)) {
                sample = true;
                playable = true;
                rightPanel.fullUpdate();
                leftPanel.addLog("MIDI loaded.");
            }
            else leftPanel.addLog("Failed to load MIDI.");
            break;
        // Export    
        case _JMenuBar.EXPORT:
            tfile = FileDialog.saveFile(this, FileDialog.IMPORT_EXPORT);
            if (tfile == null) break;   // Canceled
            leftPanel.addLog("Exporting MIDI...");
            if (mgen.exportMIDI(tfile))
                leftPanel.addLog("MIDI exported.");
            else leftPanel.addLog("Failed to export MIDI.");
            break;
        // Exit   
        case _JMenuBar.EXIT:
            System.exit(0);
            break;
            
        // Window
        // LeftPanel
        case _JMenuBar.LEFTPANEL:
            leftPanel.toggleVisible();
            break;
        // Settings   
        case _JMenuBar.SETTINGS:
            rightPanel.toggleSettingsPanel();
            break;
            
        // Theme
        // Dark
        case _JMenuBar.DARK:
            Visuals.setThemeDark();
            updateVisuals();
            break;
        case _JMenuBar.LIGHT:
            Visuals.setThemeLight();
            updateVisuals();
            break;
        }
    }

    // Handles events for when a button is clicked.
    // Either generation buttons or music control
    public void buttonClicked(ButtonEvent e) {
        // Checking if a sample has been imported.
        // TODO: Change this later when users can add notes.
        if (!sample || !playable) {
            leftPanel.addLog("Unable to complete action.\nFirst import a MIDI sample.");
            return;
        }
        
        switch (e.getID()) {
        // Generation
        // Generate
        case GenerationButtonPanel.GENERATE:
            if (mgen.isPlaying()) {
                mgen.pauseSong();
                rightPanel.togglePlayPauseView();
            }
            leftPanel.addLog("Generating Music...");
            mgen.generateMusic();
            rightPanel.fullUpdate();
            leftPanel.addLog("Finished.");
            break;
        // Recycle
        case GenerationButtonPanel.RECYCLE:
            if (mgen.isPlaying()) {
                mgen.pauseSong();
                rightPanel.togglePlayPauseView();
            }
            leftPanel.addLog("Recycleing Music...");
            mgen.recycleMusic();
            rightPanel.fullUpdate();
            leftPanel.addLog("Finished.");
            break;

        // Music Control
        // Play|Pause
        case MusicControlPanel.PLAY_PAUSE:
            if (mgen.isPlaying()) {
                mgen.pauseSong();
                leftPanel.addLog("Paused Music.");
            }
            else {
                mgen.playSong();
                leftPanel.addLog("Playing Music...");
            }
            rightPanel.togglePlayPauseView();
            break;
        // Skip Left
        case MusicControlPanel.SKIPLEFT:
            mgen.setPlayTime(0);
            rightPanel.updatePlayLine();
            rightPanel.scrollToBeginning();
            mgen.skipMusicPlayTime();
            break;
        // SkipRight
        case MusicControlPanel.SKIPRIGHT:
            if (mgen.isPlaying()) {
                mgen.pauseSong();
                rightPanel.togglePlayPauseView();
            }
            mgen.setPlayTime(1);
            rightPanel.updatePlayLine();
            rightPanel.scrollToEnd();
            mgen.skipMusicPlayTime();
            break;
        }
    }
}