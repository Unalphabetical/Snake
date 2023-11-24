package com.example.snakio;

import android.content.Intent;
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

        saveManager = new SaveManager(this);

        // Update the UI to reflect the current music state
        musicButton = findViewById(R.id.music_button);
        musicButton.setChecked(saveManager.isMusicEnabled());
    }


    public void toggleMusic(View v) {
        //// Enable or disable the music
        if (musicButton.isChecked()) {
            saveManager.setMusicEnabled(true);
        } else {
            saveManager.setMusicEnabled(false);
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
