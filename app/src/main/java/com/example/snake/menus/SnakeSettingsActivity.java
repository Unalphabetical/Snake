package com.example.snake.menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snake.R;
import com.example.snake.SnakeMainMenuActivity;
import com.example.snake.managers.LeaderboardManager;
import com.example.snake.managers.SaveManager;

public class SnakeSettingsActivity extends AppCompatActivity {

    private ToggleButton musicButton;
    private SaveManager saveManager;

    private ToggleButton soundButton;

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

        this.saveManager = new SaveManager(this);

        // Update the UI to reflect the current music state
        this.musicButton = findViewById(R.id.music_button);
        this.musicButton.setChecked(saveManager.isMusicEnabled());

        //Update the UI to reflect current sound state
        this.soundButton = findViewById(R.id.sound_button);
        this.soundButton.setChecked(saveManager.isSoundEnabled());
    }


    //// This click event toggles the music on and off
    public void toggleMusic(View v) {
        saveManager.setMusicEnabled(musicButton.isChecked());
    }

    //// This click event toggles the sound on and off
    public void toggleSound(View v){
        saveManager.setSoundEnabled(soundButton.isChecked());
    }

    //// This click event moves us from the settings menu to the game
    public void resumeGame(View v) {
        Intent intent = new Intent(this, SnakeMainMenuActivity.class);
        startActivity(intent);
    }

    //// This click event moves us from the settings menu to the main menu
    public void mainMenu(View v) {
        Intent intent = new Intent(this, SnakeMenuActivity.class);
        startActivity(intent);
    }

    //// This click event deletes the save data
    //// in case of a bug or something
    public void deleteSave(View v) {
        SaveManager saveManager = new SaveManager(this);
        saveManager.deleteData();
    }

    //// This click event deletes the leaderboard data
    //// in case of a bug or something
    public void deleteLeaderboard(View v) {
        LeaderboardManager leaderboardManager = new LeaderboardManager(this);
        leaderboardManager.deleteData();
    }

}
