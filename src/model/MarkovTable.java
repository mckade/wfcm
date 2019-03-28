package model;
/*
  @filename MarkovTable
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Manages probability modifiers and combines them into a
 * single probability table per feature (e.g., pitch & duration)
 *
 * Used by WaveFCND to generate music
 */

import jm.JMC;

import java.io.IOException;
import java.util.*;

class MarkovTable {

    // Fields
    private MidiReader midiReader;
    private Vector<double[]> chords;
    private Vector<int[]> chordPitches;
    private double[][] chordTable; // chord transition probabilities
    private double[][] chordLengthTable; // chord duration transition probabilities

    // chord holds a static reference to the unique pitch arrays in the sample
    // mapped to an integer (the index used in probability arrays)
    static HashMap<int[], Integer> chord;
    // chord holds a static reference to the unique chord durations in the sample
    // mapped to an integer (the index used in probability arrays)
    static HashMap<Double, Integer> chordLength;

    // Constructor
    MarkovTable()
    {
        midiReader = new MidiReader();
    }
    
    // Loads midi file
    boolean loadMidiFile(String filename)
    {
        // Get midi data for processing.
        try {
            midiReader.readMidi(filename);
            chords = midiReader.getChords();
        } catch (IOException ex) {
            System.out.println("Could not read file: " + filename);
        }
        // Checking if file was successfully read.
        if (chords == null) return false;

        // initialize keys to 0
        int chordKey = 0;
        int chordLengthKey = 0;
        chord = new HashMap<>();
        chordLength = new HashMap<>();

        // scan the sample and update the pitch and length maps
        System.out.println("Creating chord and chord length maps");
        chordPitches = new Vector<>();
        for (double[] chrd : chords) {

            // Make a copy of chord pitch array without the duration at the end
            int[] newChrd = new int[chrd.length - 1];
            for (int i = 0; i < chrd.length - 1; ++i) {
                newChrd[i] = (int)chrd[i];
            }

            // Add new chord to chordPitches as int[]
            chordPitches.add(newChrd);

            if (chord.putIfAbsent(newChrd, chordKey) == null)
                chordKey++;

            // TODO: Store chord durations in last index of pitch arrays
            if (chordLength.putIfAbsent(chrd[chrd.length - 1], chordLengthKey) == null)
                chordLengthKey++;

        }

        chordTable = new double[chord.size()][chord.size()];
        chordLengthTable = new double[chordLength.size()][chordLength.size()];

        generateTable(chordPitches);

        return true;
    }
    
    // Generate a music probability table given midi data
    private void generateTable(Vector<int[]> chordsWithoutDuration)
    {
        System.out.println("Starting table generation");
        List<Modifier> pitchMods = new ArrayList<>();
        List<Modifier> lengthMods = new ArrayList<>();

        // TODO link the modifier lists to UI so users can select
        // different generation parameters. For now, just using
        // dist-1 and dist-4 NoteTransition (see NoteTransition.java for details)
        // and dist-1 NoteDuration (see NoteDuration.java for details)
        //pitchMods.add(new NoteTransition(1, notes, pitch.size()));
        pitchMods.add(new ChordTransition(1, chordsWithoutDuration, chord.size()));
        lengthMods.add(new ChordDuration(1, chords, chordLength.size()));

        // TODO add modifier weighting, i.e, make some modifiers more
        // important than others with regard to final probability table
        // (Do we want this user controlled with presets?)
        System.out.println("Aggregating modifier probabilities");
        for(int i = 0; i < pitchMods.size(); i++)
        {
            double[][] modProbabilities = pitchMods.get(i).getProbabilities();
            for(int x = 0; x < chordPitches.size(); x++)
            {
                for(int y = 0; y < chordPitches.size(); y++)
                {
                    chordTable[x][y] = modProbabilities[x][y] / pitchMods.size();
                }
            }
        }

        for(int i = 0; i < lengthMods.size(); i++)
        {
            double[][] modProbabilities = lengthMods.get(i).getProbabilities();
            for(int x = 0; x < chordLength.size(); x++)
            {
                for(int y = 0; y < chordLength.size(); y++)
                {
                    chordLengthTable[x][y] = modProbabilities[x][y] / lengthMods.size();
                }
            }
        }
    }

