/**
 * @filename MidiReader.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Reads a midi file given a filename path.
 * Returns the read data to be processed.
 */

import jm.midi.MidiParser;
import jm.midi.MidiUtil;
import jm.midi.SMF;
import jm.midi.Track;
import jm.midi.event.Event;
import jm.music.data.*;
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

        Note[] notes = null;

        // with midi filename read in midi data
        // Read Score from midi file
        Score from_midi = new Score("midi_input");
        Read.midi(from_midi, filename);

        /*

        // Convert Score into raw SMF data (Standard MIDI File)
        SMF smf = new SMF();
        MidiParser.scoreToSMF(from_midi, smf);

        // Get Tracks from SMF. Track 1 should be header data.
        // Might need to check instanceof ?
        Vector<Track> tracks = smf.getTrackList();
        Vector<Vector<Event>> events = new Vector<>();

        System.out.println("PPQN: " + smf.getPPQN());
        System.out.println("Number of tracks: " + tracks.size());

        // Get EventList from each Track
        for (Track trk : tracks) {
            events.add(trk.getEvtList());
        }

        // Get Event data
        int i = 1;
        for (Vector<Event> vec : events) {
            System.out.println("\nTrack #" + i + " events:");
            System.out.println("---------------------------------------");
            for (Event ev : vec) {
                System.out.println(ev.toString());
            }
        }

        */

        // Checking if file was successfully read.
        if (from_midi.size() != 0) {
            // Might need reworked
            // i.e., needs to check whether "Phrases" and "Parts" actually exists in the score.

            Vector<Phrase> phrases = from_midi.getPart(0).getPhraseList();
            Vector<Vector<Note>> notess = new Vector<>();
            System.out.println("Number of Phrases: " + phrases.size());
            for (int i = 0; i < phrases.size(); ++i) {
                System.out.println("----------------------------------");
                System.out.print("Number of Notes in Phrase #" + i + ": ");
                notess.add(phrases.get(i).getNoteList());
                System.out.println(notess.get(i).size());
                for (int j = 0; j < notess.get(i).size(); ++j) {
                    if (!notess.get(i).get(j).isRest()) {
                        System.out.println("Note #" + j + " pitch: " + notess.get(i).get(j).getPitch());
                    }
                }
            }

            notes = from_midi.getPart(0)
                    .getPhrase(0)
                    .getNoteArray();

        }
        return notes;
    }

    /*
    public int parseEvent(Event event) {

        int pitch;

        CharSequence eventString = event.toString();

        return pitch;
    }
    */
}
