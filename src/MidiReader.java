/**
 * @filename MidiReader.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Reads a midi file given a filename path.
 * Returns the read data to be processed.
 */

import jm.music.data.Note;
import jm.music.data.Score;
import jm.util.Read;

public class MidiReader {
    
    // Constructor
    public MidiReader() {
        
    }
    
    // Given a filename it will attempt to read a midi file.
    // Returns the midi data for processing.
    // Currently returns a Vector of Notes (in the order they appear in input)
    public Note[] readMidi(String filename) {
        
        Note[] notes = null;
        
        // with midi filename read in midi data
        // Read Score from midi file
        Score from_midi = new Score("midi_input");
        Read.midi(from_midi, filename);
        
        // Checking if file was successfully read.
        if (from_midi.size() != 0) {
            // Might need reworked
            // i.e., needs to check whether "Phrases" and "Parts" actually exists in the score.
            notes = from_midi.getPart(0)
                    .getPhrase(0)
                    .getNoteArray();
        }

        return notes;
    }

}
