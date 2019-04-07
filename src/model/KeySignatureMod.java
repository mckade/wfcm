package model;

import jm.JMC;

import java.util.Random;
import java.util.Vector;

public class KeySignatureMod extends Modifier {

    private int sig;
    private double weight;
    private Vector<int[]> freshPitches;
    private int cardinality;

    public KeySignatureMod(int sig, double[][] pitchProbs, double weight) {

        // Set key signature to be weighted
        this.sig = sig;

        // Set weight
        this.weight = weight;

        // Instantiate freshChords
        freshPitches = new Vector<>();

        // Update chord pitch map in MarkovTable
        // and add any fresh pitches to freshPitches
        UpdatePitchMap();

        // set cardinality to the new number of unique sample notes
        cardinality = pitchProbs.length + freshPitches.size();

        // Initiate probability table
        probabilities = new double[cardinality][cardinality];
        fillProbabilities(pitchProbs);

        calculateProbabilities();
    }

    /**
     *
     * Increases the probability of notes in the piece's key
     * signature being included in the output by adding extra
     * pitch arrays of the key signature's notes.
     *
     */
    // TODO: Modify so the pitch octave is random
    private void UpdatePitchMap() {

        int[] C = new int[] {JMC.A4, JMC.B4, JMC.C4, JMC.D4, JMC.E4, JMC.F4, JMC.G4};
        int[] G = new int[] {JMC.A4, JMC.B4, JMC.C4, JMC.D4, JMC.E4, JMC.FS4, JMC.G4};
        int[] D = new int[] {JMC.A4, JMC.B4, JMC.CS4, JMC.D4, JMC.E4, JMC.FS4, JMC.G4};
        int[] A = new int[] {JMC.A4, JMC.B4, JMC.CS4, JMC.D4, JMC.E4, JMC.FS4, JMC.GS4};
        int[] E = new int[] {JMC.A4, JMC.B4, JMC.CS4, JMC.DS4, JMC.E4, JMC.FS4, JMC.GS4};
        int[] B = new int[] {JMC.AS4, JMC.B4, JMC.CS4, JMC.DS4, JMC.E4, JMC.FS4, JMC.GS4};
        int[] FS = new int[] {JMC.AS4, JMC.B4, JMC.CS4, JMC.DS4, JMC.F4, JMC.FS4, JMC.GS4};
        int[] CS = new int[] {JMC.AS4, JMC.C4, JMC.CS4, JMC.DS4, JMC.F4, JMC.FS4, JMC.GS4};
        int[] Af = new int[] {JMC.GS4, JMC.AS4, JMC.C4, JMC.CS4, JMC.DS4, JMC.F4, JMC.G4};
        int[] Ef = new int[] {JMC.GS4, JMC.AS4, JMC.C4, JMC.D4, JMC.DS4, JMC.F4, JMC.G4};
        int[] Bf = new int[] {JMC.A4, JMC.AS4, JMC.C4, JMC.D4, JMC.DS4, JMC.F4, JMC.G4};
        int[] F = new int[] {JMC.A4, JMC.AS4, JMC.C4, JMC.D4, JMC.E4, JMC.F4, JMC.G4};

        int[] keySig = new int[]{};

        switch(sig) {
            case 1: // C / Am
                keySig = C;
                break;
            case 2: // G / Emin
                keySig = G;
                break;
            case 3: // D / Bm
                keySig = D;
                break;
            case 4: // A / F#m
                keySig = A;
                break;
            case 5: // E / C#m
                keySig = E;
                break;
            case 6: // B / G#m
                keySig = B;
                break;
            case 7: // F# / Ebm
                keySig = FS;
                break;
            case 8: // C# / Bbm
                keySig = CS;
                break;
            case 9: // Ab / Fm
                keySig = Af;
                break;
            case 10: // Eb / Cm
                keySig = Ef;
                break;
            case 11: // Bb / Gm
                keySig = Bf;
                break;
            case 12: // F / Dm
                keySig = F;
                break;
            default:
                System.out.println("Markov table: Invalid input in ModKeySig method call.");
                break;
        }

        for (int pitch : keySig) {
            modify(new int[] {pitch});
        }
    }

    /**
     *
     * Method that attempts to add new pitches to MarkovTable's chord HashMap.
     * Also add newly added pitches to an array for later probability calculations.
     *
     * @param pitch - pitch to attempt to add
     */
    public void modify(int[] pitch) {
        if (MarkovTable.chord.putIfAbsent(pitch, MarkovTable.chordKey) == null) {
            MarkovTable.chordKey++;
            freshPitches.add(pitch);
        }
    }

    public void fillProbabilities(double[][] old) {
        for (int x = 0; x < old.length; ++x) {
            for (int y = 0; y < old.length; ++y) {
                probabilities[x][y] = old[x][y];
            }
        }
    }

    @Override
    protected void calculateProbabilities() {

        if(weight == 1.0)
            weight = .999;
        int freshIndex = cardinality - freshPitches.size();
        double adjustedWeight;
        double adjustedSum;

        // Normalize the probabilities so each row adds to 1.0
        // and make the weight be the probability a new value
        // is transitioned to, i.e., a .25 weight will transition to
        // a fresh pitch 25% of the time
        for (int x = 0; x < cardinality; ++x) {
            // add up the old row
            double sum = 0;
            for (int y = 0; y < freshIndex; ++y) {
                sum += probabilities[x][y];
            }

            // increase the sum to it represents 'weight' more data
            adjustedSum = sum / (1.0 - weight);
            // adjust the weight  to be (amount of new probability data) / (number of new pitches * adjusted sum)
            adjustedWeight = (adjustedSum - sum) / (freshPitches.size() * adjustedSum);

            // divide by the total to normalize old probabilities
            for (int y = 0; y < freshIndex; ++y) {
                probabilities[x][y] /= adjustedSum;
            }

            // set new probabilities equal to the adjusted weight
            for (int y = freshIndex; y < cardinality; ++y) {
                probabilities[x][y] = adjustedWeight;
            }
        }

        // increase the sum to it represents 'weight' more data
        adjustedSum = freshIndex / (1.0 - weight);
        // adjust the weight  to be (amount of new probability data) / (number of new pitches * adjusted sum)
        adjustedWeight = (adjustedSum - freshIndex) / (freshPitches.size() * adjustedSum);
        // Modify outgoing transition probabilities for fresh notes
        for (int x = freshIndex; x < cardinality; ++x) {
            for (int y = 0; y < freshIndex; ++y) {
                probabilities[x][y] = 1.0 / (freshIndex * adjustedSum);
            }

            for (int y = freshIndex; y < cardinality; ++y) {
                probabilities[x][y] = adjustedWeight;
            }
        }
    }
}
