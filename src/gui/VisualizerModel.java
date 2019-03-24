package gui;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class VisualizerModel extends AbstractTableModel {
    
    private int rows;
    private int cols;
    
    public VisualizerModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }
    
    public String getColumnName(int c) {
        return Integer.toString(c);
    }

    public int getColumnCount() {
        return cols;
    }

    public int getRowCount() {
        return rows;
    }

    public Object getValueAt(int row, int col) {
        // data
        return null;
    }
    
    public void setNotes() {
        
    }
}
