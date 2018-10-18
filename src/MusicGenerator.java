/**
 * @filename MusicGenerator.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer 
 * 
 * Controller to use separate pieces to generate music.
 */

public class MusicGenerator {

    private int[][] table;
    MarkovTable mTable;
    WaveFC wfc;

    public MusicGenerator()
    {
        mTable = new MarkovTable();
        wfc = new WaveFC(6);
    }
    
    // Pushes filename to Markov table
    public void loadMidiFile(String filename) {
        mTable.loadMidiFile(filename);
    }

    public void generateMusic(int length)
    {
        // use wfc and mTable
    }

    public void playSong()
    {

    }

    public void saveSong()
    {

    }
}
