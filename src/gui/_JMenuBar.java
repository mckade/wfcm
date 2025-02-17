/**
 * @filename _MenuBar.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Menu bar of the main window.
 * - File
 *  - New: Creates new visualizer table to work with. (NYI)
 *  - Open: Opens an existing visualizer table. (NYI)
 *  - Close: Closes the current visualizer table. (NYI)
 *  - Save: Saves the current generation as a midi file.
 *  - SaveAs: Saves the current generation as a midi file with a new name.
 *  - Exit: Exits the program.
 * - Window
 *  - Left Panel: Toggles the left panel display.
 *  - Settings: Toggles the settings display.
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.sun.glass.events.KeyEvent;

import coms.ButtonEvent;
import coms.ButtonListener;

@SuppressWarnings("serial")
public class _JMenuBar extends JMenuBar
implements ActionListener {
    
    // Menu item ids
    // File
    public static final String NEW = "new";
    public static final String OPEN = "open";
    public static final String CLOSE = "close";
    public static final String SAVE = "save";
    public static final String SAVEAS = "saveas";
    public static final String IMPORT = "import";
    public static final String EXPORT = "export";
    public static final String EXIT = "exit";
    // Window
    public static final String LEFTPANEL = "leftpanel";
    public static final String SETTINGS = "settings";
    // Theme
    public static final String DARK = "dark";
    public static final String LIGHT = "light";
    
    // Listeners to send events to.
    private ButtonListener listener;

    // Constructor
    public _JMenuBar(ButtonListener listener) {
        // Setup
        this.listener = listener;
        
        // File menu
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        // File menu items
        JMenuItem _new = new JMenuItem("New");
        _new.setMnemonic(KeyEvent.VK_N);
        _new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        _new.addActionListener(this);
        _new.setActionCommand(NEW);
        JMenuItem open = new JMenuItem("Open");
        open.setMnemonic(KeyEvent.VK_L);
        open.addActionListener(this);
        open.setActionCommand(OPEN);
        JMenuItem close = new JMenuItem("Close");
        close.setMnemonic(KeyEvent.VK_C);
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        close.addActionListener(this);
        close.setActionCommand(CLOSE);
        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        save.addActionListener(this);
        save.setActionCommand(SAVE);
        JMenuItem saveas = new JMenuItem("Save As");
        saveas.setMnemonic(KeyEvent.VK_A);
        saveas.addActionListener(this);
        saveas.setActionCommand(SAVEAS);
        JMenuItem _import = new JMenuItem("Import MIDI Sample");
        _import.setMnemonic(KeyEvent.VK_I);
        _import.addActionListener(this);
        _import.setActionCommand(IMPORT);
        JMenuItem export = new JMenuItem("Export MIDI");
        export.setMnemonic(KeyEvent.VK_O);
        export.addActionListener(this);
        export.setActionCommand(EXPORT);
        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(this);
        exit.setActionCommand(EXIT);
        // Adding file menu items to file menu.
        file.add(_new);
        file.add(open);
        file.addSeparator();
        file.add(close);
        file.addSeparator();
        file.add(save);
        file.add(saveas);
        file.addSeparator();
        file.add(_import);
        file.add(export);
        file.addSeparator();
        file.add(exit);      
        
        // Window menu.
        JMenu window = new JMenu("Window");
        window.setMnemonic(KeyEvent.VK_W);
        // Window menu items.
        JCheckBoxMenuItem leftPanel = new JCheckBoxMenuItem("Left Panel");
        leftPanel.setSelected(true);
        leftPanel.setMnemonic(KeyEvent.VK_L);
        leftPanel.addActionListener(this);
        leftPanel.setActionCommand(LEFTPANEL);
        JCheckBoxMenuItem settings = new JCheckBoxMenuItem("Settings");
        settings.setSelected(true);
        settings.setMnemonic(KeyEvent.VK_S);
        settings.addActionListener(this);
        settings.setActionCommand(SETTINGS);
        // Adding window menu items to window menu.
        window.add(leftPanel);
        window.add(settings);
        
        // Theme menu
        JMenu theme = new JMenu("Theme");
        theme.setMnemonic(KeyEvent.VK_T);
        // Theme menu items
        JMenuItem dark = new JMenuItem("Dark");
        dark.setMnemonic(KeyEvent.VK_D);
        dark.addActionListener(this);
        dark.setActionCommand(DARK);
        JMenuItem light = new JMenuItem("Light");
        light.setMnemonic(KeyEvent.VK_L);
        light.addActionListener(this);
        light.setActionCommand(LIGHT);
        // Adding theme menu items to theme menu.
        theme.add(dark);
        theme.add(light);
        
        // Adding menus to menu bar.
        add(file);
        add(window);
        add(theme);
    }
    
    // Sends an event to the listener that a menu item was clicked.
    // Passes on the menu item id.
    private void fireMenuItemClicked(Object source, String id) {
        listener.menuItemClicked(new ButtonEvent(source, id));
    }
    
    // Fires a menu item clicked event.
    public void actionPerformed(ActionEvent e) {
        fireMenuItemClicked(e.getSource(), e.getActionCommand());
    }
}
