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

import static java.lang.Math.sqrt;

public class MidiReader {

    // Holds MIDI data which will be grabbed by MarkovTable
    MidiData midiData = null;
    double tempo;
    Score midiScore;

    // Constructor
    public MidiReader() {
    }

    // Inner class used to encapsulate data obtained form parsing
    // Event object from SMF
    // TODO: Create an array of total durations of all pitches in the piece (including those not found in the piece)
    public class MidiData {

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
        tempo = from_midi.getTempo();
        midiScore = from_midi;

        for(Note n : from_midi.getPart(0).getPhrase(0).getNoteArray())
        {
             System.out.println("Note: " + n.getPitch());
            // SMF does not seem to like negative values
            if(n.getPitch() < 0)
                n.setPitch(0);
        }

        if (from_midi.size() != 0) {

            // Convert Score into raw SMF data (Standard MIDI File)
            SMF smf = new SMF();
            MidiParser.scoreToSMF(from_midi, smf);

            // Get Tracks from SMF. Track 1 should be header data.
            // Might need to check instanceof ?
            Vector<Track> tracks = smf.getTrackList();
            Vector<Vector<Event>> events = new Vector<>();

            int resolution = smf.getPPQN(); // Pulses per Quarter Note. Needed to calculate note durations

            // Get EventList from each Track
            for (Track trk : tracks) { events.add(trk.getEvtList()); }

            // Loop through Tracks &
            // Get Event data
            for (Vector<Event> vec : events) { parseEvents(vec, resolution); }
        }
    }

    public Score getMidiScore()
    {
        return midiScore;
    }

