package com.example.snakio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snakio.managers.LeaderboardManager;

public class SnakeLeaderboardMenuActivity extends AppCompatActivity {

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
        setContentView(R.layout.snake_leaderboard_menu);

        LeaderboardManager leaderboardManager = new LeaderboardManager(this);
        leaderboardManager.loadSnakeList();
        if (leaderboardManager.sort()) {
            TextView leaderboardTextView = findViewById(R.id.leaderboard_list);
            for (int i = 0; i < leaderboardManager.getSnakeList().size(); i++) {
                if (leaderboardManager.getSnakeList().get(i) == null) continue;
                leaderboardTextView.append((i + 1) + ". " + leaderboardManager.getSnakeList().get(i).getScore() + "\n");
            }
        }
    }

    public void mainMenu(View v) {
        Intent intent = new Intent(this, SnakeMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

}
