package model;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

/*
 * AudioFilePlayThread uses a Sequencer to play the midi data
 * located in the MusicState.OUTPUT file.
 */
class AudioFilePlayThread extends Thread {
    private MusicState ms;
    private Sequencer sequencer = null;
    private long tickPos = 0;
    private long delay = 256;
    private Timer timer = new Timer();

    AudioFilePlayThread(MusicState ms)
    {
        this.ms = ms;
    }

    public void run()
    {
        // When this thread is started, set up the sequencer
        sequencer = null;
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            InputStream is = new BufferedInputStream(new FileInputStream(new File(MusicState.OUTPUT)));
            sequencer.setSequence(is);
        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }

        // Make the MusicState object listen for Meta Events, i.e., "stopped playing"
        sequencer.addMetaEventListener(ms);
        sequencer.start();
        keepPlaybackProgress();
    }

    // Make the sequencer stop playing
    void stopMusic()
    {
        sequencer.stop();
        tickPos = 0;
        timer.cancel();
    }

    // Pause the sequencer
    void pause()
    {
        tickPos = sequencer.getTickPosition();
        sequencer.stop();
        timer.cancel();
    }

    // unpause the sequencer
    void unpause()
    {
        sequencer.setTickPosition(tickPos);
        sequencer.start();
        keepPlaybackProgress();
    }

    // Make the sequencer skip to Tick t
    void skip(long t)
    {
        if(t >= 0 && t < sequencer.getTickLength())
        {
            sequencer.setTickPosition(tickPos);
        }
    }

    // Make the sequencer skip to 'percentage' of the way through the MIDI.
    // E.g., skip(0.50) skips to half way through the MIDI.
    // If the sequencer is playing, it will remain playing.
    // If paused, the sequencer will remain paused.
    void skip(double percentage)
    {
        if(percentage < 0 || percentage >= 1)
            return;

        skip((long)(percentage * sequencer.getTickLength()));
    }

    void keepPlaybackProgress()
    {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ms.scroll(1.0 * sequencer.getTickPosition() / sequencer.getTickLength());
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, delay);
    }
}
