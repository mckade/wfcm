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
    // TODO: Store chord durations in first/last index of pitch arrays
    public class MidiData {

        Phrase myTestPhrase = new Phrase();
        Vector<double[]> chords; // Last element is chord duration

        public MidiData() {
            this.chords = new Vector<>();
        }

        public MidiData(Vector<double[]> c, Vector<Note> n) {
            this.chords = c;
        }

        public Vector<double[]> getChords() { return this.chords; }
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
            for (Vector<Event> vec : events) { parseEvents(vec, smf.getPPQN()); }
            System.out.println(midiData.myTestPhrase.toString());
        }
    }

    //TODO: Store chord durations in first/last index of pitch arrays

    // Cycles through each event in the event vector passed to it
    // Get's notes/chord data (pitch, etc.) and places it in
    // MidiReader's MidiData object
    public void parseEvents(Vector<Event> vec, int ppqn) {

        // Needed to calculate durations
        int resolution = ppqn; // Number of ticks in a quarter note

        // Needed for note tracking
        Note note;
        int onCount = 0;
        boolean offsExpected = false;

        // Needed for chord tracking
        Vector<Double> pitches = new Vector<>();

        for (Event ev : vec) { // Loop through Events

            short eventID = ev.getID(); // ID determines type of event

            // We're mostly interested in the NoteOn/NoteOff events for chords.
            switch (eventID) {
                case 17: // TimeSig event

                    TimeSig timeSig = (TimeSig) ev;
                    System.out.println("TimeSig event:");
                    System.out.println("1/32 per beat: " + timeSig.getThirtySecondNotesPerBeat());
                    System.out.println("Metronome pulse: " + timeSig.getMetronomePulse());
                    break;
                case 4: // NoteOff event

                    NoteOff off = (NoteOff) ev;
                    // Disregard extra NoteOff events from chords
                    if(pitches.size() == 0)
                    {
                        break;
                    }

                    if(offsExpected)
                    {
                        onCount--;
                        if(onCount == 0)
                        {
                            onCount = pitches.size();
                            offsExpected = false;
                        }
                        break;
                    }
                    System.out.println("NoteOff event:");

                    pitches.add(1.0 * off.getTime() / resolution);
                    midiData.chords.add(vectorToPitchArr(pitches));

                    // Clear pitch vector for next chord.
                    pitches.clear();
                    onCount--;
                    if(onCount > 0)
                    {
                        offsExpected = true;
                    }
                    break;
                case 5: // NoteOn event

                    NoteOn on = (NoteOn) ev;

                    // Some MIDI files encode NoteOns as NoteOffs with velocity=0
                    if (on.getVelocity() != 0) {

                        System.out.println("\nNoteOn event:");
                        on.print();

                        // Add pitch value to vector
                        pitches.add((double)Short.toUnsignedInt(on.getPitch()));
                        if(!offsExpected)
                            onCount++;
                    } else {
                        // Disregard extra NoteOff events from chords
                        if(pitches.size() == 0)
                        {
                            break;
                        }

                        if(offsExpected)
                        {
                            onCount--;
                            if(onCount == 0)
                            {
                                onCount = pitches.size();
                                offsExpected = false;
                            }
                            break;
                        }

                        System.out.println("\nNoteOff event:");
                        on.print();

                        pitches.add(1.0 * on.getTime() / resolution );

                        midiData.chords.add(vectorToPitchArr(pitches));

                        // Clear pitch vector for next chord.
                        pitches.clear();
                        onCount--;
                        if(onCount > 0)
                        {
                            offsExpected = true;
                        }
                    }
                    break;
            }
        }
    }

    public Vector<double[]> getChords() {
        if (this.midiData != null) {
            return this.midiData.getChords();
        } else {
            System.out.println("No MIDI data has been read yet.");
            return null;
        }
    }

    // CPhrase.addChord only takes int[] or Note[]
    // So this method takes a Vector<Integer> of pitchs and converts to an
    // int[]
    public double[] vectorToPitchArr(Vector<Double> pitches) {

        double[] ptchs = new double[pitches.size()];

        for (int k = 0; k < pitches.size(); ++k) {
            ptchs[k] = pitches.get(k);
        }

        return ptchs;
    }
}
