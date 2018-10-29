import javafx.util.Pair;
import jm.music.data.Note;
import java.util.*;

/**
 * @filename MarkovTable
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Creates a table from the midi data read from the MidiReader.
 * Signals the Music generation when done.
 * Table is actually HashMap
 * The Key is a custom key that holds two values
 * which are the Pitchs of an interval.
 * Holds the percentage of all intervals
 * that the interval occurs
 *
 * To add values:
 * MarkovKey key = new MarkovKey(int pitchConst1, int pitchConst2)
 * (where the pitchConsts are pitch constants found in JM)
 * markovTable.put(key, value)
 *
 * To get values:
 * double prob = markovTable.get(new MarkovKey(JMC.C4, JMC.D4);
 */

public class MarkovTable {

    // Fields
    private MidiReader midiReader;
    private Note[] notes;
    private Map<MarkovKey, Double> markovTable;
    //*** private Music music
    
    // Constructor
    public MarkovTable() {
        midiReader = new MidiReader();
    }
    
    // Loads midi file
    public void loadMidiFile(String filename) {
        // Get midi data for processing.
        notes = midiReader.readMidi(filename);
    }
    
    // Generate a music table given midi data
    // Then creates music.
    public /*Map<MarkovKey, Double>*/ void generateTable(Note[] notes) {
        markovTable = new HashMap<MarkovKey, Double>();

        Note note; // examining interval from note
        Note next; // to next note
        int intervals = 0;  // accumulator for occurances of intervals
        double occurs = 0;  // probability of this interval
        for (int i = 0; i < notes.length - 1; ++i) {
            note = notes[i];    // Examining all intervals starting with this note
            next = notes[i + 1];
            ++intervals;
            // search rest of notes for more occurances of interval
            for (int j = i + 1; j < notes.length - 1; ++j) {
                if (notes[j] != note)
                    continue;   // sift until we find another occurance of the first note
                if (notes[j + 1] != next)
                    continue;   // if first note matches but the 2nd does not, keep sifting
                ++intervals; // should only reach here if notes[j] == note && notes[j + 1] == next
            }

            // Generate Key for Interval
            // Calculate probability of interval occurance
            // Add Interval entry to our HashTable (Markov Table)
            MarkovKey key = new MarkovKey(note.getPitch(), next.getPitch());
            occurs = intervals / notes.length;
            markovTable.put(key, occurs);
        }
    }

}
