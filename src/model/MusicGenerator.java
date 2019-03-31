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
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import jm.audio.Instrument;
import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Score;
import jm.util.Write;

public class MusicGenerator {

    private MarkovTable mTable;
    private WaveFCND wfc;
    private Score s;
    private Rectangle[] noteData;

    private Map<String, Integer> inst;

    private MusicState ms;

    // Temp variable until proper
    private boolean playing = false;

    public MusicGenerator()
    {
        mTable = new MarkovTable();
        addInsts();
        ms = new MusicState();
    }

    // Saves the current generation table
    public void saveGenerationTable(File file) {
        // Does nothing atm
    }
    
    // Opens an existing generation table
    public boolean openGenerationTable(File file) {
        return false;
        // Does nothing atm
    }
    
    // Imports a MIDI sample to use in music generation
    public boolean importSample(File file) {
        boolean test = mTable.loadMidiFile(file.getAbsolutePath());
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

        return test;
    }
    
    // Exports a music generation table as a new MIDI file
    public void exportMIDI(File file) {
        Write.midi(s, file.getAbsolutePath());
    }
    
    // Generates music given a note length
    public void generateMusic(int length, int tempo)
    {
        // use wfc and mTable
        s = new Score("Procedural", tempo);
        Part p = new Part("Piano", Instrument.PIANO, 0);
        Vector<Note[]> notes = wfc.getNotes(length);
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
            phr.addChord(pitches, chord[0].getDuration());
        }

