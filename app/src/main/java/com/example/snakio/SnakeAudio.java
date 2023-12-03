package com.example.snakio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.example.snakio.managers.SaveManager;

import java.io.File;
import java.io.IOException;

public class SnakeAudio {

    private Context context;
    private SoundPoolHelper soundPoolHelper;
    private int mEat_ID = -1;
    private int mCrashID = -1;
    private int mSpawnAppleID = -1;

    public boolean isMusicEnabled;

    public boolean isSoundEnabled;

    //// Added MediaPlayer for background music
    private MediaPlayer mediaPlayer;

    //// Added SaveManager to get the current music state
    private SaveManager saveManager;

    public SnakeAudio(Context context) {

        this.context = context;
        this.saveManager = new SaveManager(context);

        this.soundPoolHelper = new SoundPoolHelper(2, AudioManager.STREAM_MUSIC, 0, context);

        this.mEat_ID = soundPoolHelper.load(context, R.raw.get_apple, 1);
        this.mCrashID = soundPoolHelper.load(context, R.raw.snake_death, 1);
        this.mSpawnAppleID = soundPoolHelper.load(context, R.raw.spawn_apple, 1);

        // Initialize MediaPlayer for background music
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }

        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void playEatSound() {
        if (saveManager.isSoundEnabled()) {
            soundPoolHelper.play(mEat_ID, 0.1F, 0.1F);
        }
    }

    public void playCrashSound() {
        if (saveManager.isSoundEnabled()) {
            soundPoolHelper.play(mCrashID);
        }
    }

    public void playSpawnAppleSound() {
        if (saveManager.isSoundEnabled()) {
            soundPoolHelper.play(mSpawnAppleID);
        }
    }

    // Background music methods
    public void playBackgroundMusic() {
        // Get the current music state
        isMusicEnabled = saveManager.isMusicEnabled();

        if (isMusicEnabled) {
            mediaPlayer.start();
        } else {
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
        }

    }

    public void pauseBackgroundMusic() {
        mediaPlayer.pause();
    }

    public boolean isBackgroundMusicPaused() {
        return !mediaPlayer.isPlaying();
    }

}
