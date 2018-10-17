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
    public MarkovTable() {
        midiReader = new MidiReader();
    }
    
    // Loads midi file
    public void loadMidiFile(String filename) {
        // Get midi data for processing.
        midiReader.readMidi(filename);
    }
    
    // Generate a music table given midi data
    // Then creates music.
    public int[][] generateTable(/*midi data*/) {
        int[][] table = new int[5][5];

        return table;
    }

}
