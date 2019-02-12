package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
    
    // Panes
    // Column 1
    LeftPane leftPane;
    
    // Column 2
    TitlePane titlePane;
    CenterPane centerPane;
    ButtonPane buttonPane;
    BottomPane bottomPane;
    
    // Column 3
    TopRightPane topRightPane;
    BottomRightPane bottomRightPane;
    
    // Constructor
    public MainWindow() {
        
        // Setting up main window.
        super("Proc Music");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setJMenuBar(new MenuBar());
        
        // Setting up layout and panes.
        setUpPanes();
        
        // Finished setup
        setVisible(true);
    }
    
    // Setting up layout and panes.
    private void setUpPanes() {
        
        // Setting layout
        setLayout(new GridBagLayout());
        
        // Creating Panes
        // Column 1
        leftPane = new LeftPane(this);
        
        // Column 2
        titlePane = new TitlePane();
        centerPane = new CenterPane();
        buttonPane = new ButtonPane();
        bottomPane = new BottomPane();
        
        // Column 3
        topRightPane = new TopRightPane();
        bottomRightPane = new BottomRightPane();
        
        // Adding listeners to panes.
        
        // Adding panes to Main Window
        // 4 rows by 3 columns
        GridBagConstraints gc = new GridBagConstraints();
        
        // Setting up constraint defaults
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 0;
        gc.weighty = 0;
        
        ///////////
        // Column 1
        ///////////
        
        // Left Pane spanning column 1
        gc.gridheight = 4;
        add(leftPane, gc);
        
        ///////////
        // Column 2
        ///////////
        gc.gridx++;
        gc.gridy = 0;
        gc.gridheight = 1;
        
        // Title Pane
        add(titlePane, gc);
        
        // Center Pane
        gc.gridy++;
        add(centerPane, gc);
        
        // Button Pane
        gc.gridy++;
        add(buttonPane, gc);
        
        // Bottom Pane
        gc.gridy++;
        add(bottomPane, gc);
        
        ///////////
        // Column 3
        ///////////
        gc.gridx++;
        gc.gridy = 0;
        
        // Top Right Pane spanning first 3 rows
        gc.gridheight = 3;
        add(topRightPane, gc);
        
        // Bottom Right Pane
        gc.gridy = 3;
        gc.gridheight = 1;
        add(bottomRightPane, gc);
    }
}