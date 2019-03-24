/**
 * @filename VisualizerPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Visualizer panel that allows for the user to interact with
 * the music generation process.
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class VisualizerPanel extends JPanel {
    
    // Components
    private JTable visualizer;
    private VisualizerModel model;
    private String[] rowHeaders = {"C", "B", "Bb", "A", "Ab", "G", "F#", "F", "E", "Eb", "D", "C#", "C"};
    
    // Constructor
    public VisualizerPanel() {
        
        // Setup
        setBackground(MainWindow.PANEL_BACKGROUND);
        setBorder(MainWindow.BORDER);
        setLayout(new BorderLayout());
        
        // Creating components
        model = new VisualizerModel(rowHeaders.length, 16);
        visualizer = new JTable(model);
        visualizer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel tcm = visualizer.getColumnModel();
        for (int i = 0; i < visualizer.getColumnCount(); i++) {
            tcm.getColumn(i).setMinWidth(50);
            tcm.getColumn(i).setPreferredWidth(50);
        }
        
        // Row Headers
        ListModel<Object> lm = new AbstractListModel<Object>() {
            public Object getElementAt(int index) {
                return rowHeaders[index];
            }
            public int getSize() {
                return rowHeaders.length;
            }
        };
        JList<Object> rowHeader = new JList<Object>(lm);
        rowHeader.setFixedCellWidth(50);
        rowHeader.setFixedCellHeight(visualizer.getRowHeight());
        rowHeader.setCellRenderer(new RowRenderer(visualizer));
        
        JScrollPane scrollPane = new JScrollPane(visualizer);
        scrollPane.setRowHeaderView(rowHeader);
        
        // Adding components
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void setNotes() {
        model.setNotes();
    }
}

// Sets up how a row should look.
@SuppressWarnings("serial")
class RowRenderer extends JLabel
implements ListCellRenderer<Object> {

    public RowRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
    }
    
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}