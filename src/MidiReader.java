/**
 * @filename MidiReader.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Reads a midi file given a filename path.
 * Returns the read data to be processed.
 */

import jm.music.data.*;
imoprt jm.music.tools.*;

public class MidiReader {
    
    // Constructor
    public MidiReader()
    {
        
    }
    
    // Given a filename it will attempt to read a midi file.
    // Returns the midi data for processing.
    public void/*midi data*/ readMidi(String filename) {
        // with midi filename read in midi data
        Score from_midi = new Score("midi_input");
        Read.midi(from_midi, filename);

    }

}