    /**
     *
     * Increases the probability of notes in the piece's key
     * signature being included in the output by adding extra
     * pitch arrays of the key signature's notes.
     *
     * @param key - int 1 - 12 specifying the key signature to weight
     *            the pitch table for
     */
    // TODO: Modify so the pitch octave is random
    // TODO: Modify so the duration is random (and valid)
    private void ModKeySig(int key) {

        switch(key) {
            case 1: // C / Am
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.A4, 1.0});
                    chords.add(new double[] {JMC.B4, 1.0});
                    chords.add(new double[] {JMC.C4, 1.0});
                    chords.add(new double[] {JMC.D4, 1.0});
                    chords.add(new double[] {JMC.E4, 1.0});
                    chords.add(new double[] {JMC.F4, 1.0});
                    chords.add(new double[] {JMC.G4, 1.0});
                }
                break;
            case 2: // G / Em
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.A4, 1.0});
                    chords.add(new double[] {JMC.B4, 1.0});
                    chords.add(new double[] {JMC.C4, 1.0});
                    chords.add(new double[] {JMC.D4, 1.0});
                    chords.add(new double[] {JMC.E4, 1.0});
                    chords.add(new double[] {JMC.FS4, 1.0});
                    chords.add(new double[] {JMC.G4, 1.0});
                }
                break;
            case 3: // D / Bm
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.A4, 1.0});
                    chords.add(new double[] {JMC.B4, 1.0});
                    chords.add(new double[] {JMC.CS4, 1.0});
                    chords.add(new double[] {JMC.D4, 1.0});
                    chords.add(new double[] {JMC.E4, 1.0});
                    chords.add(new double[] {JMC.FS4, 1.0});
                    chords.add(new double[] {JMC.G4, 1.0});
                }
                break;
            case 4: // A / F#m
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.A4, 1.0});
                    chords.add(new double[] {JMC.B4, 1.0});
                    chords.add(new double[] {JMC.CS4, 1.0});
                    chords.add(new double[] {JMC.D4, 1.0});
                    chords.add(new double[] {JMC.E4, 1.0});
                    chords.add(new double[] {JMC.FS4, 1.0});
                    chords.add(new double[] {JMC.GS4, 1.0});
                }
                break;
            case 5: // E / C#m
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.A4, 1.0});
                    chords.add(new double[] {JMC.B4, 1.0});
                    chords.add(new double[] {JMC.CS4, 1.0});
                    chords.add(new double[] {JMC.DS4, 1.0});
                    chords.add(new double[] {JMC.E4, 1.0});
                    chords.add(new double[] {JMC.FS4, 1.0});
                    chords.add(new double[] {JMC.GS4, 1.0});
                }
                break;
            case 6: // B / G#m
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.AS4, 1.0});
                    chords.add(new double[] {JMC.B4, 1.0});
                    chords.add(new double[] {JMC.CS4, 1.0});
                    chords.add(new double[] {JMC.DS4, 1.0});
                    chords.add(new double[] {JMC.E4, 1.0});
                    chords.add(new double[] {JMC.FS4, 1.0});
                    chords.add(new double[] {JMC.GS4, 1.0});
                }
                break;
            case 7: // F# / Ebm
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.AS4, 1.0});
                    chords.add(new double[] {JMC.B4, 1.0});
                    chords.add(new double[] {JMC.CS4, 1.0});
                    chords.add(new double[] {JMC.DS4, 1.0});
                    chords.add(new double[] {JMC.ES4, 1.0});
                    chords.add(new double[] {JMC.FS4, 1.0});
                    chords.add(new double[] {JMC.GS4, 1.0});
                }
                break;
            case 8: // C# / Bbm
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.AS4, 1.0});
                    chords.add(new double[] {JMC.BS4, 1.0});
                    chords.add(new double[] {JMC.CS4, 1.0});
                    chords.add(new double[] {JMC.DS4, 1.0});
                    chords.add(new double[] {JMC.ES4, 1.0});
                    chords.add(new double[] {JMC.FS4, 1.0});
                    chords.add(new double[] {JMC.GS4, 1.0});
                }
                break;
            case 9: // Ab / Fm
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.AF4, 1.0});
                    chords.add(new double[] {JMC.BF4, 1.0});
                    chords.add(new double[] {JMC.C4, 1.0});
                    chords.add(new double[] {JMC.DF4, 1.0});
                    chords.add(new double[] {JMC.EF4, 1.0});
                    chords.add(new double[] {JMC.F4, 1.0});
                    chords.add(new double[] {JMC.G4, 1.0});
                }
                break;
            case 10: // Eb / Cm
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.AF4, 1.0});
                    chords.add(new double[] {JMC.BF4, 1.0});
                    chords.add(new double[] {JMC.C4, 1.0});
                    chords.add(new double[] {JMC.D4, 1.0});
                    chords.add(new double[] {JMC.EF4, 1.0});
                    chords.add(new double[] {JMC.F4, 1.0});
                    chords.add(new double[] {JMC.G4, 1.0});
                }
                break;
            case 11: // Bb / Gm
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.A4, 1.0});
                    chords.add(new double[] {JMC.BF4, 1.0});
                    chords.add(new double[] {JMC.C4, 1.0});
                    chords.add(new double[] {JMC.D4, 1.0});
                    chords.add(new double[] {JMC.EF4, 1.0});
                    chords.add(new double[] {JMC.F4, 1.0});
                    chords.add(new double[] {JMC.G4, 1.0});
                }
                break;
            case 12: // F / Dm
                for (int i = 0; i < 4; ++i) {
                    chords.add(new double[] {JMC.A4, 1.0});
                    chords.add(new double[] {JMC.BF4, 1.0});
                    chords.add(new double[] {JMC.C4, 1.0});
                    chords.add(new double[] {JMC.D4, 1.0});
                    chords.add(new double[] {JMC.E4, 1.0});
                    chords.add(new double[] {JMC.F4, 1.0});
                    chords.add(new double[] {JMC.G4, 1.0});
                }
                break;
            default:
                System.out.println("Markov table: Invalid input in weightForKeySig method call.");
                break;
        }
    }

    double[][] getPitchTable()
    {
        return chordTable;
    }

    double[][] getLengthTable()
    {
        return chordLengthTable;
    }

    static int[] getPitch(int index) {
        for (Map.Entry<int[], Integer> e: chord.entrySet()) {
            if (index == e.getValue()){
                return e.getKey();
            }
        }

        return null;
    }

    static double getDuration(int index) {
        for (Map.Entry<Double, Integer> e: chordLength.entrySet()) {
            if (index == e.getValue()) {
                return e.getKey();
            }
        }

        return 0.0;
    }
}
