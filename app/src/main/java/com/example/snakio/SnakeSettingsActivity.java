package com.example.snakio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snakio.managers.SaveManager;

public class SnakeSettingsActivity extends AppCompatActivity {

    private ToggleButton musicButton;
    private SaveManager saveManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.snake_settings_menu);

        musicButton = findViewById(R.id.music_button);
        saveManager = new SaveManager(this);

        // Update the UI to reflect the current music state
        ToggleButton musicButton = findViewById(R.id.music_button);
        musicButton.setChecked(new SaveManager(this).isMusicEnabled());

        // Set listener for music toggle button
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMusic(v);
            }
        });
    }

    public void toggleMusic(View v) {
        SaveManager saveManager = new SaveManager(this);
        boolean isMusicEnabled = !saveManager.isMusicEnabled(); // Toggle the current state

        // Set the new state
        saveManager.setMusicEnabled(isMusicEnabled);
    }

    private void updateButtonText(boolean isMusicEnabled) {
        if (isMusicEnabled) {
            musicButton.setTextOn(getString(R.string.disableMusic));
            musicButton.setTextOff(getString(R.string.enableMusic));
        } else {
            musicButton.setTextOn(getString(R.string.enableMusic));
            musicButton.setTextOff(getString(R.string.disableMusic));
        }
    }

    public void resumeGame(View v) {
        Intent intent = new Intent(this, SnakeActivity.class);
        startActivity(intent);
    }

    public void mainMenu(View v) {
        Intent intent = new Intent(this, SnakeMenuActivity.class);
        startActivity(intent);
    }

    public void deleteSave(View v) {
        SaveManager saveManager = new SaveManager(this);
        saveManager.deleteData();
        resumeGame(v);
    }

}
