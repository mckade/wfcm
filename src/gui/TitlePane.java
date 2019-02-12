package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitlePane extends JPanel {
    
    private JLabel title;

    public TitlePane() {
        setBackground(Color.BLUE);
        setLayout(new BorderLayout());
        
        title = new JLabel("ProcMusic");
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.CENTER);
    }
}
