package dk.kea.bahrt.andoidgameengine;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class Music implements MediaPlayer.OnCompletionListener
{

    private MediaPlayer mediaPlayer; //for reading larger sound(music) files and stream it to speakers
    private boolean isPrepared = false; //is the media player ready?

    public Music(AssetFileDescriptor assetFileDescriptor)
    {
        mediaPlayer = new MediaPlayer();
        try
        {
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                                      assetFileDescriptor.getStartOffset(),
                                      assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Music: MediaPlayer: Could not load the music file!");
        }
    }

    public void dispose()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    public boolean isLooping()
    {
        return mediaPlayer.isLooping();
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public boolean isStopped()
    {
        return !isPrepared;
    }

    public void pause()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
    }

    public void play()
    {

        if (mediaPlayer.isPlaying())
        {
            return;
        }
        try
        {
            synchronized (this)
            {
                if (!isPrepared)
                {
                    mediaPlayer.prepare();
                    isPrepared = true;
                }
                mediaPlayer.start();
            }
        }
        catch (IllegalStateException e)
        {
            Log.d("Class: Music", "We could not play() the music");
            e.printStackTrace();
        }
        catch (IOException e2)
        {
            Log.d("Class: Music", "We got an IO Exception!");
            e2.printStackTrace();
        }

    }

    public void stop()
    {
        synchronized (this)
        {
            if (!isPrepared)
            {
                return;
            }
            mediaPlayer.stop();
            isPrepared = false;
        }
    }

    public void setLooping(boolean loop)
    {
        mediaPlayer.setLooping(loop);
    }

    public void setVolume(float volume)
    {
        mediaPlayer.setVolume(volume, volume); //Left and right volume
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        synchronized (this)
        {
            isPrepared = false;
        }
    }
}
