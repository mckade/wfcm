package model;
/*
 * @filename MidiReader.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Reads a midi file given a filename path.
 * Returns the read data to be processed.
 */

import jm.midi.MidiParser;
import jm.midi.SMF;
import jm.midi.Track;
import jm.midi.event.*;
import jm.midi.event.Event;
import jm.music.data.*;
import jm.util.Read;

import java.util.*;
import java.util.List;

import static java.lang.Math.sqrt;

class DRectangle {
    double x;
    double y;
    double width;

    DRectangle(double x, double y, double width)
    {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    void print()
    {
        System.out.println("{ x:"+x+", y:"+y+", width:"+width+" }");
    }
}

class MidiReader {

    // Holds MIDI data which will be grabbed by MarkovTable
    private MidiData midiData = null;
    private double tempo;
    private Score midiScore;

    // Constructor
    MidiReader() {
    }

    // Inner class used to encapsulate data obtained form parsing
    // Event object from SMF
    // TODO: Create an array of total durations of all pitches in the piece (including those not found in the piece)
    public class MidiData {

        Vector<double[]> chords; // Last element is chord duration

        MidiData() {
            this.chords = new Vector<>();
        }

        public MidiData(Vector<double[]> c, Vector<Note> n) {
            this.chords = c;
        }

        Vector<double[]> getChords() { return this.chords; }

        // Adding up total durations of all Notes in piece
        double[] getTotalDurations() {
            double[] durations = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            Map<Integer, List<double[]>> organizedChords = new HashMap<Integer, List<double[]>>();

            // Place chords into MultiMap so it's easier to sum each pitch
            for (double[] chord : this.chords) {
                if (chord.length < 3) {
                    putMultiMap(organizedChords, (int)chord[0] % 12, chord);
                }
            }

            // Insert placeholders for pitch classes absent in the piece
            for (int i = 0; i < 12; ++i) {
                List<double[]> placeholder = new ArrayList<>();
                organizedChords.putIfAbsent(i, placeholder);
            }

            // Sum the total duration of each pitch class
            for (int i = 0; i < 12; ++i) {
                List<double[]> pitchClass = organizedChords.get(i);
                for (double[] chord : pitchClass) {
                    durations[i] += chord[1];
                }
            }

            return durations;
        }

        // Function based off of this implementation of a MultiMap at:
        // https://www.leveluplunch.com/java/examples/guava-multimap-example/
        private void putMultiMap(Map<Integer, List<double[]>> orgChords, int key, double[] chord) {
            List<double[]> chordList = orgChords.get(key);
            if(chordList == null) {
                chordList = new ArrayList<>();
                orgChords.put(key, chordList);
            }
            chordList.add(chord);
            orgChords.put(key, chordList);
        }
    }

    // Given a filename it will attempt to read a midi file.
    // Returns the midi data for processing.
    // Currently returns a Vector of Notes (in the order they appear in input)
    void readMidi(String/*File*/ filename) {

        midiData = new MidiData();

        // with midi filename read in midi data
        // Read Score from midi file
        Score from_midi = new Score("midi_input");
        Read.midi(from_midi, filename/*.getCanonicalPath()*/);
        tempo = from_midi.getTempo();
        midiScore = from_midi;
        midiData.chords = parseScore(from_midi);

        /*if (from_midi.size() != 0) {

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

            // Testing pitch class duration summation
            double[] chordDurations = midiData.getTotalDurations();
            for (double total : chordDurations) {
                System.out.println(total);
            }
        }*/
    }

