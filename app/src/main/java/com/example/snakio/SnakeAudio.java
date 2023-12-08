package com.example.snakio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.snakio.managers.SaveManager;

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
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
        }

        this.mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
        this.mediaPlayer.setVolume(1f, 1f);
        this.mediaPlayer.setLooping(true);

        if (!this.mediaPlayer.isPlaying()) {
            playBackgroundMusic();
        }

    }

    public void playEatSound() {
        isSoundEnabled = saveManager.isSoundEnabled();

        if (isSoundEnabled) {
            soundPoolHelper.play(mEat_ID, 0.1F, 0.1F);
        }
    }

    public void playCrashSound() {
        isSoundEnabled = saveManager.isSoundEnabled();

        if (isSoundEnabled) {
            soundPoolHelper.play(mCrashID);
        }
    }

    public void playSpawnAppleSound() {
        isSoundEnabled = saveManager.isSoundEnabled();

        if (isSoundEnabled) {
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

    //// This method pauses the background music
    public void pauseBackgroundMusic() {
        mediaPlayer.pause();
    }

    //// This method checks if the background music is paused
    public boolean isBackgroundMusicPaused() {
        return !mediaPlayer.isPlaying();
    }

}
