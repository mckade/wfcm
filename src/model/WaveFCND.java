package model;
import jm.music.data.Note;

import java.util.Vector;

/*
 * @filename WaveFCND.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * An implementation of n-dimensional wave function collapse
 */

class WaveFCND
{
    private PTable[] tables;
    private boolean failed = false;

    WaveFCND(PTable[] ts)
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

                //dumpSP(heads[0]);

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

    private void dumpSP(Superposition head)
    {
        System.out.println("----------DUMP----------");
        Superposition cur = head;
        while(cur != null)
        {
            cur.print();

            cur = cur.getNext();
        }
    }

    /*
     * Return an array of generated notes
     */
    Vector<Note[]> getNotes(int length)
    {
        System.out.println("Generating music with " + length + " notes");
        Superposition[] heads = generate(length);
        while(failed)
        {
            //dumpSP(heads[0]);
            failed = false;
            for (Superposition head : heads) {
                head.reset();
            }
            heads = generate(length);
        }

        System.out.println("WFC succeeded!");
        Vector<Note[]> res = new Vector<>();

        // pitch
        int t = 0, noteIndex = 0;
        Superposition cur = heads[t];
        while(cur != null)
        {
            for(int j = 0; j < heads[t].getPTableSize(); j++)
            {
                if(cur.getSPAt(j) == 1)
                {
                    int[] chord = MarkovTable.getPitch(j);
                    Note[] note = new Note[chord.length];
                    for(int k = 0; k < chord.length; k++)
                    {
                        note[k] = new Note(chord[k], Note.DEFAULT_RHYTHM_VALUE);
                    }
                    res.add(note);
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
                    for(Note n : res.get(noteIndex))
                    {
                        n.setDuration(MarkovTable.getDuration(j));
                    }
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
        int size = head.getPTableSize();
        double logval = (-1.0/size * Math.log(1.0f/size)/Math.log(2));
        double tmp;
        double min = size*logval + 1; // bigger than the possible max
        Vector<Superposition> lowvals = new Vector<>();

        Superposition cur = head;
        while(cur != null)
        {
            tmp = cur.size() * logval;

            // update the low if needed
            // don't let entropy for collapsed notes get compared
            if(tmp < min && tmp != logval)
            {
                min = tmp;
                lowvals.clear();
                lowvals.add(cur);
                if(min < logval)
                {
                    // wfc failed
                    failed = true;
                    return null;
                }
            }
            else if(tmp == min)
            {
                lowvals.add(cur);
            }

            cur = cur.getNext();
        }

        if(min != size*logval + 1)
        {
            int rand = (int)(Math.random() * lowvals.size());
            //System.out.println("index: " + rand + " out of " + lowvals.size());
            //System.out.println("Entropy: " + min);
            return lowvals.get(rand);
        }
        else
            return null;
    }
}
