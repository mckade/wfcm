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

public class Interface {
    
    // Fields
    private MarkovTable markovTable = new MarkovTable();

    // Constructor
    public Interface() {
        // Ask for midi
        // Give filename to MidiReader
        markovTable.generateTable(/*midi filename*/);
        // Ask ?
        // Play Music || New Music || New Midi || Done
        markovTable.generateMusic(/*midi filename*/);   // New Midi
        markovTable.generateMusic(/*midi filename*/);   // New Music
        markovTable.getMusic();                         // Play Music
        
        // Done
    }
}
