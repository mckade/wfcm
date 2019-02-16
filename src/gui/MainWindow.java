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

import coms.MenuEvent;
import coms.MenuListener;
import coms.UpdateEvent;
import coms.UpdateListener;
import model.MusicGenerator;

@SuppressWarnings("serial")
public class MainWindow extends JFrame 
implements MenuListener, UpdateListener {
    
    // Default visual settings
    public static final Color BACKGROUND = new Color(0, 6, 5);
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
    
    // Constructor
    public MainWindow() {
        
        // Setup
        super("Proc Music");
        Dimension dim = new Dimension(1024, 768);
        mgen = new MusicGenerator();
        
        // Setting up main window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(dim);
        setMinimumSize(new Dimension(400,400));
        setSize(dim);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
        
        // Layout, panels, and components
        setLayout(new BorderLayout());
        
        // Creating Panels and components
        leftPanel = new LeftPanel();
        leftPanel.addUpdateListener(this);
        rightPanel = new RightPanel();
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
        menuBar = new _MenuBar();
        menuBar.addMenuListener(this);
        
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
            mgen.importSample(file);
            break;
        case _MenuBar.EXPORT:
            file = FileDialog.saveFile(this, FileDialog.IMPORT_EXPORT);
            mgen.exportMIDI(file);
            break;
        case _MenuBar.EXIT:
            System.exit(0);
            break;
            
        // Window
        case _MenuBar.BUTTONS:
            leftPanel.toggleButtonPanel();
            break;
        case _MenuBar.LOG:
            leftPanel.toggleLogPanel();
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
}