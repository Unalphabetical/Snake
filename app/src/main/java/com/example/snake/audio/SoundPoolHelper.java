package com.example.snake.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashSet;
import java.util.Set;

public class SoundPoolHelper extends SoundPool {
    private Set<Integer> loaded;
    private Context context;

    public SoundPoolHelper(int maxStreams, Context context) {
        this(maxStreams, AudioManager.STREAM_MUSIC, 0, context);
    }

    public SoundPoolHelper(int maxStreams, int streamType, int srcQuality, Context context) {
        super(maxStreams, streamType, srcQuality);
        this.context = context;
        this.loaded = new HashSet<>();
        setOnLoadCompleteListener((soundPool, sampleId, status) -> loaded.add(sampleId));
    }

    //// This method is used to play sounds with the max volume
    public void play(int soundID) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;

        // Is the sound loaded already?
        if (loaded.contains(soundID)) {
            play(soundID, volume, volume, 1, 0, 1f);
        }
    }

    //// This method is used to play sounds with a custom volume
    public void play(int soundID, float leftVolume, float rightVolume) {
        if (loaded.contains(soundID)) {
            play(soundID, leftVolume, rightVolume, 1, 0, 1f);
        }
    }
}
