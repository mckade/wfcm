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
            + "LOAD :: Loads a given midi file and automatically generates the music.\n"
                    + "\tCan specify the filename and nodeCount respectively along with command.\n"
                    + "\tIf no nodeCount is specified it will default to 10.\n"
            + "GEN  :: Generates new music given a nodeCount using exisitng midi data.\n"
                    + "\tCan specify the nodeCount along with the command.\n"
            + "SAVE :: Saves the generated music as a midi file.\n"
            + "PLAY :: Plays the generated music.\n"
            + "DONE :: Exits the program.\n"
            + "HELP :: Gives a list of commands.\n";
    
    // Fields
    private MusicGenerator mgen;
    private String filename;
    private String input;
    private boolean done, valid;

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
        valid = false;
        while (!done) {
            // Prompt and user input
            System.out.print("Command: ");
            input = scanner.nextLine();
            String[] inputs = input.split(" ");
            int i = inputs.length;  // Number of inputs.
            
// LOAD
            if (inputs[0].equalsIgnoreCase("LOAD")) {
                int nodeCount = 10;     // Default note length
                if (i > 3) System.out.println("Too many inputs!");
                
                // Complete action based on number of inputs.
                switch (i) {
                    // 3 inputs: "LOAD filename noteCount"
                    case 3:
                        try {nodeCount = Integer.parseUnsignedInt(inputs[2]);}  // noteCount
                        catch (NumberFormatException e) {}  // Ignore
                    
                    // 2 inputs: "LOAD filename"
                    case 2:
                        filename = inputs[1];   // filename
                        break;
                    
                    // 1 input: "LOAD"
                    default:
                        // Prompt and user input
                        System.out.print("Midi filename: ");
                        input = scanner.nextLine();
                        inputs = input.split(" ");
                        i = inputs.length;  // Number of inputs
                        if (i > 2) System.out.println("Too many inputs! Only loading first input.");
                        
                        // Complete action based on number of inputs.
                        switch (i) {
                        
                            // 2 inputs: "filename noteCount"
                            case 2:
                                try {nodeCount = Integer.parseUnsignedInt(inputs[1]);}  // noteCount
                                catch (NumberFormatException e) {}  // Ignore
                                
                            // 1 input: "filename"
                            default:
                                filename = inputs[0];   // filename
                        }
                }
                
                // Attempts to load midi file
                if (mgen.loadMidiFile(filename)) {
                    valid = true;                   // Marking that a file has been read.
                    mgen.generateMusic(nodeCount);  // Generating music
                }
                else
                    System.out.println("Invalid filename: " + filename);
            }
// GEN
            else if (inputs[0].equalsIgnoreCase("GEN")) {
                int nodeCount = 0;
                if (valid) {
                    if (i > 3) System.out.println("Too many inputs!");
                    
                    // Complete action based on number of inputs.
                    switch (i) {
                        // 2 inputs: "GEN nodeCount"
                        case 2:
                            try {nodeCount = Integer.parseUnsignedInt(inputs[1]);}  // noteCount
                            catch (NumberFormatException e) {System.out.println("Invalid number!");}
                            break;
                            
                        // 1 input: "GEN"
                        default:
                            // Prompt and user input
                            System.out.print("Node length: ");
                            input = scanner.nextLine();
                            try {nodeCount = Integer.parseUnsignedInt(input);}  // noteCount
                            catch (NumberFormatException e) {System.out.println("Invalid number!");}
                    }
                    if (nodeCount != 0) mgen.generateMusic(nodeCount);  // Ensuring a valid number was entered.
                }
                else System.out.println("Please first load a midi file before you generate music.");
            }
// SAVE
            else if (input.equalsIgnoreCase("SAVE"))
                if (valid) mgen.saveSong();
                else System.out.println("Please first load a midi file before you save music.");
// PLAY
            else if (input.equalsIgnoreCase("PLAY"))
                if (valid) mgen.playSong();
                else System.out.println("Please first load a midi file before you play music.");
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
        System.out.print("Exiting Program...");
        scanner.close();
        System.out.println("Done");
    }
}
