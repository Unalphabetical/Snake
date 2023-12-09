package com.example.snake;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snake.menus.SnakeLeaderboardMenuActivity;
import com.example.snake.menus.SnakeControlMenuActivity;
import com.example.snake.menus.SnakeSettingsActivity;

public class SnakeMainMenuActivity extends AppCompatActivity {


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
        setContentView(R.layout.snake_main_menu);
    }

    //// The click event moves us from the main menu to the game
    public void startGame(View v){
        Intent intent = new Intent(this, SnakeControlMenuActivity.class);
        startActivity(intent);
    }

    //// This click event moves us from the main menu to the settings menu
    public void settingsMenu(View v){
        Intent intent = new Intent(this, SnakeSettingsActivity.class);
        startActivity(intent);
    }

    //// This click event moves us from the main menu to the leaderboard menu
    public void leaderboardMenu(View v){
        Intent intent = new Intent(this, SnakeLeaderboardMenuActivity.class);
        startActivity(intent);
    }

}
