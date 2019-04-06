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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    
    // Default visuals
    public static final Color C_BORDER_OUTER = new Color(0, 92, 75);
    public static final Color C_BORDER_INNER = new Color(8, 145, 119);
    public static final Color C_BORDER_HOVERED = new Color(80, 55, 110);
    public static final Color C_BORDER_CLICKED = new Color(180, 76, 180);
    public static final Color C_DIVIDER = new Color(0, 62, 52);
    // Panel visuals
    public static final Color C_PANEL_BACKGROUND = new Color(0, 6, 5);
    public static final Border B_PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4,4,4,4),
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(C_BORDER_OUTER, 2),
                    BorderFactory.createLineBorder(C_BORDER_INNER, 2)));
    // Component visuals
    public static final Color C_COMPONENT_BACKGROUND = new Color(0, 40, 32);
    public static final Color C_COMPONENT_BORDER = new Color(14, 184, 152);
    public static final Border B_COMPONENT_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainWindow.C_BORDER_OUTER, 2),
            BorderFactory.createLineBorder(MainWindow.C_BORDER_INNER, 2));
    // Font visuals
    public static final Font F_HEADING1 = new Font(Font.DIALOG, Font.PLAIN, 16);
    public static final Font F_HEADING2 = new Font(Font.DIALOG, Font.PLAIN, 12);
    public static final Color C_FONTCOLOR1 = Color.WHITE;
    public static final Color C_FONTCOLOR2 = new Color(0,0,0);
    // Tab
    public static final Border B_BORDER_TAB = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, C_DIVIDER),
            BorderFactory.createEmptyBorder(5, 0, 0, 0));

    // Panels
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private JSplitPane splitPane;
    
    // Components
    private MainWindowMenuBar menuBar;
    
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
        Dimension dim = new Dimension(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(dim);
        setSize(dim);
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_PANEL_BACKGROUND);
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
                        g.setColor(C_DIVIDER);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                    }
                };
            }
        });

        // Creating Components
        menuBar = new MainWindowMenuBar(this);
        
        // Adding Panels and components
        add(splitPane, BorderLayout.CENTER);
        setJMenuBar(menuBar);
        
        // Finished setup
        setVisible(true);
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
        case MainWindowMenuBar.NEW:
            // NYI
            break;
        // Open    
        case MainWindowMenuBar.OPEN:
            file = FileDialog.openFile(this, FileDialog.SAVE_OPEN);
            mgen.openVisualizerTable(file);
            break;
        // Close    
        case MainWindowMenuBar.CLOSE:
            // NYI
            break;
        // Save    
        case MainWindowMenuBar.SAVE:
            if (file == null)   // Checking if file has a save location.
                file = FileDialog.saveFile(this, FileDialog.SAVE_OPEN);
            if (file != null)   // Saving at file location.
                mgen.saveVisualizerTable(file);
            break;
        // SaveAs    
        case MainWindowMenuBar.SAVEAS:
            tfile = FileDialog.saveFile(this, FileDialog.SAVE_OPEN);
            if (tfile == null) break;   // Canceled
            file = tfile;   // Updating current file save location
            mgen.saveVisualizerTable(file);
            break;
        // Import   
        case MainWindowMenuBar.IMPORT:
            tfile = FileDialog.openFile(this, FileDialog.IMPORT_EXPORT);
            if (tfile == null) break;   // Canceled
            mgen.pauseSong();
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
        case MainWindowMenuBar.EXPORT:
            tfile = FileDialog.saveFile(this, FileDialog.IMPORT_EXPORT);
            if (tfile == null) break;   // Canceled
            leftPanel.addLog("Exporting MIDI...");
            if (mgen.exportMIDI(file))
                leftPanel.addLog("MIDI exported.");
            else leftPanel.addLog("Failed to export MIDI.");
            break;
        // Exit   
        case MainWindowMenuBar.EXIT:
            System.exit(0);
            break;
            
        // Window
        // LeftPanel
        case MainWindowMenuBar.LEFTPANEL:
            leftPanel.toggleVisible();
            break;
        // Settings   
        case MainWindowMenuBar.SETTINGS:
            rightPanel.toggleSettingsPanel();
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
            mgen.pauseSong();
            leftPanel.addLog("Generating Music...");
            mgen.generateMusic();
            rightPanel.fullUpdate();
            leftPanel.addLog("Finished.");
            break;
        // Recycle
        case GenerationButtonPanel.RECYCLE:
            mgen.pauseSong();
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
            break;
        // Skip Left
        case MusicControlPanel.SKIPLEFT:
            mgen.setPlayTime(0);
            rightPanel.updatePlayLine();
            mgen.onClick();
            break;
        // SkipRight
        case MusicControlPanel.SKIPRIGHT:
            mgen.pauseSong();
            mgen.setPlayTime(1);
            rightPanel.updatePlayLine();
            mgen.onClick();
            rightPanel.scrollToEnd();
            break;
        }
    }
}