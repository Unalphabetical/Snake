package com.example.snakio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.example.snakio.managers.SaveManager;

import java.io.IOException;

public class SnakeAudio {

    private Context context;
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;

    public boolean isMusicEnabled;


    //Added MediaPlayer for background music
    private MediaPlayer mediaPlayer;

    public SnakeAudio(Context context) {
        this.context = context;

        // Initialize the SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        // Initialize MediaPlayer for background music
        mediaPlayer = new MediaPlayer();
        try {
            // Get the initial music state from SaveManager
            SaveManager saveManager = new SaveManager(context);
            isMusicEnabled = saveManager.isMusicDisabled();




            AssetFileDescriptor afd = context.getAssets().openFd("background_music.ogg");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(true);


            if (!mediaPlayer.isPlaying()) {
                playBackgroundMusic();
            }





            // Load other sounds
            try {
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor descriptor;

                descriptor = assetManager.openFd("get_apple.ogg");
                mEat_ID = mSP.load(descriptor, 0);

                descriptor = assetManager.openFd("snake_death.ogg");
                mCrashID = mSP.load(descriptor, 0);
            } catch (IOException e) {
                //Handle errors loading other sounds
            }
        } catch (IOException e) {
            //Handle errors loading background music
        }
    }

    public void playEatSound() {
        mSP.play(mEat_ID, 1, 1, 0, 0, 1);
    }

    public void playCrashSound() {
        mSP.play(mCrashID, 1, 1, 0, 0, 1);
    }

    // Background music methods
    public void playBackgroundMusic() {

        // Get the current music state
        SaveManager saveManager = new SaveManager(context);
        isMusicEnabled = !saveManager.isMusicDisabled();

        if (isMusicEnabled) {
            mediaPlayer.start();
        } else {
            mediaPlayer.pause();
        }
    }

    public void pauseBackgroundMusic() {
        mediaPlayer.pause();
    }

    public boolean isBackgroundMusicPaused() {
        return !mediaPlayer.isPlaying();
    }







    // Mute background music
    //Unecessary code for now
    /*
    public void muteBackgroundMusic() {
        mediaPlayer.setVolume(0f, 0f);
    }

    // Unmute background music
    public void unmuteBackgroundMusic() {
        mediaPlayer.setVolume(1f, 1f);
    }

     */



}

