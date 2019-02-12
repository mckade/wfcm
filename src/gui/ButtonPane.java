package gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class ButtonPane extends JPanel {
    
    private JButton generate;
    private JButton save;
    
    private JSpinner length;
    private JTextField name;

    public ButtonPane() {
        setBackground(Color.DARK_GRAY);
        setLayout(new FlowLayout());
        
        generate = new JButton("Generate");
        save = new JButton("Save");
        length = new JSpinner();
        name = new JTextField("Name");
        
        add(generate);
        add(length);
        add(save);
        add(name);
    }
}
