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
    private MusicGenerator mgen;
    private String filename;

    // Constructor
    public Interface()
    {
        // Init
        mgen = new MusicGenerator();
        
        // get filename from user
        // push filename to music generator
        mgen.loadMidiFile(filename);
        mgen.generateMusic(10);
        mgen.playSong();
        mgen.saveSong();
    }
}
