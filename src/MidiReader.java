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

        Part[] parts = null;
        Phrase[][] phrases = null;
        Note[] notes = null;

        // with midi filename read in midi data
        // Read Score from midi file
        Score from_midi = new Score("midi_input");
        Read.midi(from_midi, filename);

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

        }

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