    // Cycles through each event in the event vector passed to it
    // Get's notes/chord data (pitch, etc.) and places it in
    // MidiReader's MidiData object
    public void parseEvents(Vector<Event> vec, int ppqn) {

        // Needed for note tracking
        int onCount = 0;
        boolean offsExpected = false;
        boolean addRest = true;

        // Needed for chord tracking
        Vector<Double> pitches = new Vector<>();

        for (Event ev : vec) { // Loop through Events

            short eventID = ev.getID(); // ID determines type of event

            // We're mostly interested in the NoteOn/NoteOff events for chords.
            switch (eventID) {
                case 18: // KeySig event

                    KeySig keySig = (KeySig) ev;
                    //System.out.println("KeySig event:");
                    //System.out.println("Key Signature: " + keySig.getKeySig()); // int -7 - 7. -7 means 7 flats. 7 means sharps
                    //System.out.println("Key Quality: " + keySig.getKeyQuality()); // 0 for Major, 1 for minor
                    //System.out.println("Time?: " + keySig.getTime());
                    break;
                case 17: // TimeSig event

                    TimeSig timeSig = (TimeSig) ev;
                    //System.out.println("TimeSig event:");
                    //System.out.println("1/32 per beat: " + timeSig.getThirtySecondNotesPerBeat());
                    //System.out.println("Metronome pulse: " + timeSig.getMetronomePulse());
                    break;
                case 4: // NoteOff event

                    NoteOff off = (NoteOff) ev;

                    if(off.getPitch() == 0)
                    {
                        // this is a rest
                        if(!addRest)
                        {
                            System.out.println("Rest");
                            midiData.chords.add(new double[]{-2147483648, 1.0 * off.getTime() / ppqn});
                        }
                        addRest = !addRest;
                    }

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
                    //System.out.println("NoteOff event:");

                    pitches.add(1.0 * off.getTime() / ppqn);
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

                    // Some MIDI files encode NoteOffs as NoteOns with velocity=0
                    if (on.getVelocity() != 0) {

                        //System.out.println("\nNoteOn event:");
                        //on.print();
                        //System.out.print("On: " + on.getPitch());
                        // Add pitch value to vector
                        pitches.add((double)Short.toUnsignedInt(on.getPitch()));
                        if(!offsExpected)
                            onCount++;
                    } else {
                        //on.print();
                        if(on.getPitch() == 0)
                        {
                            // this is a rest
                            if(!addRest)
                            {
                                System.out.println("Rest: " + 1.0 * on.getTime() / ppqn);
                                midiData.chords.add(new double[]{-2147483648, 1.0 * on.getTime() / ppqn});
                            }
                            addRest = !addRest;
                            break;
                        }
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

                        System.out.println("Note: " + 1.0 * on.getTime() / ppqn);
                        pitches.add(1.0 * on.getTime() / ppqn);
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

    public double getTempo() {
        if (this.midiData != null) {
            return tempo;
        } else {
            System.out.println("No MIDI data has been read yet.");
            return -1;
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

    /**
     *     This method performs an analysis on the notes
     *     to try and best estimate the key signature
     *     of the piece.
     *     Modeled after the description of the algorithm
     *     found at this URL: http://rnhart.net/articles/key-finding
     * @param pitchDurations - an array containing the the
     *                       total durations of each note in the piece.
     * @return An int 1 - 24 corresponding to a specific key.
     *          e.g. 1 -> CMaj, 2 -> Cmin, 3 -> C#Maj, etc.
     */

    public int krumhanslSchmuckler(double[] pitchDurations) {

        // The algorithm calculates correlation coefficient(s) between
        // the Major/Minor profiles (x-coordinates) defined below and
        // the duration of each pitch found in the piece (y-coordinates).
        // The Key (ordering of pitches vs the profile values)
        // yielding the highest correlation coefficient is chosen.

        double[] majProfile = {6.35, 2.23, 3.48, 2.33, 4.38, 4.09,
                2.52, 5.19, 2.39, 3.66, 2.29, 2.88};
        double[] minProfile = {6.33, 2.68, 3.52, 5.38, 2.60, 3.53,
                2.54, 4.75, 3.98, 2.69, 3.34, 3.17};
        double majAvg = 3.4825;
        double minAvg = 3.709166667;
        double durAvg = 0;

        double[] majDiffs = new double[12];
        double[] minDiffs = new double[12];
        double[] durDiffs = new double[12];

        // This vector will hold all the calculated coefficients
        // Each index will correspond to a specific key
        // The key whose coefficient is the highest is selected
        Vector<Double> corrCoeffs = new Vector<>();

        // Calculate the average note duration
        for (double duration : pitchDurations) {
            durAvg += duration;
        }
        durAvg /= 12;

        // Calculate difference between samples and averages
        // because the equation uses them. Makes things easier.
        for (int i = 0; i < 12; ++i) {
            majDiffs[i] = majProfile[i] - majAvg;
            minDiffs[i] = minProfile[i] - minAvg;
            durDiffs[i] = pitchDurations[i] - durAvg;
        }

        double numerator = 0;
        double denominator = 0;
        double profileDiffSumSq = 0;
        double durDiffSumSq = 0;

        //
        for (int i = 0; i < 12; ++i) {
            profileDiffSumSq = 0;
            durDiffSumSq = 0;

            // Calculate coefficient for this key
                // Calculate numerator
            if (i % 2 == 0) {
                for (int j = 0; j < 12; ++j) {
                    numerator += (majDiffs[j] * durDiffs[j]);
                }
            } else {
                for (int j = 0; j < 12; ++j) {
                    numerator += (minDiffs[j] * durDiffs[j]);
                }
            }

                // Calculate denominator
            for (int j = 0; j < 12; ++j) {
                if (i % 2 == 0)
                    profileDiffSumSq += (majDiffs[j] * majDiffs[j]);
                else
                    profileDiffSumSq += (minDiffs[j] * minDiffs[j]);
                durDiffSumSq += (durDiffs[j] * durDiffs[j]);
            }
            denominator = sqrt(profileDiffSumSq * durDiffSumSq);

            // Add coefficient
            corrCoeffs.add(numerator / denominator);

            // Rotate pitchDurations left
            double temp = pitchDurations[0];
            for (int j = 1; j < 12; ++j)
                pitchDurations[j - 1] = pitchDurations[j];
            pitchDurations[11] = temp;
        }

        // Determine highest correlation coefficient and return corresponding index
        double highCoef = corrCoeffs.get(0);
        int index = 0;
        for (int i = 1; i < 24; ++i) {
            if (corrCoeffs.get(i) > highCoef) {
                highCoef = corrCoeffs.get(i);
                index = i;
            }
        }

        // Map index to a number 1 - 12 corresponding to a Key
        // on the Circle of Fifths, 1 corresponding to C,
        // 2 corresponding to G, etc.
        switch (index + 1) {
            // C
            case 1:
                return 1;
            case 20:
                return 1;
            // G
            case 10:
                return 2;
            case 15:
                return 2;
            // D
            case 5:
                return 3;
            case 24:
                return 3;
            // A
            case 14:
                return 4;
            case 19:
                return 4;
            // E
            case 4:
                return 5;
            case 9:
                return 5;
            // B
            case 18:
                return 6;
            case 23:
                return 6;
            // F#
            case 8:
                return 7;
            case 13:
                return 7;
            // C#
            case 3:
                return 8;
            case 22:
                return 8;
            // Ab
            case 12:
                return 9;
            case 17:
                return 9;
            // Eb
            case 2:
                return 10;
            case 7:
                return 10;
            // Bb
            case 16:
                return 11;
            case 21:
                return 11;
            // F
            case 6:
                return 12;
            case 11:
                return 12;
            default: // Shouldn't get here
                return 0;
        }
    }
}
