package model;
/*
 * @filename MidiReader.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Reads a midi file given a filename path.
 * Returns the read data to be processed.
 */

import com.sun.org.apache.bcel.internal.generic.DREM;
import jm.music.data.*;
import jm.util.Read;

import java.util.*;
import java.util.List;

import static java.lang.Math.abs;
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
    private DRectangle[] sampleRects;


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
                    durations[i] += chord[chord.length - 1];
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
        // System.out.println("Number of parts: " + parts.size());
        // we are only working with 1 part
        Part p = parts.elementAt(0);
        // System.out.println("Number of phrases: " + p.getSize());
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

        // System.out.println("quantize: " + quantize);
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
                if(Math.abs(dur - first) < Math.abs(dur - (first + 1)))
                    dur = first * quantize;
                else
                    dur = (first + 1) * quantize;

                first = (int)stime;
                if(first == 0)
                    stime = 0;
                else if(Math.abs(stime - first) < Math.abs(stime - (first + 1)))
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

        sampleRects = drects;

        // Create the chord arrays where the last note is the duration
        // and the rest are the pitches
        Vector<double[]> result = new Vector<>();
        Vector<Double> chord = new Vector<>();
        double start = 0.0;
        for(DRectangle d : drects)
        {
            // ignore extra rests
            if(chord.size() > 0 && d.y < 0 && d.x == start)
                continue;
            if(d.x != start)
            {
                double[] c = new double[chord.size() + 1];
                for(int i = 0; i < chord.size(); i++) {c[i] = chord.get(i);}
                // TODO: does not handle note overlap
                c[chord.size()] = d.x - start;
                // System.out.println("time: " + (d.x - start));
                result.add(c);
                chord.clear();
            }

            start = d.x;
            chord.add(d.y);
            // System.out.println("Added: " + d.y +" at time " + d.x);
        }

        double[] c = new double[chord.size() + 1];
        for(int i = 0; i < chord.size(); i++) {c[i] = chord.get(i);}
        // TODO: does not handle note overlap
        c[chord.size()] = drects[drects.length - 1].width;
        result.add(c);

        return result;
    }

    Score getMidiScore()
    {
        return midiScore;
    }

    DRectangle[] getSampleRects() { return sampleRects; }

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

        double numerator;
        double denominator;
        double profileDiffSumSq;
        double durDiffSumSq;

        //
        for (int i = 0; i < 24; ++i) {
            profileDiffSumSq = 0;
            durDiffSumSq = 0;
            numerator = 0;

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

        return mapKeyIndex(index);
    }


    int estimateKeySignature() {
        return krumhanslSchmuckler(midiData.getTotalDurations());
    }

    // Map index to a number 1 - 12 corresponding to a Key
    // on the Circle of Fifths, 1 corresponding to C,
    // 2 corresponding to G, etc.
    int mapKeyIndex(int index) {

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
