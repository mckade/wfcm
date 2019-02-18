package model;
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
import jm.midi.event.*;
import jm.music.data.*;
import jm.music.tools.ChordAnalysis;
import jm.music.tools.PhraseAnalysis;
import jm.util.Read;

import java.io.*;
import java.util.Vector;

public class MidiReader {

    // Holds MIDI data which will be grabbed by MarkovTable
    MidiData midiData = null;

    // Constructor
    public MidiReader() {
    }

    // Inner class used to encapsulate data obtained form parsing
    // Event object from SMF
    public class MidiData {

        CPhrase chords;
        Vector<Note> notes;

        public MidiData() {
            this.chords = new CPhrase();
            this.notes = new Vector<>();
        }

        public MidiData(CPhrase c, Vector<Note> n) {
            this.chords = c;
            this.notes = n;
        }

        public Note[] getNotes() { return vectorToNoteArr(this.notes); }

        public CPhrase getChords() { return this.chords; }
    }

    // Given a filename it will attempt to read a midi file.
    // Returns the midi data for processing.
    // Currently returns a Vector of Notes (in the order they appear in input)
    public void readMidi(String/*File*/ filename) throws IOException {

        Note[] notes = null;

        midiData = new MidiData();

        // with midi filename read in midi data
        // Read Score from midi file
        Score from_midi = new Score("midi_input");
        Read.midi(from_midi, filename/*.getCanonicalPath()*/);

        if (from_midi.size() != 0) {

            // Convert Score into raw SMF data (Standard MIDI File)
            SMF smf = new SMF();
            MidiParser.scoreToSMF(from_midi, smf);

            // Get Tracks from SMF. Track 1 should be header data.
            // Might need to check instanceof ?
            Vector<Track> tracks = smf.getTrackList();
            Vector<Vector<Event>> events = new Vector<>();

            // Don't really need this information
            System.out.println("PPQN: " + smf.getPPQN()); // Pulses per Quarter Note
            System.out.println("Number of tracks: " + tracks.size()); // Track 1 is header data

            // Get EventList from each Track
            for (Track trk : tracks) { events.add(trk.getEvtList()); }

            // Loop through Tracks &
            // Get Event data
            for (Vector<Event> vec : events) { parseEvents(vec); }
            System.out.println(midiData.getChords().toString());
        }

        // Will eventually want to return a MidiData object
        // Or have MarkovTable access MidiReader's personal MidiData object to get what it needs
    }

    // Cycles through each event in the event vector passed to it
    // Get's notes/chord data (pitch, etc.) and places it in
    // MidiReader's MidiData object
    public void parseEvents(Vector<Event> vec) {

        // Needed for chord tracking
        Vector<Integer> pitches = new Vector<>();

        for (Event ev : vec) { // Loop through Events

            short eventID = ev.getID(); // ID determines type of event

            // We're mostly interested in the NoteOn/NoteOff events for chords.
            switch (eventID) {
                case 4: // NoteOff event

                    NoteOff off = (NoteOff) ev;
                    System.out.println("NoteOff event:");

                    // Check if more than one NoteOn event
                    if (pitches.size() > 1) {
                        // If so, we just read a chord, so att it to CPhrase
                        midiData.chords.addChord(vectorToIntArr(pitches), 1);
                    } else if (pitches.size() == 1){ // pitches.size() should be AT LEAST one if we're here. Can change later if causing problems
                        midiData.notes.add(new Note(off.getPitch(), 1));
                    }

                    // Clear pitch vector for next chord.
                    pitches.clear();

                    break;
                case 5: // NoteOn event

                    NoteOn on = (NoteOn) ev;

                    // Some MIDI files encode NoteOns as NoteOffs with velocity=0
                    if (on.getVelocity() != 0) {

                        System.out.println("NoteOn event:");

                        // Add pitch value to vector
                        pitches.add(Short.toUnsignedInt(on.getPitch()));

                    } else {
                        System.out.println("NoteOff event:");

                        // Check if more than NoteOn event
                        if (pitches.size() > 1) {
                            // If so, we just read a chord, so att it to CPhrase
                            midiData.chords.addChord(vectorToIntArr(pitches), 1);
                        } else if (pitches.size() == 1) { // pitches.size() should be AT LEAST one if we're here. Can change later if causing problems
                            midiData.notes.add(new Note(on.getPitch(), 1));
                        }

                        // Clear pitch vector for next chord.
                        pitches.clear();
                    }
                    break;
            }
        }
    }

    // CPhrase.addChord only takes int[] or Note[]
    // So this method takes a Vector<Integer> of pitchs and converts to an
    // int[]
    public int[] vectorToIntArr(Vector<Integer> pitches) {

        int[] ints = new int[pitches.size()];

        for (int k = 0; k < pitches.size(); ++k) {
            ints[k] = pitches.get(k);
        }

        return ints;
    }

    // Same as above except for Notes instead of ints.
    // MarkovTable requires Note[]
    public Note[] vectorToNoteArr(Vector<Note> ns) {

        Note[] notes = new Note[ns.size()];

        for (int k = 0; k < ns.size(); ++k) {
            notes[k] = ns.get(k);
        }

        return notes;
    }
}
