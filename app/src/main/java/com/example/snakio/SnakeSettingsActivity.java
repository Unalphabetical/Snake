package com.example.snakio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;
import android.util.Log;


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
        musicButton.setChecked(new SaveManager(this).isMusicDisabled());

        // Set listener for music toggle button
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMusic(v);

                // Print the current state of the music_button
                boolean isMusicDisabled = musicButton.isChecked();
                Log.d("MusicState", "Music is disabled: " + isMusicDisabled);
            }
        });
    }


    public void toggleMusic(View v) {
        SaveManager saveManager = new SaveManager(this);
        boolean isMusicDisabled = !saveManager.isMusicDisabled(); // Toggle the current state


        // Set the new state
        saveManager.setMusicDisabled(isMusicDisabled);




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
