/**
 * @filename MidiReader.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Reads a midi file given a filename path.
 * Returns the read data to be processed.
 */

import jm.music.data.*;
import jm.music.tools.*;
import jm.util.Read;

import java.util.Vector;

public class MidiReader {
    
    // Constructor
    public MidiReader() {
        
    }
    
    // Given a filename it will attempt to read a midi file.
    // Returns the midi data for processing.
    // Currently returns a Vector of Notes (in the order they appear in input)
    public Note[] readMidi(String filename) {
        // with midi filename read in midi data

        // Read Score from midi file
        Score from_midi = new Score("midi_input");
        Read.midi(from_midi, filename);

        // Might need reworked
        // i.e., needs to check whether "Phrases" and "Parts" actually exists in the score.
        Note[] notes = from_midi.getPart(0)
                .getPhrase(0)
                .getNoteArray();

        return notes;
    }

}
