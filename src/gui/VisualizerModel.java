package gui;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class VisualizerModel extends AbstractTableModel {
    
    private int cols = 16;
    
    private String[] rowNames = {"C", "B", "Bb", "A", "Ab", "G", "F#", "F", "E", "Eb", "D", "C#", "C"};
    
    public String getColumnName(int c) {
        switch(c) {
            case 0:
                return "Note";
            default:
                return Integer.toString(c);
        }
    }

    public int getColumnCount() {
        return cols;
    }

    public int getRowCount() {
        return rowNames.length;
    }

    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return rowNames[row];
        }
        
        // else data
        
        return null;
    }
    
    public void setNotes() {
        
    }
}
