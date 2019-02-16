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

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class MidiReader {

    // Constructor
    public MidiReader() {
    }
    
    // Given a filename it will attempt to read a midi file.
    // Returns the midi data for processing.
    // Currently returns a Vector of Notes (in the order they appear in input)
    public Note[] readMidi(String filename) throws IOException {

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

            ++i;
            j = 0;

            for (Event ev : vec) { // Loop through Events

                short eventID = ev.getID(); // ID determines type of event

                // Needed for chord tracking
                CPhrase chord = new CPhrase();
                Vector<Integer> pitches = new Vector<>();


                // We're mostly interested in the NoteOn/NoteOff events for chords.
                switch (eventID) {
                    case 1: // ATouch event (Aftertouch. Polyphonic key pressure)
                        ATouch touch = (ATouch) ev;
                        System.out.println("ATouch event:");
                        System.out.println("Pitch: " + touch.getPitch());
                        System.out.println("Pressure: " + touch.getPressure());
                        System.out.println("Time: " + touch.getTime());
                        System.out.println("End Event: " + MidiUtil.getEndEvt(touch.getPitch(), vec, j));
                        break;
                    case 2: // CPres event (Channel pressure)
                        CPres cpres = (CPres) ev;
                        System.out.println("CPres event:");
                        System.out.println("Pressure: " + cpres.getPressure());
                        System.out.println("Time: " + cpres.getTime());
                        break;
                    case 3: // CChange event (Controller change)
                        CChange cchange = (CChange) ev;
                        System.out.println("CChange event:");
                        System.out.println("Controller #: " + cchange.getControllerNum());
                        System.out.println("Value: " + cchange.getValue());
                        System.out.println("Time: " + cchange.getTime());
                        break;
                    case 4: // NoteOff event
                        NoteOff off = (NoteOff) ev;
                        System.out.println("NoteOff event:");
                        System.out.println("Pitch: " + off.getPitch());
                        System.out.println("Velocity: " + off.getVelocity());
                        System.out.println("Time: " + off.getTime());
                        System.out.println("End Event: " + MidiUtil.getEndEvt(off.getPitch(), vec, j));
                        break;
                    case 5: // NoteOn event
                        // Some MIDI files encode NoteOns as NoteOffs with velocity=0

                        NoteOn on = (NoteOn) ev;
                        if (on.getVelocity() != 0) {

                            System.out.println("NoteOn event:");

                            // Add pitch value to vector
                            pitches.add(Short.toUnsignedInt(on.getPitch()));

                        } else {
                            System.out.println("NoteOff event:");

                            // Check if more than one pitch in vector
                            if (pitches.size() > 1) {

                                // CPhrase.addChord only takes int[] or Note[]
                                // So, "converting" Vector<Integer> to int[]
                                int[] ints = new int[pitches.size()];
                                for (int k = 0; k < pitches.size(); ++k) {
                                    ints[k] = pitches.get(k);
                                }
                                chord.addChord(ints, 1);
                            }

                            // Clear pitch vector for next chord.
                            pitches.clear();
                        }

                        System.out.println("Pitch: " + on.getPitch());
                        System.out.println("Velocity: " + on.getVelocity());
                        System.out.println("Time: " + on.getTime());
                        break;
                    case 6: // PWheel event (Pitch wheel)
                        PWheel wheel = (PWheel) ev;
                        System.out.println("PWheel event:");
                        System.out.println("Value: " + wheel.getValue());
                        System.out.println("Time: " + wheel.getTime());
                        break;
                    case 7: // PChange event (Program change)
                        PChange pchange = (PChange) ev;
                        System.out.println("PChange event:");
                        System.out.println("Value: " + pchange.getValue());
                        System.out.println("Time: " + pchange.getTime());
                        break;
                    case 8: // SysEx event
                        SysEx sysex = (SysEx) ev;
                        System.out.println("SysEx event(s):");
                        // Note sure what to do here. Has a method to return a list of System Exclusive events
                        break;
                    case 16: // Tempo event
                        TempoEvent tempevt = (TempoEvent) ev;
                        System.out.println("TempoEvent event:");
                        System.out.println("Tempo: " + tempevt.getTempo());
                        System.out.println("Time: " + tempevt.getTime());
                        break;
                    case 17: // TimeSig event
                        TimeSig timesig = (TimeSig) ev;
                        System.out.println("TimeSig event:");
                        System.out.println("Time Signature: " + timesig.getNumerator()
                                + "/" + timesig.getDenominator());
                        System.out.println("Metronome Pulse: " + timesig.getMetronomePulse());
                        System.out.println("Thirty-Second Notes per Beat: " + timesig.getThirtySecondNotesPerBeat());
                        System.out.println("Time: " + timesig.getTime());
                        break;
                    case 18: // KeySig event
                        KeySig key = (KeySig) ev;
                        System.out.println("KeySig event:" );
                        System.out.println("Key Signature: " + key.getKeySig());
                        System.out.println("Key Quality: " + key.getKeyQuality());
                        System.out.println("Time: " + key.getTime());
                        break;
                    case 23: // EndTrack event
                        EndTrack end = (EndTrack) ev;
                        System.out.println("EndTrack event:");
                        System.out.println("Time:" + end.getTime());
                        break;
                    default:
                        System.out.println("Unknown event type.");
                        break;
                }
                ++j;
            }
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

    /*
    public int parseEvent(Event event) {

        int pitch;

        CharSequence eventString = event.toString();

        return pitch;
    }
    */
}