    /*
     * parseScore takes in a score and returns a vector of chord arrays.
     * A chord array consists of n pitches followed by a duration.
     *
     * parseScore will also quantize the score to accomplish this
     */
    private Vector<double[]> parseScore(Score s)
    {
        // we are going to first load the midi data into rectangles
        Vector<Vector<DRectangle>> rects = new Vector<>();
        Vector<Part> parts = s.getPartList();
        System.out.println("Number of parts: " + parts.size());
        // we are only working with 1 part
        Part p = parts.elementAt(0);
        System.out.println("Number of phrases: " + p.getSize());
        Vector<Phrase> phrases = p.getPhraseList();

        // constant used to snap notes to a grid -- 1 / 4 snaps to quarter
        double quantize = 1.0 / 128;
        double min = -1;
        for(Phrase phr : phrases)
        {
            double shortest = phr.getShortestRhythmValue();
            if(min == -1 || shortest < min)
                min = shortest;
        }

        // auto adjust quantize so it snaps to the lowest rhythm value
        // TODO: this will need to be modified to allow for triplets
        for(int i = 0; i < 7; i++)
        {
            if(min < quantize || Math.abs(min - quantize) < Math.abs(min - 2*quantize))
                break;

            quantize *= 2;
        }

        System.out.println("quantize: " + quantize);
        int notenum = 0;

        for(Phrase phr : phrases)
        {
            Vector<DRectangle> r = new Vector<>();
            for(int i = 0; i < phr.size(); i++)
            {
                // quantize the note duration and start time, i.e., snap to a scale of (1 / quantize)
                double dur = phr.getNote(i).getDuration() / quantize;
                double stime = phr.getNoteStartTime(i) / quantize;

                int first = (int)dur;
                if(dur - first < dur - (first + 1))
                    dur = first * quantize;
                else
                    dur = (first + 1) * quantize;

                first = (int)stime;
                if(stime == 0)
                    stime = 0;
                else if(stime - first < stime - (first + 1))
                    stime = first * quantize;
                else
                    stime = (first + 1) * quantize;

                r.add(new DRectangle(
                        stime, // x = noteStartTime
                        phr.getNote(i).getPitch(), // y = pitch
                        dur // width = duration
                ));
            }
            rects.add(r);
            notenum += r.size();
        }

        // sort the rects by x value
        DRectangle[] drects = new DRectangle[notenum];
        // what index are we at on the ith rect vector
        int[] indices = new int[rects.size()];
        int cur = 0;

        while(true)
        {
            double m = -1;
            int lowestInd = 0;
            for(int i = 0; i < rects.size(); i++)
            {
                if(indices[i] >= rects.elementAt(i).size())
                    continue;
                if(rects.elementAt(i).elementAt(indices[i]).x < m || m == -1)
                {
                    m = rects.elementAt(i).elementAt(indices[i]).x;
                    lowestInd = i;
                }
            }

            if(m == -1)
                break;
            // set the next element in the array to the next smallest drectangle
            // we found this value at rects[lowestInd] at indices[lowestInd]
            drects[cur] = rects.elementAt(lowestInd).elementAt(indices[lowestInd]);
            indices[lowestInd]++;
            cur++;
        }

        // Create the chord arrays where the last note is the duration
        // and the rest are the pitches
        Vector<double[]> result = new Vector<>();
        Vector<Double> chord = new Vector<>();
        double start = 0.0;
        for(DRectangle d : drects)
        {
            // ignore extra rests
            if(chord.size() > 0 && d.y < 0)
                continue;
            if(d.x != start)
            {
                double[] c = new double[chord.size() + 1];
                for(int i = 0; i < chord.size(); i++) {c[i] = chord.get(i);}
                // TODO: does not handle note overlap
                c[chord.size()] = d.x - start;
                System.out.println("time: " + (d.x - start));
                result.add(c);
                chord.clear();
            }

            start = d.x;
            chord.add(d.y);
            System.out.println("Added: " + d.y +" at time " + d.x);
        }

        double[] c = new double[chord.size() + 1];
        for(int i = 0; i < chord.size(); i++) {c[i] = chord.get(i);}
        // TODO: does not handle note overlap
        c[chord.size()] = drects[drects.length - 1].width;
        result.add(c);

        for(double[] dbs : result)
        {
            for(int i = 0; i < dbs.length; i++)
            {
                System.out.print(dbs[i] + ", ");
            }
            System.out.println();
        }

        return result;
    }

    Score getMidiScore()
    {
        return midiScore;
    }

    // Cycles through each event in the event vector passed to it
    // Get's notes/chord data (pitch, etc.) and places it in
    // MidiReader's MidiData object
    private void parseEvents(Vector<Event> vec, int ppqn) {

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
                    /*
                    KeySig keySig = (KeySig) ev;
                    System.out.println("KeySig event:");
                    System.out.println("Key Signature: " + keySig.getKeySig()); // int -7 - 7. -7 means 7 flats. 7 means sharps
                    System.out.println("Key Quality: " + keySig.getKeyQuality()); // 0 for Major, 1 for minor
                    System.out.println("Time?: " + keySig.getTime());
                    */
                    break;
                case 17: // TimeSig event

                    /*
                    TimeSig timeSig = (TimeSig) ev;
                    System.out.println("TimeSig event:");
                    System.out.println("1/32 per beat: " + timeSig.getThirtySecondNotesPerBeat());
                    System.out.println("Metronome pulse: " + timeSig.getMetronomePulse());
                    System.out.println("Time Signaure: " + timeSig.getNumerator() + "/" + timeSig.getDenominator());
                    */
                    break;
                case 16: // Tempo Event

                    /*
                    TempoEvent temp = (TempoEvent) ev;
                    System.out.println("Tempo: " + temp.getTempo());
                    tempo = temp.getTempo();
                    */

                    break;
                case 4: // NoteOff event

                    NoteOff off = (NoteOff) ev;

                    if(off.getPitch() == 0)
                    {
                        // this is a rest
                        if(!addRest)
                        {
                            //System.out.println("Rest");
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
                                //System.out.println("Rest: " + 1.0 * on.getTime() / ppqn);
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

                        //System.out.println("Note: " + 1.0 * on.getTime() / ppqn);
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

    Vector<double[]> getChords() {
        if (this.midiData != null) {
            return this.midiData.getChords();
        } else {
            System.out.println("No MIDI data has been read yet.");
            return null;
        }
    }

    double getTempo() {
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
    private double[] vectorToPitchArr(Vector<Double> pitches) {

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
     * @return An int 1 - 12 corresponding to a specific key.
     *          e.g. 1 -> CMaj, 2 -> GMaj, 3 -> DMaj, etc.
     */

    private int krumhanslSchmuckler(double[] pitchDurations) {

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
        double denominator;
        double profileDiffSumSq;
        double durDiffSumSq;

        //
        for (int i = 0; i < 24; ++i) {
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
            System.arraycopy(pitchDurations, 1, pitchDurations, 0, 11);
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

    int EstimateKeySignature() {
        return krumhanslSchmuckler(midiData.getTotalDurations());
    }
}
