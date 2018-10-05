import jm.music.data.*;
import jm.util.*;

import static jm.constants.Durations.MINIM;
import static jm.constants.Pitches.C4;

public class Main
{
    public static void main(String[] args)
    {
        Note n = new Note(C4, MINIM);
        Phrase phr = new Phrase();

        for(int i = 0; i < 8; i++)
        {
            phr.addNote(n);
        }

        Part p = new Part();
        p.addPhrase(phr);

        Play.midi(p);
    }
}
