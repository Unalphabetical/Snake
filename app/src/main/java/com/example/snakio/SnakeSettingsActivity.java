package com.example.snakio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snakio.managers.SaveManager;

public class SnakeSettingsActivity extends AppCompatActivity {


    //// Set the main menu up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// This puts the game in full screen and
        //// hides all the other things such as navigation bar.
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //// This sets the content view to the main menu
        setContentView(R.layout.snake_settings_menu);
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
