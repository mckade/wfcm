package model;
/**
 * @filename Main.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Start of the program.
 * Starts the program interface. 
 */

import javax.swing.SwingUtilities;

import gui.MainWindow;

public class Main
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindow();
            }});
    }
}
