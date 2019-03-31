/**
 * @filename FileDialog.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Static class that sets up the file dialog box.
 * Keeps track of file dialog box selections, and locations between uses.
 */

package gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileDialog {
    
    // Flag to differentiate between save/export and open/import
    public static final boolean SAVE_OPEN = true;
    public static final boolean IMPORT_EXPORT = false;
    
    // File chooser
    // Keeps track of the last location it was at.
    private static final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
    private static final FileFilter midFilter = new FileNameExtensionFilter("MIDI File (*.MID)", "MID");
    private static final FileFilter vtFilter = new FileNameExtensionFilter("Visualizer Table File (*.vt)", "vt");
    
    // Opens the save dialog for saving a vt.
    // OR opens the export dialog for exporting a MIDI.
    // If the file already exists asks for override confirmation.
    // If override is canceled, goes back to file dialog.
    // @flag differentiates between save and import
    public static File saveFile(Component parent, boolean flag) {
        // init
        if (flag) {
            // save
            fileChooser.setFileFilter(vtFilter);
            fileChooser.setDialogTitle("Save");
        }
        else {
            // export
            fileChooser.setFileFilter(midFilter);
            fileChooser.setDialogTitle("Export MIDI");
        }
        File file = null;
        
        // Opening dialog
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            
            // Checking if file exists, and if so, asks the user if they want to override it.
            if (file.exists()) {
                Object[] options = { "Yes", "Cancel" };
                int response = JOptionPane.showOptionDialog(parent,
                        "Are you sure you want to overide file: " + file.getName() + "?", "Overide Conformation",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (response == 1) {
                    // Cancel, reopens dialog
                    file = saveFile(parent, flag);
                }
            }
        }
        return file;
    }
    
    // Opens the open dialog for opening a vt.
    // OR opens the import dialog for importing a MIDI sample.
    // @flag differentiates between open and export
    public static File openFile(Component parent, boolean flag) {
        // Init
        if (flag) {
            // open
            fileChooser.setFileFilter(vtFilter);
            fileChooser.setDialogTitle("Open");
        }
        else {
            // import
            fileChooser.setFileFilter(midFilter);
            fileChooser.setDialogTitle("Import MIDI Sample");
        }
        File file = null;
        
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            file = fileChooser.getSelectedFile();
        
        return file;
    }
}
