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
import jm.util.Read;

import java.io.*;
import java.util.Vector;

public class MidiReader {

    // Inner class used to encapsulate data obtained form parsing
    // Event object from SMF
    public class MidiData {

        CPhrase chords;
        Note[] notes;

        public MidiData(CPhrase c, Note[] n) {
            this.chords = c;
            this.notes = n;
        }
    }

    // Constructor
    public MidiReader() {
    }
    
    // Given a filename it will attempt to read a midi file.
    // Returns the midi data for processing.
    // Currently returns a Vector of Notes (in the order they appear in input)
    public Note[] readMidi(String/*File*/ filename) throws IOException {

        Note[] notes = null;

        // with midi filename read in midi data
        // Read Score from midi file
        Score from_midi = new Score("midi_input");
        Read.midi(from_midi, filename/*.getCanonicalPath()*/);


        // Convert Score into raw SMF data (Standard MIDI File)
        SMF smf = new SMF();
        MidiParser.scoreToSMF(from_midi, smf);

        // Get Tracks from SMF. Track 1 should be header data.
        // Might need to check instanceof ?
        Vector<Track> tracks = smf.getTrackList();
        Vector<Vector<Event>> events = new Vector<>();

        System.out.println("PPQN: " + smf.getPPQN()); // Pulses per Quarter Note
        System.out.println("Number of tracks: " + tracks.size()); // Track 1 is header data

        // Get EventList from each Track
        for (Track trk : tracks) {
            events.add(trk.getEvtList());
        }

        // Get Event data
        int i = 1; // Keeping track of Track #
        int j; // Event Vector index
        for (Vector<Event> vec : events) { // Loop through Tracks

            System.out.println("\nTrack #" + i + " events:");
            System.out.println("---------------------------------------");

            parseEvents(vec);

            ++i;
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


    public int parseEvents(Vector<Event> vec) {

        for (Event ev : vec) { // Loop through Events

            short eventID = ev.getID(); // ID determines type of event

            // Needed for chord tracking
            CPhrase chord = new CPhrase();
            chord.
            Vector<Integer> pitches = new Vector<>();

            // We're mostly interested in the NoteOn/NoteOff events for chords.
            switch (eventID) {
                /*
                case 1: // ATouch event (Aftertouch. Polyphonic key pressure)
                    ATouch touch = (ATouch) ev;
                    System.out.println("ATouch event:");
                    break;
                case 2: // CPres event (Channel pressure)
                    CPres cpres = (CPres) ev;
                    System.out.println("CPres event:");
                    break;
                case 3: // CChange event (Controller change)
                    CChange cchange = (CChange) ev;
                    System.out.println("CChange event:");
                    break;
                    */
                case 4: // NoteOff event

                    NoteOff off = (NoteOff) ev;
                    System.out.println("NoteOff event:");

                    // Check if more than one NoteOn event
                    if (pitches.size() > 1) {
                        // If so, we just read a chord, so att it to CPhrase
                        chord.addChord(vectorToIntArr(pitches), 1);
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
                            chord.addChord(vectorToIntArr(pitches), 1);
                        }

                        // Clear pitch vector for next chord.
                        pitches.clear();
                    }
                    break;
                    /*
                case 6: // PWheel event (Pitch wheel)
                    PWheel wheel = (PWheel) ev;
                    System.out.println("PWheel event:");
                    break;
                case 7: // PChange event (Program change)
                    PChange pchange = (PChange) ev;
                    System.out.println("PChange event:");
                    break;
                case 8: // SysEx event
                    SysEx sysex = (SysEx) ev;
                    System.out.println("SysEx event(s):");
                    // Note sure what to do here. Has a method to return a list of System Exclusive events
                    break;
                case 16: // Tempo event
                    TempoEvent tempevt = (TempoEvent) ev;
                    System.out.println("TempoEvent event:");
                    break;
                case 17: // TimeSig event
                    TimeSig timesig = (TimeSig) ev;
                    System.out.println("TimeSig event:");
                    break;
                case 18: // KeySig event
                    KeySig key = (KeySig) ev;
                    System.out.println("KeySig event:" );
                    break;
                case 23: // EndTrack event
                    EndTrack end = (EndTrack) ev;
                    System.out.println("EndTrack event:");
                    break;
                default:
                    System.out.println("Unknown event type.");
                    break;
                    */
            }
        }
        return 0;
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
}