        noteData = nd.toArray(new Rectangle[nd.size()]);
        //phr.addNoteList(notes, false);
        p.addCPhrase(phr);
        s.addPart(p);
        Write.midi(s, MusicState.OUTPUT);
    }
    
    public void playSong()
    {
        playing = true;
        ms.audioFile(MusicState.OUTPUT);
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
    
    public double getTempo() {
        return mTable.getTempo();
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
        inst.put("HONKYTONK, HONKYTONK_PIANO", 3);
        inst.put("EPIANO, ELECTRIC_PIANO, ELPIANO", 4);
        inst.put("EPIANO2, DX_EPIANO", 5);
        inst.put("HARPSICHORD", 6);
        inst.put("CLAV, CLAVINET", 7);
        inst.put("CELESTE, CELESTA", 8);
        inst.put("GLOCKENSPIEL, GLOCK", 9);
        inst.put("MUSIC_BOX", 10);
        inst.put("VIBRAPHONE, VIBES", 11);
        inst.put("MARIMBA", 12);
        inst.put("XYLOPHONE", 13);
        inst.put("TUBULAR_BELL, TUBULAR_BELLS", 14);
        inst.put("ORGAN, ELECTRIC_ORGAN, ", 16);
        inst.put("ORGAN2, JAZZ_ORGAN, HAMMOND_ORGAN", 17);
        inst.put("ORGAN3", 18);
        inst.put("CHURCH_ORGAN, PIPE_ORGAN", 19);
        inst.put("REED_ORGAN", 20);
        inst.put("ACCORDION, PIANO_ACCORDION, CONCERTINA", 21);
        inst.put("HARMONICA", 22);
        inst.put("BANDNEON", 23);
        inst.put("NYLON_GUITAR, NGUITAR, GUITAR, ACOUSTIC_GUITAR, AC_GUITAR", 24);
        inst.put("STEEL_GUITAR, SGUITAR", 25);
        inst.put("JAZZ_GUITAR, JGUITAR", 26);
        inst.put("CLEAN_GUITAR, CGUITAR, ELECTRIC_GUITAR, EL_GUITAR", 27);
        inst.put("MUTED_GUITAR, MGUITAR", 28);
        inst.put("OVERDRIVE_GUITAR, OGUITAR", 29);
        inst.put("DISTORTED_GUITAR, DGUITAR, DIST_GUITAR", 30);
        inst.put("GUITAR_HARMONICS, GT_HARMONICS, HARMONICS", 31);
        inst.put("ACOUSTIC_BASS, ABASS", 32);
        inst.put("FINGERED_BASS, BASS, FBASS, ELECTRIC_BASS, EL_BASS, EBASS", 33);
        inst.put("PICKED_BASS, PBASS", 34);
        inst.put("FRETLESS_BASS, FRETLESS", 35);
        inst.put("SLAP_BASS, SBASS, SLAP", 36);
        inst.put("SYNTH_BASS ", 38);
        inst.put("VIOLIN", 40);
        inst.put("VIOLA", 41);
        inst.put("CELLO, VIOLIN_CELLO", 42);
        inst.put("CONTRABASS, CONTRA_BASS, DOUBLE_BASS", 43);
        inst.put("TREMOLO_STRINGS, TREMOLO", 44);
        inst.put("PIZZICATO_STRINGS, PIZZ, PITZ, PSTRINGS", 45);
        inst.put("HARP", 46);
        inst.put("TIMPANI, TIMP", 47);
        inst.put("STRINGS, STR", 48);
        inst.put("SLOW_STRINGS", 51);
        inst.put("SYNTH_STRINGS, SYN_STRINGS", 50);
        inst.put("AAH, AHHS, CHOIR", 52);
        inst.put("OOH, OOHS, VOICE", 53);
        inst.put("SYNVOX, VOX", 54);
        inst.put("ORCHESTRA_HIT", 55);
        inst.put("TRUMPET", 56);
        inst.put("TROMBONE", 57);
        inst.put("TUBA", 58);
        inst.put("MUTED_TRUMPET", 59);
        inst.put("FRENCH_HORN, HORN", 60);
        inst.put("BRASS", 61);
        inst.put("SYNTH_BRASS", 62);
        inst.put("SOPRANO_SAX, SOPRANO, SOPRANO_SAXOPHONE, SOP", 64);
        inst.put("ALTO_SAX, ALTO, ALTO_SAXOPHONE", 65);
        inst.put("TENOR_SAX, TENOR, TENOR_SAXOPHONE, SAX, SAXOPHONE", 66);
        inst.put("BARITONE_SAX, BARI, BARI_SAX, BARITONE, BARITONE_SAXOPHONE", 67);
        inst.put("OBOE", 68);
        inst.put("ENGLISH_HORN", 69);
        inst.put("BASSOON", 70);
        inst.put("CLARINET, CLAR", 71);
        inst.put("PICCOLO, PIC, PICC", 72);
        inst.put("FLUTE", 73);
        inst.put("RECORDER", 74);
        inst.put("PAN_FLUTE, PANFLUTE", 75);
        inst.put("BOTTLE_BLOW, BOTTLE", 76);
        inst.put("SHAKUHACHI", 77);
        inst.put("WHISTLE", 78);
        inst.put("OCARINA", 79);
        inst.put("SQUARE_WAVE, SQUARE", 80);
        inst.put("SAW_WAVE, SAW, SAWTOOTH", 81);
        inst.put("SYNTH_CALLIOPE, CALLOPE, SYN_CALLIOPE", 81);
        inst.put("CHIFFER_LEAD, CHIFFER", 83);
        inst.put("CHARANG", 84);
        inst.put("SOLO_VOX", 85);
        inst.put("FANTASIA", 88);
        inst.put("WARM_PAD, PAD", 89);
        inst.put("POLYSYNTH, POLY_SYNTH", 90);
        inst.put("SPACE_VOICE", 91);
        inst.put("BOWED_GLASS", 92);
        inst.put("METAL_PAD", 93);
        inst.put("HALO_PAD, HALO", 94);
        inst.put("SWEEP_PAD, SWEEP", 95);
        inst.put("ICE_RAIN, ICERAIN", 96);
        inst.put("SOUNDTRACK", 97);
        inst.put("CRYSTAL", 98);
        inst.put("ATMOSPHERE", 99);
        inst.put("BRIGHTNESS", 100);
        inst.put("GOBLIN", 101);
        inst.put("ECHO_DROPS, DROPS, ECHOS, ECHO, ECHO_DROP", 102);
        inst.put("STAR_THEME", 103);
        inst.put("SITAR", 104);
        inst.put("BANJO", 105);
        inst.put("SHAMISEN", 106);
        inst.put("KOTO", 107);
        inst.put("KALIMBA, THUMB_PIANO", 108);
        inst.put("BAGPIPES, BAG_PIPES, BAGPIPE, PIPES", 109);
        inst.put("FIDDLE", 110);
        inst.put("SHANNAI", 111);
        inst.put("TINKLE_BELL, BELL, BELLS", 112);
        inst.put("AGOGO", 113);
        inst.put("STEEL_DRUMS, STEELDRUMS, STEELDRUM, STEEL_DRUM", 114);
        inst.put("WOODBLOCK, WOODBLOCKS", 115);
        inst.put("TAIKO, DRUM", 116);
        inst.put("SYNTH_DRUM, SYNTH_DRUMS", 118);
        inst.put("TOM, TOMS, TOM_TOM, TOM_TOMS, REVERSE_CYMBAL, CYMBAL", 119);
        inst.put("FRETNOISE, FRET, FRETS", 120);
        inst.put("BREATHNOISE, BREATH", 121);
        inst.put("SEASHORE, SEA, RAIN, THUNDER, WIND, STREAM, SFX, SOUNDEFFECTS, SOUNDFX", 122);
        inst.put("BIRD", 123);
        inst.put("TELEPHONE, PHONE", 124);
        inst.put("HELICOPTER", 125);
    }

    public String[] getInstStrings()
    {
        return inst.keySet().toArray(new String[inst.keySet().size()]);
    }
}
