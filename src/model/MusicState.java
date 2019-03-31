package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class MusicState
{
    private boolean stop = false;
    private boolean pause = false;
    public static String OUTPUT = "out.MID";
    private AudioFilePlayThread audio;
    private long playTime = 0;
    /*
    audioFile is used to playback MIDI files.
    NOTE: taken from the jMusic library (http://www.explodingart.com/jmusic)
    and extended to fit our needs
     */
    public void audioFile(String var0) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(var0));
            audio = new AudioFilePlayThread(audioInputStream, this);
            stop = false;
            audio.start();
            System.out.println("Playing audio file " + var0);
        } catch (IOException var2) {
            System.err.println("Play audioFile error: in playAudioFile(): " + var2.getMessage());
        } catch (UnsupportedAudioFileException var3) {
            System.err.println("Unsupported Audio File error: in Play.audioFile():" + var3.getMessage());
        }
    }

    public void stop()
    {
        stop = true;
        playTime = 0;
        pause = false;
    }

    public boolean isStopped()
    {
        return stop;
    }

    public boolean isPaused()
    {
        return pause;
    }

    // called when the thread currently playing is finished
    public void songFinished()
    {
        stop = true;
    }

    public void pause()
    {
        pause = true;
    }

    public void unpause()
    {
        pause = false;
    }

    public void updateTime(long inc)
    {
        if(pause)
            return;

        playTime += inc;
    }
}
