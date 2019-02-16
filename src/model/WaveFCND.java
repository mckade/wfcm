package model;
import jm.music.data.Note;

/**
 * @filename WaveFCND.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * An implementation of n-dimensional wave function collapse
 */

public class WaveFCND
{
    private PTable[] tables;
    private boolean failed = false;

    public WaveFCND(PTable[] ts)
    {
        if(ts.length < 1)
        {
            System.out.println("ERROR in WaveFCND: dimension must be positive");
            System.exit(1);
        }

        tables = ts;
    }

    /*
     * Generate a 2D representation of the notes
     */
    private Superposition[] generate(int length)
    {
        // initialize all superpositions
        Superposition[] heads = new Superposition[tables.length];
        Superposition cur;
        Superposition prev;
        for(int i = 0; i < tables.length; i++)
        {
            heads[i] = new Superposition(tables[i], null, null);
            prev = heads[i];
            for(int j = 1; j < length; j++)
            {
                cur = new Superposition(tables[i], prev, null);
                prev.setNext(cur);
                prev = cur;
            }
        }

        System.out.println("Initialized superpositions");

        for(int i = 0; i < tables.length; i++)
        {
            // Collapse the value with the lowest Shannon Entropy
            Superposition rtn = lowestSE(heads[i]);
            while(rtn != null)
            {
                rtn.collapse();
                rtn = lowestSE(heads[i]);
                if(rtn == null && failed)
                {
                    System.out.println("WFC failed... Restarting");
                    return heads;
                }
            }
        }

        return heads;
    }

    /*
     * Return an array of generated notes
     */
    public Note[] getNotes(int length)
    {
        System.out.println("Generating music with " + length + " notes");
        Superposition[] heads = generate(length);
        while(failed == true)
        {
            failed = false;
            for(int i = 0; i < heads.length; i++)
            {
                heads[i].reset();
            }
            heads = generate(length);
        }

        System.out.println("WFC succeeded!");
        Note[] res = new Note[length];

        // pitch
        int t = 0, noteIndex = 0;
        Superposition cur = heads[t];
        while(cur != null)
        {
            for(int j = 0; j < heads[t].getPTableSize(); j++)
            {
                if(cur.getSPAt(j) == 1)
                {
                    res[noteIndex] = new Note(MarkovTable.getPitch(j), Note.DEFAULT_RHYTHM_VALUE);
                    break;
                }
            }

            noteIndex++;
            cur = cur.getNext();
        }

        // rhythm
        t = 1;
        noteIndex = 0;
        cur = heads[t];
        while(cur != null)
        {
            for(int j = 0; j < heads[t].getPTableSize(); j++)
            {
                if(cur.getSPAt(j) == 1)
                {
                    res[noteIndex].setDuration(MarkovTable.getDuration(j));
                    break;
                }
            }

            noteIndex++;
            cur = cur.getNext();
        }

        return res;
    }

    private Superposition lowestSE(Superposition head)
    {
        /*
         * TODO: Currently this is calculating Shannon Entropy assuming
         * all notes have an equal output probability. This should probably
         * be changed to take the current list of notes into account.
         * */
        int size = head.getPTableSize();
        double logval = (-1.0/size * Math.log(1.0f/size)/Math.log(2));
        double tmp;
        double min = size*logval + 1; // bigger than the possible max
        Superposition low = head;

        Superposition cur = head;
        while(cur != null)
        {
            tmp = cur.size() * logval;

            // update the low if needed
            // don't let entropy for collapsed notes get compared
            if(tmp < min && tmp != logval)
            {
                min = tmp;
                low = cur;
                if(min < logval)
                {
                    // wfc failed
                    failed = true;
                    return null;
                }
            }

            cur = cur.getNext();
        }

        if(min != size*logval + 1)
            return low;
        else
            return null;
    }
}
