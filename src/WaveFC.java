import jm.music.data.Note;

/**
 * @filename WaveFC.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Uses wave function collapse to generate the music
 */

public class WaveFC
{
    private MarkovTable mtable;
    private double[][] table;
    private int size;

    public WaveFC(MarkovTable m)
    {
        // simulated Markov Table
        mtable = m;
    }

    private void printTable()
    {
        size = table.length;
        for (int i = 0; i < size; i++)
        {
            System.out.print("[");
            for (int j = 0; j < size; j++)
            {
                String val = String.format("%.2f", table[i][j]);
                System.out.print(" "+val+" ");
            }
            System.out.println("]");
        }
    }

    private void printArr(int[] a)
    {
        System.out.print("[");
        for(int i = 0; i < a.length; i++)
        {
            System.out.print(" "+a[i]+" ");
        }
        System.out.println("]");
    }

    private void printArr(double[] a)
    {
        System.out.print("[");
        for(int i = 0; i < a.length; i++)
        {
            String val = String.format("%.2f", a[i]);
            System.out.print(" "+val+" ");
        }
        System.out.println("]");
    }

    private void print2dArr(int[][] a)
    {
        for(int i = 0; i < a.length; i++)
        {
            printArr(a[i]);
        }
    }

    /*
     * Generate a 2D representation of the notes
     */
    public int[][] generate(int length)
    {
        // Initialize the table
        if(table == null)
        {
            table = mtable.toArray();
            size = table.length;
        }

        // initialize all notes to superposition of all possible notes
        int[][] result = new int[length][size];
        for(int i = 0; i < length; i++)
        {
            for(int j = 0; j < size; j++)
            {
                result[i][j] = 1;
            }
        }

        // Collapse the note with the lowest Shannon Entropy
        int rtn = lowestSE(result);
        while(rtn != -1)
        {
            collapse(result, rtn);
            rtn = lowestSE(result);
            if(rtn == -2)
            {
                System.out.println("WFC failed... Restarting");
                return new int[0][];
            }
        }
        // print2dArr(result);

        return result;
    }

    /*
     * Return an array of generated notes
     */
    public Note[] getNotes(int length)
    {
        int[][] n = generate(length);
        while(n.length == 0)
        {
            n = generate(length);
        }
        System.out.println("WFC succeeded!");
        Note[] res = new Note[n.length];
        for(int i = 0; i < n.length; i++)
        {
            for(int j = 0; j < size; j++)
            {
                if(n[i][j] == 1)
                {
                    res[i] = mtable.getNote(j);
                    break;
                }
            }
        }

        return res;
    }

    /*
     * Using the previous and next superpositions, find the
     * transition probabilities of the current superposition
     */
    private double[] getProbabilities(int[][] notes, int index)
    {
        double[] prev = new double[size]; // holds probabilities transitioning into superposition
        double[] next = new double[size]; // holds probabilities transitioning out of superposition
        double[] sum  = new double[size]; // holds the aggregate probabilities going in and out

        int prevc = 0;
        int nextc = 0;

        // aggregate prev and next probabilities
        for(int i = 0; i < size; i++)
        {
            if(index > 0 && notes[index-1][i] != 0)
            {
                add(prev, getRowProbability(i, notes[index]));
                prevc++;
            }

            if(index < size - 1 && notes[index][i] != 0)
            {
                add(next, getRowProbability(i, notes[index+1]));
                nextc++;
            }
        }

        // normalize prev and next probabilites
        for(int i = 0; i < size; i++)
        {
            if(index > 0 && notes[index-1][i] != 0)
            {
                prev[i] /= prevc;
            }

            if(index < size - 1 && notes[index+1][i] != 0)
            {
                next[i] /= nextc;
            }
        }

        // average prev and next
        if(index == 0)
            return next;
        else if(index == size - 1)
            return prev;

        for(int i = 0; i < size; i++)
        {
            sum[i] = (prev[i] + next[i]) / 2.0f;
        }

        return sum;
    }

    /*
     * This function returns the probabilities of how this row can
     * transition given a mask
     * The mask is an array of 0s and 1s denoting the notes in the next superposition,
     * i.e., the note that are still possible to go to
     */
    private double[] getRowProbability(int r, int[] mask)
    {
        double[] res = new double[size];
        double unused = 0f;
        int count = 0;
        for(int i = 0; i < size; i++)
        {
            if(mask[i] == 0)
            {
                unused += table[r][i];
                count++;
                res[i] = 0f;
            }
        }

        for(int i = 0; i < size; i++)
        {
            if(mask[i] != 0)
            {
                res[i] = (count > 0) ? table[r][i] + unused / count : table[r][i];
            }
        }

        return res;
    }

