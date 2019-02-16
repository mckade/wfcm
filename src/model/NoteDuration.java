package model;
import jm.music.data.Note;

/**
 * @filename NoteDuration.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * NoteDuration controls probabilities related to going from one note duration
 * to another. The probabilities are constrained by the notes in the sample.
 * If a transition is seen between two durations 'distance' notes apart, the
 * probability of those two durations appearing together in the output increases.
 * Distance of 1 denotes two adjacent notes.
 * */
public class NoteDuration extends Modifier
{
    int distance;
    Note[] notes;
    int cardinality;
    public NoteDuration(int dist, Note[] n, int c)
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
            // index of note[i]'s duration
            first = MarkovTable.length.get(notes[i].getDuration());
            // index of note[i+distance]'s duration
            second = MarkovTable.length.get(notes[i+distance].getDuration());
            probabilities[first][second] += 1.0;
            counts[first] += 1;

            i++;
        }

        // normalize the counts to make them probabilities
        // if no data was seen for a note, make all transitions
        // from that note duration have equal probability
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
