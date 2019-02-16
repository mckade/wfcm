package model;
import jm.music.data.Note;

/**
 * @filename NoteTransition.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * NoteTransition controls probabilities related to going from one note pitch
 * to another. The probabilities are constrained by the notes in the sample.
 * If a transition is seen between two pitches 'distance' notes apart, the
 * probability of those two pitches appearing together in the output increases.
 * Distance of 1 denotes two adjacent notes.
 * */
public class NoteTransition extends Modifier
{
    int distance;
    Note[] notes;
    int cardinality;
    public NoteTransition(int dist, Note[] n, int c)
    {
        // set notes to be the sample notes
        notes = n;
        // set cardinality to the number of unique sample notes
        cardinality = c;
        // instantiate the probability table
        probabilities = new double[cardinality][cardinality];

        // distance bounds checking
        if(dist < 1)
            distance = 1;
        else if(dist > notes.length - 1)
            distance = notes.length - 1;
        else
            distance = dist;

        calculateProbabilities();
    }

    @Override
    protected void calculateProbabilities()
    {
        int i = 0, first, second;
        // holds the number of occurances of note[i]
        // just a dynamic programming trick to avoid rescanning arrays
        int[] counts = new int[cardinality];
        while(i + distance < notes.length)
        {
            // index of note[i]'s pitch
            first = MarkovTable.pitch.get(notes[i].getPitch());
            // index of note[i+distance]'s pitch
            second = MarkovTable.pitch.get(notes[i+distance].getPitch());
            probabilities[first][second] += 1.0;
            counts[first] += 1;

            i++;
        }

        // normalize the counts to make them probabilities
        // if no data was seen for a note, make all transitions
        // from that note have equal probability
        for(int x = 0; x < probabilities.length; x++)
        {
            for(int y = 0; y < probabilities.length; y++)
            {
                if(counts[x] == 0)
                    probabilities[x][y] = 1.0 / cardinality;
                else
                    probabilities[x][y] /= counts[x];
            }
        }
    }
}
