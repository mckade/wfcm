/**
 * @filename ButtonPanel.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Button Panel that holds the basic functions for music generation.
 * Not yet implemented
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import coms.ButtonEvent;
import coms.ButtonListener;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel
implements ActionListener {
    
    private ArrayList<ButtonListener> listeners;
    
    // Components
    JButton generate;
    JSpinner noteLength;
    
    // Constructor
    public ButtonPanel(Dimension dim) {
        
        // Setup
        listeners = new ArrayList<ButtonListener>();
        
        // Panel settings
        setMinimumSize(dim);
        setBackground(MainWindow.BACKGROUND);
        setBorder(MainWindow.BORDER);
        
        // Layout and components
        setLayout(new BorderLayout());
        
        // Creating components
        generate = new JButton("Generate");
        generate.addActionListener(this);
        noteLength = new JSpinner();
        noteLength.setValue(10);
        
        add(generate, BorderLayout.CENTER);
        add(noteLength, BorderLayout.SOUTH);
    }
    
    public void addButtonListener(ButtonListener listener) {
        listeners.add(listener);
    }
    
    private void fireButtonClicked(Object source) {
        for (ButtonListener listener : listeners) {
            listener.generateButtonClicked(new ButtonEvent(source, (int)noteLength.getValue()));
        }
    }

    public void actionPerformed(ActionEvent e) {
        fireButtonClicked(e.getSource());
    }
}
