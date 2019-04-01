package model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import coms.UpdateListener;

public class MusicState
{
    private boolean stop = false;
    private boolean pause = false;
    public static String OUTPUT = "out.MID";
    private AudioFilePlayThread audio;
    private long playTime = 0;

    // Listener to send events to
    UpdateListener listener;
    /*
    audioFile is used to playback MIDI files.
    NOTE: taken from the jMusic library (http://www.explodingart.com/jmusic)
    and extended to fit our needs
     */

    public MusicState(UpdateListener listener) {
        this.listener = listener;
    }


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
        pause = false;
        // TODO: mess with this so gui isn't weird.
        // NOTE: Gets called everytime the music is stopped.
        //listener.updateEvent(new UpdateEvent(this, UpdateType.music));
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
