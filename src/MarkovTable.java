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
    private MidiReader midiReader;
    //*** private Music music
    
    // Constructor
    public MarkovTable(String filename) {
        midiReader = new MidiReader(filename);
    }
    
    // Generate a music table given a midi file.
    // Then creates music.
    public int[][] generateTable(/*midi filename*/) {
        int[][] table = new int[5][5];
        /*** midi data = */midiReader.readMidi(/*midi filename*/);

        return table;
    }

}
