/**
 * @filename MarkovTable
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Creates a table from the midi data read from the MidiReader.
 * Signals the Music generation when done.
 */

public class MarkovTable {
    
    // Fields
    private MidiReader midiReader = new MidiReader();
    private int[][] table;
    //*** private Music music
    
    // Constructor
    public MarkovTable() {
        
    }
    
    // Generate a music table given a midi file.
    // Then creates music.
    public void generateTable(/*midi filename*/) {
        /*** midi data = */ midiReader.readMidi(/*midi filename*/);
        // With midi data create table @table
        generateMusic();
    }
    
    // Create music
    public void generateMusic() {
        // with table generate music using WFC
        // Make sure table isn't null
    }
    
    // Return Music
    public void/*music*/ getMusic() {
        //*** return generate music
        // Make sure music isn't null
    }

}
