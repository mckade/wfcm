package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class MusicState
{
    public static boolean stop = false;
    public static String OUTPUT = "out.MID";
    public static Thread audio;
    /*
    audioFile is used to playback MIDI files.
    NOTE: taken from the jMusic library (http://www.explodingart.com/jmusic)
    and extended to fit our needs
     */
    public static void audioFile(String var0) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(var0));
            audio = new AudioFilePlayThread(audioInputStream);
            stop = false;
            audio.start();
            System.out.println("Playing audio file " + var0);
        } catch (IOException var2) {
            System.err.println("Play audioFile error: in playAudioFile(): " + var2.getMessage());
        } catch (UnsupportedAudioFileException var3) {
            System.err.println("Unsupported Audio File error: in Play.audioFile():" + var3.getMessage());
        }
    }

    public static void stop()
    {
        stop = true;
    }

    //TODO: fix pause/unpause
    public static void pause()
    {
        try
        {
            audio.wait();
        }
        catch (Exception e)
        {
            System.err.println("Thread could not wait:" + e.getMessage());
        }

    }

    public static void unpause()
    {

    }
}
