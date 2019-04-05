package model;
/*
 * @filename MusicGenerator.java
 * @project Procedural Music
 * @members McKade Umbenhower, Robert Randolph, Taylor Bleizeffer
 *
 * Controller to use separate pieces to generate music.
 */

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import coms.SettingsListener;
import coms.UpdateListener;
import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Score;
import jm.util.Write;

public class MusicGenerator
implements SettingsListener {

    // Music Generation
    private MarkovTable mTable;
    private WaveFCND wfc;
    private Score s;
    private Rectangle[] noteData;

    // Music state
    private MusicState ms;
    
    // Available instruments.
    private Map<String, Integer> inst;
    
    // Settings/Modifiers
    int noteLength = 100;
    int tempo = 100;
    String instrument = "PIANO";
    int dynamic;
    int timeSignature = 4;

    // Control
    private boolean playing = false;

    public MusicGenerator(UpdateListener listener) {
        mTable = new MarkovTable();
        addInsts();
        ms = new MusicState(listener);
    }

    // Saves the current generation table
    public void saveVisualizerTable(File file) {
        // Does nothing atm
    }
    
    // Opens an existing generation table
    public boolean openVisualizerTable(File file) {
        return false;
        // Does nothing atm
    }
    
    // Imports a MIDI sample to use in music generation
    public boolean importSample(File file) {
        boolean test = mTable.loadMidiFile(file.getAbsolutePath());
        tempo = (int)mTable.getTempo();
        PTable[] pt = new PTable[2];
        pt[0] = new PTable(mTable.getPitchTable());
        pt[1] = new PTable(mTable.getLengthTable());
        wfc = new WaveFCND(pt);

        // copy the sample file to out.MID so it can be played
        try {
            Files.copy(file.toPath(), new File(MusicState.OUTPUT).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        s = mTable.getScore();
        dynamic = s.getPart(0).getPhrase(0).getNote(0).getDynamic();

        return test;
    }
    
    // Exports a music generation table as a new MIDI file
    public void exportMIDI(File file) {
        Write.midi(s, file.getAbsolutePath());
    }
    
    // Generates music given a note length
    public void generateMusic()
    {
        ms.unpause();

        // use wfc and mTable
        s = new Score("Procedural", tempo);
        Part p = new Part("Part", inst.get(instrument), 0);
        Vector<Note[]> notes = wfc.getNotes(noteLength);
        Vector<Rectangle> nd = new Vector<>();

        CPhrase phr = new CPhrase();
        double time = 0.0;
        for(Note[] chord : notes)
        {
            // save note info in noteTable
            for(Note n : chord)
            {
                nd.add(new Rectangle((int)(time*mTable.getTimeScale()), n.getPitch(),
                        (int)(n.getDuration()*mTable.getTimeScale()), 1));
            }

            time += chord[0].getDuration();

            // System.out.println(chord);
            int[] pitches = new int[chord.length];
            for(int i = 0; i < chord.length; i++)
            {
                pitches[i] = chord[i].getPitch();
            }
            phr.addChord(pitches, chord[0].getDuration(), dynamic);
        }

        noteData = nd.toArray(new Rectangle[nd.size()]);
        p.addCPhrase(phr);
        s.addPart(p);
        Write.midi(s, MusicState.OUTPUT);
    }

    // Update the midi instrument and tempo
    private void updateMidi()
    {
        if(s == null)
            return;
        s.setTempo(tempo);
        s.getPart(0).setInstrument(inst.get(instrument));
        Write.midi(s, MusicState.OUTPUT);
    }

    // update the midi notes, instrument, and tempo
    public void updateMidi(Rectangle[] rects)
    {
        s = rectsToScore(rects);
        s.setTempo(tempo);
        s.getPart(0).setInstrument(inst.get(instrument));
        Write.midi(s, MusicState.OUTPUT);
    }

    // Covert an array of rectangles to a score
    private Score rectsToScore(Rectangle[] rects)
    {
        CPhrase phr = new CPhrase();
        Part part = new Part("Part", inst.get(instrument), 0);
        Score score = new Score("Procedural", tempo);


        Vector<Integer> pitches = new Vector<>();
        int startTime = 0;
        int i = 0;
        while(i < rects.length)
        {
            // accumulate all pitches in the chord
            while(rects[i].x == startTime && i < rects.length)
            {
                pitches.add(rects[i].x);
                i++;
            }

            Integer[] p = pitches.toArray(new Integer[0]);
            int[] pp = new int[p.length];
            for(int k = 0; k < p.length; k++) {pp[k] = p[k];}
            phr.addChord(pp, rects[i-1].width, dynamic);

            startTime = rects[i].x;
        }

        part.addCPhrase(phr);
        score.addPart(part);
        return score;
    }

    public void playSong()
    {
        playing = true;
        ms.play();
    }
    
    public boolean stopSong()
    {
        boolean tmp = playing;
        playing = false;
        ms.stop();
        return tmp;
    }
    
    public void pauseSong() {
        ms.pause();
    }
    
    public void unpauseSong() {
        ms.unpause();
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    // Returns the notes of the generation.
    public Rectangle[] getNotes() {
        return noteData;
    }

    // Returns the notes of the generation.
    public Rectangle[] getSampleNotes() {
        return mTable.getSample();
    }

    private void addInsts()
    {
        inst = new HashMap<>();
        inst.put("PIANO", 0);
        inst.put("HONKYTONK_PIANO", 3);
        inst.put("ELECTRIC_PIANO", 4);
        inst.put("DX_EPIANO", 5);
        inst.put("HARPSICHORD", 6);
        inst.put("CLAVINET", 7);
        inst.put("CELESTE", 8);
        inst.put("GLOCKENSPIEL", 9);
        inst.put("MUSIC_BOX", 10);
        inst.put("VIBRAPHONE", 11);
        inst.put("MARIMBA", 12);
        inst.put("XYLOPHONE", 13);
        inst.put("TUBULAR_BELL", 14);
        inst.put("ORGAN", 16);
        inst.put("JAZZ_ORGAN", 17);
        inst.put("ORGAN3", 18);
        inst.put("PIPE_ORGAN", 19);
        inst.put("REED_ORGAN", 20);
        inst.put("ACCORDION", 21);
        inst.put("HARMONICA", 22);
        inst.put("BANDNEON", 23);
        inst.put("ACOUSTIC_GUITAR", 24);
        inst.put("STEEL_GUITAR", 25);
        inst.put("JAZZ_GUITAR", 26);
        inst.put("CLEAN_GUITAR", 27);
        inst.put("MUTED_GUITAR", 28);
        inst.put("OVERDRIVE_GUITAR", 29);
        inst.put("DISTORTED_GUITAR", 30);
        inst.put("GUITAR_HARMONICS", 31);
        inst.put("ACOUSTIC_BASS", 32);
        inst.put("FINGERED_BASS", 33);
        inst.put("PICKED_BASS", 34);
        inst.put("FRETLESS_BASS", 35);
        inst.put("SLAP_BASS", 36);
        inst.put("SYNTH_BASS", 38);
        inst.put("VIOLIN", 40);
        inst.put("VIOLA", 41);
        inst.put("CELLO", 42);
        inst.put("CONTRABASS", 43);
        inst.put("TREMOLO_STRINGS", 44);
        inst.put("PIZZICATO_STRINGS", 45);
        inst.put("HARP", 46);
        inst.put("TIMPANI", 47);
        inst.put("STRINGS", 48);
        inst.put("SLOW_STRINGS", 51);
        inst.put("SYNTH_STRINGS", 50);
        inst.put("CHOIR", 52);
        inst.put("VOICE", 53);
        inst.put("SYNVOX", 54);
        inst.put("ORCHESTRA_HIT", 55);
        inst.put("TRUMPET", 56);
        inst.put("TROMBONE", 57);
        inst.put("TUBA", 58);
        inst.put("MUTED_TRUMPET", 59);
        inst.put("FRENCH_HORN", 60);
        inst.put("BRASS", 61);
        inst.put("SYNTH_BRASS", 62);
        inst.put("SOPRANO_SAX", 64);
        inst.put("ALTO_SAX", 65);
        inst.put("TENOR_SAX", 66);
        inst.put("BARITONE_SAX", 67);
        inst.put("OBOE", 68);
        inst.put("ENGLISH_HORN", 69);
        inst.put("BASSOON", 70);
        inst.put("CLARINET", 71);
        inst.put("PICCOLO", 72);
        inst.put("FLUTE", 73);
        inst.put("RECORDER", 74);
        inst.put("PAN_FLUTE", 75);
        inst.put("BOTTLE_BLOW", 76);
        inst.put("SHAKUHACHI", 77);
        inst.put("WHISTLE", 78);
        inst.put("OCARINA", 79);
        inst.put("SQUARE_WAVE", 80);
        inst.put("SAW_WAVE", 81);
        inst.put("SYNTH_CALLIOPE", 81);
        inst.put("CHIFFER_LEAD", 83);
        inst.put("CHARANG", 84);
        inst.put("SOLO_VOX", 85);
        inst.put("FANTASIA", 88);
        inst.put("WARM_PAD", 89);
        inst.put("POLYSYNTH", 90);
        inst.put("SPACE_VOICE", 91);
        inst.put("BOWED_GLASS", 92);
        inst.put("METAL_PAD", 93);
        inst.put("HALO_PAD", 94);
        inst.put("SWEEP_PAD", 95);
        inst.put("ICE_RAIN", 96);
        inst.put("SOUNDTRACK", 97);
        inst.put("CRYSTAL", 98);
        inst.put("ATMOSPHERE", 99);
        inst.put("BRIGHTNESS", 100);
        inst.put("GOBLIN", 101);
        inst.put("ECHO_DROPS", 102);
        inst.put("STAR_THEME", 103);
        inst.put("SITAR", 104);
        inst.put("BANJO", 105);
        inst.put("SHAMISEN", 106);
        inst.put("KOTO", 107);
        inst.put("KALIMBA", 108);
        inst.put("BAGPIPES", 109);
        inst.put("FIDDLE", 110);
        inst.put("SHANNAI", 111);
        inst.put("BELLS", 112);
        inst.put("AGOGO", 113);
        inst.put("STEEL_DRUMS", 114);
        inst.put("WOODBLOCK", 115);
        inst.put("TAIKO", 116);
        inst.put("SYNTH_DRUM", 118);
        inst.put("TOMS, CYMBAL", 119);
        inst.put("FRETNOISE", 120);
        inst.put("BREATH", 121);
        inst.put("SOUNDFX", 122);
        inst.put("BIRD", 123);
        inst.put("TELEPHONE", 124);
        inst.put("HELICOPTER", 125);
    }

    // Settings
    public void setNoteCount(int noteCount) {this.noteLength = noteCount;}
    public int getNoteCount() {return noteLength;}
    public void setTempo(int tempo) {this.tempo = tempo; updateMidi();}
    public int getTempo() {return tempo;}
    public void setInstrument(String instrument) {this.instrument = instrument; updateMidi();}
    public String getInstrument() {return instrument;}
    public String[] getInstrumentList() {
        return inst.keySet().toArray(new String[inst.keySet().size()]);
    }
    public void setTimeSignature(int timeSignature) {this.timeSignature = timeSignature;}
    public int getTimeSignature() {return timeSignature;}
}
