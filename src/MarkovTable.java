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
        int firstNoteMatch;
        double occurs = 0;  // probability of this interval
        for (int i = 0; i < notes.length - 1; ++i) {
            note = notes[i];    // Examining all intervals starting with this note
            next = notes[i + 1];
            //++intervals;
            intervals = 1;
            firstNoteMatch = 1;
            // search rest of notes for more occurances of interval
            for (int j = i + 1; j < notes.length - 1; ++j) {
                if (!notes[j].equals(note))
                    continue;   // sift until we find another occurance of the first note
                else
                    firstNoteMatch++;
                if (notes[j + 1] != next)
                    continue;   // if first note matches but the 2nd does not, keep sifting
                ++intervals; // should only reach here if notes[j] == note && notes[j + 1] == next
            }

            // Generate Key for Interval
            // Calculate probability of interval occurance
            // Add Interval entry to our HashTable (Markov Table)
            MarkovKey key = new MarkovKey(note.getPitch(), next.getPitch());
            occurs = (double)intervals / firstNoteMatch;
            markovTable.put(key, occurs);
        }
    }

    /*
    * Conversion between the map and a 2D array
    * Used in WFC for probabilities
    * */
    public double[][] toArray()
    {
        // TODO: fix the bug in this translation where probabilities per note
        // dont add to 1
        if(markovTable == null)
        {
            generateTable(notes);
        }

        // get a set of unique notes
        Set<Integer> nPitch = new HashSet<>();
        Set<Note> n = new HashSet<>();
        for (Note note : notes)
        {
            if(nPitch.add(note.getPitch()))
                n.add(note);
        }

        double[][] ret = new double[n.size()][n.size()];
        int noteInd = 0;
        int transitionInd = 0;
        for (Note note : n)
        {
            transitionInd = 0;
            boolean hasTransition = false;
            for (Note next : n)
            {
                if(markovTable.get(new MarkovKey(note.getPitch(), next.getPitch())) == null)
                {
                    ret[noteInd][transitionInd] = 0.0;
                }
                else
                {
                    hasTransition = true;
                    ret[noteInd][transitionInd] = markovTable.get(new MarkovKey(note.getPitch(), next.getPitch()));
                }
                transitionInd++;
            }

            // If no transition from this note exists, equally weight all notes
            // TODO: Is the the behavior we want?
            if(!hasTransition)
            {
                for (int i = 0; i < notes.length; i++)
                {
                    ret[noteInd][i] = 1.0 / notes.length;
                }
            }

            noteInd++;
        }

        return ret;
    }

    public Note getNote(int index)
    {
        //TODO: this should return the ith unique note, not ith note
        return notes[index];
    }

}
