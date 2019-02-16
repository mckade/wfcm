package gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileDialog {
    
    // Flag to differentiate between save/import and open/export
    public static final boolean SAVE_OPEN = true;
    public static final boolean IMPORT_EXPORT = false;
    
    // File chooser
    // Keeps track of the last location it was at.
    private static final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
    private static final FileFilter midFilter = new FileNameExtensionFilter("MIDI File (*.MID)", "MID");
    private static final FileFilter gtFilter = new FileNameExtensionFilter("Generation Table File (*.gt)", "gt");
    
    // Opens the save dialog for saving a generation table.
    // If the file already exists asks for override confirmation.
    // If override is canceled, goes back to file dialog
    // @flag differentiates between save and import
    public static File saveFile(Component parent, boolean flag) {
        // Init
        if (flag) {
            fileChooser.setFileFilter(gtFilter);
            fileChooser.setDialogTitle("Save");
        }
        else {
            fileChooser.setFileFilter(midFilter);
            fileChooser.setDialogTitle("Import MIDI sample");
        }
        File file = null;
        
        // Opening dialog
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            
            if (file.exists()) {
                Object[] options = { "Yes", "Cancel" };
                int response = JOptionPane.showOptionDialog(parent,
                        "Are you sure you want to overide file: " + file.getName() + "?", "Overide Conformation",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (response == 1) {
                    file = saveFile(parent, flag);
                }
            }
        }
        return file;
    }
    
    // Opens the open dialog for opening a generation table.
    // @flag differentiates between open and export
    public static File openFile(Component parent, boolean flag) {
        // Init

        if (flag) {
            fileChooser.setFileFilter(gtFilter);
            fileChooser.setDialogTitle("Open");
        }
        else {
            fileChooser.setFileFilter(midFilter);
            fileChooser.setDialogTitle("Export MIDI");
        }
        File file = null;
        
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        
        return file;
    }
}