    /*
     * Add this array to another and store it in this
     */
    private void add(double[] ths, double[] other)
    {
        for(int i = 0; i < ths.length; i++)
        {
            ths[i] += other[i];
        }
    }

    /*
    * Collapse takes in the array of note superpositions and
    * defines the note at index using the given transition
    * probabilities. It then propagates the constraints of
    * the selected note to its neighbors.
    * */
    private void collapse(int[][] notes, int index)
    {
        double[] probabilities = getProbabilities(notes, index);

        // random roll between 0 and 1
        double rnd = Math.random();
        // find the value associated with the roll
        double sum = 0.0f;
        // for each note in the table
        for(int i = 0; i < table.length; i++)
        {
            // if the note was picked
            if(probabilities[i] + sum >+ rnd)
            {
                // define the note in the array
                for(int j = 0; j < notes[index].length; j++)
                {
                    if(i != j)
                    {
                        notes[index][j] = 0;
                    }
                }

                // Propagate the constraints of the selection
                if (index > 0)
                {
                    propagateLeft(notes, index - 1);
                }
                if (index < notes.length - 1)
                {
                    propagateRight(notes, index + 1);
                }

                return;
            }

            sum += probabilities[i];
        }
    }

    /*
    * Purge impossibilities from the current note superposition.
    * If this note was updated, update its left note as well.
    * */
    private void propagateLeft(int[][] notes, int index)
    {
        boolean changed = false;
        boolean hasTransition = false;
        // got here from the note to the right
        // iterate over every note in notes[index]
        for(int j = 0; j < notes[index].length; j++)
        {
            if(notes[index][j] == 0)
            {
                continue;
            }

            // Check if the note can still exist
            for (int i = 0; i < notes[index].length; i++)
            {
                if (notes[index + 1][i] == 1)
                {
                    // if there is a transition from this note to the next note superposition
                    if (table[j][i] > 0)
                    {
                        hasTransition = true;
                        break;
                    }
                }
            }

            if(!hasTransition)
            {
                notes[index][j] = 0;
                changed = true;
            }
            hasTransition = false;
        }

        if(changed)
        {
            if (index > 0)
                propagateLeft(notes, index - 1);
        }
    }

    /*
     * Purge impossibilities from the current note superposition.
     * If this note was updated, update its right note as well.
     * */
    private void propagateRight(int[][] notes, int index)
    {
        boolean changed = false;
        boolean hasTransition = false;
        // got here from the note to the left
        // iterate over every note in notes[index]
        for(int j = 0; j < notes[index].length; j++)
        {
            if(notes[index][j] == 0)
            {
                continue;
            }

            // Check if the note can still exist
            for (int i = 0; i < notes[index].length; i++)
            {
                if (notes[index - 1][i] == 1)
                {
                    if (table[i][j] > 0)
                    {
                        hasTransition = true;
                        break;
                    }
                }
            }

            if(!hasTransition)
            {
                notes[index][j] = 0;
                changed = true;
            }
            hasTransition = false;
        }

        if(changed)
        {
            if (index < notes.length - 1)
                propagateRight(notes, index + 1);
        }
    }


    private int lowestSE(int[][] notes)
    {
        /*
         * TODO: Currently this is calculating Shannon Entropy assuming
         * all notes have an equal output probability. This should probably
         * be changed to take the current list of notes into account.
         * */
        double logval = (-1.0/size * Math.log(1.0f/size)/Math.log(2));
        double tmp;
        double min = size*logval + 1; // bigger than the possible max
        int index = 0;
        for(int j = 0; j < notes.length; j++)
        {
            tmp = 0f;

            for(int i = 0; i < notes[j].length; i++)
            {
                tmp += notes[j][i] * logval;
            }

            // don't let entropy for collapsed notes get compared
            if(tmp < min && tmp != logval)
            {
                min = tmp;
                index = j;
                if(min < logval)
                    return -2;
            }
        }

        if(min != size*logval + 1)
            return index;
        else
            return -1;
    }
}
