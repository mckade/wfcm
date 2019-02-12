package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar
implements ActionListener {

    public MenuBar() {
        JMenu file = new JMenu("File");
        JMenuItem item1 = new JMenuItem("Item1");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setActionCommand("exit");
        file.add(item1);
        file.addSeparator();
        file.add(exit);
        add(file);
    }

    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if (ac == "exit") {
            System.exit(0);
        }
    }
}
