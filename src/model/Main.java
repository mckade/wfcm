package model;
/**
 * @filename Main.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Start of the program.
 * Starts the program interface. 
 */

//import jm.music.data.*;
//import jm.util.*;

//import static jm.constants.Durations.MINIM;
//import static jm.constants.Pitches.C4;

import javax.swing.SwingUtilities;

import gui.MainWindow;

public class Main
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindow();
            }});
        new Interface();
        // keep in mind that the only way to exit the program atm is if you exit out of the gui
    }
}
