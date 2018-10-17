/**
 * @filename Interface.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * The interface that will act as the front end.
 * Will communicate with the user with various prompts (atm)
 * Links with the back end
 * 
 * At program start it will prompt the user for a midi file.
 * Afterwards it will pass on the filename to the MidiReader
 * When the music has been created it will play it.
 * Next it will ask the user:
 * Play music again || New music generation || new midi file || done
 */

import java.util.Scanner;

public class Interface {
    
    // Constants
    private static final String COMMANDS =
            "Commands:\n"
            + "LOAD :: Loads a given midi file. Automatically generates music with length 10.\n"
            + "GEN  :: Generates new music given a length using exisitng midi data.\n"
            + "SAVE :: Saves the generated music as a midi file.\n"
            + "PLAY :: Plays the generated music.\n"
            + "DONE :: Exits program.\n"
            + "HELP :: Gives a list of commands.\n";
    
    // Fields
    private MusicGenerator mgen;
    private String filename;
    private String input;
    private int length;
    private boolean done;

    // Constructor
    public Interface()
    {
        // Init
        Scanner scanner = new Scanner(System.in);
        mgen = new MusicGenerator();
        
        // Intro
        System.out.println(COMMANDS);
        
        // User
        done = false;
        while (!done) {
            // Prompt
            System.out.print("Command: ");
            input = scanner.nextLine();
            
            // LOAD
            if (input.equalsIgnoreCase("LOAD")) {
                System.out.print("Midi filename: ");
                filename = scanner.nextLine();
                mgen.loadMidiFile(filename);
                mgen.generateMusic(10);
            }
            // GEN
            else if (input.equalsIgnoreCase("GEN")) {
                System.out.print("Music length: ");
                length = scanner.nextInt();
                if (scanner.hasNextLine())
                    scanner.nextLine(); // Gets rid of excess input.
                mgen.generateMusic(length);
            }
            // SAVE
            else if (input.equalsIgnoreCase("SAVE"))
                mgen.saveSong();
            // PLAY
            else if (input.equalsIgnoreCase("PLAY"))
                mgen.playSong();
            // DONE
            else if (input.equalsIgnoreCase("DONE"))
                done = true;
            // HELP
            else if (input.equalsIgnoreCase("HELP"))
                System.out.println(COMMANDS);
            // Unknown
            else
                System.out.println("Unknown Command. Type HELP for a list.");
            }
        
        // Closing
        scanner.close();
        System.out.println("Exited Program");
    }
}
