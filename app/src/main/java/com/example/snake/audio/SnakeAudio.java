package com.example.snake.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.snake.R;
import com.example.snake.managers.SaveManager;

public class SnakeAudio {

    private Context context;
    private SoundPoolHelper soundPoolHelper;
    private int eatID = -1;
    private int crashID = -1;
    private int spawnAppleID = -1;

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

        this.eatID = soundPoolHelper.load(context, R.raw.get_apple, 1);
        this.crashID = soundPoolHelper.load(context, R.raw.snake_death, 1);
        this.spawnAppleID = soundPoolHelper.load(context, R.raw.spawn_apple, 1);

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
            soundPoolHelper.play(eatID, 0.1F, 0.1F);
        }
    }

    public void playCrashSound() {
        isSoundEnabled = saveManager.isSoundEnabled();

        if (isSoundEnabled) {
            soundPoolHelper.play(crashID);
        }
    }

    public void playSpawnAppleSound() {
        isSoundEnabled = saveManager.isSoundEnabled();

        if (isSoundEnabled) {
            soundPoolHelper.play(spawnAppleID);
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
